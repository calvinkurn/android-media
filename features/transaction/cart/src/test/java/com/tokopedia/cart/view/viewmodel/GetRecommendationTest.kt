package com.tokopedia.cart.view.viewmodel

import com.google.gson.Gson
import com.tokopedia.cartrevamp.view.uimodel.LoadRecommendationState
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import io.mockk.coEvery
import org.junit.Assert.assertEquals
import org.junit.Test

class GetRecommendationTest : BaseCartViewModelTest() {

    @Test
    fun `WHEN get recommendation success THEN should render recommendation section`() {
        // GIVEN
        val recommendationWidgetStringData = """
                {
                    "recommendationItemList":
                    [
                        {
                            "productId":0
                        }
                    ]
                }
        """.trimIndent()

        val response = mutableListOf<RecommendationWidget>().apply {
            val recommendationWidget =
                Gson().fromJson(recommendationWidgetStringData, RecommendationWidget::class.java)
            add(recommendationWidget)
        }

        coEvery { getRecommendationUseCase.getData(any()) } returns response

        // WHEN
        cartViewModel.processGetRecommendationData()

        // THEN
        assertEquals(
            LoadRecommendationState.Success(response),
            cartViewModel.recommendationState.value
        )
    }

    @Test
    fun `WHEN get recommendation error THEN should not render recommendation section`() {
        // GIVEN
        coEvery { getRecommendationUseCase.getData(any()) } throws IllegalStateException()

        // WHEN
        cartViewModel.processGetRecommendationData()

        // THEN
        // THEN
        assertEquals(LoadRecommendationState.Failed, cartViewModel.recommendationState.value)
    }
}
