package com.tokopedia.search.result.domain.usecase;

import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.common.domain.Repository;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.result.domain.model.SearchProductModel;
import com.tokopedia.usecase.UseCase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module
public class SearchProductUseCaseModule {

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchProduct.SEARCH_PRODUCT_FIRST_PAGE_USE_CASE)
    UseCase<SearchProductModel> provideSearchProductFirstPageUseCase(
            @Named(SearchConstant.GQL.GQL_SEARCH_PRODUCT_FIRST_PAGE) Repository<SearchProductModel> searchProductFirstPageRepository
    ) {
        return new SearchProductFirstPageUseCase(searchProductFirstPageRepository);
    }

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchProduct.SEARCH_PRODUCT_LOAD_MORE_USE_CASE)
    UseCase<SearchProductModel> provideSearchProductLoadMoreUseCase(
            @Named(SearchConstant.GQL.GQL_SEARCH_PRODUCT_LOAD_MORE) Repository<SearchProductModel> searchProductLoadMoreRepository
    ) {
        return new SearchProductLoadMoreUseCase(searchProductLoadMoreRepository);
    }
}
