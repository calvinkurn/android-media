package com.tokopedia.talk.feature.reading.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.talk.feature.reading.presentation.viewmodel.TalkReadingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TalkReadingViewModelModule {

    @Binds
    @TalkReadingScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TalkReadingViewModel::class)
    internal abstract fun talkReadingViewModel(viewModel: TalkReadingViewModel): ViewModel
}