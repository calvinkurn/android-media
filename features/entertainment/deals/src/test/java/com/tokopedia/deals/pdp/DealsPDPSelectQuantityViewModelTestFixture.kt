package com.tokopedia.deals.pdp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common_entertainment.data.DealsVerifyResponse
import com.tokopedia.deals.pdp.domain.DealsPDPVerifyUseCase
import com.tokopedia.deals.pdp.ui.viewmodel.DealsPDPSelectQuantityViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

open class DealsPDPSelectQuantityViewModelTestFixture {

    @RelaxedMockK
    lateinit var dealsPDPVerifyUseCase: DealsPDPVerifyUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: DealsPDPSelectQuantityViewModel
    protected val errorMessageGeneral: String = "Error Message"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = DealsPDPSelectQuantityViewModel(
            dealsPDPVerifyUseCase,
            CoroutineTestDispatchersProvider
        )
    }

    protected fun onGetVerify_thenReturn(dealsVerify : DealsVerifyResponse) {
        coEvery {
            dealsPDPVerifyUseCase.execute(any())
        } returns dealsVerify
    }

    protected fun onGetVerify_thenReturn(errorThrowable: Throwable) {
        coEvery {
            dealsPDPVerifyUseCase.execute(any())
        } throws errorThrowable
    }
}
