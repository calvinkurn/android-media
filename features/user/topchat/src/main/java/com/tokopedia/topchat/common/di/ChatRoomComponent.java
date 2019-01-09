package com.tokopedia.topchat.common.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.topchat.chatroom.view.activity.ChatRoomSettingsActivity;
import com.tokopedia.topchat.chatroom.view.fragment.ChatRoomSettingsFragment;

import dagger.Component;

/**
 * Created by stevenfredian on 9/14/17.
 */

@InboxChatScope
@Component(modules = ChatRoomModule.class, dependencies = AppComponent.class)
public interface ChatRoomComponent {

    SessionHandler sessionHandler();

    void inject(ChatRoomSettingsActivity chatRoomSettingsActivity);

    void inject(ChatRoomSettingsFragment chatRoomSettingsFragment);
}
