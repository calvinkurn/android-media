package com.tokopedia.statistic.di.module

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhomecommon.domain.mapper.*
import com.tokopedia.sellerhomecommon.domain.usecase.*
import com.tokopedia.statistic.di.StatisticScope
import dagger.Module
import dagger.Provides

/**
 * Created By @ilhamsuaib on 10/06/20
 */

@Module
class StatisticUseCaseModule {

    @StatisticScope
    @Provides
    fun provideGetSellerHomeLayoutUseCase(
            gqlRepository: GraphqlRepository,
            mapper: LayoutMapper,
            dispatchers: CoroutineDispatchers
    ): GetLayoutUseCase {
        return GetLayoutUseCase(gqlRepository, mapper, dispatchers)
    }

    @StatisticScope
    @Provides
    fun provideGetCardDataUseCase(
            gqlRepository: GraphqlRepository,
            mapper: CardMapper,
            dispatchers: CoroutineDispatchers
    ): GetCardDataUseCase {
        return GetCardDataUseCase(gqlRepository, mapper, dispatchers)
    }

    @StatisticScope
    @Provides
    fun provideGetLineGraphDataUseCase(
            gqlRepository: GraphqlRepository,
            mapper: LineGraphMapper,
            dispatchers: CoroutineDispatchers
    ): GetLineGraphDataUseCase {
        return GetLineGraphDataUseCase(gqlRepository, mapper, dispatchers)
    }

    @StatisticScope
    @Provides
    fun provideGetMultiLineGraphDataUseCase(
            gqlRepository: GraphqlRepository,
            mapper: MultiLineGraphMapper,
            dispatchers: CoroutineDispatchers
    ): GetMultiLineGraphUseCase {
        return GetMultiLineGraphUseCase(gqlRepository, mapper, dispatchers)
    }

    @StatisticScope
    @Provides
    fun provideGetProgressDataUseCase(
            gqlRepository: GraphqlRepository,
            progressMapper: ProgressMapper,
            dispatchers: CoroutineDispatchers
    ): GetProgressDataUseCase = GetProgressDataUseCase(gqlRepository, progressMapper, dispatchers)

    @StatisticScope
    @Provides
    fun provideGetPostDataUseCase(
            gqlRepository: GraphqlRepository,
            mapper: PostMapper,
            dispatchers: CoroutineDispatchers
    ): GetPostDataUseCase {
        return GetPostDataUseCase(gqlRepository, mapper, dispatchers)
    }

    @StatisticScope
    @Provides
    fun provideGetCarouselDataUseCase(
            gqlRepository: GraphqlRepository,
            mapper: CarouselMapper,
            dispatchers: CoroutineDispatchers
    ): GetCarouselDataUseCase {
        return GetCarouselDataUseCase(gqlRepository, mapper, dispatchers)
    }

    @StatisticScope
    @Provides
    fun provideGetTableDataUseCase(
            gqlRepository: GraphqlRepository,
            mapper: TableMapper,
            dispatchers: CoroutineDispatchers
    ): GetTableDataUseCase {
        return GetTableDataUseCase(gqlRepository, mapper, dispatchers)
    }

    @StatisticScope
    @Provides
    fun provideGetPieChartDataUseCase(
            gqlRepository: GraphqlRepository,
            mapper: PieChartMapper,
            dispatchers: CoroutineDispatchers
    ): GetPieChartDataUseCase {
        return GetPieChartDataUseCase(gqlRepository, mapper, dispatchers)
    }

    @StatisticScope
    @Provides
    fun provideGetBarChartDataUseCase(
            gqlRepository: GraphqlRepository,
            mapper: BarChartMapper,
            dispatchers: CoroutineDispatchers
    ): GetBarChartDataUseCase {
        return GetBarChartDataUseCase(gqlRepository, mapper, dispatchers)
    }

    @StatisticScope
    @Provides
    fun provideGetTickerUseCase(
            gqlRepository: GraphqlRepository,
            mapper: TickerMapper,
            dispatchers: CoroutineDispatchers
    ): GetTickerUseCase = GetTickerUseCase(gqlRepository, mapper, dispatchers)
}