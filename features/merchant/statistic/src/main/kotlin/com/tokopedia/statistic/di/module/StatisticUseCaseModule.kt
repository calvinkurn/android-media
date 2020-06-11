package com.tokopedia.statistic.di.module

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhomecommon.domain.mapper.*
import com.tokopedia.sellerhomecommon.domain.usecase.*
import com.tokopedia.statistic.di.StatisticScope
import dagger.Module
import dagger.Provides

/**
 * Created By @ilhamsuaib on 10/06/20
 */

@StatisticScope
@Module
class StatisticUseCaseModule {

    @StatisticScope
    @Provides
    fun provideGetSellerHomeLayoutUseCase(
            gqlRepository: GraphqlRepository,
            mapper: LayoutMapper
    ): GetLayoutUseCase {
        return GetLayoutUseCase(gqlRepository, mapper)
    }

    @StatisticScope
    @Provides
    fun provideGetCardDataUseCase(
            gqlRepository: GraphqlRepository,
            mapper: CardMapper
    ): GetCardDataUseCase {
        return GetCardDataUseCase(gqlRepository, mapper)
    }

    @StatisticScope
    @Provides
    fun provideGetLineGraphDataUseCase(
            gqlRepository: GraphqlRepository,
            mapper: LineGraphMapper
    ): GetLineGraphDataUseCase {
        return GetLineGraphDataUseCase(gqlRepository, mapper)
    }

    @StatisticScope
    @Provides
    fun provideGetProgressDataUseCase(
            gqlRepository: GraphqlRepository,
            progressMapper: ProgressMapper
    ): GetProgressDataUseCase = GetProgressDataUseCase(gqlRepository, progressMapper)

    @StatisticScope
    @Provides
    fun provideGetPostDataUseCase(
            gqlRepository: GraphqlRepository,
            mapper: PostMapper
    ): GetPostDataUseCase {
        return GetPostDataUseCase(gqlRepository, mapper)
    }

    @StatisticScope
    @Provides
    fun provideGetCarouselDataUseCase(
            gqlRepository: GraphqlRepository,
            mapper: CarouselMapper
    ): GetCarouselDataUseCase {
        return GetCarouselDataUseCase(gqlRepository, mapper)
    }
}