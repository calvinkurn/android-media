package com.tokopedia.search.result.data.mapper.searchprofile

import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.search.result.domain.model.SearchProfileModel
import dagger.Module
import dagger.Provides
import rx.functions.Func1

private class SearchProfileMapper : Func1<GraphqlResponse, SearchProfileModel> {

    override fun call(graphqlResponse: GraphqlResponse?): SearchProfileModel {
        if(graphqlResponse == null) return SearchProfileModel()

        return graphqlResponse.getData<SearchProfileModel>(SearchProfileModel::class.java)
    }
}

@SearchScope
@Module
class SearchProfileMapperModule {

    @SearchScope
    @Provides
    fun provideSearchProfileMapper() : Func1<GraphqlResponse, SearchProfileModel> {
        return SearchProfileMapper()
    }
}