package com.tokopedia.search.result.data.mapper.searchshop

import com.tokopedia.search.di.scope.SearchScope
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
    fun provideSearchShopMapper(): Func1<GraphqlResponse, SearchShopModel> {
        return SearchShopMapper()
    }
}
