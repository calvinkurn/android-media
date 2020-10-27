package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.domain.model.cartlist.UndoDeleteCartData
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.cart.view.presenter.PresenterProvider.undoDeleteCartUseCase
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable

object CartListPresenterUndoDeleteCartTest : Spek({

    val view: ICartListView = mockk(relaxed = true)

    Feature("undo delete cart test") {

        val cartListPresenter by memoized {
            PresenterProvider.provideCartListPresenter()
        }

        beforeEachTest {
            cartListPresenter.attachView(view)
        }

        Scenario("undo delete cart success") {

            val response = UndoDeleteCartData().apply {
                isSuccess = true
                message = "Berhasil"
            }

            Given("success response") {
                every { undoDeleteCartUseCase.createObservable(any()) } returns Observable.just(response)
            }

            When("process undo delete") {
                cartListPresenter.processUndoDeleteCartItem(listOf("123"))
            }

            Then("should render success") {
                verify {
                    view.onUndoDeleteCartDataSuccess(response)
                }
            }

        }

        Scenario("undo delete cart failed") {

            val messageError = "Gagal"

            val response = UndoDeleteCartData().apply {
                isSuccess = false
                message = messageError
            }

            Given("error response") {
                every { undoDeleteCartUseCase.createObservable(any()) } returns Observable.just(response)
            }

            When("process undo delete") {
                cartListPresenter.processUndoDeleteCartItem(listOf("123"))
            }

            Then("should render error") {
                verify {
                    view.showToastMessageRed(messageError)
                }
            }

        }

        Scenario("undo delete cart failed with exception") {

            val exception = CartResponseErrorException()

            Given("error response") {
                every { undoDeleteCartUseCase.createObservable(any()) } returns Observable.error(exception)
            }

            When("process undo delete") {
                cartListPresenter.processUndoDeleteCartItem(listOf("123"))
            }

            Then("should render error") {
                verify {
                    view.showToastMessageRed(exception)
                }
            }

        }

    }

})