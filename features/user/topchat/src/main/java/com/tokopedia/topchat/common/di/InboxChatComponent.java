package com.tokopedia.topchat.common.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.topchat.chatlist.fragment.InboxChatFragment;

import dagger.Component;

/**
 * Created by stevenfredian on 9/14/17.
 */

@InboxChatScope
@Component(modules = InboxChatModule.class, dependencies = BaseAppComponent.class)
public interface InboxChatComponent {

    void inject(InboxChatFragment fragment);

}
