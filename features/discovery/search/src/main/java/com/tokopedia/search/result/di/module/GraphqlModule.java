package com.tokopedia.search.result.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.discovery.newdiscovery.helper.GqlSearchQueryHelper;
import com.tokopedia.graphql.domain.GraphqlUseCase;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module
public class GraphqlModule {

    @SearchScope
    @Provides
    GraphqlUseCase provideGraphqlUseCase() {
        return new GraphqlUseCase();
    }

    @SearchScope
    @Provides
    GqlSearchQueryHelper provideGqlQueryHelper(@ApplicationContext Context applicationContext) {
        return new GqlSearchQueryHelper(applicationContext);
    }
}
