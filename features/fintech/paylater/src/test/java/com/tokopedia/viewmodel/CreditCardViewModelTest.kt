package com.tokopedia.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.paylater.domain.model.CreditCardSimulationResult
import com.tokopedia.paylater.domain.usecase.CreditCardBankDataUseCase
import com.tokopedia.paylater.domain.usecase.CreditCardPdpMetaInfoUseCase
import com.tokopedia.paylater.domain.usecase.CreditCardSimulationUseCase
import com.tokopedia.paylater.presentation.viewModel.CreditCardViewModel
import com.tokopedia.usecase.coroutines.Result
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
class CreditCardViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val creditCardSimulationUseCase = mockk<CreditCardSimulationUseCase>(relaxed = true)
    val creditCardPdpMetaInfoUseCase = mockk<CreditCardPdpMetaInfoUseCase>(relaxed = true)
    val creditCardBankDataUseCase = mockk<CreditCardBankDataUseCase>(relaxed = true)

    val dispatcher = TestCoroutineDispatcher()
    lateinit var viewModel: CreditCardViewModel

    val fetchFailedErrorMessage = "Fetch Failed"
    val nullDataErrorMessage = "NULL DATA"
    val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    //private val emptyCreditCardSimulationResponse = CreditCardGetSimulationResponse()
    private var creditCardSimulationObserver = mockk<Observer<Result<CreditCardSimulationResult>>>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = CreditCardViewModel(
                creditCardSimulationUseCase,
                creditCardPdpMetaInfoUseCase,
                creditCardBankDataUseCase,
                dispatcher,
                dispatcher
        )
        viewModel.creditCardSimulationResultLiveData.observeForever(creditCardSimulationObserver)
    }
/*

    @Test
    fun `Execute getPayLaterProductData product list empty`() {
        // given
        coEvery {
            payLaterProductDetailUseCase.cancelJobs()
        } just Runs

        coEvery {
            payLaterProductDetailUseCase.getPayLaterData(any(), any())
        } coAnswers {
            firstArg<(PayLaterProductData?) -> Unit>().invoke(emptyPayLaterActivityResponseResult.productData)
        }
        // when
        viewModel.getPayLaterProductData()

        //then
        assert(viewModel.payLaterActivityResultLiveData.value is Fail)
        Assertions.assertThat((viewModel.payLaterActivityResultLiveData.value as Fail).throwable.message).isEqualTo(nullDataErrorMessage)
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
        Assertions.assertThat((viewModel.payLaterSimulationResultLiveData.value as Fail).throwable.message).isEqualTo(fetchFailedErrorMessage)
    }

    @Test
    fun `Execute getPayLaterSimulationData Fail(Amount less than 10000)`() {
        coEvery {
            payLaterSimulationDataUseCase.cancelJobs()
        } just Runs
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
        viewModel.getPayLaterApplicationStatus()
        assert(viewModel.payLaterApplicationStatusResultLiveData.value is Success)
        val actual = (viewModel.payLaterApplicationStatusResultLiveData.value as Success).data.applicationDetailList?.getOrNull(0)?.payLaterGatewayName
        val expected = mockApplicationStatusData.userCreditApplicationStatus.applicationDetailList?.getOrNull(0)?.payLaterGatewayName
        Assertions.assertThat(actual).isEqualTo(expected)
    }
*/


}