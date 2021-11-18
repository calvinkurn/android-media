package com.tokopedia.shop.score.stub.di.module

import com.tokopedia.gm.common.domain.interactor.GetShopInfoPeriodUseCase
import com.tokopedia.shop.score.performance.di.scope.ShopPerformanceScope
import com.tokopedia.shop.score.performance.domain.usecase.GetShopPerformanceUseCase
import dagger.Module
import dagger.Provides

@Module
class ShopPerformanceQueryModuleStub(
    private val shopInfoPeriodUseCase: GetShopInfoPeriodUseCase,
    private val shopPerformanceUseCase: GetShopPerformanceUseCase
) {
    @Provides
    @ShopPerformanceScope
    fun provideShopInfoPeriodUseCase(): GetShopInfoPeriodUseCase = shopInfoPeriodUseCase

    @Provides
    @ShopPerformanceScope
    fun provideShopPerformanceUseCase(): GetShopPerformanceUseCase = shopPerformanceUseCase
}