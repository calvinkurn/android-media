package com.tokopedia.search.result.data.gql.initiatesearch;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.common.domain.Repository;
import com.tokopedia.discovery.common.repository.gql.GqlRepository;
import com.tokopedia.discovery.common.repository.gql.GqlSpecification;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.discovery.newdiscovery.domain.model.InitiateSearchModel;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module
public class GqlInitiateSearchModule {

    @SearchScope
    @Provides
    @Named(SearchConstant.GQL.GQL_INITIATE_SEARCH)
    GqlSpecification provideGqlInitiateSearchSpec(@ApplicationContext Context context) {
        return new GqlInitiateSearchSpec(context);
    }
}
