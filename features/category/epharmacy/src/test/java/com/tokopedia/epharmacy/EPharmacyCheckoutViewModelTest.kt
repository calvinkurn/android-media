package com.tokopedia.epharmacy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.epharmacy.network.response.EPharmacyAtcInstantResponse
import com.tokopedia.epharmacy.network.response.EPharmacyCartGeneralCheckoutResponse
import com.tokopedia.epharmacy.usecase.EPharmacyAtcUseCase
import com.tokopedia.epharmacy.usecase.EPharmacyCheckoutCartGeneralUseCase
import com.tokopedia.epharmacy.viewmodel.EPharmacyCheckoutViewModel
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class EPharmacyCheckoutViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val ePharmacyAtcUseCase = mockk<EPharmacyAtcUseCase>(relaxed = true)
    private val ePharmacyCheckoutCartGeneralUseCase = mockk<EPharmacyCheckoutCartGeneralUseCase>(relaxed = true)

    private val dispatcherBackground = TestCoroutineDispatcher()
    private lateinit var viewModel: EPharmacyCheckoutViewModel

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    @Before
    fun setUp() {
        viewModel = EPharmacyCheckoutViewModel(
            ePharmacyAtcUseCase,
            ePharmacyCheckoutCartGeneralUseCase,
            dispatcherBackground
        )
    }

    @Test
    fun `atc instant response success`() {
        val businessDataList = mockk<EPharmacyAtcInstantResponse.CartGeneralAddToCartInstant.CartGeneralAddToCartInstantData.BusinessDataList>(relaxed = true)
        val response = EPharmacyAtcInstantResponse(
            EPharmacyAtcInstantResponse.CartGeneralAddToCartInstant(
                EPharmacyAtcInstantResponse.CartGeneralAddToCartInstant.CartGeneralAddToCartInstantData(
                    businessDataList,
                    "",
                    1
                )
            )
        )
        coEvery {
            ePharmacyAtcUseCase.getEPharmacyAtcData(any(), any(), any())
        } coAnswers {
            firstArg<(EPharmacyAtcInstantResponse) -> Unit>().invoke(response)
        }
        viewModel.getEPharmacyAtcData(mockk())
        assert(viewModel.ePharmacyAtcData.value is Success)
    }

    @Test
    fun `atc instant api success response fail`() {
        val response = EPharmacyAtcInstantResponse(
            EPharmacyAtcInstantResponse.CartGeneralAddToCartInstant(
                EPharmacyAtcInstantResponse.CartGeneralAddToCartInstant.CartGeneralAddToCartInstantData(
                    null,
                    "",
                    0
                )
            )
        )
        coEvery {
            ePharmacyAtcUseCase.getEPharmacyAtcData(any(), any(), any())
        } coAnswers {
            firstArg<(EPharmacyAtcInstantResponse) -> Unit>().invoke(response)
        }
        viewModel.getEPharmacyAtcData(mockk())
        assert(viewModel.ePharmacyAtcData.value is Success)
    }

    @Test
    fun `atc instant api fail throws exception`() {
        coEvery {
            ePharmacyAtcUseCase.getEPharmacyAtcData(any(), any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getEPharmacyAtcData(mockk())
        Assert.assertEquals(
            (viewModel.ePharmacyAtcData.value as Fail).throwable,
            mockThrowable
        )
    }

    @Test
    fun `checkout cart general response success`() {
        val cartGeneralResponse =
            mockk<EPharmacyCartGeneralCheckoutResponse.CheckoutResponse.CheckoutData.CartGeneralResponse>(relaxed = true)
        val response = EPharmacyCartGeneralCheckoutResponse(
            EPharmacyCartGeneralCheckoutResponse.CheckoutResponse(
                EPharmacyCartGeneralCheckoutResponse.CheckoutResponse.CheckoutData(
                    cartGeneralResponse,
                    "",
                    "",
                    1
                )
            )
        )
        coEvery {
            ePharmacyCheckoutCartGeneralUseCase.getEPharmacyCheckoutData(any(), any(), any())
        } coAnswers {
            firstArg<(EPharmacyCartGeneralCheckoutResponse) -> Unit>().invoke(response)
        }
        viewModel.getEPharmacyCheckoutData(mockk())
        assert(viewModel.ePharmacyCheckoutData.value is Success)
        assert((viewModel.ePharmacyCheckoutData.value as? Success)?.data?.checkout?.checkoutData?.success?.orZero() == EPharmacyCartGeneralCheckoutResponse.SUCCESS)
    }

    @Test
    fun `checkout cart general api success response fail`() {
        val cartGeneralResponse =
            mockk<EPharmacyCartGeneralCheckoutResponse.CheckoutResponse.CheckoutData.CartGeneralResponse>(relaxed = true)
        val response = EPharmacyCartGeneralCheckoutResponse(
            EPharmacyCartGeneralCheckoutResponse.CheckoutResponse(
                EPharmacyCartGeneralCheckoutResponse.CheckoutResponse.CheckoutData(
                    cartGeneralResponse,
                    "",
                    "",
                    0
                )
            )
        )
        coEvery {
            ePharmacyCheckoutCartGeneralUseCase.getEPharmacyCheckoutData(any(), any(), any())
        } coAnswers {
            firstArg<(EPharmacyCartGeneralCheckoutResponse) -> Unit>().invoke(response)
        }
        viewModel.getEPharmacyCheckoutData(mockk())
        assert(viewModel.ePharmacyCheckoutData.value is Success)
        assert((viewModel.ePharmacyCheckoutData.value as? Success)?.data?.checkout?.checkoutData?.success?.orZero() == EPharmacyCartGeneralCheckoutResponse.ERROR)
    }

    @Test
    fun `checkout cart general api fail throws exception`() {
        coEvery {
            ePharmacyCheckoutCartGeneralUseCase.getEPharmacyCheckoutData(any(), any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getEPharmacyCheckoutData(mockk())
        Assert.assertEquals(
            (viewModel.ePharmacyCheckoutData.value as Fail).throwable,
            mockThrowable
        )
    }
}
