package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.domain.model.cartlist.CartItemData
import com.tokopedia.cart.domain.model.updatecart.UpdateCartData
import com.tokopedia.cart.domain.usecase.UpdateCartUseCase
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable

object CartListPresenterUpdateCartForPromoTest : Spek({

    val updateCartUseCase: UpdateCartUseCase = mockk()
    val view: ICartListView = mockk(relaxed = true)

    Feature("update cart list for promo action") {

        val cartListPresenter by memoized {
            PresenterProvider.provideCartListPresenter()
        }

        beforeEachTest {
            cartListPresenter.attachView(view)
        }

        Scenario("success update cart") {

            val updateCartData = UpdateCartData().apply {
                isSuccess = true
            }

            val cartItemDataList = mutableListOf<CartItemData>().apply {
                add(CartItemData().apply {
                    originData = CartItemData.OriginData().apply {
                        isCod = true
                        pricePlan = 1000.0
                    }
                    updatedData = CartItemData.UpdatedData().apply {
                        quantity = 10
                    }
                })
            }

            Given("shop data list") {
                every { view.getAllSelectedCartDataList() } answers { cartItemDataList }
            }

            Given("update cart data") {
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            When("process to update cart data") {
                cartListPresenter.doUpdateCartForPromo()
            }

            Then("should render success") {
                verify {
                    view.hideProgressLoading()
                    view.navigateToPromoRecommendation()
                }
            }
        }

        Scenario("failed update cart") {

            val updateCartData = UpdateCartData().apply {
                isSuccess = false
                message = "Error"
            }

            val cartItemDataList = mutableListOf<CartItemData>().apply {
                add(CartItemData().apply {
                    originData = CartItemData.OriginData().apply {
                        isCod = true
                        pricePlan = 1000.0
                    }
                    updatedData = CartItemData.UpdatedData().apply {
                        quantity = 10
                    }
                })
            }

            Given("shop data list") {
                every { view.getAllSelectedCartDataList() } answers { cartItemDataList }
            }

            Given("update cart data") {
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            When("process to update cart data") {
                cartListPresenter.doUpdateCartForPromo()
            }

            Then("should render error") {
                verify {
                    view.hideProgressLoading()
                    view.renderErrorToShipmentForm(updateCartData.message)
                }
            }
        }

        Scenario("failed update cart with exception") {

            val exception = CartResponseErrorException("error message")

            val cartItemDataList = mutableListOf<CartItemData>().apply {
                add(CartItemData().apply {
                    originData = CartItemData.OriginData().apply {
                        isCod = true
                        pricePlan = 1000.0
                    }
                    updatedData = CartItemData.UpdatedData().apply {
                        quantity = 10
                    }
                })
            }

            Given("shop data list") {
                every { view.getAllSelectedCartDataList() } answers { cartItemDataList }
            }

            Given("update cart data") {
                every { updateCartUseCase.createObservable(any()) } returns Observable.error(exception)
            }

            When("process to update cart data") {
                cartListPresenter.doUpdateCartForPromo()
            }

            Then("should render error") {
                verify {
                    view.showToastMessageRed(exception)
                }
            }
        }

        Scenario("failed update cart because data is empty") {

            Given("shop data list") {
                every { view.getAllSelectedCartDataList() } answers { emptyList() }
            }

            When("process to update cart data") {
                cartListPresenter.doUpdateCartForPromo()
            }

            Then("should hide progress loading") {
                verify {
                    view.hideProgressLoading()
                }
            }
        }
    }

})