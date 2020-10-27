package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.domain.usecase.UpdateCartAndValidateUseUseCase
import com.tokopedia.cart.domain.model.cartlist.CartItemData
import com.tokopedia.cart.domain.model.updatecart.UpdateAndValidateUseData
import com.tokopedia.cart.domain.model.updatecart.UpdateCartData
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable

object CartListPresenterUpdateCartAndValidateUseTest : Spek({

    val updateCartAndValidateUseUseCase: UpdateCartAndValidateUseUseCase = mockk()
    val view: ICartListView = mockk(relaxed = true)

    Feature("update cart and validate use for promo action") {

        val cartListPresenter by memoized {
            PresenterProvider.provideCartListPresenter()
        }

        beforeEachTest {
            cartListPresenter.attachView(view)
        }

        Scenario("success update and validate use") {

            val cartItemDataList = ArrayList<CartItemData>().apply {
                add(CartItemData(isError = false))
            }

            val updateAndValidateUseData = UpdateAndValidateUseData().apply {
                updateCartData = UpdateCartData().apply {
                    isSuccess = true
                }
                promoUiModel = PromoUiModel()
            }

            Given("update and validate use data") {
                every { updateCartAndValidateUseUseCase.createObservable(any()) } returns Observable.just(updateAndValidateUseData)
            }

            Given("shop data list") {
                every { view.getAllSelectedCartDataList() } answers { cartItemDataList }
            }

            When("process to update and validate use data") {
                cartListPresenter.doUpdateCartAndValidateUse(ValidateUsePromoRequest())
            }

            Then("should render promo button") {
                verify {
                    view.updatePromoCheckoutStickyButton(updateAndValidateUseData.promoUiModel!!)
                }
            }
        }

        Scenario("failed update and validate use with exception") {

            val exception = CartResponseErrorException("error message")

            Given("update and validate use data") {
                every { updateCartAndValidateUseUseCase.createObservable(any()) } returns Observable.error(exception)
            }

            When("process to update and validate use data") {
                cartListPresenter.doUpdateCartAndValidateUse(ValidateUsePromoRequest())
            }

            Then("should render promo button state active default") {
                verify {
                    view.renderPromoCheckoutButtonActiveDefault(emptyList())
                }
            }
        }

        Scenario("failed update cart because data is empty") {

            Given("shop data list") {
                every { view.getAllSelectedCartDataList() } answers { emptyList() }
            }

            When("process to update and validate use data") {
                cartListPresenter.doUpdateCartAndValidateUse(ValidateUsePromoRequest())
            }

            Then("should hide progress loading") {
                verify {
                    view.hideProgressLoading()
                }
            }
        }

    }

})