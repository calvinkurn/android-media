package com.tokopedia.reviewseller.reviewlist

import com.tokopedia.reviewseller.common.util.ReviewSellerConstant
import com.tokopedia.reviewseller.feature.reviewlist.data.ProductRatingOverallResponse
import com.tokopedia.reviewseller.feature.reviewlist.data.ProductReviewListResponse
import com.tokopedia.reviewseller.feature.reviewlist.util.mapper.SellerReviewProductListMapper
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductRatingOverallUiModel
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
            val productRatingOverallData = ProductRatingOverallResponse.ProductGetProductRatingOverallByShop(
                    rating = 5.0F,
                    reviewCount = 50,
                    productCount = 60,
                    filterBy = "time=7d"
            )

            onOverallProductRating_thenReturn(productRatingOverallData)
            onReviewProductList_thenReturn()

            viewModel.getProductRatingData("time=7d", anyString())

            val expectedResult = Success(ProductRatingOverallUiModel(
                    rating = 5.0F,
                    reviewCount = 50,
                    period = SellerReviewProductListMapper.getPastDateCalculate(ReviewSellerConstant.LAST_WEEK_KEY)))

            verifySuccessProductRatingOverallUseCaseCalled()
            verifySuccessReviewProductListUseCaseCalled()
            viewModel.productRatingOverall.verifySuccessEquals(expectedResult)
            assertTrue(viewModel.reviewProductList.value is Success)
            assertNotNull(viewModel.reviewProductList.value)
        }
    }

    @Test
    fun `when get overall product rating by sort and filter should return success`() {
        runBlocking {
            val productRatingOverallData = ProductRatingOverallResponse.ProductGetProductRatingOverallByShop(
                    rating = 5.0F,
                    reviewCount = 50,
                    productCount = 60)

            onOverallProductRating_thenReturn(productRatingOverallData)
            onReviewProductList_thenReturn()

            viewModel.getProductRatingData("time=7d", "review_count desc")

            val expectedResult = Success(ProductRatingOverallUiModel(
                    rating = 5.0F,
                    reviewCount = 50,
                    period = SellerReviewProductListMapper.getPastDateCalculate(ReviewSellerConstant.LAST_WEEK_KEY)))

            verifySuccessProductRatingOverallUseCaseCalled()
            verifySuccessReviewProductListUseCaseCalled()
            viewModel.productRatingOverall.verifySuccessEquals(expectedResult)
            assertTrue(viewModel.reviewProductList.value is Success)
            assertNotNull(viewModel.reviewProductList.value)
        }
    }

    @Test
    fun `when get overall product rating by filter only should return success`() {
        runBlocking {
            val productRatingOverallData = ProductRatingOverallResponse.ProductGetProductRatingOverallByShop(
                    rating = 5.0F,
                    reviewCount = 50,
                    productCount = 60)

            onOverallProductRating_thenReturn(productRatingOverallData)
            onReviewProductList_thenReturn()
            viewModel.getProductRatingData(anyString(), "review_count desc")

            val expectedResult = Success(ProductRatingOverallUiModel(
                    rating = 5.0F,
                    reviewCount = 50,
                    period = SellerReviewProductListMapper.getPastDateCalculate(ReviewSellerConstant.LAST_WEEK_KEY)))

            verifySuccessProductRatingOverallUseCaseCalled()
            verifySuccessReviewProductListUseCaseCalled()
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
        coEvery { getProductRatingOverallUse.executeOnBackground() } returns response
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