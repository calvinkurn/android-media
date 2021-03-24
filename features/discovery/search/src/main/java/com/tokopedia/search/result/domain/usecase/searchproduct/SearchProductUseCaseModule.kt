package com.tokopedia.search.result.domain.usecase.searchproduct

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.data.mapper.searchproduct.SearchProductMapperModule
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.utils.SearchLogger
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.repository.TopAdsRepository
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import rx.functions.Func1
import javax.inject.Named

@Module(includes = [SearchProductMapperModule::class])
class SearchProductUseCaseModule {
    @SearchScope
    @Provides
    @Named(SearchConstant.SearchProduct.SEARCH_PRODUCT_FIRST_PAGE_USE_CASE)
    fun provideSearchProductFirstPageUseCase(
            searchProductModelMapper: Func1<GraphqlResponse?, SearchProductModel?>,
            remoteConfig: RemoteConfig,
            userSession: UserSessionInterface
    ): UseCase<SearchProductModel> {
        // Temporarily keep 2 search use case for TDN and without TDN
        if (remoteConfig.getBoolean(RemoteConfigKey.ENABLE_SEARCH_TDN)) {
            val topAdsImageViewUseCase = TopAdsImageViewUseCase(
                    userSession.userId,
                    TopAdsRepository()
            )
            return SearchProductTDNFirstPageGqlUseCase(
                    GraphqlUseCase(),
                    searchProductModelMapper,
                    topAdsImageViewUseCase,
                    CoroutineDispatchersProvider,
                    SearchLogger()
            )
        }
        return SearchProductFirstPageGqlUseCase(GraphqlUseCase(), searchProductModelMapper)
    }

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchProduct.SEARCH_PRODUCT_LOAD_MORE_USE_CASE)
    fun provideSearchProductLoadMoreUseCase(
            searchProductModelMapper: Func1<GraphqlResponse?, SearchProductModel?>
    ): UseCase<SearchProductModel> {
        return SearchProductLoadMoreGqlUseCase(GraphqlUseCase(), searchProductModelMapper)
    }
}