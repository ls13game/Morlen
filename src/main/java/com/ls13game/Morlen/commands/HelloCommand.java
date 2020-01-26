/*
 * Copyright 2017 John Grosh (jagrosh).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ls13game.Morlen.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class HelloCommand extends Command
{
    private final EventWaiter waiter;
    public HelloCommand(EventWaiter waiter)
    {
        this.waiter = waiter;
        this.name = "hello";
        this.aliases = new String[]{"hi"};
        this.help = "says hello and waits for a response";
    }
    
    @Override
    protected void execute(CommandEvent event)
    {
        // ask what the user's name is
        event.reply("Hello. What is your name?");
        
        // wait for a response
        waiter.waitForEvent(MessageReceivedEvent.class, 
                // make sure it's by the same user, and in the same channel, and for safety, a different message
                e -> e.getAuthor().equals(event.getAuthor()) 
                        && e.getChannel().equals(event.getChannel()) 
                        && !e.getMessage().equals(event.getMessage()), 
                // respond, inserting the name they listed into the response
                e -> event.reply("Hello, `"+e.getMessage().getContentRaw()+"`! I'm `"+e.getJDA().getSelfUser().getName()+"`!"),
                // if the user takes more than a minute, time out
                1, TimeUnit.MINUTES, () -> event.reply("Sorry, you took too long."));
    }
    
}
