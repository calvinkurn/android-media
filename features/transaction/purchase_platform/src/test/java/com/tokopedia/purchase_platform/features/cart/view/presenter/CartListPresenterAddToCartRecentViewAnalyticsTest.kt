package com.tokopedia.purchase_platform.features.cart.view.presenter

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceAdd
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCartMapData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData
import com.tokopedia.purchase_platform.common.domain.schedulers.TestSchedulers
import com.tokopedia.purchase_platform.common.domain.usecase.GetInsuranceCartUseCase
import com.tokopedia.purchase_platform.common.domain.usecase.RemoveInsuranceProductUsecase
import com.tokopedia.purchase_platform.common.domain.usecase.UpdateInsuranceProductDataUsecase
import com.tokopedia.purchase_platform.features.cart.domain.usecase.*
import com.tokopedia.purchase_platform.features.cart.view.CartListPresenter
import com.tokopedia.purchase_platform.features.cart.view.ICartListView
import com.tokopedia.purchase_platform.features.cart.view.uimodel.CartRecentViewItemHolderData
import com.tokopedia.purchase_platform.features.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.seamless_login.domain.usecase.SeamlessLoginUsecase
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

object CartListPresenterAddToCartRecentViewAnalyticsTest : Spek({

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

    Feature("generate add to cart data analytics on recent view") {

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

        Scenario("1 item selected on non empty cart") {

            lateinit var result: Map<String, Any>

            When("generate add to cart recent view data analytics") {
                result = cartListPresenter.generateAddToCartEnhanceEcommerceDataLayer(CartRecentViewItemHolderData(), AddToCartDataModel(), false)
            }

            Then("should be containing 1 product") {
                val add = result[EnhancedECommerceCartMapData.ADD_ACTION] as Map<String, Any>
                val products = add[EnhancedECommerceAdd.KEY_PRODUCT] as List<Any>
                Assert.assertEquals(1, products.size)
            }

            Then("key `list` value should be `cart`") {
                val add = result[EnhancedECommerceCartMapData.ADD_ACTION] as Map<String, Any>
                val actionFields = add[EnhancedECommerceAdd.KEY_ACTION_FIELD] as Map<String, Any>
                Assert.assertTrue((actionFields[EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_RECENT_VIEW)
            }

        }

        Scenario("1 item selected on empty cart") {

            lateinit var result: Map<String, Any>

            When("generate add to cart recent view data analytics") {
                result = cartListPresenter.generateAddToCartEnhanceEcommerceDataLayer(CartRecentViewItemHolderData(), AddToCartDataModel(), true)
            }

            Then("should be containing 1 product") {
                val add = result[EnhancedECommerceCartMapData.ADD_ACTION] as Map<String, Any>
                val products = add[EnhancedECommerceAdd.KEY_PRODUCT] as List<Any>
                Assert.assertEquals(1, products.size)
            }

            Then("key `list` value should be `empty cart`") {
                val add = result[EnhancedECommerceCartMapData.ADD_ACTION] as Map<String, Any>
                val actionFields = add[EnhancedECommerceAdd.KEY_ACTION_FIELD] as Map<String, Any>
                Assert.assertTrue((actionFields[EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_RECENT_VIEW_ON_EMPTY_CART)
            }

        }

    }

})