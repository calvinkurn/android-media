package com.tokopedia.cart.view.presenter

import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.domain.model.cartlist.CartItemData
import com.tokopedia.cart.domain.model.updatecart.UpdateCartData
import com.tokopedia.cart.domain.usecase.*
import com.tokopedia.cart.view.CartListPresenter
import com.tokopedia.cart.view.ICartListView
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
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.subscriptions.CompositeSubscription

object CartListPresenterUpdateCartForPromoTest : Spek({

    val getCartListSimplifiedUseCase: GetCartListSimplifiedUseCase = mockk()
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

    Feature("update cart list for promo action") {

        val cartListPresenter by memoized {
            CartListPresenter(
                    getCartListSimplifiedUseCase, deleteCartUseCase, undoDeleteCartUseCase,
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

        Scenario("success update cart") {

            val updateCartData = UpdateCartData().apply {
                isSuccess = true
            }

            val cartItemDataList = mutableListOf<CartItemData>().apply {
                add(CartItemData().apply {
                    originData = CartItemData.OriginData().apply {
                        isCod = true
                        pricePlan = 1000.0
                    }
                    updatedData = CartItemData.UpdatedData().apply {
                        quantity = 10
                    }
                })
            }

            Given("shop data list") {
                every { view.getAllSelectedCartDataList() } answers { cartItemDataList }
            }

            Given("update cart data") {
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            When("process to update cart data") {
                cartListPresenter.doUpdateCartForPromo()
            }

            Then("should render success") {
                verify {
                    view.hideProgressLoading()
                    view.navigateToPromoRecommendation()
                }
            }
        }

        Scenario("failed update cart") {

            val updateCartData = UpdateCartData().apply {
                isSuccess = false
                message = "Error"
            }

            val cartItemDataList = mutableListOf<CartItemData>().apply {
                add(CartItemData().apply {
                    originData = CartItemData.OriginData().apply {
                        isCod = true
                        pricePlan = 1000.0
                    }
                    updatedData = CartItemData.UpdatedData().apply {
                        quantity = 10
                    }
                })
            }

            Given("shop data list") {
                every { view.getAllSelectedCartDataList() } answers { cartItemDataList }
            }

            Given("update cart data") {
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            When("process to update cart data") {
                cartListPresenter.doUpdateCartForPromo()
            }

            Then("should render error") {
                verify {
                    view.hideProgressLoading()
                    view.renderErrorToShipmentForm(updateCartData.message)
                }
            }
        }

        Scenario("failed update cart with exception") {

            val exception = CartResponseErrorException("error message")

            val cartItemDataList = mutableListOf<CartItemData>().apply {
                add(CartItemData().apply {
                    originData = CartItemData.OriginData().apply {
                        isCod = true
                        pricePlan = 1000.0
                    }
                    updatedData = CartItemData.UpdatedData().apply {
                        quantity = 10
                    }
                })
            }

            Given("shop data list") {
                every { view.getAllSelectedCartDataList() } answers { cartItemDataList }
            }

            Given("update cart data") {
                every { updateCartUseCase.createObservable(any()) } returns Observable.error(exception)
            }

            When("process to update cart data") {
                cartListPresenter.doUpdateCartForPromo()
            }

            Then("should render error") {
                verify {
                    view.showToastMessageRed(exception)
                }
            }
        }

        Scenario("failed update cart because data is empty") {

            Given("shop data list") {
                every { view.getAllSelectedCartDataList() } answers { emptyList() }
            }

            When("process to update cart data") {
                cartListPresenter.doUpdateCartForPromo()
            }

            Then("should hide progress loading") {
                verify {
                    view.hideProgressLoading()
                }
            }
        }
    }

})