package com.tokopedia.tkpdchat.common.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.tkpdchat.common.BaseChatActivity;
import com.tokopedia.tkpdchat.common.di.module.ChatModule;

import dagger.Component;

/**
 * @author by nisie on 2/1/18.
 */

@ChatScope
@Component(modules = ChatModule.class, dependencies = BaseAppComponent.class)
public interface ChatComponent {

    void inject(BaseChatActivity baseChatActivity);

}
