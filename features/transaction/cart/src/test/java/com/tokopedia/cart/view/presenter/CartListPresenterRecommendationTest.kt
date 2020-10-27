package com.tokopedia.cart.view.presenter

import com.google.gson.Gson
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.cart.view.presenter.PresenterProvider.getRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable

/**
 * Created by Irfan Khoirul on 2020-01-30.
 */

object CartListPresenterRecommendationTest : Spek({

    val view: ICartListView = mockk(relaxed = true)

    Feature("get recommendation test") {

        val cartListPresenter by memoized {
            PresenterProvider.provideCartListPresenter()
        }

        beforeEachTest {
            cartListPresenter.attachView(view)
        }

        Scenario("get recommendation success") {

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

            Given("success response") {
                every { getRecommendationUseCase.createObservable(any()) } returns Observable.just(response)
            }

            Given("request params") {
                every { getRecommendationUseCase.getRecomParams(any(), any(), any(), any(), any()) } returns RequestParams.create()
            }

            When("process get recommendation") {
                cartListPresenter.processGetRecommendationData(1, emptyList())
            }

            Then("should render recommendation") {
                verify {
                    view.renderRecommendation(response[0])
                }
            }

            Then("should try to stop firebase performance tracker") {
                verify {
                    view.setHasTriedToLoadRecommendation()
                    view.stopAllCartPerformanceTrace()
                }
            }

        }

        Scenario("get recommendation empty") {

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

            Given("empty response") {
                every { getRecommendationUseCase.createObservable(any()) } returns Observable.just(response)
            }

            Given("request params") {
                every { getRecommendationUseCase.getRecomParams(any(), any(), any(), any(), any()) } returns RequestParams.create()
            }

            When("process get recommendation") {
                cartListPresenter.processGetRecommendationData(1, emptyList())
            }

            Then("should not render recommendation") {
                verify(inverse = true) {
                    view.renderRecommendation(response[0])
                }
            }

            Then("should try to stop firebase performance tracker") {
                verify {
                    view.setHasTriedToLoadRecommendation()
                    view.stopAllCartPerformanceTrace()
                }
            }

        }

        Scenario("get recommendation empty") {

            Given("error response") {
                every { getRecommendationUseCase.createObservable(any()) } returns Observable.error(IllegalStateException())
            }

            Given("request params") {
                every { getRecommendationUseCase.getRecomParams(any(), any(), any(), any(), any()) } returns RequestParams.create()
            }

            When("process get recommendation") {
                cartListPresenter.processGetRecommendationData(1, emptyList())
            }

            Then("should try to stop firebase performance tracker") {
                verify {
                    view.setHasTriedToLoadRecommendation()
                    view.stopAllCartPerformanceTrace()
                }
            }

        }

    }

})