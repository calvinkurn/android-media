package com.tokopedia.cart.view.presenter

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.domain.usecase.*
import com.tokopedia.cart.view.CartListPresenter
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.cart.view.uimodel.CartRecommendationItemHolderData
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

object CartListPresenterAddToCartRecommendationAnalyticsTest : Spek({

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

    Feature("generate add to cart data analytics on recommendation") {

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

        Scenario("1 item selected on non empty cart") {

            lateinit var result: Map<String, Any>

            When("generate add to cart recommendation data analytics") {
                result = cartListPresenter.generateAddToCartEnhanceEcommerceDataLayer(CartRecommendationItemHolderData(false, RecommendationItem()), AddToCartDataModel(), false)
            }

            Then("should be containing 1 product") {
                val add = result[EnhancedECommerceCartMapData.ADD_ACTION] as Map<String, Any>
                val products = add[EnhancedECommerceAdd.KEY_PRODUCT] as List<Any>
                Assert.assertEquals(1, products.size)
            }

            Then("key `list` value should be `cart`") {
                val add = result[EnhancedECommerceCartMapData.ADD_ACTION] as Map<String, Any>
                val actionFields = add[EnhancedECommerceAdd.KEY_ACTION_FIELD] as Map<String, Any>
                Assert.assertTrue((actionFields[EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_CART_RECOMMENDATION)
            }

        }

        Scenario("1 item selected on non empty cart with eligible BO") {

            lateinit var result: Map<String, Any>

            When("generate add to cart recommendation data analytics") {
                val recommendationItem = RecommendationItem(
                        isFreeOngkirActive = true,
                        labelGroupList = listOf(RecommendationLabel())
                )
                result = cartListPresenter.generateAddToCartEnhanceEcommerceDataLayer(CartRecommendationItemHolderData(false, recommendationItem), AddToCartDataModel(), false)
            }

            Then("dimension 83 should be ${EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR}") {
                val add = result[EnhancedECommerceCartMapData.ADD_ACTION] as Map<String, Any>
                val products = add[EnhancedECommerceAdd.KEY_PRODUCT] as ArrayList<Map<String, Any>>
                val dimension83 = products[0][EnhancedECommerceRecomProductCartMapData.KEY_DIMENSION_83]
                Assert.assertTrue(dimension83 == EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR)
            }

        }

        Scenario("1 item selected on non empty cart with eligible BOE") {

            lateinit var result: Map<String, Any>

            When("generate add to cart recommendation data analytics") {
                val recommendationItem = RecommendationItem(
                        isFreeOngkirActive = true,
                        labelGroupList = listOf(RecommendationLabel(position = LABEL_FULFILLMENT))
                )
                result = cartListPresenter.generateAddToCartEnhanceEcommerceDataLayer(CartRecommendationItemHolderData(false, recommendationItem), AddToCartDataModel(), false)
            }

            Then("dimension 83 should be ${EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR_EXTRA}`") {
                val add = result[EnhancedECommerceCartMapData.ADD_ACTION] as Map<String, Any>
                val products = add[EnhancedECommerceAdd.KEY_PRODUCT] as ArrayList<Map<String, Any>>
                val dimension83 = products[0][EnhancedECommerceRecomProductCartMapData.KEY_DIMENSION_83]
                Assert.assertTrue(dimension83 == EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR_EXTRA)
            }

        }

        Scenario("1 item selected on non empty cart with not eligible BO & BOE") {

            lateinit var result: Map<String, Any>

            When("generate add to cart recommendation data analytics") {
                val recommendationItem = RecommendationItem(
                        isFreeOngkirActive = false
                )
                result = cartListPresenter.generateAddToCartEnhanceEcommerceDataLayer(CartRecommendationItemHolderData(false, recommendationItem), AddToCartDataModel(), false)
            }

            Then("dimension 83 should be ${EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER}") {
                val add = result[EnhancedECommerceCartMapData.ADD_ACTION] as Map<String, Any>
                val products = add[EnhancedECommerceAdd.KEY_PRODUCT] as ArrayList<Map<String, Any>>
                val dimension83 = products[0][EnhancedECommerceRecomProductCartMapData.KEY_DIMENSION_83]
                Assert.assertTrue(dimension83 == EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            }

        }

        Scenario("1 item selected on non empty cart and item is top ads") {

            lateinit var result: Map<String, Any>

            When("generate add to cart recommendation data analytics") {
                result = cartListPresenter.generateAddToCartEnhanceEcommerceDataLayer(CartRecommendationItemHolderData(false, RecommendationItem(isTopAds = true)), AddToCartDataModel(), false)
            }

            Then("key `list` value should contain ${EnhancedECommerceActionField.LIST_CART_RECOMMENDATION_TOPADS_TYPE}") {
                val add = result[EnhancedECommerceCartMapData.ADD_ACTION] as Map<String, Any>
                val actionFields = add[EnhancedECommerceAdd.KEY_ACTION_FIELD] as Map<String, Any>
                Assert.assertTrue((actionFields[EnhancedECommerceProductCartMapData.KEY_LIST] as String).contains(EnhancedECommerceActionField.LIST_CART_RECOMMENDATION_TOPADS_TYPE))
            }

        }

        Scenario("1 item selected on empty cart") {

            lateinit var result: Map<String, Any>

            When("generate add to cart recommendation data analytics") {
                result = cartListPresenter.generateAddToCartEnhanceEcommerceDataLayer(CartRecommendationItemHolderData(false, RecommendationItem()), AddToCartDataModel(), true)
            }

            Then("should be containing 1 product") {
                val add = result[EnhancedECommerceCartMapData.ADD_ACTION] as Map<String, Any>
                val products = add[EnhancedECommerceAdd.KEY_PRODUCT] as List<Any>
                Assert.assertEquals(1, products.size)
            }

            Then("key `list` value should be `empty cart`") {
                val add = result[EnhancedECommerceCartMapData.ADD_ACTION] as Map<String, Any>
                val actionFields = add[EnhancedECommerceAdd.KEY_ACTION_FIELD] as Map<String, Any>
                Assert.assertTrue((actionFields[EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_CART_RECOMMENDATION_ON_EMPTY_CART)
            }

        }

    }

})