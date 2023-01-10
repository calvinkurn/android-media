package com.tokopedia.tokochat.stub.di

import com.tokochat.tokochat_config_common.di.module.TokoChatConfigContextModule
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.tokochat.di.TokoChatComponent
import com.tokopedia.tokochat.di.TokoChatImageAttachmentNetworkModule
import com.tokopedia.tokochat.di.TokoChatModule
import com.tokopedia.tokochat.di.TokoChatViewModelModule
import com.tokopedia.tokochat.test.TokoChatTest
import dagger.Component

@ActivityScope
@Component(
    modules = [
        TokoChatModuleStub::class,
        TokoChatModule::class,
        TokoChatViewModelModule::class,
        TokoChatImageAttachmentNetworkModule::class,
        TokoChatConfigContextModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface TokoChatComponentStub: TokoChatComponent {
    fun inject(test: TokoChatTest)
}
