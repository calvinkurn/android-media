package com.tokopedia.cart.bundle.view.presenter

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.bundle.domain.usecase.*
import com.tokopedia.cart.bundle.view.CartListPresenter
import com.tokopedia.cart.bundle.view.ICartListView
import com.tokopedia.cart.bundle.view.uimodel.CartRecentViewItemHolderData
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UndoDeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.logisticcart.boaffordability.usecase.BoAffordabilityUseCase
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceAdd
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCartMapData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.subscriptions.CompositeSubscription

@ExperimentalCoroutinesApi
object CartListPresenterAddToCartRecentViewAnalyticsTest : Spek({

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

    Feature("generate add to cart data analytics on recent view") {

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