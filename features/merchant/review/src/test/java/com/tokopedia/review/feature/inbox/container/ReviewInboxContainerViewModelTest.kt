package com.tokopedia.review.feature.inbox.container

import android.accounts.NetworkErrorException
import com.tokopedia.reputation.common.data.source.cloud.model.ProductrevReviewTabCount
import com.tokopedia.reputation.common.data.source.cloud.model.ProductrevReviewTabCounterResponseWrapper
import com.tokopedia.review.feature.inbox.container.data.ReviewInboxTabs
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Assert
import org.junit.Test

class ReviewInboxContainerViewModelTest : ReviewInboxContainerViewModelTestFixture() {

    @Test
    fun `when getTabCounter should execute expected use case, update values accordingly`() {
        val count = 10
        val response = ProductrevReviewTabCounterResponseWrapper(ProductrevReviewTabCount(count = count))

        onGetTabCounter_thenReturn(response)

        viewModel.getTabCounter()

        val expectedResponse = listOf(ReviewInboxTabs.ReviewInboxPending(count.toString()), ReviewInboxTabs.ReviewInboxHistory, ReviewInboxTabs.ReviewInboxSeller )

        verifyGetTabCounterUseCaseExecuted()
        verifyTabCountersEquals(expectedResponse)
    }

    @Test
    fun `when getTabCounter fails should still execute expected use case, update UI with default values`() {
        val exception = NetworkErrorException()

        onGetTabCounterFail_thenReturn(exception)

        viewModel.getTabCounter()

        val expectedError = listOf(ReviewInboxTabs.ReviewInboxPending(), ReviewInboxTabs.ReviewInboxHistory, ReviewInboxTabs.ReviewInboxSeller)

        verifyGetTabCounterUseCaseExecuted()
        verifyTabCountersEquals(expectedError)
    }

    @Test
    fun `when getUserId should return valid userId`() {
        val actualUserId = viewModel.getUserId()
        Assert.assertTrue(actualUserId.isEmpty())
    }

    private fun verifyGetTabCounterUseCaseExecuted() {
        coVerify { productrevReviewTabCounterUseCase.executeOnBackground() }
    }

    private fun onGetTabCounter_thenReturn(response: ProductrevReviewTabCounterResponseWrapper) {
        coEvery { productrevReviewTabCounterUseCase.executeOnBackground() } returns response
    }

    private fun onGetTabCounterFail_thenReturn(throwable: Throwable) {
        coEvery { productrevReviewTabCounterUseCase.executeOnBackground()} throws throwable
    }

    private fun verifyTabCountersEquals(tabs: List<ReviewInboxTabs>) {
        viewModel.reviewTabs.value?.forEachIndexed { index, reviewInboxTabs ->
            when(reviewInboxTabs) {
                is ReviewInboxTabs.ReviewInboxPending -> {
                    Assert.assertEquals(reviewInboxTabs.counter, (tabs[index] as ReviewInboxTabs.ReviewInboxPending).counter)
                }
                is ReviewInboxTabs.ReviewInboxHistory -> {
                    Assert.assertTrue(tabs[index] is ReviewInboxTabs.ReviewInboxHistory)
                }
                is ReviewInboxTabs.ReviewInboxSeller -> {
                    Assert.assertTrue(tabs[index] is ReviewInboxTabs.ReviewInboxSeller)
                }
            }
        }
    }
}