package com.tokopedia.chat_service.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.chat_service.view.fragment.TokoChatFragment
import com.tokopedia.chat_service.view.fragment.TokoChatListFragment
import dagger.Component

@TokoChatScope
@Component(
    modules = [
        TokoChatModule::class,
        TokoChatNetworkModule::class,
        TokoChatUseCaseModule::class,
        TokoChatContextModule::class,
        TokoChatViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface TokoChatComponent {
    fun inject(fragment: TokoChatFragment)
    fun inject(fragment: TokoChatListFragment)
}
