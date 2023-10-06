package com.tokopedia.tokochat.stub.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.tokochat.config.di.module.TokoChatConfigContextModule
import com.tokopedia.tokochat.di.TokoChatComponent
import com.tokopedia.tokochat.di.TokoChatFragmentModule
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
        TokoChatViewModelModule::class,
        TokoChatConfigContextModule::class,
        TokoChatFragmentModule::class
    ],
    dependencies = [FakeBaseAppComponent::class]
)
interface TokoChatComponentStub : TokoChatComponent {
    fun inject(test: BaseTokoChatTest)
}
