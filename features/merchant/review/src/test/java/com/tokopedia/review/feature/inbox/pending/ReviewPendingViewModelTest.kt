package com.tokopedia.review.feature.inbox.pending

import android.accounts.NetworkErrorException
import com.tokopedia.review.common.data.Fail
import com.tokopedia.review.common.data.Success
import com.tokopedia.review.feature.inbox.common.ReviewInboxConstants
import com.tokopedia.review.feature.inbox.pending.data.ProductrevWaitForFeedbackResponse
import com.tokopedia.review.feature.inbox.pending.data.ProductrevWaitForFeedbackResponseWrapper
import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain
import com.tokopedia.review.utils.verifyCoroutineFailEquals
import com.tokopedia.review.utils.verifyCoroutineSuccessEquals
import com.tokopedia.review.utils.verifyErrorEquals
import com.tokopedia.review.utils.verifySuccessEquals
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.mockito.ArgumentMatchers.anyLong
import com.tokopedia.usecase.coroutines.Fail as CoroutineFail
import com.tokopedia.usecase.coroutines.Success as CoroutineSuccess

class ReviewPendingViewModelTest : ReviewPendingViewModelTestFixture() {

    @Test
    fun `when getReview should execute expected use case, update UI accordingly and get expected data`() {
        val page = ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE
        val response = ProductrevWaitForFeedbackResponseWrapper()

        onGetReview_thenReturn(response)

        viewModel.getReviewData(page)

        val expectedResponse = Success(response.productrevWaitForFeedbackWaitForFeedback, page)

        verifyGetReviewUseCaseExecuted()
        verifyReviewListEquals(expectedResponse)
    }

    @Test
    fun `when getReview fails first page should execute expected use case, update UI accordingly and fail with expected exception`() {
        val page = ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE
        val exception = NetworkErrorException()

        onGetReviewFail_thenReturn(exception)

        viewModel.getReviewData(page, isRefresh = true)

        verifyGetReviewUseCaseExecuted()
        verifyReviewListErrorEquals(Fail(exception, page))
    }

    @Test
    fun `when getReview fails next page should execute expected use case, update UI accordingly and fail with expected exception`() {
        val page = ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE + 1
        val exception = NetworkErrorException()

        onGetReviewFail_thenReturn(exception)

        viewModel.getReviewData(page)

        verifyGetReviewUseCaseExecuted()
        verifyReviewListErrorEquals(Fail(exception, page))
    }

    @Test
    fun `when getProductIncentiveOvo should execute expected use case, and get expected data`() {
        val response = ProductRevIncentiveOvoDomain()

        onGetOvoIncentive_thenReturn(response)

        runBlocking {
            viewModel.getProductIncentiveOvo()
            viewModel.coroutineContext[Job]?.children?.forEach { it.join() }
        }

        val expectedResponse = CoroutineSuccess(response)

        verifyGetOvoIncentiveUseCaseExecuted()
        viewModel.incentiveOvo.verifyCoroutineSuccessEquals(expectedResponse)
    }

    @Test
    fun `when getProductIncentiveOvo fails should execute expected use case, and fail with expected exception`() {
        val exception = NetworkErrorException()

        onGetOvoIncentiveFail_thenReturn(exception)

        runBlocking {
            viewModel.getProductIncentiveOvo()
            viewModel.coroutineContext[Job]?.children?.forEach { it.join() }
        }

        val expectedError = CoroutineFail(exception)

        verifyGetOvoIncentiveUseCaseExecuted()
        viewModel.incentiveOvo.verifyCoroutineFailEquals(expectedError)
    }

    @Test
    fun `when getUserId should return valid userId`() {
        val actualUserId = viewModel.getUserId()
        Assert.assertTrue(actualUserId.isEmpty())
    }

    @Test
    fun `when getUserName should return valid userName`() {
        val actualUserId = viewModel.getUserName()
        Assert.assertTrue(actualUserId.isEmpty())
    }

    @Test
    fun `when markAsSeen should execute expected usecase`() {
        val inboxReviewId = anyLong()

        viewModel.markAsSeen(inboxReviewId)

        verifyMarkAsSeenUseCaseExecuted()
    }

    private fun verifyMarkAsSeenUseCaseExecuted() {
        coVerify { markAsSeenUseCase.executeOnBackground() }
    }

    private fun verifyReviewListEquals(response: Success<ProductrevWaitForFeedbackResponse>) {
        viewModel.reviewList.verifySuccessEquals(response)
    }

    private fun verifyReviewListErrorEquals(error: Fail<Any>) {
        viewModel.reviewList.verifyErrorEquals(error)
    }

    private fun verifyGetReviewUseCaseExecuted() {
        coVerify { productrevWaitForFeedbackUseCase.executeOnBackground() }
    }

    private fun verifyGetOvoIncentiveUseCaseExecuted() {
        coVerify { getProductIncentiveOvo.getIncentiveOvo() }
    }

    private fun onGetOvoIncentive_thenReturn(response: ProductRevIncentiveOvoDomain) {
        coEvery { getProductIncentiveOvo.getIncentiveOvo() } returns response
    }

    private fun onGetOvoIncentiveFail_thenReturn(exception: Exception) {
        coEvery { getProductIncentiveOvo.getIncentiveOvo() } throws exception
    }

    private fun onGetReview_thenReturn(response: ProductrevWaitForFeedbackResponseWrapper) {
        coEvery { productrevWaitForFeedbackUseCase.executeOnBackground() } returns response
    }

    private fun onGetReviewFail_thenReturn(exception: Exception) {
        coEvery { productrevWaitForFeedbackUseCase.executeOnBackground() } throws exception
    }
}