package com.tokopedia.tokofood.feature.search.initialstate.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokofood.common.di.TokoFoodScope
import com.tokopedia.tokofood.feature.search.container.presentation.viewmodel.SearchContainerViewModel
import com.tokopedia.tokofood.feature.search.initialstate.di.scope.InitialStateScope
import com.tokopedia.tokofood.feature.search.initialstate.presentation.viewmodel.InitialStateSearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class InitialStateViewModelModule {

    @Binds
    @InitialStateScope
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(InitialStateSearchViewModel::class)
    internal abstract fun provideInitialStateSearchViewModel(viewModel: InitialStateSearchViewModel): ViewModel
}