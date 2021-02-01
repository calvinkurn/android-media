package com.tokopedia.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.paylater.creditcard.domain.model.*
import com.tokopedia.paylater.creditcard.domain.usecase.CreditCardBankDataUseCase
import com.tokopedia.paylater.creditcard.domain.usecase.CreditCardPdpMetaInfoUseCase
import com.tokopedia.paylater.creditcard.domain.usecase.CreditCardSimulationUseCase
import com.tokopedia.paylater.creditcard.viewmodel.CreditCardViewModel
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
    private val creditSimulationItem1 = CreditCardInstallmentItem(
            1, "", 1000, 10000.0f, 4, false, arrayListOf(),
            isDisabled = true,
            isSelected = false
    )
    private val creditSimulationItem2 = CreditCardInstallmentItem(
            2, "", 10000, 12000.0f, 4, false, arrayListOf(),
            isDisabled = true,
            isSelected = false
    )
    private val creditSimulationItem3 = CreditCardInstallmentItem(
            3, "", 1000, 15000.0f, 4, false, arrayListOf(),
            isDisabled = false,
            isSelected = true
    )


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

    @Test
    fun `Execute getCreditCardSimulationData Fail(Invoke Failed)`() {
        coEvery {
            creditCardSimulationUseCase.getCreditCardSimulationData(any(), any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        coEvery {
            creditCardSimulationUseCase.cancelJobs()
        } just Runs
        viewModel.getCreditCardSimulationData(1000000.0f)
        assert(viewModel.creditCardSimulationResultLiveData.value is Fail)
        Assertions.assertThat((viewModel.creditCardSimulationResultLiveData.value as Fail).throwable.message).isEqualTo(fetchFailedErrorMessage)
    }

    @Test
    fun `Execute getCreditCardSimulationData Fail(Data Failure)`() {
        val mockSimulationData = PdpCreditCardSimulation(
                CreditCardSimulationResult(
                        arrayListOf(),
                        1000000.0f,
                        "",
                        "",
                        ""
                )
        )
        coEvery {
            creditCardSimulationUseCase.getCreditCardSimulationData(any(), any(), any())
        } coAnswers {
            firstArg<(PdpCreditCardSimulation) -> Unit>().invoke(mockSimulationData)
        }
        coEvery {
            creditCardSimulationUseCase.cancelJobs()
        } just Runs
        viewModel.getCreditCardSimulationData(1000000.0f)
        assert(viewModel.creditCardSimulationResultLiveData.value is Fail)
        Assertions.assertThat((viewModel.creditCardSimulationResultLiveData.value as Fail).throwable.message).isEqualTo(nullDataErrorMessage)
    }

    @Test
    fun `Execute getCreditCardSimulationData Credit Card Simulation Not Applicable`() {
        val mockSimulationData = PdpCreditCardSimulation(
                CreditCardSimulationResult(
                        arrayListOf(creditSimulationItem1, creditSimulationItem2),
                        1000000.0f,
                        "",
                        "",
                        ""
                )
        )
        coEvery {
            creditCardSimulationUseCase.getCreditCardSimulationData(any(), any(), any())
        } coAnswers {
            firstArg<(PdpCreditCardSimulation) -> Unit>().invoke(mockSimulationData)
        }
        coEvery {
            creditCardSimulationUseCase.cancelJobs()
        } just Runs
        viewModel.getCreditCardSimulationData(1000000.0f)
        assert(viewModel.creditCardSimulationResultLiveData.value is Fail)
        Assertions.assertThat((viewModel.creditCardSimulationResultLiveData.value as Fail).throwable.message).isEqualTo(CreditCardViewModel.CREDIT_CARD_NOT_AVAILABLE)
    }


    @Test
    fun `Execute getCreditCardSimulationData Success`() {
        val mockSimulationData = PdpCreditCardSimulation(
                CreditCardSimulationResult(
                        arrayListOf(creditSimulationItem1, creditSimulationItem2, creditSimulationItem3),
                        1000000.0f,
                        "",
                        "",
                        ""
                )
        )
        coEvery {
            creditCardSimulationUseCase.getCreditCardSimulationData(any(), any(), any())
        } coAnswers {
            firstArg<(PdpCreditCardSimulation) -> Unit>().invoke(mockSimulationData)
        }
        coEvery {
            creditCardSimulationUseCase.cancelJobs()
        } just Runs
        viewModel.getCreditCardSimulationData(1000000.0f)
        assert(viewModel.creditCardSimulationResultLiveData.value is Success)
        val actualInstallmentAmount = (viewModel.creditCardSimulationResultLiveData.value as Success).data.creditCardInstallmentList?.getOrNull(2)?.installmentAmount
        val expectedInstallmentAmount = mockSimulationData.creditCardGetSimulationResult?.creditCardInstallmentList?.getOrNull(2)?.installmentAmount
        Assertions.assertThat(actualInstallmentAmount).isEqualTo(expectedInstallmentAmount)
    }


    @Test
    fun `Execute getCreditCardTncData Fail(Invoke Failed)`() {
        coEvery {
            creditCardPdpMetaInfoUseCase.getPdpMetaData(any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        coEvery {
            creditCardPdpMetaInfoUseCase.cancelJobs()
        } just Runs
        viewModel.getCreditCardTncData()
        assert(viewModel.creditCardPdpMetaInfoLiveData.value is Fail)
        Assertions.assertThat((viewModel.creditCardPdpMetaInfoLiveData.value as Fail).throwable.message).isEqualTo(fetchFailedErrorMessage)
    }

    @Test
    fun `Execute getCreditCardTncData Fail(Data Failed)`() {
        val mockData = CreditCardPdpMetaData("", "", "", arrayListOf())
        coEvery {
            creditCardPdpMetaInfoUseCase.getPdpMetaData(any(), any())
        } coAnswers {
            firstArg<(CreditCardPdpMetaData) -> Unit>().invoke(mockData)
        }
        coEvery {
            creditCardPdpMetaInfoUseCase.cancelJobs()
        } just Runs
        viewModel.getCreditCardTncData()
        assert(viewModel.creditCardPdpMetaInfoLiveData.value is Fail)
        Assertions.assertThat((viewModel.creditCardPdpMetaInfoLiveData.value as Fail).throwable.message).isEqualTo(nullDataErrorMessage)
    }

    @Test
    fun `Execute getCreditCardTncData Success`() {
        val pdpInfoItem = CreditCardPdpInfoContent("title1", "", "bullet", "", arrayListOf(), arrayListOf(), null, 0)
        val mockData = CreditCardPdpMetaData("", "", "", arrayListOf(pdpInfoItem))
        coEvery {
            creditCardPdpMetaInfoUseCase.getPdpMetaData(any(), any())
        } coAnswers {
            firstArg<(CreditCardPdpMetaData) -> Unit>().invoke(mockData)
        }
        coEvery {
            creditCardPdpMetaInfoUseCase.cancelJobs()
        } just Runs
        viewModel.getCreditCardTncData()
        assert(viewModel.creditCardPdpMetaInfoLiveData.value is Success)
        val actualContentType = (viewModel.creditCardPdpMetaInfoLiveData.value as Success).data.pdpInfoContentList?.getOrNull(0)?.contentType
        val expectedContentType = mockData.pdpInfoContentList?.getOrNull(0)?.contentType
        Assertions.assertThat(actualContentType).isEqualTo(expectedContentType)
    }

    @Test
    fun `Execute getBankCardList Fail(Invoke Failed)`() {
        coEvery {
            creditCardBankDataUseCase.getBankCardList(any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        coEvery {
            creditCardBankDataUseCase.cancelJobs()
        } just Runs
        viewModel.getBankCardList()
        assert(viewModel.creditCardBankResultLiveData.value is Fail)
        Assertions.assertThat((viewModel.creditCardBankResultLiveData.value as Fail).throwable.message).isEqualTo(fetchFailedErrorMessage)
    }


    @Test
    fun `Execute getBankCardList Fail(Data Failed)`() {
        val mockData = CreditCardBankData(arrayListOf())
        coEvery {
            creditCardBankDataUseCase.getBankCardList(any(), any())
        } coAnswers {
            firstArg<(CreditCardBankData) -> Unit>().invoke(mockData)
        }
        coEvery {
            creditCardBankDataUseCase.cancelJobs()
        } just Runs
        viewModel.getBankCardList()
        assert(viewModel.creditCardBankResultLiveData.value is Fail)
        Assertions.assertThat((viewModel.creditCardBankResultLiveData.value as Fail).throwable.message).isEqualTo(nullDataErrorMessage)
    }

    @Test
    fun `Execute getBankCardList Success`() {
        val mockData = CreditCardBankData(arrayListOf(BankCardListItem("bank1", "", "", "", true, arrayListOf())))
        coEvery {
            creditCardBankDataUseCase.getBankCardList(any(), any())
        } coAnswers {
            firstArg<(CreditCardBankData) -> Unit>().invoke(mockData)
        }
        coEvery {
            creditCardBankDataUseCase.cancelJobs()
        } just Runs
        viewModel.getBankCardList()
        assert(viewModel.creditCardBankResultLiveData.value is Success)
        val actualBankName = (viewModel.creditCardBankResultLiveData.value as Success).data.getOrNull(0)?.bankName
        val expectedBankName = mockData.bankCardList?.getOrNull(0)?.bankName
        Assertions.assertThat(actualBankName).isEqualTo(expectedBankName)
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