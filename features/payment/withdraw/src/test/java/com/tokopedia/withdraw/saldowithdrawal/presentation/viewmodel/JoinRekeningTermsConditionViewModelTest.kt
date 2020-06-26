package com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.withdraw.saldowithdrawal.domain.model.GqlRekPreTncResponse
import com.tokopedia.withdraw.saldowithdrawal.domain.model.RekPreTncResponse
import com.tokopedia.withdraw.saldowithdrawal.domain.model.TermTemplate
import com.tokopedia.withdraw.saldowithdrawal.domain.usecase.SaldoGQLUseCase
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule

class JoinRekeningTermsConditionViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val useCase = mockk<SaldoGQLUseCase<GqlRekPreTncResponse>>()


    @ExperimentalCoroutinesApi
    private val joinRekeningTermsConditionViewModel = spyk(JoinRekeningTermsConditionViewModel("", useCase, TestCoroutineDispatcher()))

    @Before
    fun setUp() {

    }

    @Test
    fun loadJoinRekeningTermsCondition() {
        val slot = slot<Map<String, Any?>>()
        val querySlot = slot<String>()

        every { useCase.setRequestParams(capture(slot)) } just runs
        every { useCase.setGraphqlQuery(capture(querySlot)) } just runs

        joinRekeningTermsConditionViewModel.loadJoinRekeningTermsCondition(1)

        assertEquals(slot.captured[JoinRekeningTermsConditionViewModel.PARAM_PROGRAM_ID], 1)
        assertEquals(querySlot.captured, "")
    }

    @Test
    fun `check live data`() {

        val gqlRekPreTncResponse = GqlRekPreTncResponse(RekPreTncResponse(TermTemplate("")))

        val result = Success(gqlRekPreTncResponse)
        every { useCase.setRequestParams(any()) } just runs
        every { useCase.setGraphqlQuery(any()) } just runs
        every { useCase.setTypeClass(any()) } just runs

        coEvery {
            useCase.executeUseCase()
        } returns result

        joinRekeningTermsConditionViewModel.loadJoinRekeningTermsCondition(1)

        assert(joinRekeningTermsConditionViewModel.tncResponseMutableData.value is Success)

    }
    @Test
    fun `check live data fail`() {

        val exception = Exception()

        val result = Fail(exception)
        every { useCase.setRequestParams(any()) } just runs
        every { useCase.setGraphqlQuery(any()) } just runs
        every { useCase.setTypeClass(any()) } just runs

        coEvery {
            useCase.executeUseCase()
        } returns result

        joinRekeningTermsConditionViewModel.loadJoinRekeningTermsCondition(1)

        assert(joinRekeningTermsConditionViewModel.tncResponseMutableData.value is Fail)

    }


}