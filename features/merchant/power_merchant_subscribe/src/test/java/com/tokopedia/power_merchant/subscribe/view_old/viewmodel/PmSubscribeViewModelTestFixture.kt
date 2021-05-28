package com.tokopedia.power_merchant.subscribe.view_old.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.gm.common.domain.interactor.GetPowerMerchantStatusUseCase
import com.tokopedia.power_merchant.subscribe.domain.interactor.GetPMSettingAndShopInfoUseCase
import com.tokopedia.power_merchant.subscribe.view_old.model.PMSettingAndShopInfoUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.power_merchant.subscribe.view_old.model.ViewState
import com.tokopedia.power_merchant.subscribe.view_old.util.PowerMerchantRemoteConfig
import com.tokopedia.shop.common.data.source.cloud.model.ShopFreeShippingStatus
import com.tokopedia.shop.common.domain.interactor.GetShopFreeShippingStatusUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule

open class PmSubscribeViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var getPMSettingAndShopInfoUseCase: GetPMSettingAndShopInfoUseCase
    private lateinit var getPowerMerchantStatusUseCase: GetPowerMerchantStatusUseCase
    private lateinit var getShopFreeShippingStatusUseCase: GetShopFreeShippingStatusUseCase
    private lateinit var userSession: UserSessionInterface
    private lateinit var remoteConfig: PowerMerchantRemoteConfig
    protected lateinit var viewModel: PmSubscribeViewModel

    @Before
    fun setUp() {
        getPMSettingAndShopInfoUseCase = mockk(relaxed = true)
        getPowerMerchantStatusUseCase = mockk(relaxed = true)
        getShopFreeShippingStatusUseCase = mockk(relaxed = true)
        userSession = mockk(relaxed = true)
        remoteConfig = mockk(relaxed = true)

        onGetFreeShippingEnabledConfig_thenReturn(false)

        viewModel = PmSubscribeViewModel(
                getPMSettingAndShopInfoUseCase,
                getPowerMerchantStatusUseCase,
                getShopFreeShippingStatusUseCase,
                remoteConfig,
                userSession,
                CoroutineTestDispatchersProvider
        )
    }

    protected fun onGetSettingAndShopInfo_thenReturn(pmSettingAndShopInfo: PMSettingAndShopInfoUiModel) {
        coEvery { getPMSettingAndShopInfoUseCase.executeOnBackground() } returns pmSettingAndShopInfo
    }

    protected fun onGetSettingAndShopInfo_thenReturn(error: Throwable) {
        coEvery { getPMSettingAndShopInfoUseCase.executeOnBackground() } throws error
    }

    protected fun onGetPowerMerchantStatusUseCase_thenReturn(powerMerchantStatus: PowerMerchantStatus) {
        coEvery { getPowerMerchantStatusUseCase.getData(any()) } returns powerMerchantStatus
    }

    protected fun onGetPowerMerchantStatusUseCase_thenReturn(error: Throwable) {
        coEvery { getPowerMerchantStatusUseCase.getData(any()) } throws error
    }

    protected fun onGetFreeShippingStatusUseCase_thenReturn(freeShippingStatus: ShopFreeShippingStatus) {
        coEvery { getShopFreeShippingStatusUseCase.execute(any()) } returns freeShippingStatus
    }

    protected fun onGetFreeShippingStatusUseCase_thenReturn(error: Throwable) {
        coEvery { getShopFreeShippingStatusUseCase.execute(any()) } throws  error
    }

    protected fun onGetFreeShippingEnabledConfig_thenReturn(isEnabled: Boolean) {
        every { remoteConfig.isFreeShippingEnabled() } returns isEnabled
    }

    protected fun verifyGetFreeShippingStatusUseCaseNotCalled() {
        coVerify (exactly = 0) { getShopFreeShippingStatusUseCase.execute(any()) }
    }

    protected fun verifyUnsubscribeUseCase() {
        coVerify { getPowerMerchantStatusUseCase.unsubscribe() }
    }

    protected fun verifyHideLoading() {
        viewModel.viewState
            .verifyValueEquals(ViewState.HideLoading)
    }
}