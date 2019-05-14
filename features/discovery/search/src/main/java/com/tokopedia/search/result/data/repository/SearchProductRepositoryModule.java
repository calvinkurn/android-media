package com.tokopedia.search.result.data.repository;

import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.common.domain.Repository;
import com.tokopedia.discovery.common.repository.gql.GqlSpecification;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.result.data.gql.searchproduct.GqlSearchProductSpecModule;
import com.tokopedia.search.result.domain.model.SearchProductModel;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module(includes = GqlSearchProductSpecModule.class)
public class SearchProductRepositoryModule {

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchProduct.SEARCH_PRODUCT_FIRST_PAGE_REPOSITORY)
    Repository<SearchProductModel> provideSearchProductFirstPageRepository(
            @Named(SearchConstant.GQL.GQL_SEARCH_PRODUCT_FIRST_PAGE) GqlSpecification specification) {
        return new SearchProductFirstPageGqlRepository(specification);
    }

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchProduct.SEARCH_PRODUCT_LOAD_MORE_REPOSITORY)
    Repository<SearchProductModel> provideSearchProductLoadMoreRepository(
            @Named(SearchConstant.GQL.GQL_SEARCH_PRODUCT_LOAD_MORE)GqlSpecification specification) {
        return new SearchProductLoadMoreGqlRepository(specification);
    }
}
