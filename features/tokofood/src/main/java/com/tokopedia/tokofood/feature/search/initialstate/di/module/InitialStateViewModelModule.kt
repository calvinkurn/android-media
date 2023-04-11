package com.tokopedia.tokofood.feature.search.initialstate.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokofood.feature.search.di.scope.TokoFoodSearchScope
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