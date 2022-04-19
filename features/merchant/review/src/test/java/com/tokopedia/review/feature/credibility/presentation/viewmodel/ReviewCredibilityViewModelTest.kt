package com.tokopedia.review.feature.credibility.presentation.viewmodel

import com.tokopedia.review.feature.credibility.data.ReviewerCredibilityStatsResponse
import com.tokopedia.review.feature.credibility.data.ReviewerCredibilityStatsWrapper
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import org.junit.Assert
import org.junit.Test
import org.mockito.ArgumentMatchers

class ReviewCredibilityViewModelTest : ReviewCredibilityViewModelTestFixture() {

    @Test
    fun `when getReviewCredibility should execute getReviewerCredibilityUseCase and return expected results`() {
        val source = ArgumentMatchers.anyString()
        val userId = ArgumentMatchers.anyString()

        val expectedResponse = ReviewerCredibilityStatsResponse()

        onGetReviewCredibilitySuccess_thenReturn(expectedResponse)

        viewModel.getReviewCredibility(source, userId)

        verifyGetReviewCredibilityUseCaseExecuted()
        verifyReviewCredibilitySuccessEquals(Success(expectedResponse.response))
    }

    @Test
    fun `when getReviewCredibility should execute getReviewerCredibilityUseCase and return expected error`() {
        val source = ArgumentMatchers.anyString()
        val userId = ArgumentMatchers.anyString()
        val expectedResponse = Throwable()

        onGetReviewCredibilityFail_thenReturn(expectedResponse)

        viewModel.getReviewCredibility(source, userId)

        verifyGetReviewCredibilityUseCaseExecuted()
        verifyReviewCredibilityFailEquals(Fail(expectedResponse))
    }

    @Test
    fun `when isLoggedIn should return expected value`() {
        every {
            userSessionInterface.isLoggedIn
        } returns true

        Assert.assertTrue(viewModel.isLoggedIn())
    }

    @Test
    fun `when isUsersOwnCredibility should return expected value`() {
        val expectedUserId = "100101"

        every {
            userSessionInterface.userId
        } returns expectedUserId

        Assert.assertTrue(viewModel.isUsersOwnCredibility(expectedUserId))
    }

    @Test
    fun `when getUserId should return expected value`() {
        Assert.assertEquals("", viewModel.userId)
    }

    private fun onGetReviewCredibilitySuccess_thenReturn(expectedResponse: ReviewerCredibilityStatsResponse) {
        coEvery { getReviewerCredibilityUseCase.executeOnBackground() } returns expectedResponse
    }

    private fun onGetReviewCredibilityFail_thenReturn(throwable: Throwable) {
        coEvery { getReviewerCredibilityUseCase.executeOnBackground() } throws throwable
    }

    private fun verifyGetReviewCredibilityUseCaseExecuted() {
        coVerify { getReviewerCredibilityUseCase.executeOnBackground() }
    }

    private fun verifyReviewCredibilitySuccessEquals(expectedSuccessValue: Success<ReviewerCredibilityStatsWrapper>) {
        viewModel.reviewerCredibility.verifySuccessEquals(expectedSuccessValue)
    }

    private fun verifyReviewCredibilityFailEquals(expectedErrorValue: Fail) {
        viewModel.reviewerCredibility.verifyErrorEquals(expectedErrorValue)
    }
}