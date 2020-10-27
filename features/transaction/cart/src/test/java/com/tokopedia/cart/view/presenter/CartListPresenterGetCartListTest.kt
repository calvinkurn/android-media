package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.data.model.response.recentview.GqlRecentViewResponse
import com.tokopedia.cart.domain.model.cartlist.CartListData
import com.tokopedia.cart.domain.usecase.GetCartListSimplifiedUseCase
import com.tokopedia.cart.domain.usecase.GetRecentViewUseCase
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.network.exception.ResponseErrorException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable

/**
 * Created by Irfan Khoirul on 2020-01-07.
 */

object CartListPresenterGetCartListTest : Spek({

    val getCartListSimplifiedUseCase: GetCartListSimplifiedUseCase = mockk()
    val getRecentViewUseCase: GetRecentViewUseCase = mockk()
    val view: ICartListView = mockk(relaxed = true)

    Feature("get cart list") {

        val cartListPresenter by memoized {
            PresenterProvider.provideCartListPresenter()
        }

        beforeEachTest {
            cartListPresenter.attachView(view)
        }

        Scenario("initial load success") {

            val emptyCartListData = CartListData()

            Given("empty response") {
                every { getCartListSimplifiedUseCase.createObservable(any()) } returns Observable.just(emptyCartListData)
            }

            When("process initial get cart data") {
                cartListPresenter.processInitialGetCartData("", true, false)
            }

            Then("should render success") {
                verify {
                    view.renderInitialGetCartListDataSuccess(emptyCartListData)
                }
            }

            Then("should render then finish loading") {
                verify {
                    view.renderLoadGetCartData()
                    view.renderLoadGetCartDataFinish()
                }
            }
        }

        Scenario("refresh load success") {

            val emptyCartListData = CartListData()

            Given("empty response") {
                every { getCartListSimplifiedUseCase.createObservable(any()) } returns Observable.just(emptyCartListData)
            }

            When("process initial get cart data") {
                cartListPresenter.processInitialGetCartData("", false, false)
            }

            Then("should render success") {
                verify {
                    view.renderInitialGetCartListDataSuccess(emptyCartListData)
                }
            }

            Then("should show then hide progress loading") {
                verifyOrder {
                    view.showProgressLoading()
                    view.hideProgressLoading()
                }
            }
        }

        Scenario("initial load failed") {

            val exception = ResponseErrorException("Terjadi kesalahan pada server. Ulangi beberapa saat lagi")

            Given("throw error") {
                every { getCartListSimplifiedUseCase.createObservable(any()) } returns Observable.error(exception)
                every { getRecentViewUseCase.createObservable(any()) } answers { Observable.just(GqlRecentViewResponse()) }
            }

            When("process initial get cart data") {
                cartListPresenter.processInitialGetCartData("", true, true)
            }

            Then("should render error") {
                verify {
                    view.renderErrorInitialGetCartListData(exception)
                }
            }
        }

        Scenario("refresh load failed") {

            val exception = ResponseErrorException("Terjadi kesalahan pada server. Ulangi beberapa saat lagi")

            Given("throw error") {
                every { getCartListSimplifiedUseCase.createObservable(any()) } returns Observable.error(exception)
                every { getRecentViewUseCase.createObservable(any()) } answers { Observable.just(GqlRecentViewResponse()) }
            }

            When("process initial get cart data") {
                cartListPresenter.processInitialGetCartData("", false, true)
            }

            Then("should render error") {
                verify {
                    view.renderErrorInitialGetCartListData(exception)
                }
            }
        }

    }

})