package com.tokopedia.shop.score.stub.performance.di.component

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.domain.interactor.GetShopInfoPeriodUseCase
import com.tokopedia.gm.common.domain.mapper.ShopScoreCommonMapper
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop.score.common.ShopScorePrefManager
import com.tokopedia.shop.score.performance.di.component.ShopPerformanceComponent
import com.tokopedia.shop.score.performance.di.scope.ShopPerformanceScope
import com.tokopedia.shop.score.performance.domain.mapper.ShopScoreMapper
import com.tokopedia.shop.score.performance.domain.usecase.GetShopPerformanceUseCase
import com.tokopedia.shop.score.stub.common.di.component.BaseAppComponentStub
import com.tokopedia.shop.score.stub.performance.di.module.ShopPerformanceModuleStub
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component

@ShopPerformanceScope
@Component(
    modules = [ShopPerformanceModuleStub::class],
    dependencies = [BaseAppComponentStub::class]
)
interface ShopPerformanceComponentStub: ShopPerformanceComponent {

    fun coroutineDispatcher(): CoroutineDispatchers

    fun graphQlRepository(): GraphqlRepository

    fun userSessionInterface(): UserSessionInterface

    fun getShopInfoPeriodUseCaseStub(): GetShopInfoPeriodUseCase

    fun getShopPerformanceUseCaseStub(): GetShopPerformanceUseCase

    fun shopScorePrefManager(): ShopScorePrefManager

    fun shopScoreMapper(): ShopScoreMapper

    fun shopScoreCommonMapper(): ShopScoreCommonMapper
}
