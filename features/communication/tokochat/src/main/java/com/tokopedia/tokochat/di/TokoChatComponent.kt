package com.tokopedia.tokochat.di

import com.tokochat.tokochat_config_common.di.component.TokoChatConfigComponent
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.tokochat.view.chatroom.TokoChatFragment
import dagger.Component

@ActivityScope
@Component(
    modules = [
        TokoChatModule::class,
        TokoChatUseCaseModule::class,
        TokoChatViewModelModule::class,
        TokoChatImageAttachmentNetworkModule::class
    ],
    dependencies = [BaseAppComponent::class, TokoChatConfigComponent::class]
)
interface TokoChatComponent {
    fun inject(fragment: TokoChatFragment)
}
