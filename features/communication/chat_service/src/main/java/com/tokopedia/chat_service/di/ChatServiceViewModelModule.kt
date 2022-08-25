package com.tokopedia.chat_service.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.chat_service.view.viewmodel.ChatServiceViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ChatServiceViewModelModule {
    @Binds
    @ChatServiceScope
    internal abstract fun bindViewModelFactory(
        viewModelFactory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ChatServiceScope
    @ViewModelKey(ChatServiceViewModel::class)
    internal abstract fun bindChatServiceViewModel(
        viewModel: ChatServiceViewModel
    ): ViewModel
}