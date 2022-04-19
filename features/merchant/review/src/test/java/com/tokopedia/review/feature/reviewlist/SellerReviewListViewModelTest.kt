package com.tokopedia.review.feature.reviewlist

import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.reviewlist.data.ProductRatingOverallResponse
import com.tokopedia.review.feature.reviewlist.data.ProductReviewListResponse
import com.tokopedia.review.feature.reviewlist.domain.GetProductRatingOverallUseCase
import com.tokopedia.review.feature.reviewlist.util.mapper.SellerReviewProductListMapper
import com.tokopedia.review.feature.reviewlist.view.model.ProductRatingOverallUiModel
import com.tokopedia.review.feature.reviewlist.view.model.ProductRatingWrapperUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class SellerReviewListViewModelTest: SellerReviewListViewModelTextFixture() {

    @Test
    fun `when get overall product rating by sort only should return success`() {
        runBlocking {
            val sortBy = "time=7d"
            val filterBy = "review_count desc"
            val productRatingOverallData = ProductRatingOverallResponse.ProductGetProductRatingOverallByShop(
                    rating = 5.0F,
                    reviewCount = 50,
                    productCount = 60,
                    filterBy = sortBy
            )

            getProductRatingOverallUse.requestParams = GetProductRatingOverallUseCase.createParams(sortBy, filterBy, 15, 1)

            onOverallProductRating_thenReturn(productRatingOverallData)

            viewModel.getProductRatingData(sortBy, anyString())

            val expectedResult = Success(ProductRatingOverallUiModel(
                    rating = 5.0F,
                    reviewCount = 50,
                    period = SellerReviewProductListMapper.getPastDateCalculate(ReviewConstants.LAST_WEEK_KEY)))

            verifySuccessProductRatingOverallUseCaseCalled()

            viewModel.productRatingOverall.verifySuccessEquals(expectedResult)
            assertTrue(viewModel.reviewProductList.value is Success)
            assertNotNull(viewModel.reviewProductList.value)
        }
    }

    @Test
    fun `when get overall product rating by sort and filter should return success`() {
        runBlocking {
            val sortBy = "time=7d"
            val filterBy = "review_count desc"

            val productRatingOverallData = ProductRatingOverallResponse.ProductGetProductRatingOverallByShop(
                    rating = 5.0F,
                    reviewCount = 50,
                    productCount = 60)

            getProductRatingOverallUse.requestParams = GetProductRatingOverallUseCase.createParams(sortBy, filterBy, 15, 1)

            onOverallProductRating_thenReturn(productRatingOverallData)

            viewModel.getProductRatingData(sortBy, filterBy)

            val expectedResult = Success(ProductRatingOverallUiModel(
                    rating = 5.0F,
                    reviewCount = 50,
                    period = SellerReviewProductListMapper.getPastDateCalculate(ReviewConstants.LAST_WEEK_KEY)))

            verifySuccessProductRatingOverallUseCaseCalled()
            viewModel.productRatingOverall.verifySuccessEquals(expectedResult)
            assertTrue(viewModel.reviewProductList.value is Success)
            assertNotNull(viewModel.reviewProductList.value)
        }
    }

    @Test
    fun `when get overall product rating by filter only should return success`() {
        runBlocking {
            val sortBy = "time=7d"
            val filterBy = "review_count desc"
            val productRatingOverallData = ProductRatingOverallResponse.ProductGetProductRatingOverallByShop(
                    rating = 5.0F,
                    reviewCount = 50,
                    productCount = 60)

            getProductRatingOverallUse.requestParams = GetProductRatingOverallUseCase.createParams(sortBy, filterBy, 15, 1)

            onOverallProductRating_thenReturn(productRatingOverallData)
            viewModel.getProductRatingData(anyString(), filterBy)

            val expectedResult = Success(ProductRatingOverallUiModel(
                    rating = 5.0F,
                    reviewCount = 50,
                    period = SellerReviewProductListMapper.getPastDateCalculate(ReviewConstants.LAST_WEEK_KEY)))

            verifySuccessProductRatingOverallUseCaseCalled()
            viewModel.productRatingOverall.verifySuccessEquals(expectedResult)
        }
    }

    @Test
    fun `when get overall product rating should set result fail`() {
        runBlocking {
            val error = NullPointerException()
            onOverallProductRating_thenError(error)

            viewModel.getProductRatingData(anyString(), anyString())
            val expectedResult = Fail(error)
            viewModel.productRatingOverall.verifyErrorEquals(expectedResult)
        }
    }

    @Test
    fun `when get review list product rating on lazy load by sort only should return success`() {
        runBlocking {
            onReviewProductList_thenReturn()

            viewModel.getNextProductReviewList("time=7d", anyString(), 1)

            verifySuccessReviewProductListUseCaseCalled()
            assertTrue(viewModel.reviewProductList.value is Success)
            assertNotNull(viewModel.reviewProductList.value)
        }
    }

    @Test
    fun `when get review list product rating on lazy load by sort and filter should return success`() {
        runBlocking {
            onReviewProductList_thenReturn()

            viewModel.getNextProductReviewList("time=7d", "review_count desc", 1)

            verifySuccessReviewProductListUseCaseCalled()
            assertTrue(viewModel.reviewProductList.value is Success)
            assertNotNull(viewModel.reviewProductList.value)
        }
    }

    @Test
    fun `when get review list product rating on lazy load by filter only should return success`() {
        runBlocking {
            onReviewProductList_thenReturn()
            viewModel.getNextProductReviewList(anyString(), "review_count desc", 1)

            verifySuccessReviewProductListUseCaseCalled()
            assertTrue(viewModel.reviewProductList.value is Success)
            assertNotNull(viewModel.reviewProductList.value)
        }
    }


    @Test
    fun `when get review list product rating on lazy load should set result fail`() {
        runBlocking {
            val error = NullPointerException()
            onReviewListProductRating_thenError(error)

            viewModel.getNextProductReviewList(anyString(), anyString(), 1)
            val expectedResult = Fail(error)
            viewModel.reviewProductList.verifyErrorEquals(expectedResult)
        }
    }


    private fun onOverallProductRating_thenError(exception: NullPointerException) {
        coEvery { getProductRatingOverallUse.executeOnBackground() } coAnswers { throw exception }
    }

    private fun onReviewListProductRating_thenError(exception: NullPointerException) {
        coEvery { getReviewProductListUseCase.executeOnBackground() } coAnswers { throw exception }
    }

    private fun onOverallProductRating_thenReturn(response: ProductRatingOverallResponse.ProductGetProductRatingOverallByShop) {
        coEvery { getProductRatingOverallUse.executeOnBackground() } returns ProductRatingWrapperUiModel(productRatingOverall = Success(response),
                reviewProductList = Success(ProductReviewListResponse.ProductShopRatingAggregate()))
    }

    private fun onReviewProductList_thenReturn() {
        coEvery { getReviewProductListUseCase.executeOnBackground() } returns ProductReviewListResponse.ProductShopRatingAggregate()
    }

    private fun verifySuccessProductRatingOverallUseCaseCalled() {
        coVerify { getProductRatingOverallUse.executeOnBackground() }
    }

    private fun verifySuccessReviewProductListUseCaseCalled() {
        coVerify { getReviewProductListUseCase.executeOnBackground() }
    }
}