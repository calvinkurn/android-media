package com.tokopedia.cart.view.presenter

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.domain.model.cartlist.CartListData
import com.tokopedia.cart.domain.usecase.*
import com.tokopedia.cart.view.CartListPresenter
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.cart.view.uimodel.CartRecentViewItemHolderData
import com.tokopedia.cart.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.cart.view.uimodel.CartWishlistItemHolderData
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.subscriptions.CompositeSubscription

/**
 * Created by Irfan Khoirul on 2020-01-07.
 */

object CartListPresenterAddToCartTest : Spek({

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

    Feature("add to cart") {

        val cartListPresenter by memoized {
            CartListPresenter(
                    getCartListSimplifiedUseCase, deleteCartUseCase, undoDeleteCartUseCase,
                    updateCartUseCase, compositeSubscription, addWishListUseCase,
                    addCartToWishlistUseCase, removeWishListUseCase, updateAndReloadCartUseCase,
                    userSessionInterface, clearCacheAutoApplyStackUseCase, getRecommendationUseCase,
                    getWishlistUseCase, getRecommendationUseCase, addToCartUseCase,
                    addToCartExternalUseCase, seamlessLoginUsecase, updateCartCounterUseCase,
                    updateCartAndValidateUseUseCase, validateUsePromoRevampUseCase, setCartlistCheckboxStateUseCase,
                    followShopUseCase, TestSchedulers
            )
        }

        beforeEachTest {
            cartListPresenter.attachView(view)
        }

        Scenario("success add to cart wishlist item") {

            val productModel = CartWishlistItemHolderData(id = "0", shopId = "0")
            val successMessage = "Success message add to cart"
            val addToCartDataModel = AddToCartDataModel().apply {
                status = AddToCartDataModel.STATUS_OK
                data = DataModel().apply {
                    message = arrayListOf<String>().apply {
                        add(successMessage)
                    }
                    success = 1
                }
            }

            Given("add to cart data") {
                every { addToCartUseCase.createObservable(any()) } returns Observable.just(addToCartDataModel)
                every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(0)
                every { getCartListSimplifiedUseCase.createObservable(any()) } returns Observable.just(CartListData())
                every { getCartListSimplifiedUseCase.buildParams(any()) } returns emptyMap()
            }

            Given("mock userId") {
                every { userSessionInterface.userId } returns "123"
            }

            When("process to update cart data") {
                cartListPresenter.processAddToCart(productModel)
            }

            Then("should render success add to cart wishlist item") {
                verifyOrder {
                    view.triggerSendEnhancedEcommerceAddToCartSuccess(addToCartDataModel, productModel)
                    view.showToastMessageGreen(addToCartDataModel.data.message[0])
                }
            }
        }

        Scenario("failed add to cart wishlist item") {

            val errorMessage = "Add to cart error"
            val addToCartDataModel = AddToCartDataModel().apply {
                this.status = AddToCartDataModel.STATUS_ERROR
                this.data = DataModel()
                this.errorMessage = arrayListOf<String>().apply {
                    add(errorMessage)
                }
            }

            Given("add to cart data") {
                every { addToCartUseCase.createObservable(any()) } returns Observable.just(addToCartDataModel)
            }

            Given("mock userId") {
                every { userSessionInterface.userId } returns "123"
            }

            When("process to update cart data") {
                cartListPresenter.processAddToCart(CartWishlistItemHolderData(id = "0", shopId = "0"))
            }

            Then("should show error") {
                verify {
                    view.showToastMessageRed(errorMessage)
                }
            }
        }

        Scenario("success add to cart recent view item") {

            val productModel = CartRecentViewItemHolderData(id = "0", shopId = "0")
            val addToCartDataModel = AddToCartDataModel().apply {
                status = AddToCartDataModel.STATUS_OK
                data = DataModel().apply {
                    success = 1
                    message = arrayListOf<String>().apply {
                        add("Success message")
                    }
                }
            }

            Given("add to cart data") {
                every { addToCartUseCase.createObservable(any()) } returns Observable.just(addToCartDataModel)
                every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(0)
                every { getCartListSimplifiedUseCase.createObservable(any()) } returns Observable.just(CartListData())
                every { getCartListSimplifiedUseCase.buildParams(any()) } returns emptyMap()
            }

            Given("mock userId") {
                every { userSessionInterface.userId } returns "123"
            }

            When("process to update cart data") {
                cartListPresenter.processAddToCart(productModel)
            }

            Then("should render success") {
                verifyOrder {
                    view.triggerSendEnhancedEcommerceAddToCartSuccess(addToCartDataModel, productModel)
                    view.showToastMessageGreen(addToCartDataModel.data.message[0])
                }
            }
        }

        Scenario("failed add to cart recent view item") {

            val addToCartDataModel = AddToCartDataModel().apply {
                this.status = AddToCartDataModel.STATUS_ERROR
                this.data = DataModel()
                this.errorMessage = arrayListOf<String>().apply {
                    add("Add to cart error")
                }
            }

            Given("add to cart data") {
                every { addToCartUseCase.createObservable(any()) } returns Observable.just(addToCartDataModel)
            }

            Given("mock userId") {
                every { userSessionInterface.userId } returns "123"
            }

            When("process to update cart data") {
                cartListPresenter.processAddToCart(CartRecentViewItemHolderData(id = "0", shopId = "0"))
            }

            Then("should show error") {
                verify {
                    view.showToastMessageRed(addToCartDataModel.errorMessage[0])
                }
            }
        }

        Scenario("failed add to cart recent view item with exception") {

            val exception = IllegalStateException("Add to cart error with exception")

            Given("add to cart data") {
                every { addToCartUseCase.createObservable(any()) } returns Observable.error(exception)
            }

            Given("mock userId") {
                every { userSessionInterface.userId } returns "123"
            }

            When("process to update cart data") {
                cartListPresenter.processAddToCart(CartRecentViewItemHolderData(id = "0", shopId = "0"))
            }

            Then("should show error") {
                verify {
                    view.showToastMessageRed(exception)
                }
            }
        }

        Scenario("success add to cart recommendation item") {

            val productModel = CartRecommendationItemHolderData(false, RecommendationItem(productId = 0, shopId = 0))
            val addToCartDataModel = AddToCartDataModel().apply {
                status = AddToCartDataModel.STATUS_OK
                data = DataModel().apply {
                    success = 1
                    message = arrayListOf<String>().apply {
                        add("ATC Success message")
                    }
                }
            }

            Given("add to cart data") {
                every { addToCartUseCase.createObservable(any()) } returns Observable.just(addToCartDataModel)
                every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(0)
                every { getCartListSimplifiedUseCase.createObservable(any()) } returns Observable.just(CartListData())
                every { getCartListSimplifiedUseCase.buildParams(any()) } returns emptyMap()
            }

            Given("mock userId") {
                every { userSessionInterface.userId } returns "123"
            }

            When("process to update cart data") {
                cartListPresenter.processAddToCart(productModel)
            }

            Then("should render success") {
                verifyOrder {
                    view.triggerSendEnhancedEcommerceAddToCartSuccess(addToCartDataModel, productModel)
                    view.showToastMessageGreen(addToCartDataModel.data.message[0])
                }
            }
        }

        Scenario("failed add to cart recommendation item") {

            val productModel = CartRecommendationItemHolderData(false, RecommendationItem(productId = 0, shopId = 0))
            val addToCartDataModel = AddToCartDataModel().apply {
                status = AddToCartDataModel.STATUS_ERROR
                data = DataModel().apply {
                    success = 1
                }
                errorMessage = arrayListOf<String>().apply {
                    add("Add to cart error")
                }
            }

            Given("add to cart data") {
                every { addToCartUseCase.createObservable(any()) } returns Observable.just(addToCartDataModel)
            }

            Given("mock userId") {
                every { userSessionInterface.userId } returns "123"
            }

            When("process to update cart data") {
                cartListPresenter.processAddToCart(productModel)
            }

            Then("should show error") {
                verify {
                    view.showToastMessageRed(addToCartDataModel.errorMessage[0])
                }
            }
        }

    }

})