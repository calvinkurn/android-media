package com.tokopedia.shop.score.performance.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.domain.interactor.GetShopCreatedInfoUseCase
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.shop.score.performance.domain.mapper.ShopScoreMapper
import com.tokopedia.shop.score.performance.domain.model.ShopScoreWrapperResponse
import com.tokopedia.shop.score.performance.domain.usecase.GetShopPerformanceUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class ShopPerformanceViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    protected lateinit var getShopCreatedInfoUseCase: Lazy<GetShopCreatedInfoUseCase>

    @RelaxedMockK
    protected lateinit var getShopPerformanceUseCase: Lazy<GetShopPerformanceUseCase>

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
                getShopCreatedInfoUseCase,
                getShopPerformanceUseCase
        )
    }

    protected fun onGetShopInfoPeriodUseCase_thenReturn(shopInfoPeriodUiModel: ShopInfoPeriodUiModel) {
        coEvery { getShopCreatedInfoUseCase.get().executeOnBackground() } returns shopInfoPeriodUiModel
    }

    protected fun onGetShopInfoPeriodUseCaseError_thenReturn(exception: Throwable) {
        coEvery { getShopCreatedInfoUseCase.get().executeOnBackground() } throws exception
    }

    protected fun verifyGetShopInfoPeriodUseCaseCalled() {
        coVerify { getShopCreatedInfoUseCase.get().executeOnBackground() }
    }

    protected fun onGetShopPerformanceUseCase_thenReturn(shopScoreWrapperResponse: ShopScoreWrapperResponse) {
        coEvery { getShopPerformanceUseCase.get().executeOnBackground() } returns shopScoreWrapperResponse
    }

    protected fun verifyGetShopPerformanceUseCaseCalled() {
        coVerify { getShopPerformanceUseCase.get().executeOnBackground() }
    }

    protected fun onGetShopPerformanceUseCaseError_thenReturn(exception: Throwable) {
        coEvery { getShopPerformanceUseCase.get().executeOnBackground() } throws exception
    }


}