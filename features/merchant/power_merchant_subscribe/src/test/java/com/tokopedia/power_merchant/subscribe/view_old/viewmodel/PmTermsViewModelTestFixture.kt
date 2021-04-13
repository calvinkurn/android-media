package com.tokopedia.power_merchant.subscribe.view_old.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.gm.common.domain.interactor.ActivatePowerMerchantUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.power_merchant.subscribe.domain.interactor.ValidatePowerMerchantUseCase
import com.tokopedia.power_merchant.subscribe.domain.model.GoldValidateShopBeforePMResponse
import com.tokopedia.power_merchant.subscribe.domain.model.ValidatePowerMerchantResponse
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.power_merchant.subscribe.view_old.model.ViewState.HideLoading
import com.tokopedia.usecase.RequestParams
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule

open class PmTermsViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var activatePmUseCase: ActivatePowerMerchantUseCase
    private lateinit var validatePmUseCase: ValidatePowerMerchantUseCase
    protected lateinit var viewModel: PmTermsViewModel

    @Before
    fun setUp() {
        activatePmUseCase = mockk(relaxed = true)
        validatePmUseCase = mockk(relaxed = true)

        val validationResponse = ValidatePowerMerchantResponse(data = "valid")
        onValidatePm_thenReturn(validationResponse)

        viewModel = PmTermsViewModel(activatePmUseCase, validatePmUseCase, CoroutineTestDispatchersProvider)
    }

    protected fun onValidatePm_thenReturn(response: ValidatePowerMerchantResponse) {
        coEvery { validatePmUseCase.execute() } returns GoldValidateShopBeforePMResponse(response)
    }

    protected fun onActivatePm_thenReturn(isSuccess: Boolean) {
        coEvery { activatePmUseCase.getData(RequestParams.EMPTY) } returns isSuccess
    }

    protected fun onActivatePm_thenReturn(error: Throwable) {
        coEvery { activatePmUseCase.getData(RequestParams.EMPTY) } throws error
    }

    protected fun verifyUnsubscribeUseCase() {
        coVerify { activatePmUseCase.unsubscribe() }
    }

    protected fun verifyHideLoading() {
        viewModel.viewState
            .verifyValueEquals(HideLoading)
    }
}