package com.tokopedia.cart.view.presenter

import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.domain.usecase.*
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.purchase_platform.common.feature.insurance.usecase.GetInsuranceCartUseCase
import com.tokopedia.purchase_platform.common.feature.insurance.usecase.RemoveInsuranceProductUsecase
import com.tokopedia.purchase_platform.common.feature.insurance.usecase.UpdateInsuranceProductDataUsecase
import com.tokopedia.cart.domain.model.cartlist.CartItemData
import com.tokopedia.cart.domain.model.cartlist.CartListData
import com.tokopedia.cart.domain.model.cartlist.DeleteCartData
import com.tokopedia.cart.view.CartListPresenter
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
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
 * Created by Irfan Khoirul on 2020-01-07.
 */

object CartListPresenterDeleteCartTest : Spek({

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

    Feature("delete cart item") {

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

        Scenario("remove all cart data") {

            val emptyCartListData = CartListData()
            val deleteCartData = DeleteCartData(isSuccess = true)

            Given("success delete") {
                every { deleteCartListUseCase.createObservable(any()) } returns Observable.just(deleteCartData)
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
                    view.renderInitialGetCartListDataSuccess(emptyCartListData)
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
                every { deleteCartListUseCase.createObservable(any()) } returns Observable.just(deleteCartData)
            }

            When("process delete cart item") {
                cartListPresenter.processDeleteCartItem(arrayListOf(firstCartItemData, secondCartItemData),
                        arrayListOf(firstCartItemData), false, false)
            }

            Then("should success delete") {
                verify {
                    view.onDeleteCartDataSuccess(arrayListOf("0"))
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
                every { deleteCartListUseCase.createObservable(any()) } returns Observable.just(deleteCartData)
            }

            When("process delete cart item") {
                cartListPresenter.processDeleteCartItem(arrayListOf(firstCartItemData, secondCartItemData),
                        arrayListOf(firstCartItemData), false, false)
            }

            Then("should success delete") {
                verify {
                    view.onDeleteCartDataSuccess(arrayListOf("0"))
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
                every { deleteCartListUseCase.createObservable(any()) } returns Observable.just(deleteCartData)
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
                every { deleteCartListUseCase.createObservable(any()) } returns Observable.error(exception)
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