package com.tokopedia.cart.view.presenter

import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.domain.model.cartlist.CartItemData
import com.tokopedia.cart.domain.usecase.*
import com.tokopedia.cart.view.CartListPresenter
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCartMapData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.mockk
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.subscriptions.CompositeSubscription

/**
 * Created by Irfan Khoirul on 2020-01-31.
 */

object CartListPresenterDeleteCartAnalyticsTest : Spek({

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

    Feature("generate delete cart data analytics") {

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

        Scenario("1 item selected") {

            lateinit var result: Map<String, Any>

            val cartItemDataList = mutableListOf<CartItemData>().apply {
                add(CartItemData())
            }

            When("generate cart data analytics") {
                result = cartListPresenter.generateDeleteCartDataAnalytics(cartItemDataList)
            }

            Then("should be containing 1 product") {
                val action = result[EnhancedECommerceCartMapData.REMOVE_ACTION] as Map<String, Any>
                val products = action[EnhancedECommerceCartMapData.KEY_PRODUCTS] as List<Any>
                Assert.assertEquals(1, products.size)
            }

        }

        Scenario("1 item selected and category field is not empty") {

            lateinit var result: Map<String, Any>
            val categoryName = "cat1"

            val cartItemDataList = mutableListOf<CartItemData>().apply {
                add(CartItemData(CartItemData.OriginData(categoryForAnalytics = categoryName)))
            }

            When("generate cart data analytics") {
                result = cartListPresenter.generateDeleteCartDataAnalytics(cartItemDataList)
            }

            Then("should be containing 1 product") {
                val action = result[EnhancedECommerceCartMapData.REMOVE_ACTION] as Map<String, Any>
                val products = action[EnhancedECommerceCartMapData.KEY_PRODUCTS] as List<Any>
                Assert.assertEquals(1, products.size)
            }

            Then("key `category` should be $categoryName") {
                val action = result[EnhancedECommerceCartMapData.REMOVE_ACTION] as Map<String, Any>
                val products = action[EnhancedECommerceCartMapData.KEY_PRODUCTS] as ArrayList<Map<String, Any>>
                val category = products[0][EnhancedECommerceProductCartMapData.KEY_CAT]
                Assert.assertTrue(category == categoryName)
            }

        }

        Scenario("1 item selected and tracker attribution field is not empty") {

            lateinit var result: Map<String, Any>
            val trackerAttributionValue = "attr"

            val cartItemDataList = mutableListOf<CartItemData>().apply {
                add(CartItemData(CartItemData.OriginData(trackerAttribution = trackerAttributionValue)))
            }

            When("generate cart data analytics") {
                result = cartListPresenter.generateDeleteCartDataAnalytics(cartItemDataList)
            }

            Then("key `attribution` should be $trackerAttributionValue") {
                val action = result[EnhancedECommerceCartMapData.REMOVE_ACTION] as Map<String, Any>
                val products = action[EnhancedECommerceCartMapData.KEY_PRODUCTS] as ArrayList<Map<String, Any>>
                val attribution = products[0][EnhancedECommerceProductCartMapData.KEY_ATTRIBUTION]
                Assert.assertTrue(attribution == trackerAttributionValue)
            }

            Then("key `dimension 38` should be $trackerAttributionValue") {
                val action = result[EnhancedECommerceCartMapData.REMOVE_ACTION] as Map<String, Any>
                val products = action[EnhancedECommerceCartMapData.KEY_PRODUCTS] as ArrayList<Map<String, Any>>
                val attribution = products[0][EnhancedECommerceProductCartMapData.KEY_DIMENSION_38]
                Assert.assertTrue(attribution == trackerAttributionValue)
            }

        }

        Scenario("1 item selected and tracker list name field is not empty") {

            lateinit var result: Map<String, Any>
            val trackerListNameValue = "attr"

            val cartItemDataList = mutableListOf<CartItemData>().apply {
                add(CartItemData(CartItemData.OriginData(trackerListName = trackerListNameValue)))
            }

            When("generate cart data analytics") {
                result = cartListPresenter.generateDeleteCartDataAnalytics(cartItemDataList)
            }

            Then("key `list` should be $trackerListNameValue") {
                val action = result[EnhancedECommerceCartMapData.REMOVE_ACTION] as Map<String, Any>
                val products = action[EnhancedECommerceCartMapData.KEY_PRODUCTS] as ArrayList<Map<String, Any>>
                val listName = products[0][EnhancedECommerceProductCartMapData.KEY_LIST]
                Assert.assertTrue(listName == trackerListNameValue)
            }

            Then("key `dimension 40` should be $trackerListNameValue") {
                val action = result[EnhancedECommerceCartMapData.REMOVE_ACTION] as Map<String, Any>
                val products = action[EnhancedECommerceCartMapData.KEY_PRODUCTS] as ArrayList<Map<String, Any>>
                val attribution = products[0][EnhancedECommerceProductCartMapData.KEY_DIMENSION_40]
                Assert.assertTrue(attribution == trackerListNameValue)
            }

        }
    }

})