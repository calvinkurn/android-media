package com.tokopedia.talk.feature.inbox.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.talk.feature.inbox.presentation.viewmodel.TalkInboxViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TalkInboxViewModelModule {

    @Binds
    @TalkInboxScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TalkInboxViewModel::class)
    internal abstract fun talkInboxViewModel(viewModel: TalkInboxViewModel): ViewModel
}