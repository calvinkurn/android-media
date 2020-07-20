package com.tokopedia.reviewseller.inboxreview

import com.tokopedia.reviewseller.feature.inboxreview.domain.response.InboxReviewResponse
import io.mockk.coEvery
import io.mockk.coVerify

class InboxReviewViewModelTest: InboxReviewViewModelTestTestFixture() {

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