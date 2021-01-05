package com.tokopedia.search.result.domain.usecase.searchproduct;

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.search.di.scope.SearchScope;
import com.tokopedia.search.result.data.mapper.searchproduct.SearchProductMapperModule;
import com.tokopedia.search.result.domain.model.SearchProductModel;
import com.tokopedia.search.utils.SearchLogger;
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase;
import com.tokopedia.topads.sdk.repository.TopAdsRepository;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import rx.functions.Func1;

import static com.tokopedia.remoteconfig.RemoteConfigKey.ENABLE_SEARCH_TDN;

@SearchScope
@Module(includes = {
        SearchProductMapperModule.class
})
public class SearchProductUseCaseModule {

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchProduct.SEARCH_PRODUCT_FIRST_PAGE_USE_CASE)
    UseCase<SearchProductModel> provideSearchProductFirstPageUseCase(
            Func1<GraphqlResponse, SearchProductModel> searchProductModelMapper,
            RemoteConfig remoteConfig,
            UserSessionInterface userSession
    ) {
        // Temporarily keep 2 search use case for TDN and without TDN
        if (remoteConfig.getBoolean(ENABLE_SEARCH_TDN)) {
            TopAdsImageViewUseCase topAdsImageViewUseCase = new TopAdsImageViewUseCase(
                    userSession.getUserId(),
                    new TopAdsRepository()
            );

            return new SearchProductTDNFirstPageGqlUseCase(
                    new GraphqlUseCase(),
                    searchProductModelMapper,
                    topAdsImageViewUseCase,
                    CoroutineDispatchersProvider.INSTANCE,
                    new SearchLogger()
            );
        }

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
