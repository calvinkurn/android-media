
package com.tokopedia.home.beranda.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common_wallet.pendingcashback.data.ResponsePendingCashback
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.home.beranda.data.balance.HomeHeaderUseCase
import com.tokopedia.home.beranda.data.datasource.local.HomeRoomDataSource
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.mapper.HomeDynamicChannelDataMapper
import com.tokopedia.home.beranda.data.model.GetHomeBalanceWidgetData
import com.tokopedia.home.beranda.data.model.HomeAtfData
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.data.model.TokopointsDrawerListHomeData
import com.tokopedia.home.beranda.data.newatf.AtfMapper
import com.tokopedia.home.beranda.data.newatf.HomeAtfUseCase
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.di.module.query.AtfQuery
import com.tokopedia.home.beranda.di.module.query.BusinessUnitDataQuery
import com.tokopedia.home.beranda.di.module.query.BusinessWidgetQuery
import com.tokopedia.home.beranda.di.module.query.CloseChannelQuery
import com.tokopedia.home.beranda.di.module.query.DismissSuggestedQuery
import com.tokopedia.home.beranda.di.module.query.DynamicChannelQuery
import com.tokopedia.home.beranda.di.module.query.GetHomeBalanceWidgetQuery
import com.tokopedia.home.beranda.di.module.query.HomeUserStatusQuery
import com.tokopedia.home.beranda.di.module.query.PendingCashbackQuery
import com.tokopedia.home.beranda.di.module.query.PopularKeywordGqlQuery
import com.tokopedia.home.beranda.di.module.query.SuggestedReviewQuery
import com.tokopedia.home.beranda.di.module.query.TokopoinstListQuery
import com.tokopedia.home.beranda.domain.gql.CloseChannelMutation
import com.tokopedia.home.beranda.domain.gql.ProductrevDismissSuggestion
import com.tokopedia.home.beranda.domain.gql.feed.HomeFeedTabGqlResponse
import com.tokopedia.home.beranda.domain.interactor.DismissHomeReviewUseCase
import com.tokopedia.home.beranda.domain.interactor.GetCoroutinePendingCashbackUseCase
import com.tokopedia.home.beranda.domain.interactor.GetDynamicChannelsUseCase
import com.tokopedia.home.beranda.domain.interactor.GetHomeBalanceWidgetUseCase
import com.tokopedia.home.beranda.domain.interactor.GetHomeTokopointsListDataUseCase
import com.tokopedia.home.beranda.domain.interactor.GetRecommendationTabUseCase
import com.tokopedia.home.beranda.domain.interactor.InjectCouponTimeBasedUseCase
import com.tokopedia.home.beranda.domain.interactor.repository.HomeAtfRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeBusinessUnitDataRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeBusinessUnitTabRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeChooseAddressRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeCloseChannelRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeDynamicChannelsRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeHeadlineAdsRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeIconRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeKeywordSearchRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeMissionWidgetRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomePageBannerRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomePlayRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomePopularKeywordRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeRechargeRecommendationRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeRecommendationChipRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeRecommendationFeedTabRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeRecommendationRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeReviewSuggestedRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeSalamWidgetRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeTickerRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeTodoWidgetRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeTopadsImageRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeUserStatusRepository
import com.tokopedia.home.beranda.domain.interactor.repository.TargetedTicketRepository
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeBalanceWidgetUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.domain.model.HomeChannelData
import com.tokopedia.home.beranda.domain.model.SetInjectCouponTimeBased
import com.tokopedia.home.beranda.domain.model.banner.HomeBannerData
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import com.tokopedia.home.beranda.helper.DeviceScreenHelper
import com.tokopedia.libra.LibraInstance
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
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.topads.sdk.domain.usecase.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.usecase.GetTopAdsHeadlineUseCase
import com.tokopedia.topads.sdk.data.repository.TopAdsRepository
import com.tokopedia.topads.sdk.utils.TopAdsIrisSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import dagger.Module
import dagger.Provides
import com.tokopedia.home.beranda.data.mapper.BestSellerMapper as BestSellerRevampMapper

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
        bestSellerRevampMapper: BestSellerRevampMapper,
        homeDynamicChannelsRepository: HomeDynamicChannelsRepository,
        homeUserStatusRepository: HomeUserStatusRepository,
        homeRoomDataSource: HomeRoomDataSource,
        homeDynamicChannelDataMapper: HomeDynamicChannelDataMapper,
        @ApplicationContext context: Context,
        remoteConfig: RemoteConfig,
        homePlayRepository: HomePlayRepository,
        homeReviewSuggestedRepository: HomeReviewSuggestedRepository,
        homePopularKeywordRepository: HomePopularKeywordRepository,
        homeHeadlineAdsRepository: HomeHeadlineAdsRepository,
        homeRecommendationRepository: HomeRecommendationRepository,
        homeRecommendationChipRepository: HomeRecommendationChipRepository,
        bestSellerMapper: BestSellerMapper,
        homeTopadsImageRepository: HomeTopadsImageRepository,
        homeRechargeRecommendationRepository: HomeRechargeRecommendationRepository,
        homeSalamWidgetRepository: HomeSalamWidgetRepository,
        homeChooseAddressRepository: HomeChooseAddressRepository,
        homeRecommendationFeedTabRepository: HomeRecommendationFeedTabRepository,
        homeMissionWidgetRepository: HomeMissionWidgetRepository,
        homeTodoWidgetRepository: HomeTodoWidgetRepository,
        homeAtfUseCase: HomeAtfUseCase,
        homeHeaderUseCase: HomeHeaderUseCase,
        atfMapper: AtfMapper
    ) = HomeDynamicChannelUseCase(
        homeDataMapper = homeDataMapper,
        bestSellerRevampMapper = bestSellerRevampMapper,
        homeDynamicChannelsRepository = homeDynamicChannelsRepository,
        homeUserStatusRepository = homeUserStatusRepository,
        getHomeRoomDataSource = homeRoomDataSource,
        homeDynamicChannelDataMapper = homeDynamicChannelDataMapper,
        applicationContext = context,
        remoteConfig = remoteConfig,
        homePlayRepository = homePlayRepository,
        homeReviewSuggestedRepository = homeReviewSuggestedRepository,
        homePopularKeywordRepository = homePopularKeywordRepository,
        homeHeadlineAdsRepository = homeHeadlineAdsRepository,
        homeRecommendationRepository = homeRecommendationRepository,
        homeRecommendationChipRepository = homeRecommendationChipRepository,
        bestSellerMapper = bestSellerMapper,
        homeTopadsImageRepository = homeTopadsImageRepository,
        homeRechargeRecommendationRepository = homeRechargeRecommendationRepository,
        homeSalamWidgetRepository = homeSalamWidgetRepository,
        homeChooseAddressRepository = homeChooseAddressRepository,
        homeRecommendationFeedTabRepository = homeRecommendationFeedTabRepository,
        homeMissionWidgetRepository = homeMissionWidgetRepository,
        homeTodoWidgetRepository = homeTodoWidgetRepository,
        homeAtfUseCase = homeAtfUseCase,
        homeHeaderUseCase = homeHeaderUseCase,
        atfMapper = atfMapper,
    )

    @Provides
    @HomeScope
    fun provideHomeReviewSuggestedUseCase(graphqlRepository: GraphqlRepository): HomeReviewSuggestedRepository {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<SuggestedProductReview>(graphqlRepository)
        useCase.setGraphqlQuery(SuggestedReviewQuery())
        return HomeReviewSuggestedRepository(useCase)
    }

    @Provides
    @HomeScope
    fun provideDismissHomeReviewUseCase(graphqlRepository: GraphqlRepository): DismissHomeReviewUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<ProductrevDismissSuggestion>(graphqlRepository)
        useCase.setGraphqlQuery(DismissSuggestedQuery())
        return DismissHomeReviewUseCase(useCase)
    }

    @Provides
    @HomeScope
    fun provideHomeTokopointsListDataUseCase(graphqlRepository: GraphqlRepository): GetHomeTokopointsListDataUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<TokopointsDrawerListHomeData>(graphqlRepository)
        useCase.setGraphqlQuery(TokopoinstListQuery())
        return GetHomeTokopointsListDataUseCase(useCase)
    }

    @Provides
    @HomeScope
    fun provideGetHomeBalanceWidgetUseCase(graphqlRepository: GraphqlRepository, deviceScreenHelper: DeviceScreenHelper): GetHomeBalanceWidgetUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<GetHomeBalanceWidgetData>(graphqlRepository)
        useCase.setGraphqlQuery(GetHomeBalanceWidgetQuery())
        return GetHomeBalanceWidgetUseCase(useCase, deviceScreenHelper)
    }

    @HomeScope
    @Provides
    fun getKeywordSearchUseCase(graphqlRepository: GraphqlRepository): HomeKeywordSearchRepository {
        return HomeKeywordSearchRepository(com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase(graphqlRepository))
    }

    @HomeScope
    @Provides
    fun getCoroutinePendingCashbackUseCase(graphqlRepository: GraphqlRepository): GetCoroutinePendingCashbackUseCase {
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<ResponsePendingCashback>(graphqlRepository)
        usecase.setGraphqlQuery(PendingCashbackQuery())
        return GetCoroutinePendingCashbackUseCase(usecase)
    }

    @HomeScope
    @Provides
    fun getBusinessWidgetTabUseCase(graphqlRepository: GraphqlRepository): HomeBusinessUnitTabRepository {
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeWidget.Data>(graphqlRepository)
        usecase.setGraphqlQuery(BusinessWidgetQuery())
        return HomeBusinessUnitTabRepository(usecase)
    }

    @HomeScope
    @Provides
    fun getBusinessUnitDataTab(graphqlRepository: GraphqlRepository): HomeBusinessUnitDataRepository {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeWidget.Data>(graphqlRepository)
        useCase.setGraphqlQuery(BusinessUnitDataQuery())
        return HomeBusinessUnitDataRepository(useCase)
    }

    @HomeScope
    @Provides
    fun getRecommendationTabUseCase(graphqlRepository: GraphqlRepository, remoteConfig: RemoteConfig): GetRecommendationTabUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeFeedTabGqlResponse>(graphqlRepository)
        return GetRecommendationTabUseCase(useCase)
    }

    @Provides
    @HomeScope
    fun providePopularKeywordUseCase(graphqlRepository: GraphqlRepository): HomePopularKeywordRepository {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeWidget.PopularKeywordQuery>(graphqlRepository)
        useCase.setGraphqlQuery(PopularKeywordGqlQuery())
        return HomePopularKeywordRepository(useCase)
    }

    @Provides
    @HomeScope
    fun provideGetDynamicChannels(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository, homeDynamicChannelDataMapper: HomeDynamicChannelDataMapper): GetDynamicChannelsUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeChannelData>(graphqlRepository)
        useCase.setGraphqlQuery(DynamicChannelQuery())
        return GetDynamicChannelsUseCase(useCase, homeDynamicChannelDataMapper)
    }

    @Provides
    @HomeScope
    fun provideWalletEligibilityUseCase(graphqlRepository: GraphqlRepository): GetWalletEligibilityUseCase {
        return GetWalletEligibilityUseCase(graphqlRepository)
    }

    @Provides
    @HomeScope
    fun provideGetHomeUserStatusUseCase(graphqlRepository: GraphqlRepository): HomeUserStatusRepository {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<Any>(graphqlRepository)
        useCase.setGraphqlQuery(HomeUserStatusQuery())
        useCase.setTypeClass(Any::class.java)
        return HomeUserStatusRepository(useCase)
    }

    @Provides
    @HomeScope
    fun provideGetHomePageBannerUseCase(graphqlRepository: GraphqlRepository, remoteConfig: RemoteConfig): HomePageBannerRepository {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeBannerData>(graphqlRepository)
        val isUsingV2 = remoteConfig.getBoolean(RemoteConfigKey.HOME_USE_GQL_FED_QUERY, true)
        return HomePageBannerRepository(useCase, isUsingV2)
    }

    @Provides
    @HomeScope
    fun provideHomeTickerRepository(graphqlRepository: GraphqlRepository, remoteConfig: RemoteConfig): HomeTickerRepository {
        val isUsingV2 = remoteConfig.getBoolean(RemoteConfigKey.HOME_USE_GQL_FED_QUERY, true)
        return HomeTickerRepository(graphqlRepository, isUsingV2)
    }

    @Provides
    @HomeScope
    fun provideTargetedTicketRepository(
        graphqlRepository: GraphqlRepository
    ) = TargetedTicketRepository(graphqlRepository)

    @Provides
    @HomeScope
    fun provideHomeIconRepository(graphqlRepository: GraphqlRepository, remoteConfig: RemoteConfig): HomeIconRepository {
        val isUsingV2 = remoteConfig.getBoolean(RemoteConfigKey.HOME_USE_GQL_FED_QUERY, true)
        return HomeIconRepository(graphqlRepository, isUsingV2)
    }

    @Provides
    @HomeScope
    fun provideHomeDynamicChannelRepository(graphqlRepository: GraphqlRepository, remoteConfig: RemoteConfig): HomeDynamicChannelsRepository {
        val isUsingV2 = remoteConfig.getBoolean(RemoteConfigKey.HOME_DC_USE_QUERY_V2, true)
        return HomeDynamicChannelsRepository(graphqlRepository, isUsingV2)
    }

    @Provides
    @HomeScope
    fun provideGetHomeAtfUseCase(
        graphqlRepository: GraphqlRepository,
        libraInstance: LibraInstance
    ): HomeAtfRepository {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeAtfData>(graphqlRepository)
        useCase.setGraphqlQuery(AtfQuery())
        return HomeAtfRepository(useCase, libraInstance)
    }

    @Provides
    @HomeScope
    fun provideHomeHeaderUseCase(
        userSession: UserSessionInterface,
        homeBalanceWidgetUseCase: HomeBalanceWidgetUseCase,
        libraInstance: LibraInstance
    ): HomeHeaderUseCase {
        return HomeHeaderUseCase(userSession, homeBalanceWidgetUseCase, libraInstance)
    }

    @Provides
    @HomeScope
    fun provideCloseChannelUseCase(graphqlRepository: GraphqlRepository): HomeCloseChannelRepository {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<CloseChannelMutation>(graphqlRepository)
        useCase.setGraphqlQuery(CloseChannelQuery())
        return HomeCloseChannelRepository(useCase)
    }

    @Provides
    @HomeScope
    fun provideInjectCouponTimeBasedUseCase(graphqlUseCase: com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<SetInjectCouponTimeBased>): InjectCouponTimeBasedUseCase {
        return InjectCouponTimeBasedUseCase(graphqlUseCase)
    }

    @HomeScope
    @Provides
    fun provideTopAdsImageViewUseCase(userSession: UserSessionInterface, topAdsIrisSession: TopAdsIrisSession): TopAdsImageViewUseCase {
        return TopAdsImageViewUseCase(userSession.userId, TopAdsRepository(), topAdsIrisSession.getSessionId())
    }

    @HomeScope
    @Provides
    fun providePlayWidget(
        playWidgetUseCase: PlayWidgetUseCase,
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
    fun provideTopAdsHeadlineUseCase(graphqlRepository: GraphqlRepository): GetTopAdsHeadlineUseCase {
        return GetTopAdsHeadlineUseCase(graphqlRepository)
    }
}
