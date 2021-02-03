package com.tokopedia.topchat.chatlist.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.topchat.chatlist.viewmodel.ChatItemListViewModel
import com.tokopedia.topchat.chatlist.viewmodel.ChatListWebSocketViewModel
import com.tokopedia.topchat.chatlist.viewmodel.WebSocketViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author : Steven 2019-08-08
 */

@Module
abstract class ChatListViewsModelModule {

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

    @Binds
    @IntoMap
    @ViewModelKey(ChatListWebSocketViewModel::class)
    internal abstract fun provideChatListWebsocket(
            viewModel: ChatListWebSocketViewModel
    ): ViewModel

}