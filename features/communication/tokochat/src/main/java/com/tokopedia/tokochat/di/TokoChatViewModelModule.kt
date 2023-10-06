package com.tokopedia.tokochat.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.tokochat.view.chatlist.TokoChatListViewModel
import com.tokopedia.tokochat.view.chatroom.TokoChatViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TokoChatViewModelModule {
    @Binds
    @ActivityScope
    internal abstract fun bindViewModelFactory(
        viewModelFactory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(TokoChatViewModel::class)
    internal abstract fun bindTokoChatViewModel(
        viewModel: TokoChatViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(TokoChatListViewModel::class)
    internal abstract fun bindTokoChatListViewModel(
        viewModel: TokoChatListViewModel
    ): ViewModel
}
