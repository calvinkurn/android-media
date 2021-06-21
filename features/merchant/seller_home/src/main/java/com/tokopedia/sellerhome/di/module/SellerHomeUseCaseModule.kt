package com.tokopedia.sellerhome.di.module

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.seller.menu.common.domain.usecase.*
import com.tokopedia.sellerhome.di.scope.SellerHomeScope
import com.tokopedia.sellerhome.domain.mapper.NotificationMapper
import com.tokopedia.sellerhome.domain.mapper.ShopInfoMapper
import com.tokopedia.sellerhome.domain.usecase.GetNotificationUseCase
import com.tokopedia.sellerhome.domain.usecase.GetShopInfoUseCase
import com.tokopedia.sellerhomecommon.domain.mapper.*
import com.tokopedia.sellerhomecommon.domain.usecase.*
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created By @ilhamsuaib on 2020-01-15
 */

@Module
class SellerHomeUseCaseModule {

    @SellerHomeScope
    @Provides
    fun provideGetSellerHomeLayoutUseCase(
            gqlRepository: GraphqlRepository,
            mapper: LayoutMapper,
            dispatchers: CoroutineDispatchers
    ): GetLayoutUseCase {
        return GetLayoutUseCase(gqlRepository, mapper, dispatchers)
    }

    @SellerHomeScope
    @Provides
    fun provideGetCardDataUseCase(
            gqlRepository: GraphqlRepository,
            mapper: CardMapper,
            dispatchers: CoroutineDispatchers
    ): GetCardDataUseCase {
        return GetCardDataUseCase(gqlRepository, mapper, dispatchers)
    }

    @SellerHomeScope
    @Provides
    fun provideGetLineGraphDataUseCase(
            gqlRepository: GraphqlRepository,
            mapper: LineGraphMapper,
            dispatchers: CoroutineDispatchers
    ): GetLineGraphDataUseCase {
        return GetLineGraphDataUseCase(gqlRepository, mapper, dispatchers)
    }

    @SellerHomeScope
    @Provides
    fun provideGetProgressDataUseCase(
            gqlRepository: GraphqlRepository,
            progressMapper: ProgressMapper,
            dispatchers: CoroutineDispatchers
    ): GetProgressDataUseCase = GetProgressDataUseCase(gqlRepository, progressMapper, dispatchers)

    @SellerHomeScope
    @Provides
    fun provideGetPostDataUseCase(
            gqlRepository: GraphqlRepository,
            mapper: PostMapper,
            dispatchers: CoroutineDispatchers
    ): GetPostDataUseCase {
        return GetPostDataUseCase(gqlRepository, mapper, dispatchers)
    }

    @SellerHomeScope
    @Provides
    fun provideGetCarouselDataUseCase(
            gqlRepository: GraphqlRepository,
            mapper: CarouselMapper,
            dispatchers: CoroutineDispatchers
    ): GetCarouselDataUseCase {
        return GetCarouselDataUseCase(gqlRepository, mapper, dispatchers)
    }

    @SellerHomeScope
    @Provides
    fun provideGetNotificationsUseCase(
            gqlRepository: GraphqlRepository,
            mapper: NotificationMapper
    ): GetNotificationUseCase {
        return GetNotificationUseCase(gqlRepository, mapper)
    }

    @SellerHomeScope
    @Provides
    fun provideGetShopInfoUseCase(
            gqlRepository: GraphqlRepository,
            mapper: ShopInfoMapper
    ): GetShopInfoUseCase {
        return GetShopInfoUseCase(gqlRepository, mapper)
    }

    @SellerHomeScope
    @Provides
    fun provideGetTableDataUseCase(
            gqlRepository: GraphqlRepository,
            mapper: TableMapper,
            dispatchers: CoroutineDispatchers
    ): GetTableDataUseCase {
        return GetTableDataUseCase(gqlRepository, mapper, dispatchers)
    }

    @SellerHomeScope
    @Provides
    fun provideGetPieChartDataUseCase(
            gqlRepository: GraphqlRepository,
            mapper: PieChartMapper,
            dispatchers: CoroutineDispatchers
    ): GetPieChartDataUseCase {
        return GetPieChartDataUseCase(gqlRepository, mapper, dispatchers)
    }

    @SellerHomeScope
    @Provides
    fun provideGetBarChartDataUseCase(
            gqlRepository: GraphqlRepository,
            mapper: BarChartMapper,
            dispatchers: CoroutineDispatchers
    ): GetBarChartDataUseCase {
        return GetBarChartDataUseCase(gqlRepository, mapper, dispatchers)
    }

    @SellerHomeScope
    @Provides
    fun provideGetMultiLineGraphDataUseCase(
            gqlRepository: GraphqlRepository,
            mapper: MultiLineGraphMapper,
            dispatchers: CoroutineDispatchers
    ): GetMultiLineGraphUseCase {
        return GetMultiLineGraphUseCase(gqlRepository, mapper, dispatchers)
    }

    @SellerHomeScope
    @Provides
    fun provideAnnouncementDataUseCase(
            gqlRepository: GraphqlRepository,
            mapper: AnnouncementMapper,
            dispatchers: CoroutineDispatchers
    ): GetAnnouncementDataUseCase {
        return GetAnnouncementDataUseCase(gqlRepository, mapper, dispatchers)
    }

    @SellerHomeScope
    @Provides
    fun provideGetRecommendationDataUseCase(
            gqlRepository: GraphqlRepository,
            mapper: RecommendationMapper
    ): GetRecommendationDataUseCase {
        return GetRecommendationDataUseCase(gqlRepository, mapper)
    }

    @SellerHomeScope
    @Provides
    fun provideGetTickerUseCase(
            gqlRepository: GraphqlRepository,
            mapper: TickerMapper,
            dispatchers: CoroutineDispatchers
    ): GetTickerUseCase = GetTickerUseCase(gqlRepository, mapper, dispatchers)

    @SellerHomeScope
    @Provides
    fun provideGetAllShopInfoUseCase(
            userSession: UserSessionInterface,
            balanceInfoUseCase: BalanceInfoUseCase,
            getShopBadgeUseCase: GetShopBadgeUseCase,
            getShopTotalFollowersUseCase: GetShopTotalFollowersUseCase,
            getUserShopInfoUseCase: GetUserShopInfoUseCase,
            topAdsAutoTopupUseCase: TopAdsAutoTopupUseCase,
            topAdsDashboardDepositUseCase: TopAdsDashboardDepositUseCase,
            dispatchers: CoroutineDispatchers
    ): GetAllShopInfoUseCase {
        return GetAllShopInfoUseCase(
                userSession,
                balanceInfoUseCase,
                getShopBadgeUseCase,
                getShopTotalFollowersUseCase,
                getUserShopInfoUseCase,
                topAdsAutoTopupUseCase,
                topAdsDashboardDepositUseCase,
                dispatchers
        )
    }
}