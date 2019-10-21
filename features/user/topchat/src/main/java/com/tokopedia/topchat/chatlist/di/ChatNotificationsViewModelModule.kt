package com.tokopedia.topchat.chatlist.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.topchat.chatlist.viewmodel.ChatItemListViewModel
import com.tokopedia.topchat.chatlist.viewmodel.ChatListViewModel
import com.tokopedia.topchat.chatlist.viewmodel.ChatTabCounterViewModel
import com.tokopedia.topchat.chatlist.viewmodel.WebSocketViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author : Steven 2019-08-08
 */

@Module
@ChatListScope
abstract class ChatNotificationsViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ChatTabCounterViewModel::class)
    internal abstract fun provideChatTabCounterViewModel(viewModel: ChatTabCounterViewModel): ViewModel

}