package com.tokopedia.search.result.data.mapper.dynamicfilter

import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides
import rx.functions.Func1

@Module
class DynamicFilterGqlMapperModule {
    @SearchScope
    @Provides
    fun provideDynamicFilterModelMapper(): Func1<GraphqlResponse?, DynamicFilterModel?> {
        return DynamicFilterGqlMapper()
    }
}