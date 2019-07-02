package com.tokopedia.search.result.data.mapper.searchproduct;

import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.search.result.domain.model.SearchProductModel;

import dagger.Module;
import dagger.Provides;
import rx.functions.Func1;

@SearchScope
@Module
public class SearchProductMapperModule {

    @SearchScope
    @Provides
    Func1<GraphqlResponse, SearchProductModel> provideSearchProductModelMapper() {
        return new SearchProductMapper();
    }
}
