package com.tokopedia.cart.view.presenter

import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.domain.model.cartlist.CartItemData
import com.tokopedia.cart.domain.model.cartlist.UpdateAndValidateUseData
import com.tokopedia.cart.domain.model.cartlist.UpdateCartData
import com.tokopedia.cart.domain.usecase.*
import com.tokopedia.cart.view.CartListPresenter
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.feature.insurance.usecase.GetInsuranceCartUseCase
import com.tokopedia.purchase_platform.common.feature.insurance.usecase.RemoveInsuranceProductUsecase
import com.tokopedia.purchase_platform.common.feature.insurance.usecase.UpdateInsuranceProductDataUsecase
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.seamless_login.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.subscriptions.CompositeSubscription

object CartListPresenterUpdateCartAndValidateUseTest : Spek({

    val getCartListSimplifiedUseCase: GetCartListSimplifiedUseCase = mockk()
    val deleteCartListUseCase: DeleteCartUseCase = mockk()
    val updateCartUseCase: UpdateCartUseCase = mockk()
    val updateCartAndValidateUseUseCase: UpdateCartAndValidateUseUseCase = mockk()
    val validateUsePromoRevampUseCase: ValidateUsePromoRevampUseCase = mockk()
    val compositeSubscription = CompositeSubscription()
    val addWishListUseCase: AddWishListUseCase = mockk()
    val removeWishListUseCase: RemoveWishListUseCase = mockk()
    val updateAndReloadCartUseCase: UpdateAndReloadCartUseCase = mockk()
    val userSessionInterface: UserSessionInterface = mockk()
    val clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase = mockk()
    val getRecentViewUseCase: GetRecentViewUseCase = mockk()
    val getWishlistUseCase: GetWishlistUseCase = mockk()
    val getRecommendationUseCase: GetRecommendationUseCase = mockk()
    val addToCartUseCase: AddToCartUseCase = mockk()
    val addToCartExternalUseCase: AddToCartExternalUseCase = mockk()
    val getInsuranceCartUseCase: GetInsuranceCartUseCase = mockk()
    val removeInsuranceProductUsecase: RemoveInsuranceProductUsecase = mockk()
    val updateInsuranceProductDataUsecase: UpdateInsuranceProductDataUsecase = mockk()
    val seamlessLoginUsecase: SeamlessLoginUsecase = mockk()
    val updateCartCounterUseCase: UpdateCartCounterUseCase = mockk()
    val view: ICartListView = mockk(relaxed = true)

    Feature("update cart and validate use for promo action") {

        val cartListPresenter by memoized {
            CartListPresenter(
                    getCartListSimplifiedUseCase, deleteCartListUseCase, updateCartUseCase,
                    compositeSubscription, addWishListUseCase, removeWishListUseCase,
                    updateAndReloadCartUseCase, userSessionInterface, clearCacheAutoApplyStackUseCase,
                    getRecentViewUseCase, getWishlistUseCase, getRecommendationUseCase,
                    addToCartUseCase, addToCartExternalUseCase, getInsuranceCartUseCase,
                    removeInsuranceProductUsecase, updateInsuranceProductDataUsecase, seamlessLoginUsecase,
                    updateCartCounterUseCase, updateCartAndValidateUseUseCase, validateUsePromoRevampUseCase,
                    TestSchedulers
            )
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