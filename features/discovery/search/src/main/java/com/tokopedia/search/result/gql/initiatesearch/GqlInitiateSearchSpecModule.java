package com.tokopedia.search.result.gql.initiatesearch;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.common.repository.gql.GqlSpecification;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module
public class GqlInitiateSearchSpecModule {

    @SearchScope
    @Provides
    @Named(SearchConstant.GQL_INITIATE_SEARCH)
    GqlSpecification provideGqlInitiateSearchSpec(@ApplicationContext Context context) {
        return new GqlInitiateSearchSpec(context);
    }
}
