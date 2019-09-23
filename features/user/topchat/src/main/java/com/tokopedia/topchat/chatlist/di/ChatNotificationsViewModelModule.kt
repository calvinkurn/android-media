package com.tokopedia.topchat.chatlist.di

import android.arch.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.topchat.chatlist.viewmodel.ChatTabCounterViewModel
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