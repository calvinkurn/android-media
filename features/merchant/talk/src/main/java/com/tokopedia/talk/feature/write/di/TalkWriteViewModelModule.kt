package com.tokopedia.talk.feature.write.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.talk.feature.write.presentation.viewmodel.TalkWriteViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TalkWriteViewModelModule {

    @Binds
    @TalkWriteScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TalkWriteViewModel::class)
    internal abstract fun talkWriteViewModel(viewModel: TalkWriteViewModel): ViewModel
}