package com.tokopedia.cart.bundle.view.presenter

import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.bundle.domain.model.cartlist.CartItemData
import com.tokopedia.cart.bundle.domain.model.cartlist.CartListData
import com.tokopedia.cart.bundle.domain.model.updatecart.UpdateAndReloadCartListData
import com.tokopedia.cart.bundle.domain.usecase.*
import com.tokopedia.cart.bundle.view.CartListPresenter
import com.tokopedia.cart.bundle.view.ICartListView
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UndoDeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
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

object CartListPresenterUpdateAndReloadCartTest : Spek({

    val getCartRevampV3UseCase: GetCartRevampV3UseCase = mockk()
    val deleteCartUseCase: DeleteCartUseCase = mockk()
    val undoDeleteCartUseCase: UndoDeleteCartUseCase = mockk()
    val addCartToWishlistUseCase: AddCartToWishlistUseCase = mockk()
    val updateCartUseCase: UpdateCartUseCase = mockk()
    val updateCartAndValidateUseUseCase: UpdateCartAndValidateUseUseCase = mockk()
    val validateUsePromoRevampUseCase: ValidateUsePromoRevampUseCase = mockk()
    val compositeSubscription = CompositeSubscription()
    val addWishListUseCase: AddWishListUseCase = mockk()
    val removeWishListUseCase: RemoveWishListUseCase = mockk()
    val updateAndReloadCartUseCase: UpdateAndReloadCartUseCase = mockk()
    val userSessionInterface: UserSessionInterface = mockk()
    val clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase = mockk()
    val getRecentViewUseCase: GetRecommendationUseCase = mockk()
    val getWishlistUseCase: GetWishlistUseCase = mockk()
    val getRecommendationUseCase: GetRecommendationUseCase = mockk()
    val addToCartUseCase: AddToCartUseCase = mockk()
    val addToCartExternalUseCase: AddToCartExternalUseCase = mockk()
    val seamlessLoginUsecase: SeamlessLoginUsecase = mockk()
    val updateCartCounterUseCase: UpdateCartCounterUseCase = mockk()
    val setCartlistCheckboxStateUseCase: SetCartlistCheckboxStateUseCase = mockk()
    val followShopUseCase: FollowShopUseCase = mockk()
    val view: ICartListView = mockk(relaxed = true)

    Feature("update and reload cart list") {

        val cartListPresenter by memoized {
            CartListPresenter(
                    getCartRevampV3UseCase, deleteCartUseCase, undoDeleteCartUseCase,
                    updateCartUseCase, compositeSubscription, addWishListUseCase,
                    addCartToWishlistUseCase, removeWishListUseCase, updateAndReloadCartUseCase,
                    userSessionInterface, clearCacheAutoApplyStackUseCase, getRecentViewUseCase,
                    getWishlistUseCase, getRecommendationUseCase, addToCartUseCase,
                    addToCartExternalUseCase, seamlessLoginUsecase, updateCartCounterUseCase,
                    updateCartAndValidateUseUseCase, validateUsePromoRevampUseCase, setCartlistCheckboxStateUseCase,
                    followShopUseCase, TestSchedulers
            )
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
                every { getCartRevampV3UseCase.buildParams(any(), any()) } returns emptyMap()
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
                every { getCartRevampV3UseCase.buildParams(any(), any()) } returns emptyMap()
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