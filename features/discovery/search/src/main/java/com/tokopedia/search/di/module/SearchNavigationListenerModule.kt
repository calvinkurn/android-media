package com.tokopedia.search.di.module

import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.presentation.view.listener.SearchNavigationListener
import dagger.Module
import dagger.Provides

@Module
internal class SearchNavigationListenerModule(
    private val searchNavigationListener: SearchNavigationListener?
) {

    @SearchScope
    @Provides
    fun provideSearchNavigationListener(): SearchNavigationListener? = searchNavigationListener
}
