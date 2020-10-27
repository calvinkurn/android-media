package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.domain.model.cartlist.CartItemData
import com.tokopedia.cart.domain.model.cartlist.CartListData
import com.tokopedia.cart.domain.model.cartlist.DeleteCartData
import com.tokopedia.cart.domain.usecase.DeleteCartUseCase
import com.tokopedia.cart.domain.usecase.GetCartListSimplifiedUseCase
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable

/**
 * Created by Irfan Khoirul on 2020-01-07.
 */

object CartListPresenterDeleteCartTest : Spek({

    val getCartListSimplifiedUseCase: GetCartListSimplifiedUseCase = mockk()
    val deleteCartUseCase: DeleteCartUseCase = mockk()
    val view: ICartListView = mockk(relaxed = true)

    Feature("delete cart item") {

        val cartListPresenter by memoized {
            PresenterProvider.provideCartListPresenter()
        }

        beforeEachTest {
            cartListPresenter.attachView(view)
        }

        Scenario("remove all cart data") {

            val emptyCartListData = CartListData()
            val deleteCartData = DeleteCartData(isSuccess = true)

            Given("success delete") {
                every { deleteCartUseCase.createObservable(any()) } returns Observable.just(deleteCartData)
            }

            Given("empty cart list data") {
                every { getCartListSimplifiedUseCase.createObservable(any()) } returns Observable.just(emptyCartListData)
            }

            When("process delete cart item") {
                val cartItemData = CartItemData()
                cartItemData.originData = CartItemData.OriginData()
                cartListPresenter.processDeleteCartItem(arrayListOf(cartItemData), arrayListOf(cartItemData), false, false)
            }

            Then("should render success") {
                verify {
                    view.onDeleteCartDataSuccess(arrayListOf("0"), true, false)
                }
            }
        }

        Scenario("remove some cart data") {

            val deleteCartData = DeleteCartData(isSuccess = true)
            val firstCartItemData = CartItemData().apply {
                originData = CartItemData.OriginData()
            }
            val secondCartItemData = CartItemData().apply {
                originData = CartItemData.OriginData()
                originData?.cartId = 1
            }

            Given("success delete") {
                every { deleteCartUseCase.createObservable(any()) } returns Observable.just(deleteCartData)
            }

            When("process delete cart item") {
                cartListPresenter.processDeleteCartItem(arrayListOf(firstCartItemData, secondCartItemData),
                        arrayListOf(firstCartItemData), false, false)
            }

            Then("should success delete") {
                verify {
                    view.onDeleteCartDataSuccess(arrayListOf("0"), false, false)
                }
            }
        }

        Scenario("remove some cart data and insurance data") {

            val deleteCartData = DeleteCartData(isSuccess = true)
            val firstCartItemData = CartItemData().apply {
                originData = CartItemData.OriginData()
            }
            val secondCartItemData = CartItemData().apply {
                originData = CartItemData.OriginData()
                originData?.cartId = 1
            }

            Given("success delete") {
                every { deleteCartUseCase.createObservable(any()) } returns Observable.just(deleteCartData)
            }

            When("process delete cart item") {
                cartListPresenter.processDeleteCartItem(arrayListOf(firstCartItemData, secondCartItemData),
                        arrayListOf(firstCartItemData), false, false)
            }

            Then("should success delete") {
                verify {
                    view.onDeleteCartDataSuccess(arrayListOf("0"), false, false)
                }
            }
        }

        Scenario("fail remove cart data") {

            val errorMessage = "fail testing delete"
            val deleteCartData = DeleteCartData(isSuccess = false, message = errorMessage)
            val cartItemData = CartItemData().apply {
                originData = CartItemData.OriginData()
            }

            Given("fail delete") {
                every { deleteCartUseCase.createObservable(any()) } returns Observable.just(deleteCartData)
            }

            When("process delete cart item") {
                cartListPresenter.processDeleteCartItem(arrayListOf(cartItemData), arrayListOf(cartItemData), false, false)
            }

            Then("should show error message") {
                verify {
                    view.showToastMessageRed(errorMessage)
                }
            }
        }

        Scenario("fail remove cart data with exception") {

            val cartItemData = CartItemData().apply {
                originData = CartItemData.OriginData()
            }
            val exception = CartResponseErrorException("fail testing delete")

            Given("fail delete") {
                every { deleteCartUseCase.createObservable(any()) } returns Observable.error(exception)
            }

            When("process delete cart item") {
                cartListPresenter.processDeleteCartItem(arrayListOf(cartItemData), arrayListOf(cartItemData), false, false)
            }

            Then("should show error message") {
                verify {
                    view.showToastMessageRed(exception)
                }
            }
        }

    }

})