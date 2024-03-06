package com.tokopedia.search.di.module

import com.tokopedia.discovery.common.analytics.SearchEntrance
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides

@Module
class SearchEntranceModule {

    @SearchScope
    @Provides
    fun providesSearchEntrance(): SearchEntrance = SearchEntrance()
}
