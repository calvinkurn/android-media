package com.tokopedia.topchat.stub.chattemplate.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.topchat.chattemplate.activity.base.BaseChatTemplateTest
import com.tokopedia.topchat.chattemplate.activity.base.BaseEditTemplateTest
import com.tokopedia.topchat.chattemplate.di.ChatTemplateViewModelModule
import com.tokopedia.topchat.chattemplate.di.TemplateChatComponent
import com.tokopedia.topchat.chattemplate.di.TemplateChatModule
import com.tokopedia.topchat.stub.common.di.FakeBaseAppComponent
import dagger.Component

@ActivityScope
@Component(
    modules = [
        TemplateChatModule::class,
        ChatTemplateViewModelModule::class,
        ChatTemplateUseCaseStub::class
    ],
    dependencies = [FakeBaseAppComponent::class]
)
interface TemplateChatComponentStub: TemplateChatComponent {
    fun inject(test: BaseChatTemplateTest)
    fun inject(test: BaseEditTemplateTest)
}
