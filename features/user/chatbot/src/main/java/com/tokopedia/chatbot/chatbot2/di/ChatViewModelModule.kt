package com.tokopedia.chatbot.chatbot2.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.chatbot.chatbot2.view.viewmodel.ChatbotViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ChatViewModelModule {

    @Binds
    @ChatbotScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @ChatbotScope
    @IntoMap
    @ViewModelKey(ChatbotViewModel::class)
    internal abstract fun bindTopChatViewModel(viewModel: ChatbotViewModel): ViewModel
}
