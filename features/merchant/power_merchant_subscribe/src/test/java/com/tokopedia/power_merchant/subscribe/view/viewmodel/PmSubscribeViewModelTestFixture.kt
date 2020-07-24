package com.tokopedia.power_merchant.subscribe.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.gm.common.domain.interactor.GetPowerMerchantStatusUseCase
import com.tokopedia.power_merchant.subscribe.common.coroutine.TestCoroutineDispatchers
import com.tokopedia.power_merchant.subscribe.verification.verifyValueEquals
import com.tokopedia.power_merchant.subscribe.view.model.ViewState
import com.tokopedia.power_merchant.subscribe.view.util.PowerMerchantRemoteConfig
import com.tokopedia.shop.common.domain.interactor.GetShopFreeShippingStatusUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule

open class PmSubscribeViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var getPowerMerchantStatusUseCase: GetPowerMerchantStatusUseCase
    private lateinit var getShopFreeShippingStatusUseCase: GetShopFreeShippingStatusUseCase
    private lateinit var userSession: UserSessionInterface
    private lateinit var remoteConfig: PowerMerchantRemoteConfig
    protected lateinit var viewModel: PmSubscribeViewModel

    @Before
    fun setUp() {
        getPowerMerchantStatusUseCase = mockk(relaxed = true)
        getShopFreeShippingStatusUseCase = mockk(relaxed = true)
        userSession = mockk(relaxed = true)
        remoteConfig = mockk(relaxed = true)

        coEvery {
            remoteConfig.isFreeShippingEnabled()
        } returns false

        viewModel = PmSubscribeViewModel(
            getPowerMerchantStatusUseCase,
            getShopFreeShippingStatusUseCase,
            remoteConfig,
            userSession,
            TestCoroutineDispatchers
        )
    }

    protected fun onGetPowerMerchantStatusUseCase_thenReturn(powerMerchantStatus: PowerMerchantStatus) {
        coEvery { getPowerMerchantStatusUseCase.getData(any()) } returns powerMerchantStatus
    }

    protected fun onGetPowerMerchantStatusUseCase_thenReturn(error: Throwable) {
        coEvery { getPowerMerchantStatusUseCase.getData(any()) } throws error
    }

    protected fun verifyUnsubscribeUseCase() {
        coVerify { getPowerMerchantStatusUseCase.unsubscribe() }
    }

    protected fun verifyHideLoading() {
        viewModel.viewState
            .verifyValueEquals(ViewState.HideLoading)
    }
}