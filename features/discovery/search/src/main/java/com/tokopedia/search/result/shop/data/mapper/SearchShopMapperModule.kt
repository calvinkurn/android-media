package com.tokopedia.search.result.shop.data.mapper

import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import dagger.Module
import dagger.Provides
import rx.functions.Func1

@Module
internal class SearchShopMapperModule {

    @SearchScope
    @Provides
    fun provideSearchShopMapper(): Func1<GraphqlResponse, SearchShopModel> {
        return SearchShopMapper()
    }
}
