package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.data.model.response.recentview.GqlRecentView
import com.tokopedia.cart.data.model.response.recentview.GqlRecentViewResponse
import com.tokopedia.cart.data.model.response.recentview.RecentView
import com.tokopedia.cart.domain.usecase.GetRecentViewUseCase
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.cart.view.presenter.PresenterProvider.userSessionInterface
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable

/**
 * Created by Irfan Khoirul on 2020-01-29.
 */

object CartListPresenterRecentViewTest : Spek({

    val getRecentViewUseCase: GetRecentViewUseCase = mockk()
    val view: ICartListView = mockk(relaxed = true)

    Feature("get recent view test") {

        val cartListPresenter by memoized {
            PresenterProvider.provideCartListPresenter()
        }

        beforeEachTest {
            cartListPresenter.attachView(view)
        }

        Scenario("get recent view success") {

            val response = GqlRecentViewResponse().apply {
                gqlRecentView = GqlRecentView().apply {
                    recentViewList = mutableListOf<RecentView>().apply {
                        add(RecentView())
                    }
                }
            }

            Given("success response") {
                every { getRecentViewUseCase.createObservable(any()) } returns Observable.just(response)
            }

            Given("user session") {
                every { userSessionInterface.userId } returns "1"
            }

            When("process get recent view") {
                cartListPresenter.processGetRecentViewData(emptyList())
            }

            Then("should render recent view") {
                verify {
                    view.renderRecentView(response.gqlRecentView?.recentViewList)
                }
            }

            Then("should try to stop firebase performance tracker") {
                verify {
                    view.setHasTriedToLoadRecentView()
                    view.stopAllCartPerformanceTrace()
                }
            }

        }

        Scenario("get recent view empty") {

            val response = GqlRecentViewResponse().apply {
                gqlRecentView = GqlRecentView().apply {
                    recentViewList = mutableListOf()
                }
            }

            Given("success response") {
                every { getRecentViewUseCase.createObservable(any()) } returns Observable.just(response)
            }

            Given("user session") {
                every { userSessionInterface.userId } returns "1"
            }

            When("process get recent view") {
                cartListPresenter.processGetRecentViewData(emptyList())
            }

            Then("should not render recent view") {
                verify(inverse = true) {
                    view.renderRecentView(response.gqlRecentView?.recentViewList)
                }
            }

            Then("should try to stop firebase performance tracker") {
                verify {
                    view.setHasTriedToLoadRecentView()
                    view.stopAllCartPerformanceTrace()
                }
            }

        }

        Scenario("get recent view error") {

            Given("error response") {
                every { getRecentViewUseCase.createObservable(any()) } returns Observable.error(IllegalStateException())
            }

            Given("user session") {
                every { userSessionInterface.userId } returns "1"
            }

            When("process get recent view") {
                cartListPresenter.processGetRecentViewData(emptyList())
            }

            Then("should try to stop firebase performance tracker") {
                verify {
                    view.setHasTriedToLoadRecentView()
                    view.stopAllCartPerformanceTrace()
                }
            }

        }

    }

})