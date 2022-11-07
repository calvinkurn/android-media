package com.tokopedia.tokochat.di

import com.tokochat.tokochat_config_common.di.component.TokoChatConfigComponent
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokochat.view.fragment.TokoChatFragment
import dagger.Component

@TokoChatScope
@Component(
    modules = [
        TokoChatModule::class,
        TokoChatUseCaseModule::class,
        TokoChatContextModule::class,
        TokoChatViewModelModule::class,
        TokoChatImageAttachmentNetworkModule::class
    ],
    dependencies = [BaseAppComponent::class, TokoChatConfigComponent::class]
)
interface TokoChatComponent {
    fun inject(fragment: TokoChatFragment)
}
