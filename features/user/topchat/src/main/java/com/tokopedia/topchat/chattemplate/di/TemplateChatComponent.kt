package com.tokopedia.topchat.chattemplate.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.topchat.chattemplate.view.fragment.TemplateChatFragment
import com.tokopedia.topchat.chattemplate.view.fragment.EditTemplateChatFragment
import dagger.Component

/**
 * Created by stevenfredian on 9/14/17.
 */
@ActivityScope
@Component(
    modules = [TemplateChatModule::class, ChatTemplateViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface TemplateChatComponent {
    fun inject(fragment: TemplateChatFragment)
    fun inject(fragment: EditTemplateChatFragment)
}