package com.tokopedia.search.result.presentation.viewmodel

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.coroutines.ProductionDispatcherProvider
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides
import javax.inject.Named

@SearchScope
@Module
internal class SearchViewModelFactoryModule {

    @SearchScope
    @Provides
    @Named(SearchConstant.SEARCH_VIEW_MODEL_FACTORY)
    fun provideSearchViewModelFactory(): ViewModelProvider.Factory {
        return SearchViewModelFactory(ProductionDispatcherProvider())
    }
}