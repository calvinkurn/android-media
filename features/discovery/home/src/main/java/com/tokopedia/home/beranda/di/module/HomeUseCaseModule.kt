
package com.tokopedia.home.beranda.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.common_wallet.balance.data.entity.WalletBalanceResponse
import com.tokopedia.common_wallet.pendingcashback.data.ResponsePendingCashback
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.datasource.local.HomeRoomDataSource
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.mapper.HomeDynamicChannelDataMapper
import com.tokopedia.home.beranda.data.mapper.HomeRecommendationMapper
import com.tokopedia.home.beranda.data.model.HomeAtfData
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.data.model.TokopointsDrawerListHomeData
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.di.module.query.QueryBusinessWidget.businessUnitDataQuery
import com.tokopedia.home.beranda.di.module.query.QueryBusinessWidget.businessWidgetQuery
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
import com.tokopedia.home.beranda.di.module.query.QueryHomeWallet.walletBalanceQuery
import com.tokopedia.home.beranda.di.module.query.QueryPopularKeyword.popularKeywordQuery
import com.tokopedia.home.beranda.di.module.query.QuerySuggestedReview.dismissSuggestedQuery
import com.tokopedia.home.beranda.di.module.query.QuerySuggestedReview.suggestedReviewQuery
import com.tokopedia.home.beranda.domain.gql.CloseChannelMutation
import com.tokopedia.home.beranda.domain.gql.ProductrevDismissSuggestion
import com.tokopedia.home.beranda.domain.gql.feed.HomeFeedContentGqlResponse
import com.tokopedia.home.beranda.domain.gql.feed.HomeFeedTabGqlResponse
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.domain.interactor.repository.*
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeBalanceWidgetUseCase
import com.tokopedia.home.beranda.domain.model.*
import com.tokopedia.home.beranda.domain.model.banner.HomeBannerData
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import com.tokopedia.navigation_common.usecase.GetWalletEligibilityUseCase
import com.tokopedia.play.widget.di.PlayWidgetModule
import com.tokopedia.play.widget.domain.PlayWidgetReminderUseCase
import com.tokopedia.play.widget.domain.PlayWidgetUpdateChannelUseCase
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.mapper.PlayWidgetUiMapper
import com.tokopedia.play.widget.util.PlayWidgetConnectionUtil
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
    fun homeRevampUseCase(
            homeDataMapper: HomeDataMapper,
            homeDynamicChannelsRepository: HomeDynamicChannelsRepository,
            homeDataRepository: HomeDataRepository,
            homeAtfRepository: HomeAtfRepository,
            homeFlagRepository: HomeFlagRepository,
            homePageBannerRepository: HomePageBannerRepository,
            homeIconRepository: HomeIconRepository,
            homeTickerRepository: HomeTickerRepository,
            homeRoomDataSource: HomeRoomDataSource,
            homeDynamicChannelDataMapper: HomeDynamicChannelDataMapper,
            @ApplicationContext context: Context,
            remoteConfig: RemoteConfig,
            homePlayRepository: HomePlayRepository,
            homeReviewSuggestedRepository: HomeReviewSuggestedRepository,
            homePlayLiveDynamicRepository: HomePlayLiveDynamicRepository,
            homePopularKeywordRepository: HomePopularKeywordRepository,
            homeHeadlineAdsRepository: HomeHeadlineAdsRepository,
            homeRecommendationRepository: HomeRecommendationRepository,
            homeRecommendationChipRepository: HomeRecommendationChipRepository,
            bestSellerMapper: BestSellerMapper,
            homeTopadsImageRepository: HomeTopadsImageRepository,
            homeRechargeRecommendationRepository: HomeRechargeRecommendationRepository,
            homeSalamWidgetRepository: HomeSalamWidgetRepository,
            homeBalanceWidgetUseCase: HomeBalanceWidgetUseCase,
            homeChooseAddressRepository: HomeChooseAddressRepository,
            homeRecommendationFeedTabRepository: HomeRecommendationFeedTabRepository,
            userSession: UserSessionInterface
    ) = HomeDynamicChannelUseCase(
            homeDataMapper = homeDataMapper,
            homeDynamicChannelsRepository = homeDynamicChannelsRepository,
            homeDataRepository = homeDataRepository,
            atfDataRepository = homeAtfRepository,
            homeFlagRepository = homeFlagRepository,
            homePageBannerRepository = homePageBannerRepository,
            homeIconRepository = homeIconRepository,
            homeTickerRepository = homeTickerRepository,
            getHomeRoomDataSource = homeRoomDataSource,
            homeDynamicChannelDataMapper = homeDynamicChannelDataMapper,
            applicationContext = context,
            remoteConfig = remoteConfig,
            homePlayRepository = homePlayRepository,
            homeReviewSuggestedRepository = homeReviewSuggestedRepository,
            homePlayLiveDynamicRepository = homePlayLiveDynamicRepository,
            homePopularKeywordRepository = homePopularKeywordRepository,
            homeHeadlineAdsRepository = homeHeadlineAdsRepository,
            homeRecommendationRepository = homeRecommendationRepository,
            homeRecommendationChipRepository = homeRecommendationChipRepository,
            bestSellerMapper = bestSellerMapper,
            homeTopadsImageRepository = homeTopadsImageRepository,
            homeRechargeRecommendationRepository = homeRechargeRecommendationRepository,
            homeSalamWidgetRepository = homeSalamWidgetRepository,
            homeBalanceWidgetUseCase = homeBalanceWidgetUseCase,
            homeChooseAddressRepository = homeChooseAddressRepository,
            homeRecommendationFeedTabRepository = homeRecommendationFeedTabRepository,
            userSessionInterface = userSession
    )


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
    fun provideHomeReviewSuggestedUseCase(graphqlRepository: GraphqlRepository): HomeReviewSuggestedRepository {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<SuggestedProductReview>(graphqlRepository)
        useCase.setGraphqlQuery(suggestedReviewQuery)
        return HomeReviewSuggestedRepository(useCase)
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
    fun provideHomeTokopointsListDataUseCase(graphqlRepository: GraphqlRepository): GetHomeTokopointsListDataUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<TokopointsDrawerListHomeData>(graphqlRepository)
        useCase.setGraphqlQuery(tokopointsListQuery)
        return GetHomeTokopointsListDataUseCase(useCase)
    }

    @Provides
    @HomeScope
    fun provideGetPlayLiveDynamicDataUseCase(graphqlRepository: GraphqlRepository): HomePlayLiveDynamicRepository {
        return HomePlayLiveDynamicRepository(com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase(graphqlRepository))
    }

    @HomeScope
    @Provides
    fun getKeywordSearchUseCase(graphqlRepository: GraphqlRepository): HomeKeywordSearchRepository {
        return HomeKeywordSearchRepository(com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase(graphqlRepository))
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
    fun getBusinessWidgetTabUseCase(graphqlRepository: GraphqlRepository): HomeBusinessUnitTabRepository {
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeWidget.Data>(graphqlRepository)
        usecase.setGraphqlQuery(businessWidgetQuery)
        return HomeBusinessUnitTabRepository(usecase)
    }

    @HomeScope
    @Provides
    fun getBusinessUnitDataTab(graphqlRepository: GraphqlRepository): HomeBusinessUnitDataRepository {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeWidget.Data>(graphqlRepository)
        useCase.setGraphqlQuery(businessUnitDataQuery)
        return HomeBusinessUnitDataRepository(useCase)
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
    fun providePopularKeywordUseCase(graphqlRepository: GraphqlRepository): HomePopularKeywordRepository {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeWidget.PopularKeywordQuery>(graphqlRepository)
        useCase.setGraphqlQuery(popularKeywordQuery)
        return HomePopularKeywordRepository(useCase)
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
    fun provideWalletEligibilityUseCase(graphqlRepository: GraphqlRepository): GetWalletEligibilityUseCase{
        return GetWalletEligibilityUseCase(graphqlRepository)
    }

    @Provides
    @HomeScope
    fun provideGetHomeData(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository, homeDynamicChannelDataMapper: HomeDynamicChannelDataMapper): HomeDataRepository {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeData>(graphqlRepository)
        useCase.setGraphqlQuery(homeQuery)
        return HomeDataRepository(useCase)
    }

    @Provides
    @HomeScope
    fun provideGetHomeFlagUseCase(graphqlRepository: GraphqlRepository): HomeFlagRepository {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeFlagData>(graphqlRepository)
        useCase.setGraphqlQuery(homeDataRevampQuery)
        return HomeFlagRepository(useCase)
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
    fun provideGetHomePageBannerUseCase(graphqlRepository: GraphqlRepository, homeRoomDataSource: HomeRoomDataSource): HomePageBannerRepository {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeBannerData>(graphqlRepository)
        useCase.setGraphqlQuery(homeSlidesQuery)
        return HomePageBannerRepository(useCase, homeRoomDataSource)
    }

    @Provides
    @HomeScope
    fun provideHomeTickerRepository(graphqlRepository: GraphqlRepository): HomeTickerRepository {
        return HomeTickerRepository(graphqlRepository)
    }

    @Provides
    @HomeScope
    fun provideHomeIconRepository(graphqlRepository: GraphqlRepository): HomeIconRepository {
        return HomeIconRepository(graphqlRepository)
    }

    @Provides
    @HomeScope
    fun provideHomeDynamicChannelRepository(graphqlRepository: GraphqlRepository): HomeDynamicChannelsRepository {
        return HomeDynamicChannelsRepository(graphqlRepository)
    }

    @HomeScope
    @Provides
    fun provideGetHomeAtfUseCase(graphqlRepository: GraphqlRepository, homeRoomDataSource: HomeRoomDataSource): HomeAtfRepository {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeAtfData>(graphqlRepository)
        useCase.setGraphqlQuery(atfQuery)
        return HomeAtfRepository(useCase, homeRoomDataSource)
    }

    @Provides
    @HomeScope
    fun provideCloseChannelUseCase(graphqlRepository: GraphqlRepository): HomeCloseChannelRepository {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<CloseChannelMutation>(graphqlRepository)
        useCase.setGraphqlQuery(closeChannel)
        return HomeCloseChannelRepository(useCase)
    }

    @Provides
    @HomeScope
    fun provideInjectCouponTimeBasedUseCase(graphqlUseCase: com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<SetInjectCouponTimeBased>): InjectCouponTimeBasedUseCase {
        return InjectCouponTimeBasedUseCase(graphqlUseCase)
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
                          mapper: PlayWidgetUiMapper,
                          connectionUtil: PlayWidgetConnectionUtil
    ): PlayWidgetTools {
        return PlayWidgetTools(playWidgetUseCase, playWidgetReminderUseCase, playWidgetUpdateChannelUseCase, mapper, connectionUtil)
    }

    @HomeScope
    @Provides
    fun provideBestSellerMapper(@ApplicationContext context: Context) = BestSellerMapper(context)

    @Provides
    @HomeScope
    fun provideHomeBeautyFestUseCase(): HomeBeautyFestRepository {
        return HomeBeautyFestRepository()
    }
}