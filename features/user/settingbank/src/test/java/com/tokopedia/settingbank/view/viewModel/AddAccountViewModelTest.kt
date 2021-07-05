package com.tokopedia.settingbank.view.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.settingbank.domain.model.AddBankResponse
import com.tokopedia.settingbank.domain.model.TemplateData
import com.tokopedia.settingbank.domain.usecase.*
import com.tokopedia.settingbank.view.viewState.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddAccountViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val addBankAccountUseCase: dagger.Lazy<AddBankAccountUseCase> = mockk()
    private val termsAndConditionUseCase: dagger.Lazy<TermsAndConditionUseCase> = mockk()
    private val checkBankAccountUseCase: dagger.Lazy<CheckBankAccountUseCase> = mockk()
    private val validateAccountNumberUseCase: dagger.Lazy<ValidateAccountNumberUseCase> = mockk()

    private lateinit var viewModel: AddAccountViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        viewModel = spyk(AddAccountViewModel(addBankAccountUseCase,
                termsAndConditionUseCase,
                checkBankAccountUseCase,
                validateAccountNumberUseCase, TestCoroutineDispatcher()))
    }

    @Test
    fun `addBank success`() {
        val result = mockk<AddBankResponse>()
        coEvery { addBankAccountUseCase.get().addBankAccount(any(), any(), any()) }
                .coAnswers {
                    secondArg<(AddBankResponse) -> Unit>().invoke(result)
                }
        viewModel.addBank(mockk())
        assert(viewModel.addBankAccountLiveData.value is Success)
    }

    @Test
    fun `addBank Fail`() {
        val result = mockk<Throwable>()
        coEvery { addBankAccountUseCase.get().addBankAccount(any(), any(), any()) }
                .coAnswers {
                    thirdArg<(Throwable) -> Unit>().invoke(result)
                }
        viewModel.addBank(mockk())
        assert(viewModel.addBankAccountLiveData.value is Fail)
    }

    @Test
    fun `checkAccountNumber success`() {
        val result = mockk<EditableAccountName>()
        coEvery { checkBankAccountUseCase.get().checkAccountNumber(any(), any(), any(), any()) }
                .coAnswers {
                    thirdArg<(CheckAccountNameState) -> Unit>().invoke(result)
                }
        viewModel.checkAccountNumber(120L, "")
        assert(viewModel.checkAccountDataLiveData.value is Success)
    }

    @Test
    fun `checkAccountNumber success with error`() {
        val result = mockk<AccountNameCheckError>()
        coEvery { checkBankAccountUseCase.get().checkAccountNumber(any(), any(), any(), any()) }
                .coAnswers {
                    thirdArg<(CheckAccountNameState) -> Unit>().invoke(result)
                }
        viewModel.checkAccountNumber(120L, "")
        assert(viewModel.checkAccountDataLiveData.value is Success)
    }

    @Test
    fun `checkAccountNumber success with validated account`() {
        val result = mockk<AccountNameFinalValidationSuccess>()
        coEvery { checkBankAccountUseCase.get().checkAccountNumber(any(), any(), any(), any()) }
                .coAnswers {
                    thirdArg<(CheckAccountNameState) -> Unit>().invoke(result)
                }
        viewModel.checkAccountNumber(120L, "")
        assert(viewModel.checkAccountDataLiveData.value is Success)
    }

    @Test
    fun `checkAccountNumber Fail`() {
        val result = mockk<Throwable>()
        coEvery { checkBankAccountUseCase.get().checkAccountNumber(any(), any(), any(), any()) }
                .coAnswers {
                    lastArg<(Throwable) -> Unit>().invoke(result)
                }
        viewModel.checkAccountNumber(120L, "")
        assert(viewModel.checkAccountDataLiveData.value is Fail)
    }

    @Test
    fun `validateEditedAccountInfo success`() {
        val result = mockk<EditableAccountName>()
        coEvery { checkBankAccountUseCase.get().validateAccountNumber(any(), any(), any(), any(), any())  }
                .coAnswers {
                    val fourthArg = arg<(CheckAccountNameState) -> Unit>(n = 3)
                    fourthArg.invoke(result)
                }
        viewModel.validateEditedAccountInfo(120L, "","")
        assert(viewModel.checkAccountDataLiveData.value is Success)
    }
    @Test
    fun `validateEditedAccountInfo success with error`() {
        val result = mockk<AccountNameCheckError>()
        coEvery { checkBankAccountUseCase.get().validateAccountNumber(any(), any(), any(), any(), any())  }
                .coAnswers {
                    val fourthArg = arg<(CheckAccountNameState) -> Unit>(n = 3)
                    fourthArg.invoke(result)
                }
        viewModel.validateEditedAccountInfo(120L, "","")
        assert(viewModel.checkAccountDataLiveData.value is Success)
    }

    @Test
    fun `validateEditedAccountInfo success with validated account`() {
        val result = mockk<AccountNameFinalValidationSuccess>()
        coEvery { checkBankAccountUseCase.get().validateAccountNumber(any(), any(), any(), any(), any())  }
                .coAnswers {
                    val fourthArg = arg<(CheckAccountNameState) -> Unit>(n = 3)
                    fourthArg.invoke(result)
                }
        viewModel.validateEditedAccountInfo(120L, "","")
        assert(viewModel.checkAccountDataLiveData.value is Success)
    }

    @Test
    fun `validateEditedAccountInfo Fail`() {
        val result = mockk<Throwable>()
        coEvery { checkBankAccountUseCase.get().validateAccountNumber(any(), any(), any(), any(), any()) }
                .coAnswers {
                    lastArg<(Throwable) -> Unit>().invoke(result)
                }
        viewModel.validateEditedAccountInfo(120L, "","")
        assert(viewModel.checkAccountDataLiveData.value is Fail)
    }

    @Test
    fun `loadTermsAndCondition Success`() {
        val result = mockk<TemplateData>()
        coEvery { termsAndConditionUseCase.get().getTermsAndCondition(any(), any()) }
                .coAnswers {
                    firstArg<(TemplateData) -> Unit>().invoke(result)
                }
        viewModel.loadTermsAndCondition()
        assert(viewModel.termsAndConditionLiveData.value is Success)
    }

    @Test
    fun `loadTermsAndCondition Fail`() {
        val result = mockk<Throwable>()
        coEvery { termsAndConditionUseCase.get().getTermsAndCondition(any(), any()) }
                .coAnswers {
                    secondArg<(Throwable) -> Unit>().invoke(result)
                }
        viewModel.loadTermsAndCondition()
        assert(viewModel.termsAndConditionLiveData.value is Fail)
    }

    @Test
    fun `validateAccountNumber Success`() {
        val result = mockk<ValidateAccountNumberSuccess>()
        every { validateAccountNumberUseCase.get().cancelJobs() } returns Unit
        coEvery { validateAccountNumberUseCase.get().onTextChanged(any(), any(), any()) }
                .coAnswers {
                    thirdArg<(ValidateAccountNumberState) -> Unit>().invoke(result)
                }
        viewModel.validateAccountNumber(mockk(), "")
        assert(viewModel.validateAccountNumberStateLiveData.value is ValidateAccountNumberSuccess)
    }

    @Test
    fun `validateAccountNumber Failed`() {
        val result = mockk<OnNOBankSelected>()
        every { validateAccountNumberUseCase.get().cancelJobs() } returns Unit
        coEvery { validateAccountNumberUseCase.get().onTextChanged(any(), any(), any()) }
                .coAnswers {
                    thirdArg<(ValidateAccountNumberState) -> Unit>().invoke(result)
                }
        viewModel.validateAccountNumber(null, "")
        assert(viewModel.validateAccountNumberStateLiveData.value is OnNOBankSelected)
    }

}