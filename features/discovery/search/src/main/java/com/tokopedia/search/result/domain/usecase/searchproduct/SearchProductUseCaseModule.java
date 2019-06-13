package com.tokopedia.search.result.domain.usecase.searchproduct;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.search.R;
import com.tokopedia.search.result.data.mapper.searchproduct.SearchProductMapperModule;
import com.tokopedia.search.result.domain.model.SearchProductModel;
import com.tokopedia.usecase.UseCase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import rx.functions.Func1;

@SearchScope
@Module(includes = SearchProductMapperModule.class)
public class SearchProductUseCaseModule {

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchProduct.SEARCH_PRODUCT_FIRST_PAGE_USE_CASE)
    UseCase<SearchProductModel> provideSearchProductFirstPageUseCase(
            @ApplicationContext Context context,
            Func1<GraphqlResponse, SearchProductModel> searchProductModelMapper
    ) {
        GraphqlRequest graphqlRequest = new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.gql_search_product_first_page),
                SearchProductModel.class
        );

        return new SearchProductFirstPageGqlUseCase(graphqlRequest, new GraphqlUseCase(), searchProductModelMapper);
    }

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchProduct.SEARCH_PRODUCT_LOAD_MORE_USE_CASE)
    UseCase<SearchProductModel> provideSearchProductLoadMoreUseCase(
            @ApplicationContext Context context,
            Func1<GraphqlResponse, SearchProductModel> searchProductModelMapper
    ) {
        GraphqlRequest graphqlRequest = new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.gql_search_product_load_more),
                SearchProductModel.class
        );

        return new SearchProductLoadMoreGqlUseCase(graphqlRequest, new GraphqlUseCase(), searchProductModelMapper);
    }
}
