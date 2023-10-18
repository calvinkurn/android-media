package com.tokopedia.cart.view.presenter

import com.google.gson.Gson
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import io.mockk.coEvery
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Test

class GetRecentViewTest : BaseCartTest() {

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
            val recommendationWidget = Gson().fromJson(recommendationWidgetStringData, RecommendationWidget::class.java)
            add(recommendationWidget)
        }

        coEvery { getRecentViewUseCase.getData(any()) } returns response

        // WHEN
        cartListPresenter.processGetRecentViewData(emptyList())

        // THEN
        verifyOrder {
            view.renderRecentView(response[0])
            view.setHasTriedToLoadRecentView()
            view.stopAllCartPerformanceTrace()
        }
    }

    @Test
    fun `WHEN get recent view empty THEN should not render recent view section`() {
        // GIVEN
        val recommendationWidgetStringData = """
                {
                    "recommendationItemList":
                    [
                    ]
                }
        """.trimIndent()
        val response = mutableListOf<RecommendationWidget>().apply {
            val recommendationWidget = Gson().fromJson(recommendationWidgetStringData, RecommendationWidget::class.java)
            add(recommendationWidget)
        }

        coEvery { getRecentViewUseCase.getData(any()) } returns response

        // WHEN
        cartListPresenter.processGetRecentViewData(emptyList())

        // THEN
        verify(inverse = true) {
            view.renderRecentView(response[0])
        }
        verify {
            view.setHasTriedToLoadRecentView()
            view.stopAllCartPerformanceTrace()
        }
    }

    @Test
    fun `WHEN get recent view failed THEN should not render recent view section`() {
        // GIVEN
        coEvery { getRecentViewUseCase.getData(any()) } throws IllegalStateException()

        // WHEN
        cartListPresenter.processGetRecentViewData(emptyList())

        // THEN
        verify(inverse = true) {
            view.renderRecentView(any())
        }
        verify {
            view.setHasTriedToLoadRecentView()
            view.stopAllCartPerformanceTrace()
        }
    }

    @Test
    fun `WHEN get recent view with view is detached THEN should not render view`() {
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
            val recommendationWidget = Gson().fromJson(recommendationWidgetStringData, RecommendationWidget::class.java)
            add(recommendationWidget)
        }

        coEvery { getRecentViewUseCase.getData(any()) } returns response

        cartListPresenter.detachView()

        // WHEN
        cartListPresenter.processGetRecentViewData(emptyList())

        // THEN
        verify(inverse = true) {
            view.showItemLoading()
        }
    }
}
