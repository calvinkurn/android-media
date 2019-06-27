package com.tokopedia.topchat.chattemplate.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.topchat.chattemplate.view.fragment.EditTemplateChatFragment;
import com.tokopedia.topchat.chattemplate.view.fragment.TemplateChatFragment;

import dagger.Component;

/**
 * Created by stevenfredian on 9/14/17.
 */

@TemplateChatScope
@Component(modules = TemplateChatModule.class, dependencies = BaseAppComponent.class)
public interface TemplateChatComponent {
    void inject(TemplateChatFragment fragment);

    void inject(EditTemplateChatFragment fragment);
}
