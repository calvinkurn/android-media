package com.tokopedia.cart.view.presenter

import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.domain.model.cartlist.CartItemData
import com.tokopedia.cart.domain.model.cartlist.CartListData
import com.tokopedia.cart.domain.usecase.*
import com.tokopedia.cart.view.CartListPresenter
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.cartcommon.data.response.deletecart.Data
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UndoDeleteCartUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.subscriptions.CompositeSubscription

/**
 * Created by Irfan Khoirul on 2020-01-07.
 */

object CartListPresenterDeleteCartTest : Spek({

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

    Feature("delete cart item") {

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

        Scenario("remove all cart data") {

            val emptyCartListData = CartListData()
            val deleteCartData = RemoveFromCartData(
                    status = "OK",
                    data = Data(success = 1)
            )

            Given("success delete") {
                coEvery { deleteCartUseCase.setParams(any()) } just Runs
                coEvery { deleteCartUseCase.execute(any(), any()) } answers {
                    firstArg<(RemoveFromCartData) -> Unit>().invoke(deleteCartData)
                }
                every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(1)
            }

            Given("empty cart list data") {
                every { getCartListSimplifiedUseCase.createObservable(any()) } returns Observable.just(emptyCartListData)
            }

            When("process delete cart item") {
                val cartItemData = CartItemData()
                cartItemData.originData = CartItemData.OriginData()
                cartListPresenter.processDeleteCartItem(arrayListOf(cartItemData), arrayListOf(cartItemData), false, false)
            }

            Then("should render success") {
                verify {
                    view.onDeleteCartDataSuccess(arrayListOf("0"), true, false, false, false)
                }
            }
        }

        Scenario("remove some cart data") {

            val deleteCartData = RemoveFromCartData(
                    status = "OK",
                    data = Data(success = 1)
            )
            val firstCartItemData = CartItemData().apply {
                originData = CartItemData.OriginData()
            }
            val secondCartItemData = CartItemData().apply {
                originData = CartItemData.OriginData()
                originData.cartId = 1
            }

            Given("success delete") {
                coEvery { deleteCartUseCase.setParams(any()) } just Runs
                coEvery { deleteCartUseCase.execute(any(), any()) } answers {
                    firstArg<(RemoveFromCartData) -> Unit>().invoke(deleteCartData)
                }
                every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(1)
            }

            When("process delete cart item") {
                cartListPresenter.processDeleteCartItem(arrayListOf(firstCartItemData, secondCartItemData),
                        arrayListOf(firstCartItemData), false, false)
            }

            Then("should success delete") {
                verify {
                    view.onDeleteCartDataSuccess(arrayListOf("0"), false, false, false, false)
                }
            }
        }

        Scenario("remove some cart data and insurance data") {

            val deleteCartData = RemoveFromCartData(
                    status = "OK",
                    data = Data(success = 1)
            )
            val firstCartItemData = CartItemData().apply {
                originData = CartItemData.OriginData()
            }
            val secondCartItemData = CartItemData().apply {
                originData = CartItemData.OriginData()
                originData.cartId = 1
            }

            Given("success delete") {
                coEvery { deleteCartUseCase.setParams(any()) } just Runs
                coEvery { deleteCartUseCase.execute(any(), any()) } answers {
                    firstArg<(RemoveFromCartData) -> Unit>().invoke(deleteCartData)
                }
                every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(1)
            }

            When("process delete cart item") {
                cartListPresenter.processDeleteCartItem(arrayListOf(firstCartItemData, secondCartItemData),
                        arrayListOf(firstCartItemData), false, false)
            }

            Then("should success delete") {
                verify {
                    view.onDeleteCartDataSuccess(arrayListOf("0"), false, false, false, false)
                }
            }
        }

        Scenario("fail remove cart data with exception") {

            val cartItemData = CartItemData().apply {
                originData = CartItemData.OriginData()
            }
            val errorMessage = "Error Message"
            val throwable = ResponseErrorException(errorMessage)

            Given("fail delete") {
                coEvery { deleteCartUseCase.setParams(any()) } just Runs
                coEvery { deleteCartUseCase.execute(any(), any()) } answers {
                    secondArg<(Throwable) -> Unit>().invoke(throwable)
                }
                every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(1)
            }

            When("process delete cart item") {
                cartListPresenter.processDeleteCartItem(arrayListOf(cartItemData), arrayListOf(cartItemData), false, false)
            }

            Then("should show error message") {
                verify {
                    view.showToastMessageRed(throwable)
                }
            }
        }

    }

})