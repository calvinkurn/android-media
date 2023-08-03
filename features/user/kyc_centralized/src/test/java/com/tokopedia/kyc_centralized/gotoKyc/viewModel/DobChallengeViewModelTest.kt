package com.tokopedia.kyc_centralized.gotoKyc.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.GetChallengeResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.GetChallengeUseCase
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.SubmitChallengeResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.SubmitChallengeUseCase
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.challenge.DobChallengeViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import util.getOrAwaitValue
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class DobChallengeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var viewModel: DobChallengeViewModel

    private val getChallengeUseCase = mockk<GetChallengeUseCase>(relaxed = true)
    private val submitChallengeUseCase = mockk<SubmitChallengeUseCase>(relaxed = true)

    @Before
    fun setup() {
        viewModel = DobChallengeViewModel(getChallengeUseCase, submitChallengeUseCase, dispatcher)
    }

    @Test
    fun `when get challenge then return success`() {
        val questionId = "AsDew-SdJdw5F"
        val challengeId = "VBRew-SdJdfDf"
        val expected = GetChallengeResult.Success(questionId)

        coEvery { getChallengeUseCase(any()) } returns expected
        viewModel.getChallenge(challengeId)

        val result = viewModel.getChallenge.getOrAwaitValue()
        val resultQuestionId = viewModel.questionId
        assertTrue(result is GetChallengeResult.Success)
        assertEquals(questionId, result.questionId)
        assertEquals(questionId, resultQuestionId)
    }

    @Test
    fun `when get challenge then return failed from BE`() {
        val challengeId = "VBRew-SdJdfDf"
        val throwable = Throwable()
        val expected = GetChallengeResult.Failed(throwable)

        coEvery { getChallengeUseCase(any()) } returns expected
        viewModel.getChallenge(challengeId)

        val result = viewModel.getChallenge.getOrAwaitValue()
        val resultQuestionId = viewModel.questionId
        assertTrue(result is GetChallengeResult.Failed)
        assertEquals(throwable, result.throwable)
        assertNotEquals(challengeId, resultQuestionId)
    }

    @Test
    fun `when get challenge then return failed`() {
        val challengeId = "VBRew-SdJdfDf"
        val throwable = Throwable()

        coEvery { getChallengeUseCase(any()) } throws throwable
        viewModel.getChallenge(challengeId)

        val result = viewModel.getChallenge.getOrAwaitValue()
        assertTrue(result is GetChallengeResult.Failed)
        assertEquals(throwable, result.throwable)
    }

    @Test
    fun `when submit challenge then return success`() {
        val questionId = "AsDew-SdJdw5F"
        val challengeId = "VBRew-SdJdfDf"
        val selectedDate = "1999-01-01"
        val expected = SubmitChallengeResult.Success

        coEvery { submitChallengeUseCase(any()) } returns expected
        viewModel.submitChallenge(challengeId, questionId, selectedDate)

        val result = viewModel.submitChallenge.getOrAwaitValue()
        assertTrue(result is SubmitChallengeResult.Success)
    }

    @Test
    fun `when submit challenge then return wrong answers`() {
        val questionId = "AsDew-SdJdw5F"
        val challengeId = "VBRew-SdJdfDf"
        val selectedDate = "1999-01-01"
        val attemptsRemaining = "2"
        val expected = SubmitChallengeResult.WrongAnswer(attemptsRemaining)

        coEvery { submitChallengeUseCase(any()) } returns expected
        viewModel.submitChallenge(challengeId, questionId, selectedDate)

        val result = viewModel.submitChallenge.getOrAwaitValue()
        assertTrue(result is SubmitChallengeResult.WrongAnswer)
        assertEquals(attemptsRemaining, result.attemptsRemaining)
    }

    @Test
    fun `when submit challenge then return exhausted`() {
        val questionId = "AsDew-SdJdw5F"
        val challengeId = "VBRew-SdJdfDf"
        val selectedDate = "1999-01-01"
        val cooldownTimeInSeconds = "200000"
        val maximumAttemptsAllowed = "3"
        val expected = SubmitChallengeResult.Exhausted(cooldownTimeInSeconds, maximumAttemptsAllowed)

        coEvery { submitChallengeUseCase(any()) } returns expected
        viewModel.submitChallenge(challengeId, questionId, selectedDate)

        val result = viewModel.submitChallenge.getOrAwaitValue()
        assertTrue(result is SubmitChallengeResult.Exhausted)
        assertEquals(cooldownTimeInSeconds, result.cooldownTimeInSeconds)
        assertEquals(maximumAttemptsAllowed, result.maximumAttemptsAllowed)
    }

    @Test
    fun `when submit challenge then return failed`() {
        val questionId = "AsDew-SdJdw5F"
        val challengeId = "VBRew-SdJdfDf"
        val selectedDate = "1999-01-01"
        val throwable = Throwable()

        coEvery { submitChallengeUseCase(any()) } throws throwable
        viewModel.submitChallenge(challengeId, questionId, selectedDate)

        val result = viewModel.submitChallenge.getOrAwaitValue()
        assertTrue(result is SubmitChallengeResult.Failed)
        assertEquals(throwable, result.throwable)
    }

}
