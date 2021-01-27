package com.tokopedia.topchat.chatsetting.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.topchat.chatsetting.viewmodel.ChatSettingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ChatSettingViewsModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ChatSettingViewModel::class)
    abstract fun provideChatTabCounterViewModel(viewModel: ChatSettingViewModel): ViewModel

    @Binds
    @ChatSettingScope
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

}