
package com.tokopedia.home.beranda.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.atc_common.data.model.request.chosenaddress.ChosenAddressAddToCartRequestHelper
import com.tokopedia.atc_common.domain.mapper.AddToCartDataMapper
import com.tokopedia.atc_common.domain.usecase.AddToCartOccUseCase
import com.tokopedia.common_wallet.balance.data.entity.WalletBalanceResponse
import com.tokopedia.common_wallet.pendingcashback.data.ResponsePendingCashback
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.mapper.HomeDynamicChannelDataMapper
import com.tokopedia.home.beranda.data.mapper.HomeRecommendationMapper
import com.tokopedia.home.beranda.data.model.HomeAtfData
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.data.model.TokopointsDrawerHomeData
import com.tokopedia.home.beranda.data.model.TokopointsDrawerListHomeData
import com.tokopedia.home.beranda.data.repository.HomeRepository
import com.tokopedia.home.beranda.data.repository.HomeRevampRepository
import com.tokopedia.home.beranda.data.usecase.HomeRevampUseCase
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.di.module.query.QueryBusinessWidget.businessUnitDataQuery
import com.tokopedia.home.beranda.di.module.query.QueryBusinessWidget.businessWidgetQuery
import com.tokopedia.home.beranda.di.module.query.QueryCartHome.addToCartOneClickCheckout
import com.tokopedia.home.beranda.di.module.query.QueryHome.atfQuery
import com.tokopedia.home.beranda.di.module.query.QueryHome.closeChannel
import com.tokopedia.home.beranda.di.module.query.QueryHome.dynamicChannelQuery
import com.tokopedia.home.beranda.di.module.query.QueryHome.homeDataRevampQuery
import com.tokopedia.home.beranda.di.module.query.QueryHome.homeIconQuery
import com.tokopedia.home.beranda.di.module.query.QueryHome.homeQuery
import com.tokopedia.home.beranda.di.module.query.QueryHome.homeSlidesQuery
import com.tokopedia.home.beranda.di.module.query.QueryHome.recommendationQuery
import com.tokopedia.home.beranda.di.module.query.QueryHomeWallet.pendingCashBackQuery
import com.tokopedia.home.beranda.di.module.query.QueryHomeWallet.tokopointsListQuery
import com.tokopedia.home.beranda.di.module.query.QueryHomeWallet.tokopointsQuery
import com.tokopedia.home.beranda.di.module.query.QueryHomeWallet.walletBalanceQuery
import com.tokopedia.home.beranda.di.module.query.QueryPopularKeyword.popularKeywordQuery
import com.tokopedia.home.beranda.di.module.query.QuerySuggestedReview.dismissSuggestedQuery
import com.tokopedia.home.beranda.di.module.query.QuerySuggestedReview.suggestedReviewQuery
import com.tokopedia.home.beranda.domain.gql.CloseChannelMutation
import com.tokopedia.home.beranda.domain.gql.ProductrevDismissSuggestion
import com.tokopedia.home.beranda.domain.gql.feed.HomeFeedContentGqlResponse
import com.tokopedia.home.beranda.domain.gql.feed.HomeFeedTabGqlResponse
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.domain.model.*
import com.tokopedia.home.beranda.domain.model.banner.HomeBannerData
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import com.tokopedia.play.widget.di.PlayWidgetModule
import com.tokopedia.play.widget.domain.PlayWidgetReminderUseCase
import com.tokopedia.play.widget.domain.PlayWidgetUpdateChannelUseCase
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.mapper.PlayWidgetMapper
import com.tokopedia.play.widget.ui.type.PlayWidgetSize
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.recommendation_widget_common.di.RecommendationCoroutineModule
import com.tokopedia.recommendation_widget_common.widget.bestseller.mapper.BestSellerMapper
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.repository.TopAdsRepository
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import dagger.Module
import dagger.Provides

@Module(includes = [PlayWidgetModule::class, RecommendationCoroutineModule::class])
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


    @HomeScope
    @Provides
    fun homeRevampUseCase(homeRepository: HomeRevampRepository, homeDataMapper: HomeDataMapper) = HomeRevampUseCase(homeRepository, homeDataMapper)

    @Provides
    fun provideSendGeolocationInfoUseCase(homeRepository: HomeRepository): SendGeolocationInfoUseCase {
        return SendGeolocationInfoUseCase(homeRepository)
    }

    @Provides
    fun provideGetHomeRecommendationUseCase(
            @ApplicationContext context: Context,
            graphqlRepository: GraphqlRepository,
            homeRecommendationMapper: HomeRecommendationMapper
    ): GetHomeRecommendationUseCase {
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.gql_home_feed)
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeFeedContentGqlResponse>(graphqlRepository)
        useCase.setGraphqlQuery(query)
        return GetHomeRecommendationUseCase(useCase, homeRecommendationMapper)
    }

    @Provides
    @HomeScope
    fun provideHomeReviewSuggestedUseCase(graphqlRepository: GraphqlRepository): GetHomeReviewSuggestedUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<SuggestedProductReview>(graphqlRepository)
        useCase.setGraphqlQuery(suggestedReviewQuery)
        return GetHomeReviewSuggestedUseCase(useCase)
    }

    @Provides
    @HomeScope
    fun provideDismissHomeReviewUseCase(graphqlRepository: GraphqlRepository): DismissHomeReviewUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<ProductrevDismissSuggestion>(graphqlRepository)
        useCase.setGraphqlQuery(dismissSuggestedQuery)
        return DismissHomeReviewUseCase(useCase)
    }

    @Provides
    @HomeScope
    fun provideHomeTokopointsDataUseCase(graphqlRepository: GraphqlRepository): GetHomeTokopointsDataUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<TokopointsDrawerHomeData>(graphqlRepository)
        useCase.setGraphqlQuery(tokopointsQuery)
        return GetHomeTokopointsDataUseCase(useCase)
    }

    @Provides
    @HomeScope
    fun provideHomeTokopointsListDataUseCase(graphqlRepository: GraphqlRepository): GetHomeTokopointsListDataUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<TokopointsDrawerListHomeData>(graphqlRepository)
        useCase.setGraphqlQuery(tokopointsListQuery)
        return GetHomeTokopointsListDataUseCase(useCase)
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
    fun getCoroutineWalletBalanceUseCase(graphqlRepository: GraphqlRepository, userSession: UserSessionInterface, remoteConfig: RemoteConfig, localCacheHandler: LocalCacheHandler): GetCoroutineWalletBalanceUseCase {
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<WalletBalanceResponse>(graphqlRepository)
        usecase.setGraphqlQuery(walletBalanceQuery)
        return GetCoroutineWalletBalanceUseCase(usecase, remoteConfig, userSession, localCacheHandler)
    }

    @HomeScope
    @Provides
    fun getCoroutinePendingCashbackUseCase(graphqlRepository: GraphqlRepository): GetCoroutinePendingCashbackUseCase {
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<ResponsePendingCashback>(graphqlRepository)
        usecase.setGraphqlQuery(pendingCashBackQuery)
        return GetCoroutinePendingCashbackUseCase(usecase)
    }

    @HomeScope
    @Provides
    fun getBusinessWidgetTab(graphqlRepository: GraphqlRepository): GetBusinessWidgetTab {
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeWidget.Data>(graphqlRepository)
        usecase.setGraphqlQuery(businessWidgetQuery)
        return GetBusinessWidgetTab(usecase)
    }

    @HomeScope
    @Provides
    fun getBusinessUnitDataTab(graphqlRepository: GraphqlRepository): GetBusinessUnitDataUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeWidget.Data>(graphqlRepository)
        useCase.setGraphqlQuery(businessUnitDataQuery)
        return GetBusinessUnitDataUseCase(useCase)
    }

    @HomeScope
    @Provides
    fun getRecommendationTabUseCase(graphqlRepository: GraphqlRepository): GetRecommendationTabUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeFeedTabGqlResponse>(graphqlRepository)
        useCase.setGraphqlQuery(recommendationQuery)
        return GetRecommendationTabUseCase(useCase)
    }

    @Provides
    @HomeScope
    fun providePopularKeywordUseCase(graphqlRepository: GraphqlRepository): GetPopularKeywordUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeWidget.PopularKeywordQuery>(graphqlRepository)
        useCase.setGraphqlQuery(popularKeywordQuery)
        return GetPopularKeywordUseCase(useCase)
    }

    @Provides
    @HomeScope
    fun provideGetDynamicChannels(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository, homeDynamicChannelDataMapper: HomeDynamicChannelDataMapper): GetDynamicChannelsUseCase{
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeChannelData>(graphqlRepository)
        useCase.setGraphqlQuery(dynamicChannelQuery)
        return GetDynamicChannelsUseCase(useCase, homeDynamicChannelDataMapper)
    }

    @Provides
    @HomeScope
    fun provideGetHomeData(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository, homeDynamicChannelDataMapper: HomeDynamicChannelDataMapper): GetHomeDataUseCase{
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeData>(graphqlRepository)
        useCase.setGraphqlQuery(homeQuery)
        return GetHomeDataUseCase(useCase)
    }

    @Provides
    @HomeScope
    fun provideGetHomeFlagUseCase(graphqlRepository: GraphqlRepository): GetHomeFlagUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeFlagData>(graphqlRepository)
        useCase.setGraphqlQuery(homeDataRevampQuery)
        return GetHomeFlagUseCase(useCase)
    }

    @Provides
    @HomeScope
    fun provideGetHomeIconUseCase(graphqlRepository: GraphqlRepository): GetHomeIconUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeIconData>(graphqlRepository)
        useCase.setGraphqlQuery(homeIconQuery)
        return GetHomeIconUseCase(useCase)
    }

    @Provides
    @HomeScope
    fun provideGetHomePageBannerUseCase(graphqlRepository: GraphqlRepository): GetHomePageBannerUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeBannerData>(graphqlRepository)
        useCase.setGraphqlQuery(homeSlidesQuery)
        return GetHomePageBannerUseCase(useCase)
    }

    @Provides
    @HomeScope
    fun provideHomeTickerRepository(graphqlRepository: GraphqlRepository): GetHomeTickerRepository {
        return GetHomeTickerRepository(graphqlRepository)
    }

    @Provides
    @HomeScope
    fun provideHomeIconRepository(graphqlRepository: GraphqlRepository): GetHomeIconRepository {
        return GetHomeIconRepository(graphqlRepository)
    }

    @Provides
    @HomeScope
    fun provideHomeDynamicChannelRepository(graphqlRepository: GraphqlRepository): GetHomeDynamicChannelsRepository {
        return GetHomeDynamicChannelsRepository(graphqlRepository)
    }

    @HomeScope
    @Provides
    fun provideGetHomeAtfUseCase(graphqlRepository: GraphqlRepository): GetHomeAtfUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeAtfData>(graphqlRepository)
        useCase.setGraphqlQuery(atfQuery)
        return GetHomeAtfUseCase(useCase)
    }

    @Provides
    @HomeScope
    fun provideAddToCartOccUseCase(graphqlUseCase: GraphqlUseCase, chosenAddressAddToCartRequestHelper: ChosenAddressAddToCartRequestHelper): AddToCartOccUseCase{
        return AddToCartOccUseCase(addToCartOneClickCheckout, graphqlUseCase, AddToCartDataMapper(), chosenAddressAddToCartRequestHelper)
    }

    @Provides
    @HomeScope
    fun provideCloseChannelUseCase(graphqlRepository: GraphqlRepository): CloseChannelUseCase{
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<CloseChannelMutation>(graphqlRepository)
        useCase.setGraphqlQuery(closeChannel)
        return CloseChannelUseCase(useCase)
    }

    @Provides
    @HomeScope
    fun provideInjectCouponTimeBasedUseCase(graphqlUseCase: com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<SetInjectCouponTimeBased>): InjectCouponTimeBasedUseCase {
        return InjectCouponTimeBasedUseCase(graphqlUseCase)
    }

    @Provides
    @HomeScope
    fun provideGetDisplayHeadlineAds(graphqlUseCase: com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<DisplayHeadlineAdsEntity>): GetDisplayHeadlineAds {
        return GetDisplayHeadlineAds(graphqlUseCase)
    }

    @HomeScope
    @Provides
    fun provideTopAdsImageViewUseCase(userSession: UserSessionInterface): TopAdsImageViewUseCase {
        return TopAdsImageViewUseCase(userSession.userId, TopAdsRepository())
    }

    @HomeScope
    @Provides
    fun providePlayWidget(playWidgetUseCase: PlayWidgetUseCase,
                          playWidgetReminderUseCase: Lazy<PlayWidgetReminderUseCase>,
                          playWidgetUpdateChannelUseCase: Lazy<PlayWidgetUpdateChannelUseCase>,
                          mapperProviders: Map<PlayWidgetSize, @JvmSuppressWildcards PlayWidgetMapper>): PlayWidgetTools {
        return PlayWidgetTools(playWidgetUseCase, playWidgetReminderUseCase, playWidgetUpdateChannelUseCase, mapperProviders)
    }

    @HomeScope
    @Provides
    fun provideBestSellerMapper(@ApplicationContext context: Context) = BestSellerMapper(context)

}