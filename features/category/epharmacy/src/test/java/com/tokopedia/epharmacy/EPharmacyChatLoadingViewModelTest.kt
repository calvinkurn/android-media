package com.tokopedia.epharmacy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.epharmacy.network.response.EPharmacyVerifyConsultationResponse
import com.tokopedia.epharmacy.usecase.EPharmacyVerifyConsultationOrderUseCase
import com.tokopedia.epharmacy.viewmodel.EPharmacyChatLoadingViewModel
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
class EPharmacyChatLoadingViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val ePharmacyVerifyConsultationOrderUseCase = mockk<EPharmacyVerifyConsultationOrderUseCase>(relaxed = true)

    private val dispatcherBackground = TestCoroutineDispatcher()
    private lateinit var viewModel: EPharmacyChatLoadingViewModel

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    @Before
    fun setUp() {
        viewModel = EPharmacyChatLoadingViewModel(
            ePharmacyVerifyConsultationOrderUseCase,
            dispatcherBackground
        )
    }

    @Test
    fun `epharmacy verify consul order response success`() {
        val response = EPharmacyVerifyConsultationResponse(
            EPharmacyVerifyConsultationResponse.VerifyConsultationOrder(
                EPharmacyVerifyConsultationResponse.VerifyConsultationOrder.VerifyConsultationOrderData(
                    true,
                    "dd",
                    ""
                ),
                null
            )
        )
        coEvery {
            ePharmacyVerifyConsultationOrderUseCase.getEPharmacyVerifyConsultationOrder(any(), any(), 1, true)
        } coAnswers {
            firstArg<(EPharmacyVerifyConsultationResponse) -> Unit>().invoke(response)
        }
        viewModel.getVerifyConsultationOrder(1, true)
        assert(viewModel.ePharmacyVerifyConsultationData.value is Success)
    }

    @Test
    fun `epharmacy verify consul order success response fail`() {
        val response = EPharmacyVerifyConsultationResponse(
            EPharmacyVerifyConsultationResponse.VerifyConsultationOrder(
                EPharmacyVerifyConsultationResponse.VerifyConsultationOrder.VerifyConsultationOrderData(
                    false,
                    "",
                    "1"
                ),
                null
            )
        )
        coEvery {
            ePharmacyVerifyConsultationOrderUseCase.getEPharmacyVerifyConsultationOrder(any(), any(), 1, true)
        } coAnswers {
            firstArg<(EPharmacyVerifyConsultationResponse) -> Unit>().invoke(response)
        }
        viewModel.getVerifyConsultationOrder(1, true)
        assert(viewModel.ePharmacyVerifyConsultationData.value is Success)
    }

    @Test
    fun `epharmacy verify consul order api fail throws exception`() {
        coEvery {
            ePharmacyVerifyConsultationOrderUseCase.getEPharmacyVerifyConsultationOrder(any(), any(), 1, true)
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getVerifyConsultationOrder(1, true)
        Assert.assertEquals(
            (viewModel.ePharmacyVerifyConsultationData.value as Fail).throwable,
            mockThrowable
        )
    }
}
