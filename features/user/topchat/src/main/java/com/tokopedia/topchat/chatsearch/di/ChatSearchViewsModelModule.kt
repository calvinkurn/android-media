package com.tokopedia.topchat.chatsearch.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.topchat.chatsearch.viewmodel.ChatContactLoadMoreViewModel
import com.tokopedia.topchat.chatsearch.viewmodel.ChatSearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ChatSearchViewsModelModule {

    @Binds
    @ChatSearchScope
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ChatSearchViewModel::class)
    abstract fun provideChatSearchViewModel(viewModel: ChatSearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChatContactLoadMoreViewModel::class)
    abstract fun provideChatSearchContactDetailViewModel(viewModel: ChatContactLoadMoreViewModel): ViewModel

}