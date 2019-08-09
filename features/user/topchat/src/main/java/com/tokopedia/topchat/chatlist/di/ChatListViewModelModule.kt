package com.tokopedia.topchat.chatlist.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.topchat.chatlist.viewmodel.ChatItemListViewModel
import com.tokopedia.topchat.chatlist.viewmodel.ChatListViewModel
import com.tokopedia.topchat.chatlist.viewmodel.WebSocketViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author : Steven 2019-08-08
 */

@Module
@ChatListScope
abstract class ChatListViewModelModule {

    @Binds
    @ChatListScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ChatItemListViewModel::class)
    internal abstract fun getChatItemList(viewModel: ChatItemListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WebSocketViewModel::class)
    internal abstract fun provideWebsocket(viewModel: WebSocketViewModel): ViewModel

}