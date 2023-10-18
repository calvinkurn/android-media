package com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.withdraw.saldowithdrawal.domain.model.GQLSubmitWithdrawalResponse
import com.tokopedia.withdraw.saldowithdrawal.domain.model.SubmitWithdrawalResponse
import com.tokopedia.withdraw.saldowithdrawal.domain.model.WithdrawalRequest
import com.tokopedia.withdraw.saldowithdrawal.domain.usecase.GQLSubmitWithdrawalUseCase
import com.tokopedia.withdraw.util.observeOnce
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SubmitWithdrawalViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val useCase = mockk<GQLSubmitWithdrawalUseCase>()

    @ExperimentalCoroutinesApi
    private val saldoWithdrawalViewModel = spyk(SubmitWithdrawalViewModel(
            useCase,
            TestCoroutineDispatcher()))


    @Test
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
        every { submitWithdrawalResponse.shouldRedirectToThankYouPage() } returns true
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
    }
}
