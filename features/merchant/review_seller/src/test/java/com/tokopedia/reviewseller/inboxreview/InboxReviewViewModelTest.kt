package com.tokopedia.reviewseller.inboxreview

import com.tokopedia.reviewseller.common.util.ReviewSellerConstant.ANSWERED_VALUE
import com.tokopedia.reviewseller.common.util.ReviewSellerConstant.UNANSWERED_VALUE
import com.tokopedia.reviewseller.feature.inboxreview.domain.response.InboxReviewResponse
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.ListItemRatingWrapper
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.SortFilterInboxItemWrapper
import com.tokopedia.usecase.coroutines.Fail
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

            viewModel.feedbackInboxReviewMediator.observe( {lifecycle}) {}

            viewModel.updateStatusFilterData(arrayListOf(answeredFilter))
            viewModel.setFilterStatusDataText(arrayListOf(answeredFilter))

            verifySuccessGetInboxReviewUseCaseCalled()
            Assert.assertTrue(viewModel.inboxReview.value is Success)
            Assert.assertNotNull(viewModel.inboxReview.value)
            Assert.assertNotNull(viewModel.getStatusFilterListUpdated())
        }
    }

    @Test
    fun `when get inbox review unanswered filter should return success`() {
        runBlocking {
            onGetInboxReview_thenReturn()
            val answeredFilter = SortFilterInboxItemWrapper(isSelected = true, sortValue = "answered")

            viewModel.feedbackInboxReviewMediator.observe( {lifecycle}) {}

            viewModel.updateStatusFilterData(arrayListOf(answeredFilter))
            viewModel.setFilterStatusDataText(arrayListOf(answeredFilter))

            verifySuccessGetInboxReviewUseCaseCalled()
            Assert.assertTrue(viewModel.inboxReview.value is Success)
            Assert.assertNotNull(viewModel.inboxReview.value)
            Assert.assertNotNull(viewModel.getStatusFilterListUpdated())
        }
    }

    @Test
    fun `when get inbox review all status filter should return success`() {
        runBlocking {
            onGetInboxReview_thenReturn()
            val statusFilter = ArrayList<SortFilterInboxItemWrapper>().apply {
                add(SortFilterInboxItemWrapper(isSelected = true, sortValue = "answered"))
            }

            viewModel.feedbackInboxReviewMediator.observe( {lifecycle}) {}

            viewModel.updateStatusFilterData(statusFilter)
            viewModel.setFilterStatusDataText(statusFilter)

            verifySuccessGetInboxReviewUseCaseCalled()
            Assert.assertTrue(viewModel.inboxReview.value is Success)
            Assert.assertNotNull(viewModel.inboxReview.value)
            Assert.assertNotNull(viewModel.getStatusFilterListUpdated())
        }
    }

    @Test
    fun `when get inbox review all filter should return success`() {
        runBlocking {
            onGetInboxReview_thenReturn()
            val statusFilter = ArrayList<SortFilterInboxItemWrapper>().apply {
                add(SortFilterInboxItemWrapper(isSelected = true, sortValue = "answered"))
                add(SortFilterInboxItemWrapper(isSelected = true, sortValue = "unanswered"))
            }

            val ratingFilter = ArrayList<ListItemRatingWrapper>().apply {
                add(ListItemRatingWrapper(isSelected = true, sortValue = "1"))
                add(ListItemRatingWrapper(isSelected = true, sortValue = "2"))
                add(ListItemRatingWrapper(isSelected = true, sortValue = "3"))
                add(ListItemRatingWrapper(isSelected = true, sortValue = "4"))
                add(ListItemRatingWrapper(isSelected = true, sortValue = "5"))
            }

            viewModel.feedbackInboxReviewMediator.observe( {lifecycle}) {}

            viewModel.updateRatingFilterData(ratingFilter)
            viewModel.updateStatusFilterData(statusFilter)
            viewModel.setFilterRatingDataText(ratingFilter)
            viewModel.setFilterStatusDataText(statusFilter)

            verifySuccessGetInboxReviewUseCaseCalled()
            Assert.assertTrue(viewModel.inboxReview.value is Success)
            Assert.assertNotNull(viewModel.inboxReview.value)
            Assert.assertNotNull(viewModel.getStatusFilterListUpdated())
            Assert.assertNotNull(viewModel.getRatingFilterListUpdated())
        }
    }

    @Test
    fun `when get inbox reset all filter should return success`() {
        runBlocking {
            onGetInboxReview_thenReturn()

            viewModel.resetAllFilter()

            verifySuccessGetInboxReviewUseCaseCalled()
            Assert.assertTrue(viewModel.inboxReview.value is Success)
            Assert.assertNotNull(viewModel.inboxReview.value)
            Assert.assertTrue(viewModel.getStatusFilterListUpdated().isEmpty())
            Assert.assertTrue(viewModel.getRatingFilterListUpdated().isEmpty())
        }
    }


    @Test
    fun `when get inbox review lazy load answered filter should return success`() {
        runBlocking {
            onGetInboxReview_thenReturn()
            val answeredFilter = SortFilterInboxItemWrapper(isSelected = true, sortValue = "answered")

            viewModel.feedbackInboxReviewMediator.observe( {lifecycle}) {}

            viewModel.updateStatusFilterData(arrayListOf(answeredFilter))
            viewModel.setFilterStatusDataText(arrayListOf(answeredFilter))

            viewModel.getFeedbackInboxReviewListNext(2)

            verifySuccessGetInboxReviewUseCaseCalled()
            Assert.assertTrue(viewModel.feedbackInboxReview.value is Success)
            Assert.assertNotNull(viewModel.feedbackInboxReview.value)
            Assert.assertNotNull(viewModel.getStatusFilterListUpdated())
        }
    }

    @Test
    fun `when get inbox review lazy load unanswered filter should return success`() {
        runBlocking {
            onGetInboxReview_thenReturn()
            val answeredFilter = SortFilterInboxItemWrapper(isSelected = true, sortValue = "unanswered")

            viewModel.feedbackInboxReviewMediator.observe( {lifecycle}) {}

            viewModel.updateStatusFilterData(arrayListOf(answeredFilter))
            viewModel.setFilterStatusDataText(arrayListOf(answeredFilter))

            viewModel.getFeedbackInboxReviewListNext(2)

            verifySuccessGetInboxReviewUseCaseCalled()
            Assert.assertTrue(viewModel.feedbackInboxReview.value is Success)
            Assert.assertNotNull(viewModel.feedbackInboxReview.value)
            Assert.assertNotNull(viewModel.getStatusFilterListUpdated())
        }
    }


    @Test
    fun `when get inbox review lazy load all rating filter should return success`() {
        runBlocking {
            onGetInboxReview_thenReturn()

            val ratingFilter = ArrayList<ListItemRatingWrapper>().apply {
                add(ListItemRatingWrapper(isSelected = true, sortValue = "1"))
                add(ListItemRatingWrapper(isSelected = true, sortValue = "2"))
                add(ListItemRatingWrapper(isSelected = true, sortValue = "3"))
                add(ListItemRatingWrapper(isSelected = true, sortValue = "4"))
                add(ListItemRatingWrapper(isSelected = true, sortValue = "5"))
            }
            viewModel.feedbackInboxReviewMediator.observe( {lifecycle}) {}

            viewModel.updateRatingFilterData(ratingFilter)
            viewModel.setFilterRatingDataText(ratingFilter)

            viewModel.getFeedbackInboxReviewListNext(2)

            verifySuccessGetInboxReviewUseCaseCalled()
            Assert.assertTrue(viewModel.feedbackInboxReview.value is Success)
            Assert.assertNotNull(viewModel.feedbackInboxReview.value)
            Assert.assertNotNull(viewModel.getRatingFilterListUpdated())
        }
    }

    @Test
    fun `when get inbox review lazy load all filter should return success`() {
        runBlocking {
            onGetInboxReview_thenReturn()
            val statusFilter = ArrayList<SortFilterInboxItemWrapper>().apply {
                add(SortFilterInboxItemWrapper(isSelected = true, sortValue = "unanswered"))
            }

            val ratingFilter = ArrayList<ListItemRatingWrapper>().apply {
                add(ListItemRatingWrapper(isSelected = true, sortValue = "1"))
                add(ListItemRatingWrapper(isSelected = true, sortValue = "2"))
                add(ListItemRatingWrapper(isSelected = true, sortValue = "3"))
                add(ListItemRatingWrapper(isSelected = true, sortValue = "4"))
                add(ListItemRatingWrapper(isSelected = true, sortValue = "5"))
            }

            viewModel.apply {
                feedbackInboxReviewMediator.observe( {lifecycle}) {}
                updateRatingFilterData(ratingFilter)
                updateStatusFilterData(statusFilter)
                setFilterRatingDataText(ratingFilter)
                setFilterStatusDataText(statusFilter)
            }

            viewModel.getFeedbackInboxReviewListNext(3)

            verifySuccessGetInboxReviewUseCaseCalled()
            Assert.assertTrue(viewModel.feedbackInboxReview.value is Success)
            Assert.assertNotNull(viewModel.feedbackInboxReview.value)
            Assert.assertNotNull(viewModel.getRatingFilterListUpdated())
            Assert.assertNotNull(viewModel.getStatusFilterListUpdated())
        }
    }

    @Test
    fun `when get inbox review without filter should return fail`() {
        runBlocking {
            val error = NullPointerException()
            onGetInboxReview_thenError(error)

            viewModel.getInboxReview()

            val expectedResult = Fail(error)
            viewModel.inboxReview.verifyErrorEquals(expectedResult)
        }
    }

    @Test
    fun `when get inbox review lazy load without filter should return fail`() {
        runBlocking {
            val error = NullPointerException()
            onGetInboxReview_thenError(error)

            viewModel.getFeedbackInboxReviewListNext(2)

            val expectedResult = Fail(error)
            viewModel.feedbackInboxReview.verifyErrorEquals(expectedResult)
        }
    }

    @Test
    fun `when get inbox review rating filter should return fail`() {
        runBlocking {
            val error = NullPointerException()
            onGetInboxReview_thenError(error)

            val statusFilter = ArrayList<SortFilterInboxItemWrapper>().apply {
                add(SortFilterInboxItemWrapper(isSelected = true, sortValue = "answered"))
            }

            viewModel.apply {
                feedbackInboxReviewMediator.observe( {lifecycle}) {}
                updateStatusFilterData(statusFilter)
                setFilterStatusDataText(statusFilter)
            }

            viewModel.getFeedbackInboxReviewListNext(3)

            val expectedResult = Fail(error)
            viewModel.inboxReview.verifyErrorEquals(expectedResult)
        }
    }

    @Test
    fun `when init get inbox review on lazy load should return success`() {
        runBlocking {
            onGetInboxReview_thenReturn()

            viewModel.getInitInboxReview(statusFilter = UNANSWERED_VALUE)

            verifySuccessGetInboxReviewUseCaseCalled()
            Assert.assertTrue(viewModel.inboxReview.value is Success)
            Assert.assertNotNull(viewModel.inboxReview.value)


            viewModel.getInitFeedbackInboxReviewListNext(2, ANSWERED_VALUE)
            verifySuccessGetInboxReviewUseCaseCalled()
            Assert.assertTrue(viewModel.feedbackInboxReview.value is Success)
            Assert.assertNotNull(viewModel.feedbackInboxReview.value)
        }
    }

    @Test
    fun `when get inbox review on lazy load with rating filter should return success`() {
        onGetInboxReview_thenReturn()
        val ratingFilter = ArrayList<ListItemRatingWrapper>().apply {
            add(ListItemRatingWrapper(isSelected = true, sortValue = "2"))
            add(ListItemRatingWrapper(isSelected = true, sortValue = "3"))
            add(ListItemRatingWrapper(isSelected = true, sortValue = "4"))
        }

        viewModel.updateRatingFilterData(ratingFilter)
        viewModel.getInitFeedbackInboxReviewListNext(2, ANSWERED_VALUE)

        verifySuccessGetInboxReviewUseCaseCalled()
        Assert.assertTrue(viewModel.feedbackInboxReviewMediator.value is Success)
        Assert.assertNotNull(viewModel.feedbackInboxReviewMediator.value)
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