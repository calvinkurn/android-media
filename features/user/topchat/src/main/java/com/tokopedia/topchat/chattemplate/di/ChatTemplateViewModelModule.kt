package com.tokopedia.topchat.chattemplate.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.topchat.chattemplate.view.viewmodel.ChatTemplateViewModel
import com.tokopedia.topchat.chattemplate.view.viewmodel.EditTemplateViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ChatTemplateViewModelModule {
    @Binds
    @TemplateChatScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @TemplateChatScope
    @IntoMap
    @ViewModelKey(ChatTemplateViewModel::class)
    internal abstract fun bindChatTemplateViewModel(viewModel: ChatTemplateViewModel): ViewModel

    @Binds
    @TemplateChatScope
    @IntoMap
    @ViewModelKey(EditTemplateViewModel::class)
    internal abstract fun bindEditTemplateViewModel(viewModel: EditTemplateViewModel): ViewModel
}