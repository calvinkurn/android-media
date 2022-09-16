package com.tokopedia.chat_service.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.chat_service.view.fragment.ChatServiceFragment
import com.tokopedia.chat_service.view.fragment.ChatServiceListFragment
import dagger.Component

@ChatServiceScope
@Component(
    modules = [
        ChatServiceModule::class,
        ChatServiceNetworkModule::class,
        ChatServiceUseCaseModule::class,
        ChatServiceContextModule::class,
        ChatServiceViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface ChatServiceComponent {
    fun inject(fragment: ChatServiceFragment)
    fun inject(fragment: ChatServiceListFragment)
}
