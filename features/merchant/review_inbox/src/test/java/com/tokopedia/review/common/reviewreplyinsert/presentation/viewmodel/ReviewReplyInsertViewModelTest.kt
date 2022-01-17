package com.tokopedia.review.common.reviewreplyinsert.presentation.viewmodel

import com.tokopedia.review.common.reviewreplyinsert.domain.model.ReviewReplyInsertResponse
import com.tokopedia.review.common.reviewreplyinsert.presentation.model.ReviewReplyInsertUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.ArgumentMatchers

class ReviewReplyInsertViewModelTest: ReviewReplyInsertViewModelTestFixture() {
    @Test
    fun `when insert review reply should return success`() {
        runBlocking {
            onInsertReviewReply_thenReturn()
            viewModel.insertReviewReply(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())

            verifySuccessInsertReviewReplyUseCalled()
            val expectedValue = Success(ReviewReplyInsertUiModel(ArgumentMatchers.anyBoolean()))
            TestCase.assertTrue(viewModel.insertReviewReply.value is Success)
            viewModel.insertReviewReply.verifyValueEquals(expectedValue)
        }
    }

    @Test
    fun `when insert review reply return fail`() {
        runBlocking {
            val error = NullPointerException()
            onInsertReviewReply_thenError(error)

            viewModel.insertReviewReply(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())
            val expectedResult = Fail(error)
            viewModel.insertReviewReply.verifyErrorEquals(expectedResult)
        }
    }

    private fun onInsertReviewReply_thenReturn() {
        coEvery { reviewReplyInsertUseCase.executeOnBackground() } returns ReviewReplyInsertResponse.ProductrevInsertSellerResponse()
    }

    private fun onInsertReviewReply_thenError(exception: NullPointerException) {
        coEvery { reviewReplyInsertUseCase.executeOnBackground() } coAnswers { throw exception }
    }

    private fun verifySuccessInsertReviewReplyUseCalled() {
        coVerify { reviewReplyInsertUseCase.executeOnBackground() }
    }
}