package com.tokopedia.cart.view.viewmodel

import com.google.gson.Gson
import com.tokopedia.cartrevamp.view.uimodel.LoadRecentReviewState
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import io.mockk.coEvery
import org.junit.Assert.assertEquals
import org.junit.Test

class GetRecentViewTest : BaseCartViewModelTest() {

    @Test
    fun `WHEN get recent view success THEN should render recent view section`() {
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

        coEvery { getRecentViewUseCase.getData(any()) } returns response

        // WHEN
        cartViewModel.processGetRecentViewData()

        // THEN
        assertEquals(
            LoadRecentReviewState.Success(response),
            cartViewModel.recentViewState.value
        )
    }

    @Test
    fun `WHEN get recent view failed THEN should not render recent view section`() {
        // GIVEN
        val exception = IllegalStateException()
        coEvery { getRecentViewUseCase.getData(any()) } throws exception

        // WHEN
        cartViewModel.processGetRecentViewData()

        // THEN
        assertEquals(
            LoadRecentReviewState.Failed(exception),
            cartViewModel.recentViewState.value
        )
    }
}
