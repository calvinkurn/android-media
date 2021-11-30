package com.tokopedia.review.feature.reviewdetail

import com.tokopedia.review.feature.reviewdetail.data.ProductFeedbackDetailResponse
import com.tokopedia.review.feature.reviewdetail.data.ProductFeedbackFilterResponse
import com.tokopedia.review.feature.reviewdetail.data.ProductReviewDetailOverallResponse
import com.tokopedia.review.feature.reviewdetail.data.ProductReviewInitialDataResponse
import com.tokopedia.review.feature.reviewdetail.util.mapper.SellerReviewProductDetailMapper
import com.tokopedia.review.feature.reviewdetail.view.model.ProductReviewFilterUiModel
import com.tokopedia.review.feature.reviewdetail.view.model.RatingBarUiModel
import com.tokopedia.review.feature.reviewdetail.view.model.SortFilterItemWrapper
import com.tokopedia.review.feature.reviewdetail.view.model.TopicUiModel
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

            viewModel.getProductRatingDetail(anyString(), anyString())
            val dataResponse = (viewModel.reviewInitialData.value as Success).data
            viewModel.updateRatingFilterData(dataResponse.first.filterIsInstance<ProductReviewFilterUiModel>().firstOrNull()?.ratingBarList
                    ?: listOf())
            viewModel.updateTopicsFilterData(dataResponse.first.filterIsInstance<TopicUiModel>().firstOrNull()?.sortFilterItemList
                    ?: arrayListOf())

            verifySuccessProductReviewInitialUseCaseCalled()
            assertTrue(viewModel.reviewInitialData.value is Success)
            assertNotNull(viewModel.reviewInitialData.value)
            assertNotNull(viewModel.getFilterRatingData())
            assertNotNull(viewModel.getFilterTopicData())
        }
    }

    @Test
    fun `when set sort topic data should return not null`() {

        val sortTopicData = SellerReviewProductDetailMapper.mapToItemSortTopic()

        viewModel.setSortTopicData(sortTopicData)

        assertEquals(viewModel.getSortTopicData(), sortTopicData)
        assertNotNull(viewModel.getSortTopicData())
    }

    @Test
    fun `when set update rating filter data should return not null`() {

        val dataList = mutableListOf<RatingBarUiModel>().apply {
            add(RatingBarUiModel(anyInt(), anyInt(), anyFloat(), anyBoolean()))
        }

        viewModel.updateRatingFilterData(dataList)

        assertEquals(viewModel.getFilterRatingData(), dataList)
        assertNotNull(viewModel.getFilterRatingData())
    }

    @Test
    fun `when set update topics filter data should return not null`() {

        val sortFilterItemList = mutableListOf<SortFilterItemWrapper>().apply {
            add(SortFilterItemWrapper(ArgumentMatchers.any(), anyBoolean(), anyInt(), anyString()))
        }

        viewModel.updateTopicsFilterData(sortFilterItemList)

        assertEquals(viewModel.getFilterTopicData(), sortFilterItemList)
        assertNotNull(viewModel.getFilterTopicData())
    }

    @Test
    fun `when get feedback detail on lazy load by sort should return success`() {
        runBlocking {
            onProductFeedbackDetailList_thenReturn()
            viewModel.getFeedbackDetailListNext(anyString(), "create_time desc", anyInt())
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

            viewModel.getProductRatingDetail(anyString(), anyString())
            val expectedResult = Fail(error)
            viewModel.reviewInitialData.verifyErrorEquals(expectedResult)
        }
    }

    @Test
    fun `when get product feedback detail list should set result fail`() {
        runBlocking {
            val error = NullPointerException()
            onProductFeedbackDetailList_thenError(error)

            viewModel.getFeedbackDetailListNext(anyString(), anyString(), anyInt())
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

    @Test
    fun `when get product feedback detail by sort and topic only should return success`() {
        runBlocking {
            onProductFeedbackDetailList_thenReturn()

            val sortFilterItemList = mutableListOf<SortFilterItemWrapper>().apply {
                add(SortFilterItemWrapper(titleUnformated = "kualitas", isSelected = true))
                add(SortFilterItemWrapper(titleUnformated = "kemasan", isSelected = true))
            }
            val sortBy = "rating desc"
            val sortAndFilter = Pair(sortFilterItemList, sortBy)

            viewModel.productFeedbackDetailMediator.observe( {lifecycle}) {}

            viewModel.setSortAndFilterTopicData(sortAndFilter)
            viewModel.updateSortAndFilterTopicData(sortAndFilter)

            verifySuccessProductFeedbackDetailListUseCaseCalled()
            assertTrue(viewModel.productFeedbackDetail.value is Success)
            assertNotNull(viewModel.productFeedbackDetail.value)
            assertNotNull(viewModel.getSortAndFilter())
        }
    }

    @Test
    fun `when get product feedback detail by topic only should return success`() {
        runBlocking {
            onProductFeedbackDetailList_thenReturn()

            val sortFilterItemList = mutableListOf<SortFilterItemWrapper>().apply {
                add(SortFilterItemWrapper(titleUnformated = "kualitas", isSelected = true))
                add(SortFilterItemWrapper(titleUnformated = "kemasan", isSelected = true))
            }

            viewModel.productFeedbackDetailMediator.observe( {lifecycle}) {}

            viewModel.setFilterTopicDataText(sortFilterItemList)
            viewModel.updateTopicsFilterData(sortFilterItemList)

            verifySuccessProductFeedbackDetailListUseCaseCalled()
            assertTrue(viewModel.productFeedbackDetail.value is Success)
            assertNotNull(viewModel.productFeedbackDetail.value)
            assertNotNull(viewModel.getFilterTopicData())
        }
    }

    @Test
    fun `when get product feedback detail by rating only should return success`() {
        runBlocking {
            onProductFeedbackDetailList_thenReturn()

            val ratingBarList = mutableListOf<RatingBarUiModel>().apply {
                add(RatingBarUiModel(ratingLabel = 2, ratingIsChecked = true))
                add(RatingBarUiModel(ratingLabel = 3, ratingIsChecked = true))
            }

            viewModel.productFeedbackDetailMediator.observe( {lifecycle}) {}

            viewModel.setFilterRatingDataText(ratingBarList)
            viewModel.updateRatingFilterData(ratingBarList)

            verifySuccessProductFeedbackDetailListUseCaseCalled()
            assertTrue(viewModel.productFeedbackDetail.value is Success)
            assertNotNull(viewModel.productFeedbackDetail.value)
            assertNotNull(viewModel.getFilterRatingData())
        }
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