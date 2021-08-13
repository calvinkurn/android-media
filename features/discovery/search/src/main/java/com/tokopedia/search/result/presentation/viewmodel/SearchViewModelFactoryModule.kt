package com.tokopedia.search.result.presentation.viewmodel

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
internal class SearchViewModelFactoryModule {

    @SearchScope
    @Provides
    @Named(SearchConstant.SEARCH_VIEW_MODEL_FACTORY)
    fun provideSearchViewModelFactory(coroutineDispatchers: CoroutineDispatchers): ViewModelProvider.Factory {
        return SearchViewModelFactory(coroutineDispatchers)
    }
}