package com.tokopedia.search.result.data.mapper.initiatesearch;

import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.discovery.newdiscovery.domain.model.InitiateSearchModel;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import dagger.Module;
import dagger.Provides;
import rx.functions.Func1;

@SearchScope
@Module
public class InitiateSearchMapperModule {

    @SearchScope
    @Provides
    Func1<GraphqlResponse, InitiateSearchModel> provideInitiateSearchMapper() {
        return new InitiateSearchMapper();
    }
}
