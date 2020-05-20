package com.tokopedia.reviewseller.reviewdetail

import com.tokopedia.reviewseller.feature.reviewdetail.data.ProductFeedbackDetailResponse
import com.tokopedia.reviewseller.feature.reviewdetail.data.ProductFeedbackFilterResponse
import com.tokopedia.reviewseller.feature.reviewdetail.data.ProductReviewDetailOverallResponse
import com.tokopedia.reviewseller.feature.reviewdetail.data.ProductReviewInitialDataResponse
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.RatingBarUiModel
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.SortFilterItemWrapper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.TestCase.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.*

class ProductReviewDetailViewModelTest : ProductReviewDetailViewModelTestFixture() {

    @Test
    fun `when get review product initial by sort only should return success`() {
        runBlocking {
            viewModel.setChipFilterDateText(anyString())
            onProductReviewInitial_thenReturn()

            viewModel.getProductRatingDetail(anyInt(), anyString())

            verifySuccessProductReviewInitialUseCaseCalled()
            assertTrue(viewModel.reviewInitialData.value is Success)
            assertNotNull(viewModel.reviewInitialData.value)
        }
    }

    @Test
    fun `when set update rating filter data should return not null`() {

        val dataList = mutableListOf<RatingBarUiModel>().apply {
            add(RatingBarUiModel(anyInt(), anyInt(), anyFloat(), anyBoolean()))
        }

        viewModel.updateRatingFilterData(dataList)

        assertEquals(viewModel.filterRatingData, dataList)
        assertNotNull(viewModel.filterRatingData)
    }

    @Test
    fun `when set update topics filter data should return not null`() {

        val sortFilterItemList = mutableListOf<SortFilterItemWrapper>().apply {
            add(SortFilterItemWrapper(ArgumentMatchers.any(), anyBoolean(), anyInt(), anyString()))
        }

        viewModel.updateTopicsFilterData(sortFilterItemList)

        assertEquals(viewModel.filterTopicData, sortFilterItemList)
        assertNotNull(viewModel.filterTopicData)
    }

    @Test
    fun `when set value filter topic data should return not null`() {

        val dataList = mutableListOf<SortFilterItemWrapper>().apply {
            add(SortFilterItemWrapper(ArgumentMatchers.any(), anyBoolean(), anyInt(), anyString()))
        }

        viewModel.setFilterTopicDataText(dataList)

        assertEquals(viewModel.topicFilterData.value, dataList)
        assertNotNull(viewModel.topicFilterData.value)
    }

    @Test
    fun `when set value sort and topic filter data should return not null`() {

        val sortFilterItemList = mutableListOf<SortFilterItemWrapper>().apply {
            add(SortFilterItemWrapper(ArgumentMatchers.any(), anyBoolean(), anyInt(), anyString()))
        }

        val dataList = Pair(sortFilterItemList, anyString())

        viewModel.setSortAndFilterTopicData(dataList)

        assertEquals(viewModel.topicAndSortFilterData.value?.first, sortFilterItemList)
        assertNotNull(viewModel.topicAndSortFilterData.value)
    }

    @Test
    fun `when set value filter rating data should return not null`() {

        val dataList = mutableListOf<RatingBarUiModel>().apply {
            add(RatingBarUiModel(anyInt(), anyInt(), anyFloat(), anyBoolean()))
        }

        viewModel.setFilterRatingDataText(dataList)

        assertEquals(viewModel.ratingFilterData.value, dataList)
        assertNotNull(viewModel.ratingFilterData.value)
    }

    @Test
    fun `when get feedback detail on lazy load by sort should return success`() {
        runBlocking {
            onProductFeedbackDetailList_thenReturn()
            viewModel.getFeedbackDetailListNext(anyInt(), "create_time desc", anyInt())
            verifySuccessProductFeedbackDetailListUseCaseCalled()
            assertTrue(viewModel.productFeedbackDetail.value is Success)
            assertNotNull(viewModel.productFeedbackDetail.value)
        }
    }

    @Test
    fun `when get product initial rating detail should set result fail`() {
        runBlocking {
            val error = NullPointerException()
            onProductReviewInitial_thenError(error)

            viewModel.getProductRatingDetail(anyInt(), anyString())
            val expectedResult = Fail(error)
            viewModel.reviewInitialData.verifyErrorEquals(expectedResult)
        }
    }

    @Test
    fun `when get product feedback detail list should set result fail`() {
        runBlocking {
            val error = NullPointerException()
            onProductFeedbackDetailList_thenError(error)

            viewModel.getFeedbackDetailListNext(anyInt(), anyString(), anyInt())
            val expectedResult = Fail(error)
            viewModel.productFeedbackDetail.verifyErrorEquals(expectedResult)
        }
    }

    @Test
    fun `when set period filter should return equal expected and not empty`() {
        viewModel.filterPeriod = "create_time desc"
        val expectedResult = "create_time desc"
        assertEquals(viewModel.filterPeriod, expectedResult)
        assertNotNull(viewModel.filterPeriod)
    }

    @Test
    fun `when set sort by should return equal expected and not empty`() {
        viewModel.sortBy = "time=7d"
        val expectedResult = "time=7d"
        assertEquals(viewModel.sortBy, expectedResult)
        assertNotNull(viewModel.sortBy)
    }

    private fun onProductReviewInitial_thenError(exception: NullPointerException) {
        coEvery { getProductReviewInitialUseCase.executeOnBackground() } coAnswers { throw exception }
    }

    private fun onProductFeedbackDetailList_thenError(exception: NullPointerException) {
        coEvery { getProductFeedbackDetailListUseCase.executeOnBackground() } coAnswers { throw exception }
    }

    private fun onProductReviewInitial_thenReturn() {
        coEvery { getProductReviewInitialUseCase.executeOnBackground() } returns ProductReviewInitialDataResponse(
                productFeedBackResponse = ProductFeedbackDetailResponse(),
                productReviewDetailOverallResponse = ProductReviewDetailOverallResponse(),
                productReviewFilterResponse = ProductFeedbackFilterResponse()
        )
    }

    private fun onProductFeedbackDetailList_thenReturn() {
        coEvery { getProductFeedbackDetailListUseCase.executeOnBackground() } returns ProductFeedbackDetailResponse.ProductFeedbackDataPerProduct()
    }

    private fun verifySuccessProductReviewInitialUseCaseCalled() {
        coVerify { getProductReviewInitialUseCase.executeOnBackground() }
    }

    private fun verifySuccessProductFeedbackDetailListUseCaseCalled() {
        coVerify { getProductFeedbackDetailListUseCase.executeOnBackground() }
    }
}