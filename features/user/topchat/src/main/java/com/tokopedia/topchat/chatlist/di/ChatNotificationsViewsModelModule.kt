package com.tokopedia.topchat.chatlist.di

import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.topchat.chatlist.viewmodel.ChatTabCounterViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author : Steven 2019-08-08
 */

@Module
abstract class ChatNotificationsViewsModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ChatTabCounterViewModel::class)
    internal abstract fun provideChatTabCounterViewModel(viewModel: ChatTabCounterViewModel): ViewModel

}