package com.tokopedia.withdraw.auto_withdrawal.presentation.viewModel


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.withdraw.auto_withdrawal.domain.usecase.*
import com.tokopedia.withdraw.saldowithdrawal.domain.model.GqlRekPreTncResponse
import com.tokopedia.withdraw.saldowithdrawal.domain.usecase.JoinRekeningTNCUseCase
import com.tokopedia.withdraw.saldowithdrawal.domain.usecase.JoinRekeningTNCUseCase.Companion.PARAM_PROGRAM_ID
import com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel.JoinRekeningTermsConditionViewModel
import com.tokopedia.withdraw.util.observeOnce
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class AutoWDSettingsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val autoWDStatusUseCase = mockk<AutoWDStatusUseCase>()
    private val autoWDInfoUseCase = mockk<AutoWDInfoUseCase>()
    private val bankAccountListUseCase = mockk<GQLBankAccountListUseCase>()
    private val autoWDTNCUseCase = mockk<AutoWDTNCUseCase>()
    private val autoWDUpsertUseCase = mockk<AutoWDUpsertUseCase>()


    @ExperimentalCoroutinesApi
    private val autoWithdrawalViewModel = spyk(AutoWDSettingsViewModel(
            autoWDStatusUseCase, autoWDInfoUseCase, bankAccountListUseCase, autoWDTNCUseCase,
            autoWDUpsertUseCase, TestCoroutineDispatcher()))

    @Test
    fun `check getAutoWDInfo fail`(){
        /*val result = mockk<Fail>()
        coEvery { autoWDInfoUseCase.getAutoWDInfo(any(), any()) } returns result
        autoWithdrawalViewModel.getAutoWDInfo()
        autoWithdrawalViewModel.infoAutoWDResultLiveData.observeOnce {
            assert(it is Fail)
        }*/
    }





}


/*
    val autoWDStatusDataResultLiveData = MutableLiveData<Result<AutoWDStatusData>>()
    val infoAutoWDResultLiveData = MutableLiveData<Result<GetInfoAutoWD>>()
    val bankListResultLiveData = MutableLiveData<Result<ArrayList<BankAccount>>>()
    val autoWDTNCResultLiveData = MutableLiveData<Result<String>>()
    val upsertResponseLiveData = SingleLiveEvent<Result<UpsertResponse>>()*/


/* @Test
    fun `submitWithdraw fail`() {
        val result = mockk<Fail>()
        val withdrawalRequest = mockk<WithdrawalRequest>()
        val validateToken = ""
        coEvery { useCase.submitWithdrawal(any(), any()) } returns result
        saldoWithdrawalViewModel.submitWithdraw(withdrawalRequest, validateToken)
        saldoWithdrawalViewModel.submitWithdrawalResponseLiveData.observeOnce {
            assert(it is Fail)
        }
    }

    @Test
    fun `submitWithdraw success`() {
        val result = mockk<Success<GQLSubmitWithdrawalResponse>>()
        val withdrawalRequest = mockk<WithdrawalRequest>()
        val validateToken = ""
        val submitWithdrawalResponse = mockk<SubmitWithdrawalResponse>()
        coEvery { useCase.submitWithdrawal(any(), any()) } returns result
        every { result.data.submitWithdrawalResponse} returns submitWithdrawalResponse
        every { submitWithdrawalResponse.status } returns "success"
        saldoWithdrawalViewModel.submitWithdraw(withdrawalRequest, validateToken)
        saldoWithdrawalViewModel.submitWithdrawalResponseLiveData.observeOnce {
            when (it) {
                is Success -> {
                    assertEquals(it.data, submitWithdrawalResponse)
                }
                else -> assert(false)
            }
        }
    }

    @Test
    fun `submitWithdraw fail with error message`() {
        val result = mockk<Success<GQLSubmitWithdrawalResponse>>()
        val withdrawalRequest = mockk<WithdrawalRequest>()
        val validateToken = ""
        val submitWithdrawalResponse = mockk<SubmitWithdrawalResponse>()
        coEvery { useCase.submitWithdrawal(any(), any()) } returns result
        every { result.data.submitWithdrawalResponse} returns submitWithdrawalResponse
        every { submitWithdrawalResponse.status } returns ""
        saldoWithdrawalViewModel.submitWithdraw(withdrawalRequest, validateToken)
        saldoWithdrawalViewModel.submitWithdrawalResponseLiveData.observeOnce {
            when (it) {
                is Success -> {
                    assertEquals(it.data, submitWithdrawalResponse)
                }
                is Fail -> assert(true)
            }
        }
    }*/