package com.tokopedia.tokopedianow.search.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokopedianow.search.domain.usecase.GetSearchUseCaseModule
import com.tokopedia.tokopedianow.search.presentation.viewmodel.SearchViewModel
import com.tokopedia.tokopedianow.searchcategory.di.GraphqlModule
import com.tokopedia.tokopedianow.searchcategory.domain.usecase.GetFilterUseCaseModule
import com.tokopedia.tokopedianow.searchcategory.domain.usecase.GetProductCountUseCaseModule
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [
    GetSearchUseCaseModule::class,
    GetFilterUseCaseModule::class,
    GetProductCountUseCaseModule::class,
    GraphqlModule::class,
])
abstract class SearchViewModelModule {

    @SearchScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    internal abstract fun searchViewModel(viewModel: SearchViewModel): ViewModel
}