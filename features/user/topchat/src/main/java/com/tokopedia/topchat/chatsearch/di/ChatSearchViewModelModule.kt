package com.tokopedia.topchat.chatsearch.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.topchat.chatsearch.viewmodel.ChatSearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@ChatSearchScope
abstract class ChatSearchViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ChatSearchViewModel::class)
    abstract fun provideChatSearchViewModel(viewModel: ChatSearchViewModel): ViewModel

    @Binds
    @ChatSearchScope
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

}