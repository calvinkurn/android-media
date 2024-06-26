package com.tokopedia.topchat.chattemplate.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.topchat.chattemplate.view.fragment.EditTemplateChatFragment
import com.tokopedia.topchat.chattemplate.view.fragment.TemplateChatFragment
import dagger.Component

@ActivityScope
@Component(
    modules = [TemplateChatModule::class, ChatTemplateViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface TemplateChatComponent {
    fun inject(fragment: TemplateChatFragment)
    fun inject(fragment: EditTemplateChatFragment)
}