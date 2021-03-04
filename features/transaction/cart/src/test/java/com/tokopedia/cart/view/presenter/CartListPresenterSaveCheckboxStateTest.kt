package com.tokopedia.cart.view.presenter

import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.domain.model.cartlist.CartItemData
import com.tokopedia.cart.domain.model.cartlistcheckboxstate.CartlistCheckboxStateData
import com.tokopedia.cart.domain.usecase.*
import com.tokopedia.cart.view.CartListPresenter
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.every
import io.mockk.mockk
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.subscriptions.CompositeSubscription

object CartListPresenterSaveCheckboxStateTest : Spek({

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

        Scenario("success save checkbox state") {

            val cartItemDataList = ArrayList<CartItemHolderData>().apply {
                add(
                        CartItemHolderData(
                                CartItemData().apply {
                                    originData = CartItemData.OriginData().apply {
                                        cartId = 123
                                    }
                                },
                                isSelected = true
                        )
                )
            }

            val cartlistCheckboxStateData = CartlistCheckboxStateData().apply {
                success = true
            }

            Given("mock save checkbox state response") {
                every { setCartlistCheckboxStateUseCase.createObservable(any()) } returns Observable.just(cartlistCheckboxStateData)
            }

            Given("mock params") {
                every { setCartlistCheckboxStateUseCase.buildRequestParams(any()) } returns RequestParams.EMPTY
            }

            When("process save checkbox state") {
                cartListPresenter.saveCheckboxState(cartItemDataList)
            }

            Then("should success") {
                assert(cartlistCheckboxStateData.success)
            }
        }

        Scenario("failed save checkbox state") {

            val cartItemDataList = ArrayList<CartItemHolderData>().apply {
                add(
                        CartItemHolderData(
                                CartItemData().apply {
                                    originData = CartItemData.OriginData().apply {
                                        cartId = 123
                                    }
                                },
                                isSelected = true
                        )
                )
            }

            val cartlistCheckboxStateData = CartlistCheckboxStateData().apply {
                success = false
            }

            Given("mock save checkbox state response") {
                every { setCartlistCheckboxStateUseCase.createObservable(any()) } returns Observable.just(cartlistCheckboxStateData)
            }

            Given("mock params") {
                every { setCartlistCheckboxStateUseCase.buildRequestParams(any()) } returns RequestParams.EMPTY
            }

            When("process save checkbox state") {
                cartListPresenter.saveCheckboxState(cartItemDataList)
            }

            Then("should failed") {
                assert(!cartlistCheckboxStateData.success)
            }
        }

    }

})