package com.tokopedia.cart.bundle.view.presenter

import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.bundle.domain.usecase.*
import com.tokopedia.cart.bundle.view.CartListPresenter
import com.tokopedia.cart.bundle.view.ICartListView
import com.tokopedia.cart.bundle.view.uimodel.CartItemHolderData
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UndoDeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.subscriptions.CompositeSubscription

object CartListPresenterLocalDataChangeTest : Spek({

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
    val view: ICartListView = mockk(relaxed = true)

    Feature("Local data changes") {

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

        Scenario("Quantity changed") {

            var result = false

            val cartDataList = mutableListOf<CartItemHolderData>().apply {
                add(CartItemHolderData().apply {
                    originalQty = 1
                    quantity = 2
                })
            }

            Given("cart data") {
                every { view.getAllCartDataList() } returns cartDataList
            }

            When("check is data changed") {
                result = cartListPresenter.dataHasChanged()
            }

            Then("data should be changed") {
                Assert.assertTrue(result)
            }

        }

        Scenario("Notes changed") {

            var result = false

            val cartDataList = mutableListOf<CartItemHolderData>().apply {
                add(CartItemHolderData().apply {
                    originalNotes = "on"
                    notes = "n"
                })
            }

            Given("cart data") {
                every { view.getAllCartDataList() } returns cartDataList
            }

            When("check is data changed") {
                result = cartListPresenter.dataHasChanged()
            }

            Then("data should be changed") {
                Assert.assertTrue(result)
            }

        }

        Scenario("Quantity and notes changed") {

            var result = false

            val cartDataList = mutableListOf<CartItemHolderData>().apply {
                add(CartItemHolderData().apply {
                    originalQty = 1
                    quantity = 2
                    originalNotes = "on"
                    notes = "n"
                })
            }

            Given("cart data") {
                every { view.getAllCartDataList() } returns cartDataList
            }

            When("check is data changed") {
                result = cartListPresenter.dataHasChanged()
            }

            Then("data should be changed") {
                Assert.assertTrue(result)
            }

        }

        Scenario("Quantity and notes did not changed") {

            var result = false

            val cartDataList = mutableListOf<CartItemHolderData>().apply {
                add(CartItemHolderData().apply {
                    originalQty = 1
                    quantity = 1
                    originalNotes = "n"
                    notes = "n"
                })
            }

            Given("cart data") {
                every { view.getAllCartDataList() } returns cartDataList
            }

            When("check is data changed") {
                result = cartListPresenter.dataHasChanged()
            }

            Then("data should not be changed") {
                Assert.assertFalse(result)
            }

        }

    }

})