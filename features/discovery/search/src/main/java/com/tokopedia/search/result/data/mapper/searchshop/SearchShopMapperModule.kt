package com.tokopedia.search.result.data.mapper.searchshop

import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.search.result.domain.model.SearchShopModel
import dagger.Module
import dagger.Provides
import rx.functions.Func1

@SearchScope
@Module
class SearchShopMapperModule {

    @SearchScope
    @Provides
    internal fun provideSearchShopMapper(): Func1<GraphqlResponse, SearchShopModel> {
        return SearchShopMapper()
    }
}
