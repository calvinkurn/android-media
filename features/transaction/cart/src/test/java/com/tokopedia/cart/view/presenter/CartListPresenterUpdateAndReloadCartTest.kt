package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.domain.model.cartlist.CartItemData
import com.tokopedia.cart.domain.model.cartlist.CartListData
import com.tokopedia.cart.domain.model.updatecart.UpdateAndReloadCartListData
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.cart.view.presenter.PresenterProvider.updateAndReloadCartUseCase
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable

/**
 * Created by Irfan Khoirul on 2020-01-08.
 */

object CartListPresenterUpdateAndReloadCartTest : Spek({

    val view: ICartListView = mockk(relaxed = true)

    Feature("update and reload cart list") {

        val cartListPresenter by memoized {
            PresenterProvider.provideCartListPresenter()
        }

        beforeEachTest {
            cartListPresenter.attachView(view)
        }

        Scenario("success update and reload empty cart") {

            val emptyCartListData = UpdateAndReloadCartListData()

            Given("empty data") {
                every { updateAndReloadCartUseCase.createObservable(any()) } returns Observable.just(emptyCartListData)
            }

            When("process to update and reload cart data") {
                cartListPresenter.processToUpdateAndReloadCartData("0")
            }

            Then("should hide loading") {
                verify {
                    view.hideProgressLoading()
                }
            }
        }

        Scenario("success update and reload cart") {

            val cartItemData = CartItemData().apply {
                updatedData = CartItemData.UpdatedData().apply {
                    remark = ""
                }
                originData = CartItemData.OriginData()
            }
            val updateAndReloadCartListData = UpdateAndReloadCartListData().apply {
                cartListData = CartListData()
            }

            Given("cart data") {
                every { updateAndReloadCartUseCase.createObservable(any()) } returns Observable.just(updateAndReloadCartListData)
            }

            Given("all available cart data") {
                every { view.getAllAvailableCartDataList() } returns arrayListOf(cartItemData)
            }

            When("process to update and reload cart data") {
                cartListPresenter.processToUpdateAndReloadCartData("0")
            }

            Then("should render success") {
                verifyOrder {
                    view.renderLoadGetCartDataFinish()
                    view.renderInitialGetCartListDataSuccess(updateAndReloadCartListData.cartListData)
                }
            }
        }

        Scenario("failed update and reload cart with exception") {

            val exception = CartResponseErrorException("error message")
            val cartItemData = CartItemData().apply {
                originData = CartItemData.OriginData()
                updatedData = CartItemData.UpdatedData().apply {
                    remark = ""
                }
            }

            Given("cart data") {
                every { updateAndReloadCartUseCase.createObservable(any()) } returns Observable.error(exception)
            }

            Given("all available cart data") {
                every { view.getAllAvailableCartDataList() } returns arrayListOf(cartItemData)
            }

            When("process to update and reload cart data") {
                cartListPresenter.processToUpdateAndReloadCartData("0")
            }

            Then("should render success") {
                verify {
                    view.showToastMessageRed(exception)
                }
            }
        }

    }

})