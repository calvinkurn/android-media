package com.tokopedia.topchat.stub.chatroom.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.topchat.chatroom.di.ChatScope
import com.tokopedia.topchat.chatroom.view.viewmodel.StickerViewModel
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ChatRoomFakeViewModelModule {

    @Binds
    @ChatScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @ChatScope
    @IntoMap
    @ViewModelKey(TopChatViewModel::class)
    internal abstract fun bindTopChatViewModel(viewModel: TopChatViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StickerViewModel::class)
    internal abstract fun getStickerViewModel(viewModel: StickerViewModel): ViewModel
}