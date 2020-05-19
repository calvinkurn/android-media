package com.tokopedia.home.beranda.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.atc_common.domain.mapper.AddToCartDataMapper
import com.tokopedia.atc_common.domain.usecase.AddToCartOccUseCase
import com.tokopedia.common_wallet.balance.data.entity.WalletBalanceResponse
import com.tokopedia.common_wallet.pendingcashback.data.ResponsePendingCashback
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.mapper.FeedTabMapper
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.mapper.HomeRecommendationMapper
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.data.model.TokopointsDrawerHomeData
import com.tokopedia.home.beranda.data.repository.HomeRepository
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.domain.gql.CloseChannelMutation
import com.tokopedia.home.beranda.domain.gql.ProductrevDismissSuggestion
import com.tokopedia.home.beranda.domain.gql.feed.HomeFeedContentGqlResponse
import com.tokopedia.home.beranda.domain.gql.feed.HomeFeedTabGqlResponse
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.domain.usecase.coroutine.StickyLoginUseCase
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class HomeUseCaseModule {

    @HomeScope
    @Provides
    fun graphqlUseCase(): GraphqlUseCase {
        return GraphqlUseCase()
    }

    @HomeScope
    @Provides
    fun provideGraphqlUseCase(graphqlRepository: GraphqlRepository) =
            com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<Any>(graphqlRepository)

    @HomeScope
    @Provides
    fun homeUseCase(homeRepository: HomeRepository, homeDataMapper: HomeDataMapper) = HomeUseCase(homeRepository, homeDataMapper)

    @Provides
    fun provideSendGeolocationInfoUseCase(homeRepository: HomeRepository): SendGeolocationInfoUseCase {
        return SendGeolocationInfoUseCase(homeRepository)
    }

    @Provides
    fun provideGetHomeRecommendationUseCase(
            @ApplicationContext context: Context,
            graphqlRepository: GraphqlRepository,
            homeRecommendationMapper: HomeRecommendationMapper
    ): GetHomeRecommendationUseCase{
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.gql_home_feed)
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeFeedContentGqlResponse>(graphqlRepository)
        useCase.setGraphqlQuery(query)
        return GetHomeRecommendationUseCase(useCase, homeRecommendationMapper)
    }

    @Provides
    fun provideSendTopAdsUseCase() = SendTopAdsUseCase()

    @Provides
    fun provideGetFeedTabUseCase(@ApplicationContext context: Context?,
                                 graphqlUseCase: GraphqlUseCase?,
                                 feedTabMapper: FeedTabMapper?): GetFeedTabUseCase {
        return GetFeedTabUseCase(context, graphqlUseCase, feedTabMapper)
    }

    @Provides
    @HomeScope
    fun provideStickyLoginUseCase(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository): StickyLoginUseCase {
        val query = GraphqlHelper.loadRawString(context.resources, com.tokopedia.stickylogin.R.raw.gql_sticky_login_query)
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<StickyLoginTickerPojo.TickerResponse>(graphqlRepository)
        usecase.setGraphqlQuery(query)
        return StickyLoginUseCase(usecase)
    }

    @Provides
    @HomeScope
    fun provideHomeReviewSuggestedUseCase(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository): GetHomeReviewSuggestedUseCase {
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.suggested_review_query)
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<SuggestedProductReview>(graphqlRepository)
        usecase.setGraphqlQuery(query)
        return GetHomeReviewSuggestedUseCase(usecase)
    }

    @Provides
    @HomeScope
    fun provideDismissHomeReviewUseCase(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository): DismissHomeReviewUseCase {
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.dismiss_suggested_query)
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<ProductrevDismissSuggestion>(graphqlRepository)
        usecase.setGraphqlQuery(query)
        return DismissHomeReviewUseCase(usecase)
    }

    @Provides
    @HomeScope
    fun provideHomeTokopointsDataUseCase(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository): GetHomeTokopointsDataUseCase {
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.home_gql_tokopoints_details)
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<TokopointsDrawerHomeData>(graphqlRepository)
        usecase.setGraphqlQuery(query)
        return GetHomeTokopointsDataUseCase(usecase)
    }

    @Provides
    @HomeScope
    fun provideGetPlayLiveDynamicDataUseCase(graphqlRepository: GraphqlRepository): GetPlayLiveDynamicUseCase {
        return GetPlayLiveDynamicUseCase(com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase(graphqlRepository))
    }

    @HomeScope
    @Provides
    fun getKeywordSearchUseCase(graphqlRepository: GraphqlRepository): GetKeywordSearchUseCase {
        return GetKeywordSearchUseCase(com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase(graphqlRepository))
    }

    @HomeScope
    @Provides
    fun getCoroutineWalletBalanceUseCase(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository, userSession: UserSessionInterface, remoteConfig: RemoteConfig, localCacheHandler: LocalCacheHandler): GetCoroutineWalletBalanceUseCase {
        val query = GraphqlHelper.loadRawString(context.resources, com.tokopedia.common_wallet.R.raw.wallet_balance_query)
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<WalletBalanceResponse>(graphqlRepository)
        usecase.setGraphqlQuery(query)
        return GetCoroutineWalletBalanceUseCase(usecase, remoteConfig, userSession, localCacheHandler)
    }

    @HomeScope
    @Provides
    fun getCoroutinePendingCashbackUseCase(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository): GetCoroutinePendingCashbackUseCase {
        val query = GraphqlHelper.loadRawString(context.resources, com.tokopedia.common_wallet.R.raw.wallet_pending_cashback_query)
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<ResponsePendingCashback>(graphqlRepository)
        usecase.setGraphqlQuery(query)
        return GetCoroutinePendingCashbackUseCase(usecase)
    }

    @HomeScope
    @Provides
    fun getBusinessWidgetTab(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository): GetBusinessWidgetTab {
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.query_tab_business_widget)
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeWidget.Data>(graphqlRepository)
        usecase.setGraphqlQuery(query)
        return GetBusinessWidgetTab(usecase)
    }

    @HomeScope
    @Provides
    fun getBusinessUnitDataTab(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository): GetBusinessUnitDataUseCase {
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.query_content_tab_business_widget)
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeWidget.Data>(graphqlRepository)
        usecase.setGraphqlQuery(query)
        return GetBusinessUnitDataUseCase(usecase)
    }

    @HomeScope
    @Provides
    fun getRecommendationTabUseCase(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository): GetRecommendationTabUseCase {
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.gql_home_feed_tab)
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeFeedTabGqlResponse>(graphqlRepository)
        usecase.setGraphqlQuery(query)
        return GetRecommendationTabUseCase(usecase)
    }

    @Provides
    @HomeScope
    fun providePopularKeywordUseCase(graphqlUseCase: com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<Any>): GetPopularKeywordUseCase {
        return GetPopularKeywordUseCase(graphqlUseCase as com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeWidget.PopularKeywordQuery>)
    }

    @Provides
    @HomeScope
    fun provideGetDynamicChannels(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository, homeDataMapper: HomeDataMapper): GetDynamicChannelsUseCase{
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.dynamic_channel_query)
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeData>(graphqlRepository)
        useCase.setGraphqlQuery(query)
        return GetDynamicChannelsUseCase(useCase, homeDataMapper)
    }

    @Provides
    @HomeScope
    fun provideAddToCartOccUseCase(@ApplicationContext context: Context, graphqlUseCase: GraphqlUseCase): AddToCartOccUseCase{
        val query = GraphqlHelper.loadRawString(context.resources, com.tokopedia.atc_common.R.raw.mutation_add_to_cart_one_click_checkout)
        return AddToCartOccUseCase(query, graphqlUseCase, AddToCartDataMapper())
    }

    @Provides
    @HomeScope
    fun provideCloseChannelUseCase(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository): CloseChannelUseCase{
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.mutation_close_channel_query)
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<CloseChannelMutation>(graphqlRepository)
        useCase.setGraphqlQuery(query)
        return CloseChannelUseCase(useCase)
    }
}