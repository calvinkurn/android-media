package com.tokopedia.cart.view.presenter

import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.domain.model.cartlist.AddCartToWishlistData
import com.tokopedia.cart.domain.usecase.*
import com.tokopedia.cart.view.CartListPresenter
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
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

object CartListPresenterAddCartToWishlistTest : Spek({

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

    Feature("add cart item to wishlist") {

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

        Scenario("success add cart item to wishlist") {

            val productId = "1"
            val cartId = "2"
            val isLastItem = false
            val source = "source"
            val forceExpandCollapsedUnavailableItems = false

            val addToCartWishlistData = AddCartToWishlistData().apply {
                isSuccess = true
                message = "success"
            }

            Given("mock add cart item to wishlist response") {
                every { addCartToWishlistUseCase.createObservable(any()) } returns Observable.just(addToCartWishlistData)
            }

            Given("mock update cart counter response") {
                every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(0)
            }

            When("process add cart item to wishlist") {
                cartListPresenter.processAddCartToWishlist(productId, cartId, isLastItem, source, forceExpandCollapsedUnavailableItems)
            }

            Then("should render success") {
                verify {
                    view.onAddCartToWishlistSuccess(addToCartWishlistData.message, productId, cartId, isLastItem, source, forceExpandCollapsedUnavailableItems)
                }
            }
        }

        Scenario("failed add cart item to wishlist") {

            val productId = "1"
            val cartId = "2"
            val isLastItem = false
            val source = "source"
            val forceExpandCollapsedUnavailableItems = false

            val addToCartWishlistData = AddCartToWishlistData().apply {
                isSuccess = false
                message = "failed"
            }

            Given("mock add cart item to wishlist response") {
                every { addCartToWishlistUseCase.createObservable(any()) } returns Observable.just(addToCartWishlistData)
            }

            Given("mock update cart counter response") {
                every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(0)
            }

            When("process add cart item to wishlist") {
                cartListPresenter.processAddCartToWishlist(productId, cartId, isLastItem, source, forceExpandCollapsedUnavailableItems)
            }

            Then("should render failed") {
                verify {
                    view.showToastMessageRed(addToCartWishlistData.message)
                }
            }
        }

        Scenario("failed add cart item to wishlist with exception") {

            val productId = "1"
            val cartId = "2"
            val isLastItem = false
            val source = "source"
            val forceExpandCollapsedUnavailableItems = false

            val exception = ResponseErrorException("Error")

            Given("mock add cart item to wishlist response") {
                every { addCartToWishlistUseCase.createObservable(any()) } returns Observable.error(exception)
            }

            Given("mock update cart counter response") {
                every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(0)
            }

            When("process add cart item to wishlist") {
                cartListPresenter.processAddCartToWishlist(productId, cartId, isLastItem, source, forceExpandCollapsedUnavailableItems)
            }

            Then("should render failed") {
                verify {
                    view.showToastMessageRed(exception)
                }
            }
        }
    }

})