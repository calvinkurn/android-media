package com.tokopedia.review.feature.inbox.buyerreview.view.viewmodel

import com.tokopedia.review.feature.reviewreply.update.domain.model.ReviewReplyUpdateResponse
import com.tokopedia.review.feature.reviewreply.update.presenter.model.ReviewReplyUpdateUiModel
import com.tokopedia.review.feature.reviewreply.insert.domain.model.ReviewReplyInsertResponse
import com.tokopedia.review.feature.reviewreply.insert.presentation.model.ReviewReplyInsertUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyString

class InboxReputationDetailViewModelTest : InboxReputationDetailViewModelTestFixture() {
    @Test
    fun `when insert review reply should return success`() {
        runBlocking {
            onInsertReviewReply_thenReturn()
            viewModel.insertReviewReply(anyString(), anyString())

            verifySuccessInsertReviewReplyUseCalled()
            val expectedValue = Success(ReviewReplyInsertUiModel(anyBoolean()))
            assertTrue(viewModel.insertReviewReply.value is Success)
            viewModel.insertReviewReply.verifyValueEquals(expectedValue)
        }
    }

    @Test
    fun `when insert review reply return fail`() {
        runBlocking {
            val error = NullPointerException()
            onInsertReviewReply_thenError(error)

            viewModel.insertReviewReply(anyString(), anyString())
            val expectedResult = Fail(error)
            viewModel.insertReviewReply.verifyErrorEquals(expectedResult)
        }
    }

    @Test
    fun `when delete review reply response should return success`() {
        runBlocking {
            onUpdateReviewReplyResponse_thenReturn()
            viewModel.deleteReviewResponse(anyString())

            verifySuccessUpdateReviewReplyResponseUseCaseCalled()
            val expectedValue = Success(ReviewReplyUpdateUiModel(anyBoolean(), anyString()))
            assertTrue(viewModel.deleteReviewReply.value is Success)
            viewModel.deleteReviewReply.verifyValueEquals(expectedValue)
        }
    }

    @Test
    fun `when delete review reply response return fail`() {
        runBlocking {
            val error = NullPointerException()
            onUpdateReviewReplyResponse_thenError(error)

            viewModel.deleteReviewResponse(anyString())
            val expectedResult = Fail(error)
            viewModel.deleteReviewReply.verifyErrorEquals(expectedResult)
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

    private fun onUpdateReviewReplyResponse_thenReturn() {
        coEvery { reviewReplyUpdateUseCase.executeOnBackground() } returns ReviewReplyUpdateResponse.ProductrevUpdateSellerResponse()
    }

    private fun onUpdateReviewReplyResponse_thenError(error: NullPointerException) {
        coEvery { reviewReplyUpdateUseCase.executeOnBackground() } coAnswers { throw error }
    }

    private fun verifySuccessUpdateReviewReplyResponseUseCaseCalled() {
        coVerify { reviewReplyUpdateUseCase.executeOnBackground() }
    }
}