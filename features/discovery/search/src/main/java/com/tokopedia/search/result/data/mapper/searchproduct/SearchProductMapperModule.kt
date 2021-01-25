package com.tokopedia.search.result.data.mapper.searchproduct

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.domain.model.SearchProductModel
import dagger.Module
import dagger.Provides
import rx.functions.Func1

@Module
class SearchProductMapperModule {
    @SearchScope
    @Provides
    fun provideSearchProductModelMapper(): Func1<GraphqlResponse?, SearchProductModel?> {
        return SearchProductMapper()
    }
}