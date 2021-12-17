
package com.tokopedia.home.beranda.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.common_wallet.balance.data.entity.WalletBalanceResponse
import com.tokopedia.common_wallet.pendingcashback.data.ResponsePendingCashback
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.mapper.HomeDynamicChannelDataMapper
import com.tokopedia.home.beranda.data.mapper.HomeRecommendationMapper
import com.tokopedia.home.beranda.data.model.HomeAtfData
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.data.model.TokopointsDrawerListHomeData
import com.tokopedia.home.beranda.data.repository.HomeRevampRepository
import com.tokopedia.home.beranda.data.usecase.HomeRevampUseCase
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.di.module.query.TokopoinstListQuery
import com.tokopedia.home.beranda.di.module.query.WalletBalanceQuery
import com.tokopedia.home.beranda.di.module.query.HomeFeedQuery
import com.tokopedia.home.beranda.di.module.query.SuggestedReviewQuery
import com.tokopedia.home.beranda.di.module.query.DismissSuggestedQuery
import com.tokopedia.home.beranda.di.module.query.PendingCashbackQuery
import com.tokopedia.home.beranda.di.module.query.BusinessWidgetQuery
import com.tokopedia.home.beranda.di.module.query.BusinessUnitDataQuery
import com.tokopedia.home.beranda.di.module.query.RecommendationQuery
import com.tokopedia.home.beranda.di.module.query.PopularKeywordGqlQuery
import com.tokopedia.home.beranda.di.module.query.DynamicChannelQuery
import com.tokopedia.home.beranda.di.module.query.HomeQuery
import com.tokopedia.home.beranda.di.module.query.HomeDataRevampQuery
import com.tokopedia.home.beranda.di.module.query.HomeIconQuery
import com.tokopedia.home.beranda.di.module.query.HomeSlidesQuery
import com.tokopedia.home.beranda.di.module.query.AtfQuery
import com.tokopedia.home.beranda.di.module.query.CloseChannelQuery
import com.tokopedia.home.beranda.domain.gql.CloseChannelMutation
import com.tokopedia.home.beranda.domain.gql.ProductrevDismissSuggestion
import com.tokopedia.home.beranda.domain.gql.feed.HomeFeedContentGqlResponse
import com.tokopedia.home.beranda.domain.gql.feed.HomeFeedTabGqlResponse
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.domain.model.*
import com.tokopedia.home.beranda.domain.model.banner.HomeBannerData
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import com.tokopedia.navigation_common.usecase.GetWalletEligibilityUseCase
import com.tokopedia.play.widget.di.PlayWidgetModule
import com.tokopedia.play.widget.domain.PlayWidgetReminderUseCase
import com.tokopedia.play.widget.domain.PlayWidgetUpdateChannelUseCase
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.mapper.PlayWidgetMapper
import com.tokopedia.play.widget.ui.type.PlayWidgetSize
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
    fun homeRevampUseCase(homeRepository: HomeRevampRepository, homeDataMapper: HomeDataMapper) = HomeRevampUseCase(homeRepository, homeDataMapper)


    @Provides
    fun provideGetHomeRecommendationUseCase(
            graphqlRepository: GraphqlRepository,
            homeRecommendationMapper: HomeRecommendationMapper
    ): GetHomeRecommendationUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeFeedContentGqlResponse>(graphqlRepository)
        useCase.setGraphqlQuery(HomeFeedQuery())
        return GetHomeRecommendationUseCase(useCase, homeRecommendationMapper)
    }

    @Provides
    @HomeScope
    fun provideHomeReviewSuggestedUseCase(graphqlRepository: GraphqlRepository): GetHomeReviewSuggestedUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<SuggestedProductReview>(graphqlRepository)
        useCase.setGraphqlQuery(SuggestedReviewQuery())
        return GetHomeReviewSuggestedUseCase(useCase)
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
        usecase.setGraphqlQuery(WalletBalanceQuery())
        return GetCoroutineWalletBalanceUseCase(usecase, remoteConfig, userSession, localCacheHandler)
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
    fun getBusinessWidgetTab(graphqlRepository: GraphqlRepository): GetBusinessWidgetTab {
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeWidget.Data>(graphqlRepository)
        usecase.setGraphqlQuery(BusinessWidgetQuery())
        return GetBusinessWidgetTab(usecase)
    }

    @HomeScope
    @Provides
    fun getBusinessUnitDataTab(graphqlRepository: GraphqlRepository): GetBusinessUnitDataUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeWidget.Data>(graphqlRepository)
        useCase.setGraphqlQuery(BusinessUnitDataQuery())
        return GetBusinessUnitDataUseCase(useCase)
    }

    @HomeScope
    @Provides
    fun getRecommendationTabUseCase(graphqlRepository: GraphqlRepository): GetRecommendationTabUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeFeedTabGqlResponse>(graphqlRepository)
        useCase.setGraphqlQuery(RecommendationQuery())
        return GetRecommendationTabUseCase(useCase)
    }

    @Provides
    @HomeScope
    fun providePopularKeywordUseCase(graphqlRepository: GraphqlRepository): GetPopularKeywordUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeWidget.PopularKeywordQuery>(graphqlRepository)
        useCase.setGraphqlQuery(PopularKeywordGqlQuery())
        return GetPopularKeywordUseCase(useCase)
    }

    @Provides
    @HomeScope
    fun provideGetDynamicChannels(graphqlRepository: GraphqlRepository, homeDynamicChannelDataMapper: HomeDynamicChannelDataMapper): GetDynamicChannelsUseCase{
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeChannelData>(graphqlRepository)
        useCase.setGraphqlQuery(DynamicChannelQuery())
        return GetDynamicChannelsUseCase(useCase, homeDynamicChannelDataMapper)
    }


    @Provides
    @HomeScope
    fun provideWalletEligibilityUseCase(graphqlRepository: GraphqlRepository): GetWalletEligibilityUseCase{
        return GetWalletEligibilityUseCase(graphqlRepository)
    }

    @Provides
    @HomeScope
    fun provideGetHomeData(graphqlRepository: GraphqlRepository): GetHomeDataUseCase{
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeData>(graphqlRepository)
        useCase.setGraphqlQuery(HomeQuery())
        return GetHomeDataUseCase(useCase)
    }

    @Provides
    @HomeScope
    fun provideGetHomeFlagUseCase(graphqlRepository: GraphqlRepository): GetHomeFlagUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeFlagData>(graphqlRepository)
        useCase.setGraphqlQuery(HomeDataRevampQuery())
        return GetHomeFlagUseCase(useCase)
    }

    @Provides
    @HomeScope
    fun provideGetHomeIconUseCase(graphqlRepository: GraphqlRepository): GetHomeIconUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeIconData>(graphqlRepository)
        useCase.setGraphqlQuery(HomeIconQuery())
        return GetHomeIconUseCase(useCase)
    }

    @Provides
    @HomeScope
    fun provideGetHomePageBannerUseCase(graphqlRepository: GraphqlRepository): GetHomePageBannerUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeBannerData>(graphqlRepository)
        useCase.setGraphqlQuery(HomeSlidesQuery())
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
        useCase.setGraphqlQuery(AtfQuery())
        return GetHomeAtfUseCase(useCase)
    }

    @Provides
    @HomeScope
    fun provideCloseChannelUseCase(graphqlRepository: GraphqlRepository): CloseChannelUseCase{
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<CloseChannelMutation>(graphqlRepository)
        useCase.setGraphqlQuery(CloseChannelQuery())
        return CloseChannelUseCase(useCase)
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
                          mapperProviders: Map<PlayWidgetSize, @JvmSuppressWildcards PlayWidgetMapper>,
                          connectionUtil: PlayWidgetConnectionUtil
    ): PlayWidgetTools {
        return PlayWidgetTools(playWidgetUseCase, playWidgetReminderUseCase, playWidgetUpdateChannelUseCase, mapperProviders, connectionUtil)
    }

    @HomeScope
    @Provides
    fun provideBestSellerMapper(@ApplicationContext context: Context) = BestSellerMapper(context)

}