package com.tokopedia.shop.score.performance.presentation.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.domain.interactor.GetShopInfoPeriodUseCase
import com.tokopedia.shop.score.performance.domain.mapper.ShopScoreMapper
import com.tokopedia.shop.score.performance.domain.usecase.GetShopPerformanceUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before

abstract class ShopPerformanceViewModelTestFixture {

    @RelaxedMockK
    protected lateinit var getShopInfoPeriodUseCase: GetShopInfoPeriodUseCase

    @RelaxedMockK
    protected lateinit var getShopPerformanceUseCase: GetShopPerformanceUseCase

    @RelaxedMockK
    protected lateinit var shopScoreMapper: ShopScoreMapper

    @RelaxedMockK
    protected lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    private lateinit var dispatchers: CoroutineDispatchers

    protected lateinit var shopPerformanceViewModel: ShopPerformanceViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        shopPerformanceViewModel = ShopPerformanceViewModel(
                dispatchers,
                shopScoreMapper,
                userSession,
                getShopInfoPeriodUseCase,
                getShopPerformanceUseCase
        )
    }
}