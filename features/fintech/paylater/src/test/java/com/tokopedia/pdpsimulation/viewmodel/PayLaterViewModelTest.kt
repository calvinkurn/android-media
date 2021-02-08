package com.tokopedia.pdpsimulation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.tokopedia.pdpsimulation.PayLaterHelper
import com.tokopedia.pdpsimulation.paylater.domain.model.*
import com.tokopedia.pdpsimulation.paylater.domain.usecase.*
import com.tokopedia.pdpsimulation.paylater.viewModel.PayLaterViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
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

    private val payLaterProductDetailUseCase = mockk<PayLaterProductDetailUseCase>(relaxed = true)
    private val payLaterApplicationStatusUseCase = mockk<PayLaterApplicationStatusUseCase>(relaxed = true)
    private val payLaterSimulationDataUseCase = mockk<PayLaterSimulationUseCase>(relaxed = true)
    private val payLaterTenureMapperUseCase = mockk<PayLaterTenureMapperUseCase>(relaxed = true)
    private val payLaterApplicationStatusMapperUseCase = mockk<PayLaterApplicationStatusMapperUseCase>(relaxed = true)

    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: PayLaterViewModel

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val nullDataErrorMessage = "NULL DATA"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)
    private var payLaterActivityObserver = mockk<Observer<Result<PayLaterProductData>>>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = PayLaterViewModel(
                payLaterProductDetailUseCase,
                payLaterApplicationStatusUseCase,
                payLaterSimulationDataUseCase,
                payLaterTenureMapperUseCase,
                payLaterApplicationStatusMapperUseCase,
                dispatcher,
        )
        viewModel.payLaterActivityResultLiveData.observeForever(payLaterActivityObserver)
    }

    @Test
    fun `Execute getPayLaterProductData Fail(Invoke Failed)`() {
        coEvery {
            payLaterProductDetailUseCase.getPayLaterData(any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        coEvery {
            payLaterProductDetailUseCase.cancelJobs()
        } just Runs
        viewModel.getPayLaterProductData()
        assert(viewModel.payLaterActivityResultLiveData.value is Fail)
        Assertions.assertThat((viewModel.payLaterActivityResultLiveData.value as Fail).throwable.message).isEqualTo(fetchFailedErrorMessage)
    }

    @Test
    fun `Execute getPayLaterProductData Success`() {
        val mockPayLaterData = Gson().fromJson(PayLaterHelper.getJson("paylaterproduct.json"), PayLaterActivityResponse::class.java)

        coEvery {
            payLaterProductDetailUseCase.getPayLaterData(any(), any())
        } coAnswers {
            firstArg<(PayLaterProductData) -> Unit>().invoke(mockPayLaterData.productData)
        }
        coEvery {
            payLaterProductDetailUseCase.cancelJobs()
        } just Runs

        viewModel.getPayLaterProductData()
        assert(viewModel.payLaterActivityResultLiveData.value is Success)
        val partnerNameActual = (viewModel.payLaterActivityResultLiveData.value as Success).data.productList?.getOrNull(0)?.partnerName
        val partnerNameExpected = mockPayLaterData.productData.productList?.getOrNull(0)?.partnerName
        Assertions.assertThat(partnerNameActual).isEqualTo(partnerNameExpected)
    }

    @Test
    fun `Execute getPayLaterSimulationData Fail(Invoke Failed)`() {
        coEvery {
            payLaterSimulationDataUseCase.getSimulationData(any(), any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        coEvery {
            payLaterSimulationDataUseCase.cancelJobs()
        } just Runs
        viewModel.getPayLaterSimulationData(1000000)

        coVerify(exactly = 0) { payLaterTenureMapperUseCase.mapTenureToSimulation(any(), any(), any()) }
        assert(viewModel.payLaterSimulationResultLiveData.value is Fail)
        Assertions.assertThat((viewModel.payLaterSimulationResultLiveData.value as Fail).throwable.message).isEqualTo(fetchFailedErrorMessage)
    }

    @Test
    fun `Execute getPayLaterSimulationData Response Data Success`() {
        val mockSimulationData = PayLaterGetSimulationResponse(PayLaterGetSimulationGateway(arrayListOf()))

        coEvery {
            payLaterSimulationDataUseCase.getSimulationData(any(), any(), any())
        } coAnswers {
            firstArg<(PayLaterGetSimulationResponse) -> Unit>().invoke(mockSimulationData)
        }
        coEvery {
            payLaterSimulationDataUseCase.cancelJobs()
        } just Runs

        viewModel.getPayLaterSimulationData(1000000)
        coVerify(exactly = 1) { payLaterTenureMapperUseCase.mapTenureToSimulation(any(), any(), any()) }
    }

    @Test
    fun `Execute mapTenureToSimulation Data Failure`() {
        val mockSimulationData = PayLaterGetSimulationResponse(PayLaterGetSimulationGateway(arrayListOf()))

        coEvery {
            payLaterTenureMapperUseCase.mapTenureToSimulation(any(), any(), any())
        } coAnswers {
            secondArg<(PayLaterSimulationDataStatus) -> Unit>().invoke(StatusDataFailure)
        }

        viewModel.onPayLaterSimulationDataSuccess(mockSimulationData)
        assert(viewModel.payLaterSimulationResultLiveData.value is Fail)
        Assertions.assertThat((viewModel.payLaterSimulationResultLiveData.value as Fail).throwable.message).isEqualTo(PayLaterViewModel.DATA_FAILURE)
    }

    @Test
    fun `Execute mapTenureToSimulation PayLater Not Applicable`() {
        val item = PayLaterSimulationGatewayItem(1, "Kredivo", "", "", arrayListOf(), HashMap(), false)
        val mockSimulationData = PayLaterGetSimulationResponse(PayLaterGetSimulationGateway(arrayListOf(item, item)))

        coEvery {
            payLaterTenureMapperUseCase.mapTenureToSimulation(any(), any(), any())
        } coAnswers {
            secondArg<(PayLaterSimulationDataStatus) -> Unit>().invoke(StatusPayLaterNotAvailable)
        }

        viewModel.onPayLaterSimulationDataSuccess(mockSimulationData)
        assert(viewModel.payLaterSimulationResultLiveData.value is Fail)
        Assertions.assertThat((viewModel.payLaterSimulationResultLiveData.value as Fail).throwable.message).isEqualTo(PayLaterViewModel.PAY_LATER_NOT_APPLICABLE)
    }

    @Test
    fun `Execute getPayLaterSimulationData Success`() {
        val mockSimulationData = Gson().fromJson(PayLaterHelper.getJson("simulationtabledata.json"), PayLaterGetSimulationResponse::class.java)

        coEvery {
            payLaterTenureMapperUseCase.mapTenureToSimulation(mockSimulationData, any(), any())
        } coAnswers {
            secondArg<(PayLaterSimulationDataStatus) -> Unit>().invoke(StatusSuccess(mockSimulationData.payLaterGetSimulationGateway?.payLaterGatewayList!!))
        }

        viewModel.onPayLaterSimulationDataSuccess(mockSimulationData)

        assert(viewModel.payLaterSimulationResultLiveData.value is Success)
        val expectedGatewayName = mockSimulationData.payLaterGetSimulationGateway?.payLaterGatewayList?.getOrNull(0)?.gatewayName
        val actualGatewayName = (viewModel.payLaterSimulationResultLiveData.value as Success).data.getOrNull(0)?.gatewayName
        Assertions.assertThat(actualGatewayName).isEqualTo(expectedGatewayName)
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
        coVerify(exactly = 0) { payLaterApplicationStatusMapperUseCase.mapLabelDataToApplicationStatus(any(), any(), any()) }

        assert(viewModel.payLaterApplicationStatusResultLiveData.value is Fail)
        Assertions.assertThat((viewModel.payLaterApplicationStatusResultLiveData.value as Fail).throwable.message).isEqualTo(fetchFailedErrorMessage)
    }

    @Test
    fun `Execute getPayLaterApplicationStatus Response Success`() {
        val mockData = UserCreditApplicationStatus("", arrayListOf())
        coEvery {
            payLaterApplicationStatusUseCase.getPayLaterApplicationStatus(any(), any())
        } coAnswers {
            firstArg<(UserCreditApplicationStatus) -> Unit>().invoke(mockData)
        }
        coEvery {
            payLaterApplicationStatusUseCase.cancelJobs()
        } just Runs

        viewModel.getPayLaterApplicationStatus()
        coVerify(exactly = 1) { payLaterApplicationStatusMapperUseCase.mapLabelDataToApplicationStatus(any(), any(), any()) }
    }


    @Test
    fun `Execute mapLabelDataToApplicationStatus Exception`() {
        val mockData = UserCreditApplicationStatus("", arrayListOf())
        val mockThrowable = Throwable(nullDataErrorMessage)
        coEvery {
            payLaterApplicationStatusMapperUseCase.mapLabelDataToApplicationStatus(any(), any(), any())
        } coAnswers {
            thirdArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.onPayLaterApplicationStatusSuccess(mockData)
        assert(viewModel.payLaterApplicationStatusResultLiveData.value is Fail)
        Assertions.assertThat((viewModel.payLaterApplicationStatusResultLiveData.value as Fail).throwable.message).isEqualTo(nullDataErrorMessage)
    }

    @Test
    fun `Execute mapLabelDataToApplicationStatus Data Failure`() {
        val mockData = UserCreditApplicationStatus("", arrayListOf())
        coEvery {
            payLaterApplicationStatusMapperUseCase.mapLabelDataToApplicationStatus(any(), any(), any())
        } coAnswers {
            secondArg<(PayLaterAppStatus) -> Unit>().invoke(StatusFail)
        }

        viewModel.onPayLaterApplicationStatusSuccess(mockData)
        assert(viewModel.payLaterApplicationStatusResultLiveData.value is Fail)
        Assertions.assertThat((viewModel.payLaterApplicationStatusResultLiveData.value as Fail).throwable.message).isEqualTo(nullDataErrorMessage)
    }

    @Test
    fun `Execute getPayLaterApplicationStatus Success`() {
        val mockApplicationStatusData = Gson().fromJson(PayLaterHelper.getJson("applicationstatusdata.json"), PayLaterApplicationStatusResponse::class.java)

        coEvery {
            payLaterApplicationStatusMapperUseCase.mapLabelDataToApplicationStatus(mockApplicationStatusData.userCreditApplicationStatus, any(), any())
        } coAnswers {
            secondArg<(PayLaterAppStatus) -> Unit>().invoke(StatusAppSuccess(mockApplicationStatusData.userCreditApplicationStatus, false))
        }

        viewModel.onPayLaterApplicationStatusSuccess(mockApplicationStatusData.userCreditApplicationStatus)
        assert(viewModel.payLaterApplicationStatusResultLiveData.value is Success)
        val actual = (viewModel.payLaterApplicationStatusResultLiveData.value as Success).data.applicationDetailList?.getOrNull(0)?.payLaterGatewayName
        val expected = mockApplicationStatusData.userCreditApplicationStatus.applicationDetailList?.getOrNull(0)?.payLaterGatewayName
        Assertions.assertThat(actual).isEqualTo(expected)
    }
}