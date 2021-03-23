package com.tokopedia.cart.view.presenter

import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.domain.usecase.*
import com.tokopedia.cart.view.CartListPresenter
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.cart.view.uimodel.CartRecentViewItemHolderData
import com.tokopedia.cart.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.cart.view.uimodel.CartWishlistItemHolderData
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCartMapData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.subscriptions.CompositeSubscription

/**
 * Created by Irfan Khoirul on 2020-01-30.
 */

object CartListPresenterImpressionAnalyticsTest : Spek({

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

    Feature("generate recommendation impression data analytics") {

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

        Scenario("1 item selected and cart is not empty") {

            lateinit var result: Map<String, Any>

            val recommendationDataList = mutableListOf<CartRecommendationItemHolderData>().apply {
                add(CartRecommendationItemHolderData(recommendationItem = RecommendationItem()))
            }

            When("generate recommendation data analytics") {
                result = cartListPresenter.generateRecommendationImpressionDataAnalytics(0, recommendationDataList, false)
            }

            Then("should be containing 1 product") {
                val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as List<Map<String, Any>>
                assertEquals(1, impression.size)
            }

            Then("key `list` value should be `cart`") {
                val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as List<Map<String, Any>>
                assertTrue((impression[0][EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_CART_RECOMMENDATION)
            }

        }

        Scenario("1 item selected and cart is empty") {

            lateinit var result: Map<String, Any>

            val recommendationDataList = mutableListOf<CartRecommendationItemHolderData>().apply {
                add(CartRecommendationItemHolderData(recommendationItem = RecommendationItem()))
            }

            When("generate recommendation data analytics") {
                result = cartListPresenter.generateRecommendationImpressionDataAnalytics(0, recommendationDataList, true)
            }

            Then("should be containing 1 product") {
                val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as List<Map<String, Any>>
                assertEquals(1, impression.size)
            }

            Then("key `list` value should be `empty cart`") {
                val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as List<Map<String, Any>>
                assertTrue((impression[0][EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_CART_RECOMMENDATION_ON_EMPTY_CART)
            }

        }

    }

    Feature("generate wishlist impression data analytics") {

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

        Scenario("1 item selected and cart is not empty") {

            lateinit var result: Map<String, Any>

            val wishlistDataList = mutableListOf<CartWishlistItemHolderData>().apply {
                add(CartWishlistItemHolderData())
            }

            When("generate wishlist data analytics") {
                result = cartListPresenter.generateWishlistDataImpressionAnalytics(wishlistDataList, false)
            }

            Then("should be containing 1 product") {
                val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as List<Any>
                assertEquals(1, impression.size)
            }

            Then("key `list` value should be `cart`") {
                val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as List<Map<String, Any>>
                assertTrue((impression[0][EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_WISHLIST)
            }

        }

        Scenario("1 item selected and cart is empty") {

            lateinit var result: Map<String, Any>

            val wishlistDataList = mutableListOf<CartWishlistItemHolderData>().apply {
                add(CartWishlistItemHolderData())
            }

            When("generate wishlist data analytics") {
                result = cartListPresenter.generateWishlistDataImpressionAnalytics(wishlistDataList, true)
            }

            Then("should be containing 1 product") {
                val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as List<Any>
                assertEquals(1, impression.size)
            }

            Then("key `list` value should be `empty cart`") {
                val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as List<Map<String, Any>>
                assertTrue((impression[0][EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_WISHLIST_ON_EMPTY_CART)
            }

        }

    }

    Feature("generate recent view impression data analytics") {

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

        Scenario("1 item selected and cart is not empty") {

            lateinit var result: Map<String, Any>

            val recentViewDataList = mutableListOf<CartRecentViewItemHolderData>().apply {
                add(CartRecentViewItemHolderData())
            }

            When("generate recent view data analytics") {
                result = cartListPresenter.generateRecentViewDataImpressionAnalytics(recentViewDataList, false)
            }

            Then("should be containing 1 product") {
                val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as List<Any>
                assertEquals(1, impression.size)
            }

            Then("key `list` value should be `cart`") {
                val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as List<Map<String, Any>>
                assertTrue((impression[0][EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_RECENT_VIEW)
            }

        }

        Scenario("1 item selected and cart is empty") {

            lateinit var result: Map<String, Any>

            val recentViewDataList = mutableListOf<CartRecentViewItemHolderData>().apply {
                add(CartRecentViewItemHolderData())
            }

            When("generate recent view data analytics") {
                result = cartListPresenter.generateRecentViewDataImpressionAnalytics(recentViewDataList, true)
            }

            Then("should be containing 1 product") {
                val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as List<Any>
                assertEquals(1, impression.size)
            }

            Then("key `list` value should be `empty cart`") {
                val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as List<Map<String, Any>>
                assertTrue((impression[0][EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_RECENT_VIEW_ON_EMPTY_CART)
            }

        }

    }

})