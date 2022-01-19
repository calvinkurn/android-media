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
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UndoDeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.logisticcart.boaffordability.usecase.BoAffordabilityUseCase
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCartMapData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceRecomProductCartMapData
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.extension.LABEL_FULFILLMENT
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationLabel
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.subscriptions.CompositeSubscription

@ExperimentalCoroutinesApi
object CartListPresenterImpressionAnalyticsTest : Spek({

    val getCartRevampV3UseCase: GetCartRevampV3UseCase = mockk()
    val deleteCartUseCase: DeleteCartUseCase = mockk()
    val undoDeleteCartUseCase: UndoDeleteCartUseCase = mockk()
    val addCartToWishlistUseCase: AddCartToWishlistUseCase = mockk()
    val updateCartUseCase: UpdateCartUseCase = mockk()
    val updateCartAndValidateUseUseCase: UpdateCartAndValidateUseUseCase = mockk()
    val validateUsePromoRevampUseCase: OldValidateUsePromoRevampUseCase = mockk()
    val compositeSubscription = CompositeSubscription()
    val addWishListUseCase: AddWishListUseCase = mockk()
    val removeWishListUseCase: RemoveWishListUseCase = mockk()
    val updateAndReloadCartUseCase: UpdateAndReloadCartUseCase = mockk()
    val userSessionInterface: UserSessionInterface = mockk()
    val clearCacheAutoApplyStackUseCase: OldClearCacheAutoApplyStackUseCase = mockk()
    val getRecentViewUseCase: GetRecommendationUseCase = mockk()
    val getWishlistUseCase: GetWishlistUseCase = mockk()
    val getRecommendationUseCase: GetRecommendationUseCase = mockk()
    val addToCartUseCase: AddToCartUseCase = mockk()
    val addToCartExternalUseCase: AddToCartExternalUseCase = mockk()
    val seamlessLoginUsecase: SeamlessLoginUsecase = mockk()
    val updateCartCounterUseCase: UpdateCartCounterUseCase = mockk()
    val setCartlistCheckboxStateUseCase: SetCartlistCheckboxStateUseCase = mockk()
    val followShopUseCase: FollowShopUseCase = mockk()
    val boAffordabilityUseCase: BoAffordabilityUseCase = mockk()
    val view: ICartListView = mockk(relaxed = true)

    Feature("generate recommendation impression data analytics") {

        val cartListPresenter by memoized {
            CartListPresenter(
                    getCartRevampV3UseCase, deleteCartUseCase, undoDeleteCartUseCase,
                    updateCartUseCase, compositeSubscription, addWishListUseCase,
                    addCartToWishlistUseCase, removeWishListUseCase, updateAndReloadCartUseCase,
                    userSessionInterface, clearCacheAutoApplyStackUseCase, getRecentViewUseCase,
                    getWishlistUseCase, getRecommendationUseCase, addToCartUseCase,
                    addToCartExternalUseCase, seamlessLoginUsecase, updateCartCounterUseCase,
                    updateCartAndValidateUseUseCase, validateUsePromoRevampUseCase, setCartlistCheckboxStateUseCase,
                    followShopUseCase, boAffordabilityUseCase, TestSchedulers, CoroutineTestDispatchers
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

        Scenario("1 item selected and cart is not empty and item has category breadcrumb") {

            lateinit var result: Map<String, Any>
            val categoryBreadcrumb = "cat1/cat2/cat3"

            val recommendationDataList = mutableListOf<CartRecommendationItemHolderData>().apply {
                add(CartRecommendationItemHolderData(recommendationItem = RecommendationItem(categoryBreadcrumbs = categoryBreadcrumb)))
            }

            When("generate recommendation data analytics") {
                result = cartListPresenter.generateRecommendationImpressionDataAnalytics(0, recommendationDataList, false)
            }

            Then("key `category` should be $categoryBreadcrumb") {
                val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as ArrayList<Map<String, Any>>
                val category = impression[0][EnhancedECommerceRecomProductCartMapData.KEY_CAT]
                Assert.assertTrue(category == categoryBreadcrumb)
            }
        }

        Scenario("1 item selected and cart is not empty and item is eligible for BO") {

            lateinit var result: Map<String, Any>

            val recommendationDataList = mutableListOf<CartRecommendationItemHolderData>().apply {
                add(CartRecommendationItemHolderData(recommendationItem = RecommendationItem(isFreeOngkirActive = true)))
            }

            When("generate recommendation data analytics") {
                result = cartListPresenter.generateRecommendationImpressionDataAnalytics(0, recommendationDataList, false)
            }

            Then("dimension 83 should be ${EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR}") {
                val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as ArrayList<Map<String, Any>>
                val dimension83 = impression[0][EnhancedECommerceRecomProductCartMapData.KEY_DIMENSION_83]
                Assert.assertTrue(dimension83 == EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR)
            }

        }

        Scenario("1 item selected and cart is not empty and item is eligible for BOE") {

            lateinit var result: Map<String, Any>

            val recommendationDataList = mutableListOf<CartRecommendationItemHolderData>().apply {
                add(CartRecommendationItemHolderData(recommendationItem = RecommendationItem(isFreeOngkirActive = true, labelGroupList = arrayListOf(RecommendationLabel(position = LABEL_FULFILLMENT)))))
            }

            When("generate recommendation data analytics") {
                result = cartListPresenter.generateRecommendationImpressionDataAnalytics(0, recommendationDataList, false)
            }

            Then("dimension 83 should be ${EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR_EXTRA}") {
                val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as ArrayList<Map<String, Any>>
                val dimension83 = impression[0][EnhancedECommerceRecomProductCartMapData.KEY_DIMENSION_83]
                Assert.assertTrue(dimension83 == EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR_EXTRA)
            }

        }

        Scenario("1 item selected and cart is not empty and item is not eligible for BO & BOE") {

            lateinit var result: Map<String, Any>

            val recommendationDataList = mutableListOf<CartRecommendationItemHolderData>().apply {
                add(CartRecommendationItemHolderData(recommendationItem = RecommendationItem()))
            }

            When("generate recommendation data analytics") {
                result = cartListPresenter.generateRecommendationImpressionDataAnalytics(0, recommendationDataList, false)
            }

            Then("dimension 83 should be ${EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER}") {
                val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as ArrayList<Map<String, Any>>
                val dimension83 = impression[0][EnhancedECommerceRecomProductCartMapData.KEY_DIMENSION_83]
                Assert.assertTrue(dimension83 == EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
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
                    getCartRevampV3UseCase, deleteCartUseCase, undoDeleteCartUseCase,
                    updateCartUseCase, compositeSubscription, addWishListUseCase,
                    addCartToWishlistUseCase, removeWishListUseCase, updateAndReloadCartUseCase,
                    userSessionInterface, clearCacheAutoApplyStackUseCase, getRecentViewUseCase,
                    getWishlistUseCase, getRecommendationUseCase, addToCartUseCase,
                    addToCartExternalUseCase, seamlessLoginUsecase, updateCartCounterUseCase,
                    updateCartAndValidateUseUseCase, validateUsePromoRevampUseCase, setCartlistCheckboxStateUseCase,
                    followShopUseCase, boAffordabilityUseCase, TestSchedulers, CoroutineTestDispatchers
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
                    getCartRevampV3UseCase, deleteCartUseCase, undoDeleteCartUseCase,
                    updateCartUseCase, compositeSubscription, addWishListUseCase,
                    addCartToWishlistUseCase, removeWishListUseCase, updateAndReloadCartUseCase,
                    userSessionInterface, clearCacheAutoApplyStackUseCase, getRecentViewUseCase,
                    getWishlistUseCase, getRecommendationUseCase, addToCartUseCase,
                    addToCartExternalUseCase, seamlessLoginUsecase, updateCartCounterUseCase,
                    updateCartAndValidateUseUseCase, validateUsePromoRevampUseCase, setCartlistCheckboxStateUseCase,
                    followShopUseCase, boAffordabilityUseCase, TestSchedulers, CoroutineTestDispatchers
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
