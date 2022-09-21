package com.tokopedia.chat_service.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.chat_service.view.viewmodel.TokoChatViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TokoChatViewModelModule {
    @Binds
    @TokoChatScope
    internal abstract fun bindViewModelFactory(
        viewModelFactory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @TokoChatScope
    @ViewModelKey(TokoChatViewModel::class)
    internal abstract fun bindChatServiceViewModel(
        viewModel: TokoChatViewModel
    ): ViewModel
}
