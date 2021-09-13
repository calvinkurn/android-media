package com.tokopedia.pdpsimulation.paylater.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.tokopedia.pdpsimulation.PayLaterHelper
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterApplicationStatusResponse
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterProductData
import com.tokopedia.pdpsimulation.paylater.domain.model.UserCreditApplicationStatus
import com.tokopedia.pdpsimulation.paylater.domain.usecase.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PayLaterViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val payLaterApplicationStatusUseCase =
        mockk<PayLaterApplicationStatusUseCase>(relaxed = true)
    private val payLaterApplicationStatusMapperUseCase =
        mockk<PayLaterApplicationStatusMapperUseCase>(relaxed = true)
    private val productDetail = mockk<ProductDetailUseCase>(relaxed = true)
    private val payLaterSimulationData = mockk<PayLaterSimulationV2UseCase>(relaxed = true)
    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: PayLaterViewModel

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val nullDataErrorMessage = "NULL DATA"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)
    private var payLaterActivityObserver =
        mockk<Observer<Result<PayLaterProductData>>>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = PayLaterViewModel(
            payLaterApplicationStatusUseCase,
            payLaterApplicationStatusMapperUseCase,
            payLaterSimulationData,
            productDetail,
            dispatcher
        )
    }


    @Test
    fun `Execute getPayLaterApplicationStatus Fail(Invoke Failed)`() {
        coEvery {
            payLaterApplicationStatusUseCase.getPayLaterApplicationStatus(any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        coEvery {
            payLaterApplicationStatusUseCase.cancelJobs()
        } just Runs

        viewModel.getPayLaterApplicationStatus()
        assert(viewModel.payLaterApplicationStatusResultLiveData.value is Fail)
        Assertions.assertThat((viewModel.payLaterApplicationStatusResultLiveData.value as Fail).throwable.message)
            .isEqualTo(fetchFailedErrorMessage)
    }

    @Test
    fun `Execute getPayLaterApplicationStatus Success`() {
        val mockApplicationStatusData = Gson().fromJson(
            PayLaterHelper.getJson("applicationstatusdata.json"),
            PayLaterApplicationStatusResponse::class.java
        )
        coEvery {
            payLaterApplicationStatusUseCase.getPayLaterApplicationStatus(any(), any())
        } coAnswers {
            firstArg<(UserCreditApplicationStatus) -> Unit>().invoke(mockApplicationStatusData.userCreditApplicationStatus)
        }
        coEvery {
            payLaterApplicationStatusUseCase.cancelJobs()
        } just Runs

        coEvery {
            payLaterApplicationStatusMapperUseCase.mapLabelDataToApplicationStatus(
                mockApplicationStatusData.userCreditApplicationStatus,
                any(),
                any()
            )
        } coAnswers {
            secondArg<(PayLaterAppStatus) -> Unit>().invoke(
                StatusAppSuccess(
                    mockApplicationStatusData.userCreditApplicationStatus,
                    false
                )
            )
        }

        viewModel.getPayLaterApplicationStatus()
        assert(viewModel.payLaterApplicationStatusResultLiveData.value is Success)
        val actual =
            (viewModel.payLaterApplicationStatusResultLiveData.value as Success).data.applicationDetailList?.getOrNull(
                0
            )?.payLaterGatewayName
        val expected =
            mockApplicationStatusData.userCreditApplicationStatus.applicationDetailList?.getOrNull(0)?.payLaterGatewayName
        Assertions.assertThat(actual).isEqualTo(expected)
    }


}