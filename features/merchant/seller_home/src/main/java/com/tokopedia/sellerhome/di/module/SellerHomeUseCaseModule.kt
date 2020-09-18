package com.tokopedia.sellerhome.di.module

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhome.di.scope.SellerHomeScope
import com.tokopedia.sellerhome.domain.mapper.NotificationMapper
import com.tokopedia.sellerhome.domain.mapper.ShopInfoMapper
import com.tokopedia.sellerhome.domain.usecase.GetNotificationUseCase
import com.tokopedia.sellerhome.domain.usecase.GetShopInfoUseCase
import com.tokopedia.sellerhomecommon.domain.mapper.*
import com.tokopedia.sellerhomecommon.domain.usecase.*
import dagger.Module
import dagger.Provides

/**
 * Created By @ilhamsuaib on 2020-01-15
 */

@SellerHomeScope
@Module
class SellerHomeUseCaseModule {

    @SellerHomeScope
    @Provides
    fun provideGetSellerHomeLayoutUseCase(
            gqlRepository: GraphqlRepository,
            mapper: LayoutMapper
    ): GetLayoutUseCase {
        return GetLayoutUseCase(gqlRepository, mapper)
    }

    @SellerHomeScope
    @Provides
    fun provideGetCardDataUseCase(
            gqlRepository: GraphqlRepository,
            mapper: CardMapper
    ): GetCardDataUseCase {
        return GetCardDataUseCase(gqlRepository, mapper)
    }

    @SellerHomeScope
    @Provides
    fun provideGetLineGraphDataUseCase(
            gqlRepository: GraphqlRepository,
            mapper: LineGraphMapper
    ): GetLineGraphDataUseCase {
        return GetLineGraphDataUseCase(gqlRepository, mapper)
    }

    @SellerHomeScope
    @Provides
    fun provideGetProgressDataUseCase(
            gqlRepository: GraphqlRepository,
            progressMapper: ProgressMapper
    ): GetProgressDataUseCase = GetProgressDataUseCase(gqlRepository, progressMapper)

    @SellerHomeScope
    @Provides
    fun provideGetPostDataUseCase(
            gqlRepository: GraphqlRepository,
            mapper: PostMapper
    ): GetPostDataUseCase {
        return GetPostDataUseCase(gqlRepository, mapper)
    }

    @SellerHomeScope
    @Provides
    fun provideGetCarouselDataUseCase(
            gqlRepository: GraphqlRepository,
            mapper: CarouselMapper
    ): GetCarouselDataUseCase {
        return GetCarouselDataUseCase(gqlRepository, mapper)
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
            mapper: TableMapper
    ): GetTableDataUseCase {
        return GetTableDataUseCase(gqlRepository, mapper)
    }

    @SellerHomeScope
    @Provides
    fun provideGetPieChartDataUseCase(
            gqlRepository: GraphqlRepository,
            mapper: PieChartMapper
    ): GetPieChartDataUseCase {
        return GetPieChartDataUseCase(gqlRepository, mapper)
    }

    @SellerHomeScope
    @Provides
    fun provideGetBarChartDataUseCase(
            gqlRepository: GraphqlRepository,
            mapper: BarChartMapper
    ): GetBarChartDataUseCase {
        return GetBarChartDataUseCase(gqlRepository, mapper)
    }

    @SellerHomeScope
    @Provides
    fun provideGetTickerUseCase(
            gqlRepository: GraphqlRepository,
            mapper: TickerMapper
    ): GetTickerUseCase = GetTickerUseCase(gqlRepository, mapper)
}