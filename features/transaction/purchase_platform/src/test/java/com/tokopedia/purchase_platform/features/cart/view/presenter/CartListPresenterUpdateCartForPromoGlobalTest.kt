package com.tokopedia.purchase_platform.features.cart.view.presenter

import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.view.model.PromoStackingData
import com.tokopedia.purchase_platform.common.domain.schedulers.TestSchedulers
import com.tokopedia.purchase_platform.common.domain.usecase.GetInsuranceCartUseCase
import com.tokopedia.purchase_platform.common.domain.usecase.RemoveInsuranceProductUsecase
import com.tokopedia.purchase_platform.common.domain.usecase.UpdateInsuranceProductDataUsecase
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateCartData
import com.tokopedia.purchase_platform.features.cart.domain.usecase.*
import com.tokopedia.purchase_platform.features.cart.view.CartFragment
import com.tokopedia.purchase_platform.features.cart.view.CartListPresenter
import com.tokopedia.purchase_platform.features.cart.view.ICartListView
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.seamless_login.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyOrder
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.subscriptions.CompositeSubscription

/**
 * Created by Irfan Khoirul on 2020-01-08.
 */

object CartListPresenterUpdateCartForPromoGlobalTest : Spek({

    val getCartListSimplifiedUseCase: GetCartListSimplifiedUseCase = mockk()
    val deleteCartListUseCase: DeleteCartUseCase = mockk()
    val updateCartUseCase: UpdateCartUseCase = mockk()
    val checkPromoStackingCodeUseCase: CheckPromoStackingCodeUseCase = mockk()
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
    val getInsuranceCartUseCase: GetInsuranceCartUseCase = mockk()
    val removeInsuranceProductUsecase: RemoveInsuranceProductUsecase = mockk()
    val updateInsuranceProductDataUsecase: UpdateInsuranceProductDataUsecase = mockk()
    val seamlessLoginUsecase: SeamlessLoginUsecase = mockk()
    val view: ICartListView = mockk(relaxed = true)

    Feature("update cart list for promo global") {

        val cartListPresenter by memoized {
            CartListPresenter(
                    getCartListSimplifiedUseCase, deleteCartListUseCase, updateCartUseCase,
                    checkPromoStackingCodeUseCase, compositeSubscription, addWishListUseCase,
                    removeWishListUseCase, updateAndReloadCartUseCase, userSessionInterface,
                    clearCacheAutoApplyStackUseCase, getRecentViewUseCase, getWishlistUseCase,
                    getRecommendationUseCase, addToCartUseCase, getInsuranceCartUseCase,
                    removeInsuranceProductUsecase, updateInsuranceProductDataUsecase,
                    seamlessLoginUsecase, TestSchedulers
            )
        }

        beforeEachTest {
            cartListPresenter.attachView(view)
        }

        Scenario("success update cart and redirect to coupon list") {

            val updateCartData = UpdateCartData().apply {
                isSuccess = true
            }

            Given("update cart data") {
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartDataPromoGlobal(arrayListOf(), PromoStackingData(), CartFragment.GO_TO_LIST)
            }

            Then("should render success and redirect to coupon list") {
                verifyOrder {
                    view.hideProgressLoading()
                    view.goToCouponList()
                }
            }
        }

        Scenario("success update cart and redirect to coupon detail") {

            val updateCartData = UpdateCartData().apply {
                isSuccess = true
            }
            val promoStackingData = PromoStackingData()

            Given("update cart data") {
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartDataPromoGlobal(arrayListOf(), promoStackingData, CartFragment.GO_TO_DETAIL)
            }

            Then("should render success and redirect to coupon list") {
                verifyOrder {
                    view.hideProgressLoading()
                    view.goToDetailPromoStacking(promoStackingData)
                }
            }
        }

        Scenario("failed update cart") {

            val updateCartData = UpdateCartData().apply {
                isSuccess = false
                message = "Error"
            }

            Given("update cart data") {
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartDataPromoGlobal(arrayListOf(), PromoStackingData(), CartFragment.GO_TO_LIST)
            }

            Then("should show error") {
                verifyOrder {
                    view.hideProgressLoading()
                    view.showToastMessageRed(updateCartData.message)
                }
            }
        }

        Scenario("failed update cart with exception") {

            val exception = IllegalStateException()

            Given("update cart data") {
                every { updateCartUseCase.createObservable(any()) } returns Observable.error(exception)
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartDataPromoGlobal(arrayListOf(), PromoStackingData(), CartFragment.GO_TO_LIST)
            }

            Then("should show error") {
                verifyOrder {
                    view.hideProgressLoading()
                    view.showToastMessageRed(exception)
                }
            }
        }

    }

})