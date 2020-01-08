package com.tokopedia.purchase_platform.features.cart.view.presenter

import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
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
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.subscriptions.CompositeSubscription

/**
 * Created by Irfan Khoirul on 2020-01-08.
 */

class CartListPresenterUpdateCartForPromoGlobalTest : Spek({

    val getCartListSimplifiedUseCase: GetCartListSimplifiedUseCase = mockk()
    val deleteCartListUseCase: DeleteCartUseCase = mockk()
    val updateCartUseCase: UpdateCartUseCase = mockk()
    val checkPromoStackingCodeUseCase: CheckPromoStackingCodeUseCase = mockk()
    val checkPromoStackingCodeMapper: CheckPromoStackingCodeMapper = mockk()
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
                    checkPromoStackingCodeUseCase, checkPromoStackingCodeMapper, compositeSubscription,
                    addWishListUseCase, removeWishListUseCase, updateAndReloadCartUseCase,
                    userSessionInterface, clearCacheAutoApplyStackUseCase, getRecentViewUseCase,
                    getWishlistUseCase, getRecommendationUseCase, addToCartUseCase, getInsuranceCartUseCase,
                    removeInsuranceProductUsecase, updateInsuranceProductDataUsecase, seamlessLoginUsecase, TestSchedulers
            )
        }

        Scenario("success update cart and redirect to coupon list") {

            val updateCartData = UpdateCartData()

            Given("update cart data") {
                updateCartData.isSuccess = true
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartDataPromoGlobal(arrayListOf(), PromoStackingData(), CartFragment.GO_TO_LIST)
            }

            Then("should render success and redirect to coupon list") {
                verify {
                    view.hideProgressLoading()
                    view.goToCouponList()
                }
            }
        }

        Scenario("success update cart and redirect to coupon detail") {

            val updateCartData = UpdateCartData()
            val promoStackingData = PromoStackingData()

            Given("update cart data") {
                updateCartData.isSuccess = true
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartDataPromoGlobal(arrayListOf(), promoStackingData, 0)
            }

            Then("should render success and redirect to coupon list") {
                verify {
                    view.hideProgressLoading()
                    view.goToDetailPromoStacking(promoStackingData)
                }
            }
        }

        Scenario("failed update cart") {

            val updateCartData = UpdateCartData()

            Given("update cart data") {
                updateCartData.isSuccess = false
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartDataPromoGlobal(arrayListOf(), PromoStackingData(), 0)
            }

            Then("should show error") {
                verify {
                    view.hideProgressLoading()
                    view.showToastMessageRed(any())
                }
            }
        }

        Scenario("failed update cart with exception") {

            val errorMessage = "Error"

            Given("update cart data") {
                every { updateCartUseCase.createObservable(any()) } returns Observable.error(IllegalStateException(errorMessage))
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartDataPromoGlobal(arrayListOf(), PromoStackingData(), 0)
            }

            Then("should show error") {
                verify {
                    view.hideProgressLoading()
                    view.showToastMessageRed(any())
                }
            }
        }

    }

})