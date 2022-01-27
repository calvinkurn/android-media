package com.tokopedia.topchat.stub.chattemplate.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.topchat.chattemplate.view.viewmodel.ChatTemplateViewModel
import com.tokopedia.topchat.chattemplate.view.viewmodel.EditTemplateViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TemplateChatViewModelModuleStub {
    @Binds
    @ActivityScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(ChatTemplateViewModel::class)
    internal abstract fun bindChatTemplateViewModel(viewModel: ChatTemplateViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(EditTemplateViewModel::class)
    internal abstract fun bindEditTemplateViewModel(viewModel: EditTemplateViewModel): ViewModel
}