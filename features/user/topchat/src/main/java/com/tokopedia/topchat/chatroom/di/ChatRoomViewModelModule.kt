package com.tokopedia.topchat.chatroom.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.topchat.chatroom.view.viewmodel.StickerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ChatRoomViewModelModule {

    @Binds
    @ChatScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(StickerViewModel::class)
    internal abstract fun getStickerViewModel(viewModel: StickerViewModel): ViewModel

}