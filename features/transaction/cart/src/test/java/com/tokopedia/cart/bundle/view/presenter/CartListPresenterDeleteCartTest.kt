package com.tokopedia.cart.bundle.view.presenter

import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.bundle.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.cart.bundle.domain.usecase.*
import com.tokopedia.cart.bundle.view.CartListPresenter
import com.tokopedia.cart.bundle.view.ICartListView
import com.tokopedia.cart.bundle.view.uimodel.CartItemHolderData
import com.tokopedia.cartcommon.data.response.deletecart.Data
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UndoDeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldValidateUsePromoRevampUseCase
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

object CartListPresenterDeleteCartTest : Spek({

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

    Feature("delete cart item") {

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

        Scenario("remove all cart data") {

            Given("success delete") {
                coEvery { deleteCartUseCase.setParams(any()) } just Runs
                coEvery { deleteCartUseCase.execute(any(), any()) } answers {
                    firstArg<(RemoveFromCartData) -> Unit>().invoke(RemoveFromCartData(status = "OK", data = Data(success = 1)))
                }
            }

            Given("empty cart list data") {
                coEvery { getCartRevampV3UseCase.setParams(any(), any()) } just Runs
                coEvery { getCartRevampV3UseCase.execute(any(), any()) } answers {
                    firstArg<(CartData) -> Unit>().invoke(CartData())
                }
            }

            Given("update cart counter success") {
                every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(1)
            }

            When("process delete cart item") {
                val cartItemData = CartItemHolderData(cartId = "0")
                cartListPresenter.processDeleteCartItem(arrayListOf(cartItemData), arrayListOf(cartItemData), false, false, true, false)
            }

            Then("should render success") {
                verify {
                    view.onDeleteCartDataSuccess(arrayListOf("0"), true, false, false, true, false)
                }
            }
        }

        Scenario("remove some cart data") {

            val firstCartItemData = CartItemHolderData()
            val secondCartItemData = CartItemHolderData().apply {
                cartId = "1"
            }

            Given("success delete") {
                coEvery { deleteCartUseCase.setParams(any()) } just Runs
                coEvery { deleteCartUseCase.execute(any(), any()) } answers {
                    firstArg<(RemoveFromCartData) -> Unit>().invoke(RemoveFromCartData(status = "OK", data = Data(success = 1)))
                }
            }

            Given("update cart counter success") {
                every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(1)
            }

            When("process delete cart item") {
                cartListPresenter.processDeleteCartItem(arrayListOf(firstCartItemData, secondCartItemData),
                        arrayListOf(secondCartItemData), false, false)
            }

            Then("should success delete") {
                verify {
                    view.onDeleteCartDataSuccess(arrayListOf("1"), false, false, false, false, false)
                }
            }
        }

        Scenario("fail remove cart data") {

            val throwable = ResponseErrorException("fail testing delete")
            val cartItemData = CartItemHolderData()

            Given("fail delete") {
                coEvery { deleteCartUseCase.setParams(any()) } just Runs
                coEvery { deleteCartUseCase.execute(any(), any()) } answers {
                    secondArg<(Throwable) -> Unit>().invoke(throwable)
                }
            }

            Given("update cart counter success") {
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