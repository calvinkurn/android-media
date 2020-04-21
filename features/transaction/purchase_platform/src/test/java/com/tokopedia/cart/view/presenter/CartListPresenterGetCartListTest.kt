package com.tokopedia.cart.view.presenter

import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.purchase_platform.common.feature.insurance.usecase.GetInsuranceCartUseCase
import com.tokopedia.purchase_platform.common.feature.insurance.usecase.RemoveInsuranceProductUsecase
import com.tokopedia.purchase_platform.common.feature.insurance.usecase.UpdateInsuranceProductDataUsecase
import com.tokopedia.purchase_platform.features.cart.data.model.response.recentview.GqlRecentView
import com.tokopedia.purchase_platform.features.cart.data.model.response.recentview.GqlRecentViewResponse
import com.tokopedia.purchase_platform.features.cart.data.model.response.recentview.RecentView
import com.tokopedia.cart.domain.model.cartlist.CartListData
import com.tokopedia.cart.view.CartListPresenter
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.purchase_platform.common.feature.promo.domain.ValidateUsePromoRevampUseCase
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.seamless_login.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.subscriptions.CompositeSubscription

/**
 * Created by Irfan Khoirul on 2020-01-07.
 */

object CartListPresenterGetCartListTest : Spek({

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
    val getInsuranceCartUseCase: GetInsuranceCartUseCase = mockk()
    val removeInsuranceProductUsecase: RemoveInsuranceProductUsecase = mockk()
    val updateInsuranceProductDataUsecase: UpdateInsuranceProductDataUsecase = mockk()
    val seamlessLoginUsecase: SeamlessLoginUsecase = mockk()
    val updateCartCounterUseCase: UpdateCartCounterUseCase = mockk()
    val view: ICartListView = mockk(relaxed = true)

    Feature("get cart list") {

        val cartListPresenter by memoized {
            CartListPresenter(
                    getCartListSimplifiedUseCase, deleteCartListUseCase,
                    updateCartUseCase, compositeSubscription,
                    addWishListUseCase, removeWishListUseCase, updateAndReloadCartUseCase,
                    userSessionInterface, clearCacheAutoApplyStackUseCase, getRecentViewUseCase,
                    getWishlistUseCase, getRecommendationUseCase, addToCartUseCase,
                    getInsuranceCartUseCase, removeInsuranceProductUsecase,
                    updateInsuranceProductDataUsecase, seamlessLoginUsecase,
                    updateCartCounterUseCase, updateCartAndValidateUseUseCase,
                    validateUsePromoRevampUseCase, TestSchedulers
            )
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