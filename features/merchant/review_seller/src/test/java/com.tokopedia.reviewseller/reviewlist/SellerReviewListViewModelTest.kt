package com.tokopedia.reviewseller.reviewlist

import com.tokopedia.reviewseller.feature.reviewlist.data.ProductRatingOverallResponse
import com.tokopedia.reviewseller.feature.reviewlist.data.ProductReviewListResponse
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductRatingOverallUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class SellerReviewListViewModelTest: SellerReviewListViewModelTextFixture() {

    @Test
    fun `when get overall product rating by sort only should return success`() {
        runBlocking {
            viewModel.sortBy = "time=7d"

            val productRatingOverallData = ProductRatingOverallResponse.ProductGetProductRatingOverallByShop(
                    rating = 5.0F,
                    reviewCount = 50,
                    productCount = 60,
                    filterBy = "time=7d"
            )

            onOverallProductRating_thenReturn(productRatingOverallData)
            onReviewProductList_thenReturn()

            viewModel.getProductRatingData(viewModel.sortBy.orEmpty(), anyString())

            val expectedResult = Success(ProductRatingOverallUiModel(
                    rating = 5.0F,
                    reviewCount = 50,
                    period = "11 May 2020"
            ))

            verifySuccessProductRatingOverallUseCaseCalled()
            verifySuccessReviewProductListUseCaseCalled()
            viewModel.productRatingOverall.verifySuccessEquals(expectedResult)
        }
    }

    @Test
    fun `when get overall product rating by sort and filter should return success`() {
        runBlocking {
            viewModel.sortBy = "time=7d"
            viewModel.filterBy = "review_count desc"

            val productRatingOverallData = ProductRatingOverallResponse.ProductGetProductRatingOverallByShop(
                    rating = 5.0F,
                    reviewCount = 50,
                    productCount = 60,
                    filterBy = "time=7d"
            )

            onOverallProductRating_thenReturn(productRatingOverallData)
            onReviewProductList_thenReturn()

            viewModel.getProductRatingData(viewModel.sortBy.orEmpty(), viewModel.filterBy.orEmpty())

            val expectedResult = Success(ProductRatingOverallUiModel(
                    rating = 5.0F,
                    reviewCount = 50,
                    period = "11 May 2020"
            ))

            verifySuccessProductRatingOverallUseCaseCalled()
            verifySuccessReviewProductListUseCaseCalled()
            viewModel.productRatingOverall.verifySuccessEquals(expectedResult)
        }
    }

    @Test
    fun `when get overall product rating by filter only should return success`() {
        runBlocking {
            viewModel.filterBy = "review_count desc"

            val productRatingOverallData = ProductRatingOverallResponse.ProductGetProductRatingOverallByShop(
                    rating = 5.0F,
                    reviewCount = 50,
                    productCount = 60,
                    filterBy = "time=7d"
            )

            onOverallProductRating_thenReturn(productRatingOverallData)
            onReviewProductList_thenReturn()
            viewModel.getProductRatingData(anyString(), viewModel.filterBy.orEmpty())

            val expectedResult = Success(ProductRatingOverallUiModel(
                    rating = 5.0F,
                    reviewCount = 50,
                    period = "11 May 2020"
            ))

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


    private fun onOverallProductRating_thenError(exception: NullPointerException) {
        coEvery { getProductRatingOverallUse.executeOnBackground() } coAnswers { throw exception }
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