package com.tokopedia.tokofood.feature.search.container.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokofood.feature.search.container.di.scope.SearchContainerScope
import com.tokopedia.tokofood.feature.search.container.presentation.viewmodel.SearchContainerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class SearchContainerViewModelModule {

    @Binds
    @SearchContainerScope
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SearchContainerViewModel::class)
    internal abstract fun provideSearchContainerViewModel(viewModel: SearchContainerViewModel): ViewModel
}