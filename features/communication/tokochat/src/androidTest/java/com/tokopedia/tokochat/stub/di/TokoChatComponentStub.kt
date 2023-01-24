package com.tokopedia.tokochat.stub.di

import com.tokochat.tokochat_config_common.di.module.TokoChatConfigContextModule
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.tokochat.di.TokoChatComponent
import com.tokopedia.tokochat.di.TokoChatImageAttachmentNetworkModule
import com.tokopedia.tokochat.di.TokoChatModule
import com.tokopedia.tokochat.di.TokoChatViewModelModule
import com.tokopedia.tokochat.stub.di.base.FakeBaseAppComponent
import com.tokopedia.tokochat.test.base.BaseTokoChatTest
import dagger.Component

@ActivityScope
@Component(
    modules = [
        // Stub modules
        TokoChatModuleStub::class,
        TokoChatNetworkModuleStub::class,
        TokoChatUseCaseModuleStub::class,
        TokoChatCourierConversationModule::class,

        // Real modules
        TokoChatModule::class,
        TokoChatViewModelModule::class,
        TokoChatImageAttachmentNetworkModule::class,
        TokoChatConfigContextModule::class
    ],
    dependencies = [FakeBaseAppComponent::class]
)
interface TokoChatComponentStub : TokoChatComponent {
    fun inject(test: BaseTokoChatTest)
}
