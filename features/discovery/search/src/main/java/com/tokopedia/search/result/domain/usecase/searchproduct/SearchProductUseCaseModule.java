package com.tokopedia.search.result.domain.usecase.searchproduct;

import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.search.di.scope.SearchScope;
import com.tokopedia.search.result.data.mapper.searchproduct.SearchProductMapperModule;
import com.tokopedia.search.result.domain.model.SearchProductModel;
import com.tokopedia.usecase.UseCase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import rx.functions.Func1;

@SearchScope
@Module(includes = {
        SearchProductMapperModule.class
})
public class SearchProductUseCaseModule {

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchProduct.SEARCH_PRODUCT_FIRST_PAGE_USE_CASE)
    UseCase<SearchProductModel> provideSearchProductFirstPageUseCase(
            Func1<GraphqlResponse, SearchProductModel> searchProductModelMapper
    ) {
        return new SearchProductFirstPageGqlUseCase(new GraphqlUseCase(), searchProductModelMapper);
    }

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchProduct.SEARCH_PRODUCT_LOAD_MORE_USE_CASE)
    UseCase<SearchProductModel> provideSearchProductLoadMoreUseCase(
            Func1<GraphqlResponse, SearchProductModel> searchProductModelMapper
    ) {
        return new SearchProductLoadMoreGqlUseCase(new GraphqlUseCase(), searchProductModelMapper);
    }
}
