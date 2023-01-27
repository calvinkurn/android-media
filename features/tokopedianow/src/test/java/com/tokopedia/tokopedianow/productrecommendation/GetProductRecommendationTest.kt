package com.tokopedia.tokopedianow.productrecommendation

import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.common.domain.mapper.ProductRecommendationMapper.mapResponseToProductRecommendation
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class GetProductRecommendationTest : TokoNowProductRecommendationViewModelTestFixture() {

    private val productRecommendations = listOf(
        RecommendationItem(
            productId = 21222,
            name = "product a",
            price = "Rp. 10000",
            maxOrder = 10,
            minOrder = 2,
            stock = 22
        ),
        RecommendationItem(
            productId = 21223,
            name = "product b",
            price = "Rp. 20000",
            maxOrder = 20,
            minOrder = 1,
            stock = 44
        ),
        RecommendationItem(
            productId = 21224,
            name = "product c",
            price = "Rp. 30000",
            maxOrder = 9,
            minOrder = 2,
            stock = 12
        )
    )

    @Test
    fun `while getting product recommendation, the request should be success with returning empty value`() = runBlockingTest {
        onGetRecommendation_thenReturn(listOf())

        viewModel.getRecommendationCarousel(GetRecommendationRequestParam())

        viewModel.productRecommendation.verifyErrorEquals(Fail(Throwable()))
        viewModel.loadingState.verifyValueEquals(false)
    }

    @Test
    fun `while getting product recommendation, the request should be success with returning the value and an empty recommendation list`() = runBlockingTest {
        onGetRecommendation_thenReturn(listOf(RecommendationWidget()))

        viewModel.getRecommendationCarousel(GetRecommendationRequestParam())

        viewModel.productRecommendation.verifyErrorEquals(Fail(Throwable()))
        viewModel.loadingState.verifyValueEquals(false)
    }

    @Test
    fun `while getting product recommendation, the request should be success with returning the value`() = runBlockingTest {
        val recommendationWidget = RecommendationWidget(
            title = "product recom",
            seeMoreAppLink = "tokopedia://now",
            recommendationItemList = productRecommendations
        )

        onGetRecommendation_thenReturn(listOf(recommendationWidget))

        viewModel.getRecommendationCarousel(GetRecommendationRequestParam())

        val expectedProductModels = mapResponseToProductRecommendation(
            recommendationWidget = recommendationWidget,
            miniCartData = null
        )

        viewModel.productRecommendation.verifySuccessEquals(Success(expectedProductModels))
        viewModel.loadingState.verifyValueEquals(false)
    }

    @Test
    fun `while getting product recommendation, the request should be success with returning the value without seeMoreAppLink`() = runBlockingTest {
        val recommendationWidget = RecommendationWidget(
            title = "product recom",
            recommendationItemList = productRecommendations
        )

        onGetRecommendation_thenReturn(listOf(recommendationWidget))

        viewModel.getRecommendationCarousel(GetRecommendationRequestParam())

        val expectedProductModels = mapResponseToProductRecommendation(
            recommendationWidget = recommendationWidget,
            miniCartData = null
        )

        viewModel.productRecommendation.verifySuccessEquals(Success(expectedProductModels))
        viewModel.loadingState.verifyValueEquals(false)
    }

    @Test
    fun `while getting product recommendation, the request should fail`() = runBlockingTest {
        onGetRecommendation_thenReturn(Throwable())

        viewModel.getRecommendationCarousel(GetRecommendationRequestParam())

        viewModel.productRecommendation.verifyErrorEquals(Fail(Throwable()))
        viewModel.loadingState.verifyValueEquals(false)
    }

    @Test
    fun `while updating and setting recommendation page name should get expected result`() = runBlockingTest {
        /**
         * 1. doesn't have any job and the page name is not yet set, it should do nothing
         */
        viewModel.updateProductRecommendation(GetRecommendationRequestParam(pageName = "tokonow"))

        viewModel.productRecommendation.verifyValueEquals(null)

        /**
         * 2. set the job and should get response value
         */
        var recommendationWidget = RecommendationWidget(
            title = "product recom",
            seeMoreAppLink = "tokopedia://now",
            recommendationItemList = productRecommendations
        )

        onGetRecommendation_thenReturn(listOf(recommendationWidget))

        viewModel.getRecommendationCarousel(GetRecommendationRequestParam(pageName = "tokonow"))

        var expectedProductModels = mapResponseToProductRecommendation(
            recommendationWidget = recommendationWidget,
            miniCartData = null
        )

        viewModel.productRecommendation.verifySuccessEquals(Success(expectedProductModels))

        /**
         * 3. set the page name
         */
        viewModel.setRecommendationPageName("tokonow")

        /**
         * 4. the job is not null and the page name is in the list, so it will update the recommendation
         */
        val newProducts = productRecommendations.subList(0,2)

        recommendationWidget = RecommendationWidget(
            title = "product recom",
            seeMoreAppLink = "tokopedia://now",
            recommendationItemList = newProducts
        )

        onGetRecommendation_thenReturn(listOf(recommendationWidget))

        viewModel.updateProductRecommendation(GetRecommendationRequestParam(pageName = "tokonow"))

        expectedProductModels = mapResponseToProductRecommendation(
            recommendationWidget = recommendationWidget,
            miniCartData = null
        )

        viewModel.productRecommendation.verifySuccessEquals(Success(expectedProductModels))

        /**
         * 5. set the same page name, it should not add into list
         */
        viewModel.setRecommendationPageName("tokonow")

        /**
         * 6. the job is not null but the page name is not in the list, so it should do nothing
         */
        viewModel.updateProductRecommendation(GetRecommendationRequestParam(pageName = "category"))

        viewModel.productRecommendation.verifySuccessEquals(Success(expectedProductModels))
    }
}
