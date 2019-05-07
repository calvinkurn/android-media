package com.tokopedia.search.result.data.gql.searchproduct;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.common.domain.Repository;
import com.tokopedia.discovery.common.repository.gql.GqlRepository;
import com.tokopedia.discovery.common.repository.gql.GqlSpecification;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.result.domain.model.SearchProductModel;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module
public class GqlSearchProductSpecModule {

    @SearchScope
    @Provides
    @Named(SearchConstant.GQL.GQL_SEARCH_PRODUCT_FIRST_PAGE)
    GqlSpecification provideGqlSearchProductFirstPageSpec(@ApplicationContext Context context) {
        return new GqlSearchProductFirstPageSpec(context);
    }

    @SearchScope
    @Provides
    @Named(SearchConstant.GQL.GQL_SEARCH_PRODUCT_LOAD_MORE)
    GqlSpecification provideGqlSearchProductLoadMoreSpec(@ApplicationContext Context context) {
        return new GqlSearchProductLoadMoreSpec(context);
    }

    @SearchScope
    @Provides
    @Named(SearchConstant.GQL.GQL_SEARCH_PRODUCT_FIRST_PAGE)
    Repository<SearchProductModel> provideSearchProductFirstPageRepository(@Named(SearchConstant.GQL.GQL_SEARCH_PRODUCT_FIRST_PAGE)GqlSpecification specification) {
        return new GqlRepository<>(specification);
    }

    @SearchScope
    @Provides
    @Named(SearchConstant.GQL.GQL_SEARCH_PRODUCT_LOAD_MORE)
    Repository<SearchProductModel> provideSearchProductLoadMoreRepository(@Named(SearchConstant.GQL.GQL_SEARCH_PRODUCT_LOAD_MORE)GqlSpecification specification) {
        return new GqlRepository<>(specification);
    }
}
