package com.tokopedia.tokofood.feature.search.initialstate.di.module

import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokofood.feature.search.initialstate.presentation.viewmodel.InitialStateSearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class InitialStateViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(InitialStateSearchViewModel::class)
    internal abstract fun provideInitialStateSearchViewModel(viewModel: InitialStateSearchViewModel): ViewModel
}
