package com.tokopedia.cart.view.presenter

import com.google.gson.Gson
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Test
import rx.Observable

class GetRecommendationTest : BaseCartTest() {

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
            val recommendationWidget = Gson().fromJson(recommendationWidgetStringData, RecommendationWidget::class.java)
            add(recommendationWidget)
        }

        every { getRecommendationUseCase.createObservable(any()) } returns Observable.just(response)
        every { getRecommendationUseCase.getRecomParams(any(), any(), any(), any(), any()) } returns RequestParams.create()

        // WHEN
        cartListPresenter.processGetRecommendationData(1, emptyList())

        // THEN
        verifyOrder {
            view.renderRecommendation(response[0])
            view.setHasTriedToLoadRecommendation()
            view.stopAllCartPerformanceTrace()
        }
    }

    @Test
    fun `WHEN get recommendation empty THEN should not render recommendation section`() {
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

        every { getRecommendationUseCase.createObservable(any()) } returns Observable.just(response)
        every { getRecommendationUseCase.getRecomParams(any(), any(), any(), any(), any()) } returns RequestParams.create()

        // WHEN
        cartListPresenter.processGetRecommendationData(1, emptyList())

        // THEN
        verify(inverse = true) {
            view.renderRecommendation(response[0])
        }

        verifyOrder {
            view.setHasTriedToLoadRecommendation()
            view.stopAllCartPerformanceTrace()
        }
    }

    @Test
    fun `WHEN get recommendation error THEN should not render recommendation section`() {
        // GIVEN
        every { getRecommendationUseCase.createObservable(any()) } returns Observable.error(IllegalStateException())
        every { getRecommendationUseCase.getRecomParams(any(), any(), any(), any(), any()) } returns RequestParams.create()

        // WHEN
        cartListPresenter.processGetRecommendationData(1, emptyList())

        // THEN
        // THEN
        verify(inverse = true) {
            view.renderRecommendation(any())
        }

        verifyOrder {
            view.setHasTriedToLoadRecommendation()
            view.stopAllCartPerformanceTrace()
        }

    }

    @Test
    fun `WHEN get recommendation with view is detached THEN should not render`() {
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

        every { getRecommendationUseCase.createObservable(any()) } returns Observable.just(response)
        every { getRecommendationUseCase.getRecomParams(any(), any(), any(), any(), any()) } returns RequestParams.create()

        cartListPresenter.detachView()

        // WHEN
        cartListPresenter.processGetRecommendationData(1, emptyList())

        // THEN
        verify(inverse = true) {
            view.showItemLoading()
        }
    }

}