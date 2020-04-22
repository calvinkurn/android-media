package com.tokopedia.cart.view.presenter

import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.domain.usecase.*
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.purchase_platform.common.feature.insurance.usecase.GetInsuranceCartUseCase
import com.tokopedia.purchase_platform.common.feature.insurance.usecase.RemoveInsuranceProductUsecase
import com.tokopedia.purchase_platform.common.feature.insurance.usecase.UpdateInsuranceProductDataUsecase
import com.tokopedia.cart.view.CartListPresenter
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.purchase_platform.common.feature.promo.domain.ValidateUsePromoRevampUseCase
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.seamless_login.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.data.source.cloud.model.Wishlist
import com.tokopedia.wishlist.common.response.GetWishlistResponse
import com.tokopedia.wishlist.common.response.WishlistDataResponse
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
 * Created by Irfan Khoirul on 2020-01-29.
 */

object CartListPresenterWishlistTest : Spek({

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

    Feature("get wishlist test") {

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

        Scenario("get wishlist success") {

            val response = GetWishlistResponse().apply {
                gqlWishList = WishlistDataResponse().apply {
                    wishlistDataList = mutableListOf<Wishlist>().apply {
                        add(Wishlist())
                    }
                }
            }

            Given("success response") {
                every { getWishlistUseCase.createObservable(any()) } returns Observable.just(response)
            }

            When("process get wishlist") {
                cartListPresenter.processGetWishlistData()
            }

            Then("should render wishlist") {
                verify {
                    view.renderWishlist(response.gqlWishList?.wishlistDataList)
                }
            }

            Then("should try to stop firebase performance tracker") {
                verify {
                    view.setHasTriedToLoadWishList()
                    view.stopAllCartPerformanceTrace()
                }
            }

        }

        Scenario("get wishlist empty") {

            val response = GetWishlistResponse().apply {
                gqlWishList = WishlistDataResponse().apply {
                    wishlistDataList = mutableListOf()
                }
            }

            Given("success response") {
                every { getWishlistUseCase.createObservable(any()) } returns Observable.just(response)
            }

            When("process get wishlist") {
                cartListPresenter.processGetWishlistData()
            }

            Then("should not render wishlist") {
                verify(inverse = true) {
                    view.renderWishlist(response.gqlWishList?.wishlistDataList)
                }
            }

            Then("should try to stop firebase performance tracker") {
                verify {
                    view.setHasTriedToLoadWishList()
                    view.stopAllCartPerformanceTrace()
                }
            }

        }

        Scenario("get wishlist error") {

            Given("error response") {
                every { getWishlistUseCase.createObservable(any()) } returns Observable.error(IllegalStateException())
            }

            When("process get wishlist") {
                cartListPresenter.processGetWishlistData()
            }

            Then("should try to stop firebase performance tracker") {
                verify {
                    view.setHasTriedToLoadWishList()
                    view.stopAllCartPerformanceTrace()
                }
            }

        }

    }

})