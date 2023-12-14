package com.tokopedia.epharmacy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.epharmacy.component.model.EPharmacyDataModel
import com.tokopedia.epharmacy.network.response.OrderButtonData
import com.tokopedia.epharmacy.usecase.EPharmacyConsultationOrderDetailUseCase
import com.tokopedia.epharmacy.viewmodel.EPharmacyOrderDetailViewModel
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
class EPharmacyOrderDetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val ePharmacyConsultationOrderDetailUseCase = mockk<EPharmacyConsultationOrderDetailUseCase>(relaxed = true)

    private val dispatcherBackground = TestCoroutineDispatcher()
    private lateinit var viewModel: EPharmacyOrderDetailViewModel

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    @Before
    fun setUp() {
        viewModel = EPharmacyOrderDetailViewModel(
            ePharmacyConsultationOrderDetailUseCase,
            dispatcherBackground
        )
    }

    @Test
    fun `order detail response success`() {
        val response = EPharmacyDataModel()
        coEvery {
            ePharmacyConsultationOrderDetailUseCase.getEPharmacyOrderDetail(any(), any(), 0L, "", false)
        } coAnswers {
            firstArg<(EPharmacyDataModel, OrderButtonData) -> Unit>().invoke(response, OrderButtonData(null, null, null))
        }
        viewModel.getEPharmacyOrderDetail(0L, "", false)
        assert(viewModel.ePharmacyOrderDetailData.value is Success)
        assert(viewModel.ePharmacyOrderDetailData.value != null)
    }

    @Test
    fun `order detail api fail throws exception`() {
        coEvery {
            ePharmacyConsultationOrderDetailUseCase.getEPharmacyOrderDetail(any(), any(), 0L, "", false)
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getEPharmacyOrderDetail(0L, "", false)
        Assert.assertEquals(
            (viewModel.ePharmacyOrderDetailData.value as Fail).throwable,
            mockThrowable
        )
    }
}
