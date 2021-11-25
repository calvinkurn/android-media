package com.tokopedia.shop.score.uitest.stub.performance.di.module

import com.tokopedia.gm.common.domain.interactor.GetShopInfoPeriodUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop.score.performance.di.scope.ShopPerformanceScope
import com.tokopedia.shop.score.performance.domain.model.ShopScoreWrapperResponse
import com.tokopedia.shop.score.uitest.stub.common.graphql.usecase.UseCaseStub
import dagger.Module
import dagger.Provides

@Module
class ShopPerformanceModuleStub {
    @Provides
    @ShopPerformanceScope
    fun provideShopInfoPeriodUseCase(): GetShopInfoPeriodUseCase {
        
    }

    @Provides
    @ShopPerformanceScope
    fun provideGetShopPerformanceUseCase(graphqlRepository: GraphqlRepository): UseCaseStub<ShopScoreWrapperResponse> {

    }

}