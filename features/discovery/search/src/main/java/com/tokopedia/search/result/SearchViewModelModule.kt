package com.tokopedia.search.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(
    includes = [
        SearchStateModule::class
    ]
)
internal class SearchViewModelModule {

    @Provides
    @SearchScope
    fun provideViewModelProviders(
        viewModelProvider: ViewModelFactory
    ): ViewModelProvider.Factory = viewModelProvider

    @Provides
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    @SearchScope
    fun provideSearchViewModel(
        searchState: SearchState,
        coroutineDispatchers: CoroutineDispatchers,
    ): ViewModel =
        SearchViewModel(searchState, coroutineDispatchers)
}
