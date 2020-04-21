package com.tokopedia.sellerhome.di.module

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhome.di.scope.SellerHomeScope
import com.tokopedia.sellerhome.domain.mapper.*
import com.tokopedia.sellerhome.domain.usecase.*
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
}