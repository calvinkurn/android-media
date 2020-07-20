package com.tokopedia.reviewseller.inboxreview

import com.tokopedia.reviewseller.feature.inboxreview.domain.response.InboxReviewResponse
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.SortFilterInboxItemWrapper
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class InboxReviewViewModelTest: InboxReviewViewModelTestTestFixture() {

    @Test
    fun `when get inbox review without filter should return success`() {
        runBlocking {
            onGetInboxReview_thenReturn()

            viewModel.getInboxReview( 1)

            verifySuccessGetInboxReviewUseCaseCalled()
            Assert.assertTrue(viewModel.inboxReview.value is Success)
            Assert.assertNotNull(viewModel.inboxReview.value)
        }
    }

    @Test
    fun `when get inbox review answered filter should return success`() {
        runBlocking {
            onGetInboxReview_thenReturn()
            val answeredFilter = SortFilterInboxItemWrapper(isSelected = true, sortValue = "answered")
            viewModel.updateStatusFilterData(arrayListOf(answeredFilter))
            viewModel.setFilterStatusDataText(arrayListOf(answeredFilter))

            verifySuccessGetInboxReviewUseCaseCalled()
            Assert.assertTrue(viewModel.inboxReview.value is Success)
            Assert.assertNotNull(viewModel.inboxReview.value)
            Assert.assertNotNull(viewModel.getStatusFilterListUpdated())
        }
    }

    private fun onGetInboxReview_thenReturn() {
        coEvery { getInboxReviewUseCase.executeOnBackground() } returns InboxReviewResponse.ProductGetInboxReviewByShop()
    }

    private fun verifySuccessGetInboxReviewUseCaseCalled() {
        coVerify { getInboxReviewUseCase.executeOnBackground() }
    }

    private fun onGetInboxReview_thenError(exception: NullPointerException) {
        coEvery { getInboxReviewUseCase.executeOnBackground() } coAnswers { throw exception }
    }

}