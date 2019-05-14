package com.tokopedia.search.result.data.gql.dynamicfilter;

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
public class GqlDynamicFilterModule {

    @SearchScope
    @Provides
    @Named(SearchConstant.GQL.GQL_DYNAMIC_FILTER_SPEC)
    GqlSpecification provideGqlDynamicFilterSpec(@ApplicationContext Context context) {
        return new GqlDynamicFilterSpec(context);
    }
}