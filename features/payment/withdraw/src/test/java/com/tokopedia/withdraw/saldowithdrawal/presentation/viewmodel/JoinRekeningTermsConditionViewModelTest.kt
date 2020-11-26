package com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.withdraw.saldowithdrawal.domain.model.GqlRekPreTncResponse
import com.tokopedia.withdraw.saldowithdrawal.domain.usecase.JoinRekeningTNCUseCase
import com.tokopedia.withdraw.saldowithdrawal.domain.usecase.JoinRekeningTNCUseCase.Companion.PARAM_PROGRAM_ID
import com.tokopedia.withdraw.util.observeOnce
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class JoinRekeningTermsConditionViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val useCase = mockk<JoinRekeningTNCUseCase>()

    @ExperimentalCoroutinesApi
    private val joinRekeningTermsConditionViewModel = spyk(JoinRekeningTermsConditionViewModel(
            useCase, TestCoroutineDispatcher()))


    @Test
    fun `check live data`() {
        val gqlRekPreTncResponse = mockk<Success<GqlRekPreTncResponse>>()
        val template = ""
        coEvery {
            useCase.loadJoinRekeningTermsCondition(any())
        } returns gqlRekPreTncResponse

        every {
            gqlRekPreTncResponse.data
                    .rekPreTncResponse.rekPreTncResponseData.template
        } returns template

        joinRekeningTermsConditionViewModel.loadJoinRekeningTermsCondition(1)
        joinRekeningTermsConditionViewModel.tncResponseMutableData.observeOnce {
            assert(it is Success)
            when (it) {
                is Success -> assertEquals(it.data, template)
                else -> assert(false)
            }
        }
    }


    @Test
    fun `check live data fail`() {

        val exception = Exception()
        val result = mockk<Fail>()
        every { result.throwable } returns exception

        coEvery {
            useCase.loadJoinRekeningTermsCondition(any())
        } returns result

        joinRekeningTermsConditionViewModel.loadJoinRekeningTermsCondition(-1)
        joinRekeningTermsConditionViewModel.tncResponseMutableData.observeOnce {
            assert(it is Fail)
        }

    }

}