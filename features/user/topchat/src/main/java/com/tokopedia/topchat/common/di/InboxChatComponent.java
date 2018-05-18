package com.tokopedia.topchat.common.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.topchat.chatroom.data.network.ChatBotApi;
import com.tokopedia.topchat.chatlist.fragment.InboxChatFragment;
import com.tokopedia.topchat.chatroom.view.fragment.ChatRoomFragment;

import dagger.Component;

/**
 * Created by stevenfredian on 9/14/17.
 */

@InboxChatScope
@Component(modules = InboxChatModule.class, dependencies = AppComponent.class)
public interface InboxChatComponent {
    void inject (InboxChatFragment fragment);

    void inject (ChatRoomFragment fragment);

    ChatBotApi chatRatingApi();
}
