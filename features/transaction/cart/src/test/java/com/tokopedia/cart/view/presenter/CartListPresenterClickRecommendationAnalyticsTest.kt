package com.tokopedia.cart.view.presenter

import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.domain.usecase.*
import com.tokopedia.cart.view.CartListPresenter
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.cart.view.analytics.EnhancedECommerceData
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.*
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.extension.LABEL_FULFILLMENT
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationLabel
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

object CartListPresenterClickRecommendationAnalyticsTest : Spek({

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

    Feature("generate recommendation data click analytics") {

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

            When("generate recommendation data click analytics") {
                result = cartListPresenter.generateRecommendationDataOnClickAnalytics(RecommendationItem(), false, 0)
            }

            Then("should be containing 1 product") {
                val click = result[EnhancedECommerceCartMapData.KEY_CLICK] as Map<String, Any>
                val productList = click[EnhancedECommerceCheckout.KEY_PRODUCT] as ArrayList<Map<String, Any>>
                Assert.assertEquals(1, productList.size)
            }

            Then("key `list` value should be `cart`") {
                val click = result[EnhancedECommerceCartMapData.KEY_CLICK] as Map<String, Any>
                val actionField = click[EnhancedECommerceCheckout.KEY_ACTION_FIELD] as Map<String, Any>
                Assert.assertTrue((actionField[EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_CART_RECOMMENDATION)
            }

        }

        Scenario("1 item selected and cart is not empty and item has category breadcrumb") {

            lateinit var result: Map<String, Any>
            val categoryBreadcrumb = "cat1/cat2/cat3"

            When("generate recommendation data click analytics") {
                result = cartListPresenter.generateRecommendationDataOnClickAnalytics(RecommendationItem(categoryBreadcrumbs = categoryBreadcrumb), false, 0)
            }

            Then("should be containing 1 product") {
                val click = result[EnhancedECommerceCartMapData.KEY_CLICK] as Map<String, Any>
                val productList = click[EnhancedECommerceCheckout.KEY_PRODUCT] as ArrayList<Map<String, Any>>
                Assert.assertEquals(1, productList.size)
            }

            Then("key `category` should be $categoryBreadcrumb") {
                val click = result[EnhancedECommerceCartMapData.KEY_CLICK] as Map<String, Any>
                val products = click[EnhancedECommerceAdd.KEY_PRODUCT] as ArrayList<Map<String, Any>>
                val category = products[0][EnhancedECommerceRecomProductCartMapData.KEY_CAT]
                Assert.assertTrue(category == categoryBreadcrumb)
            }

        }

        Scenario("1 item selected and cart is not empty and eligible for BO") {

            lateinit var result: Map<String, Any>

            When("generate recommendation data click analytics") {
                val recommendationItem = RecommendationItem(
                        isFreeOngkirActive = true,
                        labelGroupList = listOf(RecommendationLabel())
                )
                result = cartListPresenter.generateRecommendationDataOnClickAnalytics(recommendationItem, false, 0)
            }

            Then("should be containing 1 product") {
                val click = result[EnhancedECommerceCartMapData.KEY_CLICK] as Map<String, Any>
                val productList = click[EnhancedECommerceCheckout.KEY_PRODUCT] as ArrayList<Map<String, Any>>
                Assert.assertEquals(1, productList.size)
            }

            Then("dimension 83 should be `bebas ongkir`") {
                val click = result[EnhancedECommerceCartMapData.KEY_CLICK] as Map<String, Any>
                val products = click[EnhancedECommerceAdd.KEY_PRODUCT] as ArrayList<Map<String, Any>>
                val dimension83 = products[0][EnhancedECommerceRecomProductCartMapData.KEY_DIMENSION_83]
                Assert.assertTrue(dimension83 == EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR)
            }

        }

        Scenario("1 item selected and cart is not empty and eligible for BOE") {

            lateinit var result: Map<String, Any>

            When("generate recommendation data click analytics") {
                val recommendationItem = RecommendationItem(
                        isFreeOngkirActive = true,
                        labelGroupList = listOf(RecommendationLabel(position = LABEL_FULFILLMENT))
                )
                result = cartListPresenter.generateRecommendationDataOnClickAnalytics(recommendationItem, false, 0)
            }

            Then("should be containing 1 product") {
                val click = result[EnhancedECommerceCartMapData.KEY_CLICK] as Map<String, Any>
                val productList = click[EnhancedECommerceCheckout.KEY_PRODUCT] as ArrayList<Map<String, Any>>
                Assert.assertEquals(1, productList.size)
            }

            Then("dimension 83 should be `bebas ongkir extra`") {
                val click = result[EnhancedECommerceCartMapData.KEY_CLICK] as Map<String, Any>
                val products = click[EnhancedECommerceAdd.KEY_PRODUCT] as ArrayList<Map<String, Any>>
                val dimension83 = products[0][EnhancedECommerceRecomProductCartMapData.KEY_DIMENSION_83]
                Assert.assertTrue(dimension83 == EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR_EXTRA)
            }

        }

        Scenario("1 item selected and cart is not empty and not eligible for BO & BOE") {

            lateinit var result: Map<String, Any>

            When("generate recommendation data click analytics") {
                val recommendationItem = RecommendationItem(
                        isFreeOngkirActive = false
                )
                result = cartListPresenter.generateRecommendationDataOnClickAnalytics(recommendationItem, false, 0)
            }

            Then("should be containing 1 product") {
                val click = result[EnhancedECommerceCartMapData.KEY_CLICK] as Map<String, Any>
                val productList = click[EnhancedECommerceCheckout.KEY_PRODUCT] as ArrayList<Map<String, Any>>
                Assert.assertEquals(1, productList.size)
            }

            Then("dimension 83 should be `none / other`") {
                val click = result[EnhancedECommerceCartMapData.KEY_CLICK] as Map<String, Any>
                val products = click[EnhancedECommerceAdd.KEY_PRODUCT] as ArrayList<Map<String, Any>>
                val dimension83 = products[0][EnhancedECommerceRecomProductCartMapData.KEY_DIMENSION_83]
                Assert.assertTrue(dimension83 == EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            }

        }

        Scenario("1 item selected and cart is empty") {

            lateinit var result: Map<String, Any>

            When("generate recommendation data click analytics") {
                result = cartListPresenter.generateRecommendationDataOnClickAnalytics(RecommendationItem(), true, 0)
            }

            Then("should be containing 1 product") {
                val click = result[EnhancedECommerceCartMapData.KEY_CLICK] as Map<String, Any>
                val productList = click[EnhancedECommerceCheckout.KEY_PRODUCT] as ArrayList<Map<String, Any>>
                Assert.assertEquals(1, productList.size)
            }

            Then("key `list` value should be `empty cart`") {
                val click = result[EnhancedECommerceCartMapData.KEY_CLICK] as Map<String, Any>
                val actionField = click[EnhancedECommerceCheckout.KEY_ACTION_FIELD] as Map<String, Any>
                Assert.assertTrue((actionField[EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_CART_RECOMMENDATION_ON_EMPTY_CART)
            }

        }

    }

})