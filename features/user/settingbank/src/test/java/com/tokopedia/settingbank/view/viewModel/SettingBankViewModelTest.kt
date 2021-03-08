package com.tokopedia.settingbank.view.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.settingbank.domain.model.Bank
import com.tokopedia.settingbank.domain.model.KYCInfo
import com.tokopedia.settingbank.domain.model.TemplateData
import com.tokopedia.settingbank.domain.usecase.DeleteBankAccountUseCase
import com.tokopedia.settingbank.domain.usecase.GetUserBankAccountUseCase
import com.tokopedia.settingbank.domain.usecase.KyCInfoUseCase
import com.tokopedia.settingbank.domain.usecase.TermsAndConditionUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SettingBankViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()


    private val termsAndConditionUseCase: dagger.Lazy<TermsAndConditionUseCase> = mockk()
    private val getUserBankAccountUseCase: dagger.Lazy<GetUserBankAccountUseCase> = mockk()
    private val deleteBankAccountUseCase: dagger.Lazy<DeleteBankAccountUseCase> = mockk()
    private val kyCInfoUseCase: dagger.Lazy<KyCInfoUseCase> = mockk()

    private lateinit var viewModel: SettingBankViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        viewModel = spyk(SettingBankViewModel(termsAndConditionUseCase, getUserBankAccountUseCase,
                deleteBankAccountUseCase, kyCInfoUseCase,
                TestCoroutineDispatcher()))
    }

    @Test
    fun `loadUserAddedBankList Success button state disabled`() {
        val bankListResult = mockk<ArrayList<Bank>>()
        val buttonState = false
        coEvery { getUserBankAccountUseCase.get().getUserBankAccountList(any(), any()) }
                .coAnswers {
                    firstArg<(ArrayList<Bank>, Boolean) -> Unit>()
                            .invoke(bankListResult, buttonState)
                }
        viewModel.loadUserAddedBankList()
        assert(viewModel.bankAccountListLiveData.value is Success)
        assert(viewModel.addBankAccountStateLiveData.value == false)
    }

    @Test
    fun `loadUserAddedBankList Success button state enable`() {
        val bankListResult = mockk<ArrayList<Bank>>()
        val buttonState = true
        coEvery { getUserBankAccountUseCase.get().getUserBankAccountList(any(), any()) }
                .coAnswers {
                    firstArg<(ArrayList<Bank>, Boolean) -> Unit>()
                            .invoke(bankListResult, buttonState)
                }
        viewModel.loadUserAddedBankList()
        assert(viewModel.bankAccountListLiveData.value is Success)
        assert(viewModel.addBankAccountStateLiveData.value == true)
    }

    @Test
    fun `loadUserAddedBankList Failed`() {
        val failedResult = mockk<Throwable>()
        coEvery { getUserBankAccountUseCase.get().getUserBankAccountList(any(), any()) }
                .coAnswers {
                    secondArg<(Throwable) -> Unit>()
                            .invoke(failedResult)
                }
        viewModel.loadUserAddedBankList()
        assert(viewModel.bankAccountListLiveData.value is Fail)
        assert(viewModel.addBankAccountStateLiveData.value == null)
    }


    @Test
    fun `deleteBankAccount Failed`() {
        val failedResult = mockk<Throwable>()
        coEvery { deleteBankAccountUseCase.get().deleteBankAccount(any(), any(), any()) }
                .coAnswers {
                    thirdArg<(Throwable) -> Unit>()
                            .invoke(failedResult)
                }
        viewModel.deleteBankAccount(mockk())
        assert(viewModel.deleteBankAccountLiveData.value is Fail)
    }

    @Test
    fun `deleteBankAccount Success`() {
        val success = "Success"
        coEvery { deleteBankAccountUseCase.get().deleteBankAccount(any(), any(), any()) }
                .coAnswers {
                    secondArg<(String) -> Unit>()
                            .invoke(success)
                }
        viewModel.deleteBankAccount(mockk())
        assert(viewModel.deleteBankAccountLiveData.value is Success)
    }

    @Test
    fun `loadTermsAndCondition Failed`() {
        val failedResult = mockk<Throwable>()
        coEvery { termsAndConditionUseCase.get().getTermsAndCondition(any(), any()) }
                .coAnswers {
                    secondArg<(Throwable) -> Unit>()
                            .invoke(failedResult)
                }
        viewModel.loadTermsAndCondition()
        assert(viewModel.termsAndConditionLiveData.value is Fail)
    }

    @Test
    fun `loadTermsAndCondition Success`() {
        val success = mockk<TemplateData>()
        coEvery { termsAndConditionUseCase.get().getTermsAndCondition(any(), any()) }
                .coAnswers {
                    firstArg<(TemplateData) -> Unit>()
                            .invoke(success)
                }
        viewModel.loadTermsAndCondition()
        assert(viewModel.termsAndConditionLiveData.value is Success)
    }

    @Test
    fun `loadTermsAndConditionNotes Failed`() {
        val failedResult = mockk<Throwable>()
        coEvery { termsAndConditionUseCase.get().getNotes(any(), any()) }
                .coAnswers {
                    secondArg<(Throwable) -> Unit>()
                            .invoke(failedResult)
                }
        viewModel.loadTermsAndConditionNotes()
        assert(viewModel.tncNotesLiveData.value is Fail)
    }

    @Test
    fun `loadTermsAndConditionNotes Success`() {
        val success = mockk<TemplateData>()
        coEvery { termsAndConditionUseCase.get().getNotes(any(), any()) }
                .coAnswers {
                    firstArg<(TemplateData) -> Unit>()
                            .invoke(success)
                }
        viewModel.loadTermsAndConditionNotes()
        assert(viewModel.tncNotesLiveData.value is Success)
    }


    @Test
    fun `getKYCInfo Success`() {
        val success = mockk<KYCInfo>()
        coEvery { kyCInfoUseCase.get().getKYCCheckInfo(any(), any()) }
                .coAnswers {
                    firstArg<(KYCInfo) -> Unit>()
                            .invoke(success)
                }
        viewModel.getKYCInfo()
        assert(viewModel.kycInfoLiveData.value is Success)
    }

    @Test
    fun `getKYCInfo Failed`() {
        val failedResult = mockk<Throwable>()
        coEvery { kyCInfoUseCase.get().getKYCCheckInfo(any(), any()) }
                .coAnswers {
                    secondArg<(Throwable) -> Unit>()
                            .invoke(failedResult)
                }
        viewModel.getKYCInfo()
        assert(viewModel.kycInfoLiveData.value is Fail)
    }

}
