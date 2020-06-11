package com.tokopedia.statistic.di.module

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhomecommon.domain.mapper.LayoutMapper
import com.tokopedia.sellerhomecommon.domain.usecase.GetLayoutUseCase
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
}