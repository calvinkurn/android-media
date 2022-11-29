package com.tokopedia.deals.checkout

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common_entertainment.data.DealsCheckoutInstantResponse
import com.tokopedia.common_entertainment.data.DealsCheckoutResponse
import com.tokopedia.deals.checkout.domain.DealsCheckoutGeneralV2InstantUseCase
import com.tokopedia.deals.checkout.domain.DealsCheckoutGeneralV2UseCase
import com.tokopedia.deals.checkout.ui.viewmodel.DealsCheckoutViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

open class DealsCheckoutViewModelTestFixture {

    @RelaxedMockK
    lateinit var dealsCheckoutGeneralV2UseCase: DealsCheckoutGeneralV2UseCase

    @RelaxedMockK
    lateinit var dealsCheckoutGeneralV2InstantUseCase: DealsCheckoutGeneralV2InstantUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: DealsCheckoutViewModel
    protected val errorMessageGeneral: String = "Error Message"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = DealsCheckoutViewModel(
            dealsCheckoutGeneralV2UseCase,
            dealsCheckoutGeneralV2InstantUseCase,
            CoroutineTestDispatchersProvider
        )
    }

    protected fun onGetCheckoutGeneral_thenReturn(dealsCheckout : DealsCheckoutResponse) {
        coEvery {
            dealsCheckoutGeneralV2UseCase.execute(any())
        } returns dealsCheckout
    }

    protected fun onGetCheckoutGeneral_thenReturn(errorThrowable: Throwable) {
        coEvery {
            dealsCheckoutGeneralV2UseCase.execute(any())
        } throws errorThrowable
    }

    protected fun onGetCheckoutGeneralInstant_thenReturn(dealsCheckout : DealsCheckoutInstantResponse) {
        coEvery {
            dealsCheckoutGeneralV2InstantUseCase.execute(any())
        } returns dealsCheckout
    }

    protected fun onGetCheckoutGeneralInstant_thenReturn(errorThrowable: Throwable) {
        coEvery {
            dealsCheckoutGeneralV2InstantUseCase.execute(any())
        } throws errorThrowable
    }
}
