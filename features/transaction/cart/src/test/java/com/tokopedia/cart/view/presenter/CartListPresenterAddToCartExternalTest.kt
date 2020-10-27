package com.tokopedia.cart.view.presenter

import com.tokopedia.atc_common.domain.model.response.atcexternal.AddToCartExternalModel
import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.network.exception.MessageErrorException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyOrder
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable

/**
 * Created by Irfan Khoirul on 2020-01-07.
 */

object CartListPresenterAddToCartExternalTest : Spek({

    val addToCartExternalUseCase: AddToCartExternalUseCase = mockk()
    val view: ICartListView = mockk(relaxed = true)

    Feature("add to cart") {

        val cartListPresenter by memoized {
            PresenterProvider.provideCartListPresenter()
        }

        beforeEachTest {
            cartListPresenter.attachView(view)
        }

        Scenario("success add to cart") {

            val addToCartExternalModel = AddToCartExternalModel().apply {
                success = 1
                message = arrayListOf<String>().apply {
                    add("Success message")
                }
            }

            Given("add to cart data") {
                every { addToCartExternalUseCase.createObservable(any()) } returns Observable.just(addToCartExternalModel)
            }

            When("process to add to cart") {
                cartListPresenter.processAddToCartExternal(1)
            }

            Then("should render success") {
                verifyOrder {
                    view.hideProgressLoading()
                    view.showToastMessageGreen(addToCartExternalModel.message[0])
                    view.refreshCart()
                }
            }
        }

        Scenario("failed add to cart") {

            val errorMessage = "Error message"
            val exception = MessageErrorException(errorMessage)

            Given("add to cart data") {
                every { addToCartExternalUseCase.createObservable(any()) } returns Observable.error(exception)
            }

            When("process to update cart data") {
                cartListPresenter.processAddToCartExternal(1)
            }

            Then("should show error") {
                verifyOrder {
                    view.hideProgressLoading()
                    view.showToastMessageRed(exception)
                    view.refreshCart()
                }
            }
        }

    }

})