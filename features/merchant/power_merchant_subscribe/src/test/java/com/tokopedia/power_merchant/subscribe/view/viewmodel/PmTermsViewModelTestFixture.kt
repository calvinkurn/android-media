package com.tokopedia.power_merchant.subscribe.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.gm.common.domain.interactor.ActivatePowerMerchantUseCase
import com.tokopedia.power_merchant.subscribe.common.coroutine.TestCoroutineDispatchers
import com.tokopedia.power_merchant.subscribe.view.viewmodel.ViewState.*
import com.tokopedia.power_merchant.subscribe.verification.verifyValueEquals
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
    protected lateinit var viewModel: PmTermsViewModel

    @Before
    fun setUp() {
        activatePmUseCase = mockk(relaxed = true)
        viewModel = PmTermsViewModel(activatePmUseCase, TestCoroutineDispatchers)
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