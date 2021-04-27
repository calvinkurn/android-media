package com.tokopedia.shop.score.performance.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.domain.interactor.GetShopInfoPeriodUseCase
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.shop.score.performance.domain.mapper.ShopScoreMapper
import com.tokopedia.shop.score.performance.domain.model.ShopScoreWrapperResponse
import com.tokopedia.shop.score.performance.domain.usecase.GetShopPerformanceUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class ShopPerformanceViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    protected lateinit var getShopInfoPeriodUseCase: GetShopInfoPeriodUseCase

    @RelaxedMockK
    protected lateinit var getShopPerformanceUseCase: GetShopPerformanceUseCase

    @RelaxedMockK
    protected lateinit var shopScoreMapper: ShopScoreMapper

    @RelaxedMockK
    protected lateinit var userSession: UserSessionInterface

    private lateinit var dispatchers: CoroutineDispatchers

    protected lateinit var shopPerformanceViewModel: ShopPerformanceViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        dispatchers = CoroutineTestDispatchersProvider
        shopPerformanceViewModel = ShopPerformanceViewModel(
                dispatchers,
                shopScoreMapper,
                userSession,
                getShopInfoPeriodUseCase,
                getShopPerformanceUseCase
        )
    }

    protected fun onGetShopInfoPeriodUseCase_thenReturn(shopInfoPeriodUiModel: ShopInfoPeriodUiModel) {
        coEvery { getShopInfoPeriodUseCase.executeOnBackground() } returns shopInfoPeriodUiModel
    }

    protected fun onGetShopInfoPeriodUseCaseError_thenReturn(exception: Throwable) {
        coEvery { getShopInfoPeriodUseCase.executeOnBackground() } throws exception
    }

    protected fun verifyGetShopInfoPeriodUseCaseCalled() {
        coVerify { getShopInfoPeriodUseCase.executeOnBackground() }
    }

    protected fun onGetShopPerformanceUseCase_thenReturn(shopScoreWrapperResponse: ShopScoreWrapperResponse) {
        coEvery { getShopPerformanceUseCase.executeOnBackground() } returns shopScoreWrapperResponse
    }

    protected fun verifyGetShopPerformanceUseCaseCalled() {
        coVerify { getShopPerformanceUseCase.executeOnBackground() }
    }

    protected fun onGetShopPerformanceUseCaseError_thenReturn(exception: Throwable) {
        coEvery { getShopPerformanceUseCase.executeOnBackground() } throws exception
    }


}