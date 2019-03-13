package com.tokopedia.groupchat.chatroom.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.groupchat.chatroom.view.activity.GroupChatActivity;
import com.tokopedia.groupchat.chatroom.view.fragment.ChannelInfoFragment;
import com.tokopedia.groupchat.chatroom.view.fragment.ChannelVoteFragment;
import com.tokopedia.groupchat.chatroom.view.fragment.GroupChatFragment;
import com.tokopedia.groupchat.room.view.fragment.PlayWebviewFragment;
import com.tokopedia.groupchat.common.di.component.GroupChatComponent;

import dagger.Component;

/**
 * @author by nisie on 2/15/18.
 */

@ChatroomScope
@Component(modules = ChatroomModule.class, dependencies = GroupChatComponent.class)
public interface ChatroomComponent {
    @ApplicationContext
    Context getApplicationContext();

    void inject(GroupChatFragment fragment);

    void inject(ChannelVoteFragment fragment);

    void inject(ChannelInfoFragment fragment);

    void inject(GroupChatActivity activity);
}
