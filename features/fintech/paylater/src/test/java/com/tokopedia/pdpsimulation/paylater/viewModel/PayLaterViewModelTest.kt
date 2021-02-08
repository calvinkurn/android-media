package com.tokopedia.pdpsimulation.paylater.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.tokopedia.pdpsimulation.PayLaterHelper
import com.tokopedia.pdpsimulation.paylater.domain.model.*
import com.tokopedia.pdpsimulation.paylater.domain.usecase.*
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
        assert(viewModel.payLaterSimulationResultLiveData.value is Fail)
        coVerify(exactly = 0) { payLaterTenureMapperUseCase.mapTenureToSimulation(any(), any(), any()) }
        Assertions.assertThat((viewModel.payLaterSimulationResultLiveData.value as Fail).throwable.message).isEqualTo(fetchFailedErrorMessage)
    }

    @Test
    fun `Execute getPayLaterSimulationData Fail(Data Failure)`() {
        val mockSimulationData = PayLaterGetSimulationResponse(PayLaterGetSimulationGateway(arrayListOf()))

        coEvery {
            payLaterSimulationDataUseCase.getSimulationData(any(), any(), any())
        } coAnswers {
            firstArg<(PayLaterGetSimulationResponse) -> Unit>().invoke(mockSimulationData)
        }
        coEvery {
            payLaterSimulationDataUseCase.cancelJobs()
        } just Runs

        coEvery {
            payLaterTenureMapperUseCase.mapTenureToSimulation(mockSimulationData, any(), any())
        } coAnswers {
            secondArg<(PayLaterSimulationDataStatus) -> Unit>().invoke(StatusDataFailure)
        }

        viewModel.getPayLaterSimulationData(1000000)
        assert(viewModel.payLaterSimulationResultLiveData.value is Fail)
        Assertions.assertThat((viewModel.payLaterSimulationResultLiveData.value as Fail).throwable.message).isEqualTo(nullDataErrorMessage)
    }

    @Test
    fun `Execute getPayLaterSimulationData PayLater Not Applicable`() {
        val item = PayLaterSimulationGatewayItem(1, "Kredivo", "", "", arrayListOf(), HashMap(), false)
        val mockSimulationData = PayLaterGetSimulationResponse(PayLaterGetSimulationGateway(arrayListOf(item, item)))

        coEvery {
            payLaterSimulationDataUseCase.getSimulationData(any(), any(), any())
        } coAnswers {
            firstArg<(PayLaterGetSimulationResponse) -> Unit>().invoke(mockSimulationData)
        }
        coEvery {
            payLaterSimulationDataUseCase.cancelJobs()
        } just Runs

        coEvery {
            payLaterTenureMapperUseCase.mapTenureToSimulation(any(), any(), any())
        } coAnswers {
            secondArg<(PayLaterSimulationDataStatus) -> Unit>().invoke(StatusPayLaterNotAvailable)
        }

        viewModel.getPayLaterSimulationData(100)
        assert(viewModel.payLaterSimulationResultLiveData.value is Fail)
        Assertions.assertThat((viewModel.payLaterSimulationResultLiveData.value as Fail).throwable.message).isEqualTo(PayLaterViewModel.PAY_LATER_NOT_APPLICABLE)
    }

    @Test
    fun `Execute getPayLaterSimulationData Success`() {
        val mockSimulationData = Gson().fromJson(PayLaterHelper.getJson("simulationtabledata.json"), PayLaterGetSimulationResponse::class.java)

        coEvery {
            payLaterSimulationDataUseCase.getSimulationData(any(), any(), any())
        } coAnswers {
            firstArg<(PayLaterGetSimulationResponse) -> Unit>().invoke(mockSimulationData)
        }
        coEvery {
            payLaterSimulationDataUseCase.cancelJobs()
        } just Runs

        coEvery {
            payLaterTenureMapperUseCase.mapTenureToSimulation(mockSimulationData, any(), any())
        } coAnswers {
            secondArg<(PayLaterSimulationDataStatus) -> Unit>().invoke(StatusSuccess(mockSimulationData.payLaterGetSimulationGateway?.payLaterGatewayList!!))
        }

        viewModel.getPayLaterSimulationData(1000000)

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
        assert(viewModel.payLaterApplicationStatusResultLiveData.value is Fail)
        Assertions.assertThat((viewModel.payLaterApplicationStatusResultLiveData.value as Fail).throwable.message).isEqualTo(fetchFailedErrorMessage)
    }

    @Test
    fun `Execute getPayLaterApplicationStatus Success`() {
        val mockApplicationStatusData = Gson().fromJson(PayLaterHelper.getJson("applicationstatusdata.json"), PayLaterApplicationStatusResponse::class.java)
        coEvery {
            payLaterApplicationStatusUseCase.getPayLaterApplicationStatus(any(), any())
        } coAnswers {
            firstArg<(UserCreditApplicationStatus) -> Unit>().invoke(mockApplicationStatusData.userCreditApplicationStatus)
        }
        coEvery {
            payLaterApplicationStatusUseCase.cancelJobs()
        } just Runs

        coEvery {
            payLaterApplicationStatusMapperUseCase.mapLabelDataToApplicationStatus(mockApplicationStatusData.userCreditApplicationStatus, any(), any())
        } coAnswers {
            secondArg<(PayLaterAppStatus) -> Unit>().invoke(StatusAppSuccess(mockApplicationStatusData.userCreditApplicationStatus, false))
        }

        viewModel.getPayLaterApplicationStatus()
        assert(viewModel.payLaterApplicationStatusResultLiveData.value is Success)
        val actual = (viewModel.payLaterApplicationStatusResultLiveData.value as Success).data.applicationDetailList?.getOrNull(0)?.payLaterGatewayName
        val expected = mockApplicationStatusData.userCreditApplicationStatus.applicationDetailList?.getOrNull(0)?.payLaterGatewayName
        Assertions.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `Execute getPayLaterOptions on Success`() {
        val mockSimulationData = Gson().fromJson(PayLaterHelper.getJson("simulationtabledata.json"), PayLaterGetSimulationResponse::class.java)
        coEvery {
            payLaterTenureMapperUseCase.mapTenureToSimulation(mockSimulationData, any(), any())
        } coAnswers {
            secondArg<(PayLaterSimulationDataStatus) -> Unit>().invoke(StatusSuccess(mockSimulationData.payLaterGetSimulationGateway?.payLaterGatewayList!!))
        }

        viewModel.getPayLaterSimulationData(1000000)

        Assertions.assertThat(mockSimulationData.payLaterGetSimulationGateway?.payLaterGatewayList?.size == viewModel.getPayLaterOptions().size)

    }

    @Test
    fun `Execute getPayLaterOptions on Fail`() {
        val mockSimulationData = PayLaterGetSimulationResponse(PayLaterGetSimulationGateway(arrayListOf()))

        coEvery {
            payLaterSimulationDataUseCase.cancelJobs()
        } just Runs

        coEvery {
            payLaterTenureMapperUseCase.mapTenureToSimulation(mockSimulationData, any(), any())
        } coAnswers {
            secondArg<(PayLaterSimulationDataStatus) -> Unit>().invoke(StatusDataFailure)
        }

        viewModel.getPayLaterSimulationData(1000000)

        Assertions.assertThat(0 == viewModel.getPayLaterOptions().size)

    }
}