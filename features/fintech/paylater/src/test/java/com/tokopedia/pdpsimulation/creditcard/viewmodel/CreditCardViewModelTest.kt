package com.tokopedia.pdpsimulation.creditcard.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.pdpsimulation.creditcard.domain.model.*
import com.tokopedia.pdpsimulation.creditcard.domain.usecase.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.assertj.core.api.Assertions
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CreditCardViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val creditCardSimulationUseCase = mockk<CreditCardSimulationUseCase>(relaxed = true)
    private val creditCardPdpMetaInfoUseCase = mockk<CreditCardPdpMetaInfoUseCase>(relaxed = true)
    private val creditCardBankDataUseCase = mockk<CreditCardBankDataUseCase>(relaxed = true)
    private val creditTncusecase = mockk<CreditCardTncMapperUseCase>(relaxed = true)
    private val creditCardSimulationMapperUseCase = mockk<CreditCardSimulationMapperUseCase>(relaxed = true)

    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: CreditCardViewModel

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val nullDataErrorMessage = "NULL DATA"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)
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
    private var creditCardSimulationObserver = mockk<Observer<Result<CreditCardSimulationResult>>>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = CreditCardViewModel(
                creditCardSimulationUseCase,
                creditCardPdpMetaInfoUseCase,
                creditCardBankDataUseCase,
                creditTncusecase,
                creditCardSimulationMapperUseCase,
                dispatcher,
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
        viewModel.getCreditCardSimulationData(1000000L)
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

        coEvery {
            creditCardSimulationMapperUseCase.parseSimulationData(any(), any(), any())
        } coAnswers {
            secondArg<(CCSimulationDataStatus) -> Unit>().invoke(StatusApiFail)
        }
        viewModel.getCreditCardSimulationData(1000000L)
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

        coEvery {
            creditCardSimulationMapperUseCase.parseSimulationData(any(), any(), any())
        } coAnswers {
            secondArg<(CCSimulationDataStatus) -> Unit>().invoke(StatusCCNotAvailable)
        }
        viewModel.getCreditCardSimulationData(1000000L)
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

        coEvery {
            creditCardSimulationMapperUseCase.parseSimulationData(any(), any(), any())
        } coAnswers {
            secondArg<(CCSimulationDataStatus) -> Unit>().invoke(StatusApiSuccess(mockSimulationData.creditCardGetSimulationResult!!))
        }

        viewModel.getCreditCardSimulationData(1000000L)
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
        val mockThrowable = Throwable(message = nullDataErrorMessage)
        coEvery {
            creditCardPdpMetaInfoUseCase.getPdpMetaData(any(), any())
        } coAnswers {
            firstArg<(CreditCardPdpMetaData) -> Unit>().invoke(mockData)
        }
        coEvery {
            creditCardPdpMetaInfoUseCase.cancelJobs()
        } just Runs

        coEvery {
            creditTncusecase.parseTncData(any(), any(), any())
        } coAnswers {
            thirdArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
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

        coEvery {
            creditTncusecase.parseTncData(any(), any(), any())
        } coAnswers {
            secondArg<(CreditCardPdpMetaData) -> Unit>().invoke(mockData)
        }
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
    fun `Execute getBankCardList Success`() {
        val mockData = CreditCardBankData(arrayListOf(BankCardListItem("bank1", "", "", "", true, arrayListOf())))
        coEvery {
            creditCardBankDataUseCase.getBankCardList(any(), any())
        } coAnswers {
            firstArg<(ArrayList<BankCardListItem>) -> Unit>().invoke(mockData.bankCardList!!)
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

    @Test
    fun `Execute getRedirectionUrl Success`() {
        val mockData = CreditCardPdpMetaData("", "url", "", arrayListOf())
        coEvery {
            creditCardPdpMetaInfoUseCase.getPdpMetaData(any(), any())
        } coAnswers {
            firstArg<(CreditCardPdpMetaData) -> Unit>().invoke(mockData)
        }
        coEvery {
            creditCardPdpMetaInfoUseCase.cancelJobs()
        } just Runs

        coEvery {
            creditTncusecase.parseTncData(any(), any(), any())
        } coAnswers {
            secondArg<(CreditCardPdpMetaData) -> Unit>().invoke(mockData)
        }
        viewModel.getCreditCardTncData()
        Assertions.assertThat(viewModel.getRedirectionUrl() == mockData.ctaRedirectionUrl)
    }

    @Test
    fun `Execute getRedirectionUrl Fail`() {
        coEvery {
            creditCardPdpMetaInfoUseCase.getPdpMetaData(any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        coEvery {
            creditCardPdpMetaInfoUseCase.cancelJobs()
        } just Runs
        viewModel.getCreditCardTncData()
        assert(viewModel.getRedirectionUrl().isEmpty())
    }

}