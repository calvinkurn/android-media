package com.tokopedia.topchat.stub.chattemplate.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.topchat.chattemplate.activity.base.BaseChatTemplateTest
import com.tokopedia.topchat.chattemplate.activity.base.BaseEditTemplateTest
import com.tokopedia.topchat.chattemplate.di.TemplateChatComponent
import com.tokopedia.topchat.stub.chattemplate.view.fragment.TemplateChatFragmentStub
import com.tokopedia.topchat.stub.common.di.FakeBaseAppComponent
import dagger.Component

@ActivityScope
@Component(
    modules = [TemplateChatModuleStub::class, TemplateChatViewModelModuleStub::class],
    dependencies = [FakeBaseAppComponent::class]
)
interface TemplateChatComponentStub: TemplateChatComponent {
    fun inject(templateChatFragmentStub: TemplateChatFragmentStub)
    fun inject(templateChatTest: BaseChatTemplateTest)
    fun inject(edtTemplateTest: BaseEditTemplateTest)
}