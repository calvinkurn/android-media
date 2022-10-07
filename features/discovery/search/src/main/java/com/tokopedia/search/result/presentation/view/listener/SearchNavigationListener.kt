package com.tokopedia.search.result.presentation.view.listener

import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides

interface SearchNavigationListener {
    fun removeSearchPageLoading()
    fun updateSearchParameter(searchParameter: SearchParameter?)
    fun updateSearchBarNotification()
}

@Module
internal class SearchNavigationListenerModule(
    private val searchNavigationListener: SearchNavigationListener?
) {

    @SearchScope
    @Provides
    fun provideSearchNavigationListener(): SearchNavigationListener? = searchNavigationListener
}
