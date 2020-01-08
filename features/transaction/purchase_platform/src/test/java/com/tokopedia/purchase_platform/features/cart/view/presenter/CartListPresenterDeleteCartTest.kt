package com.tokopedia.purchase_platform.features.cart.view.presenter

import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.purchase_platform.common.data.api.CartResponseErrorException
import com.tokopedia.purchase_platform.common.domain.schedulers.TestSchedulers
import com.tokopedia.purchase_platform.common.domain.usecase.GetInsuranceCartUseCase
import com.tokopedia.purchase_platform.common.domain.usecase.RemoveInsuranceProductUsecase
import com.tokopedia.purchase_platform.common.domain.usecase.UpdateInsuranceProductDataUsecase
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartItemData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.DeleteCartData
import com.tokopedia.purchase_platform.features.cart.domain.usecase.*
import com.tokopedia.purchase_platform.features.cart.view.CartListPresenter
import com.tokopedia.purchase_platform.features.cart.view.ICartListView
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.seamless_login.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.subscriptions.CompositeSubscription

/**
 * Created by Irfan Khoirul on 2020-01-07.
 */

class CartListPresenterDeleteCartTest : Spek({

    val getCartListSimplifiedUseCase: GetCartListSimplifiedUseCase = mockk()
    val deleteCartListUseCase: DeleteCartUseCase = mockk()
    val updateCartUseCase: UpdateCartUseCase = mockk()
    val checkPromoStackingCodeUseCase: CheckPromoStackingCodeUseCase = mockk()
    val checkPromoStackingCodeMapper: CheckPromoStackingCodeMapper = mockk()
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

    Feature("delete cart item") {

        val cartListPresenter by memoized {
            CartListPresenter(
                    getCartListSimplifiedUseCase, deleteCartListUseCase, updateCartUseCase,
                    checkPromoStackingCodeUseCase, checkPromoStackingCodeMapper, compositeSubscription,
                    addWishListUseCase, removeWishListUseCase, updateAndReloadCartUseCase,
                    userSessionInterface, clearCacheAutoApplyStackUseCase, getRecentViewUseCase,
                    getWishlistUseCase, getRecommendationUseCase, addToCartUseCase, getInsuranceCartUseCase,
                    removeInsuranceProductUsecase, updateInsuranceProductDataUsecase, seamlessLoginUsecase, TestSchedulers
            )
        }

        Scenario("remove all cart data") {

            val view: ICartListView = mockk(relaxed = true)
            val emptyCartListData = CartListData()

            Given("success delete") {
                val deleteCartData = DeleteCartData(isSuccess = true)
                every { deleteCartListUseCase.createObservable(any()) } returns Observable.just(deleteCartData)
            }

            Given("empty cart list data") {
                every { getCartListSimplifiedUseCase.createObservable(any()) } returns Observable.just(emptyCartListData)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process delete cart item") {
                val cartItemData = CartItemData()
                cartItemData.originData = CartItemData.OriginData()
                cartListPresenter.processDeleteCartItem(arrayListOf(cartItemData), arrayListOf(cartItemData), arrayListOf(), false, false)
            }

            Then("should render success") {
                verify {
                    view.renderInitialGetCartListDataSuccess(emptyCartListData)
                }
            }
        }

        Scenario("remove some cart data") {

            val view: ICartListView = mockk(relaxed = true)

            Given("success delete") {
                val deleteCartData = DeleteCartData(isSuccess = true)
                every { deleteCartListUseCase.createObservable(any()) } returns Observable.just(deleteCartData)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process delete cart item") {
                val firstCartItemData = CartItemData()
                firstCartItemData.originData = CartItemData.OriginData()
                val secondCartItemData = CartItemData()
                secondCartItemData.originData = CartItemData.OriginData()
                secondCartItemData.originData?.cartId = 1

                cartListPresenter.processDeleteCartItem(arrayListOf(firstCartItemData, secondCartItemData),
                        arrayListOf(firstCartItemData), arrayListOf(), false, false)
            }

            Then("should success delete") {
                verify {
                    view.onDeleteCartDataSuccess(arrayListOf("0"))
                }
            }
        }

        Scenario("remove some cart data and insurance data") {

            val view: ICartListView = mockk(relaxed = true)

            Given("success delete") {
                val deleteCartData = DeleteCartData(isSuccess = true)
                every { deleteCartListUseCase.createObservable(any()) } returns Observable.just(deleteCartData)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process delete cart item") {
                val firstCartItemData = CartItemData()
                firstCartItemData.originData = CartItemData.OriginData()
                val secondCartItemData = CartItemData()
                secondCartItemData.originData = CartItemData.OriginData()
                secondCartItemData.originData?.cartId = 1

                cartListPresenter.processDeleteCartItem(arrayListOf(firstCartItemData, secondCartItemData),
                        arrayListOf(firstCartItemData), arrayListOf(), false, false)
            }

            Then("should success delete") {
                verify {
                    view.onDeleteCartDataSuccess(arrayListOf("0"))
                }
            }
        }

        Scenario("fail remove cart data") {

            val view: ICartListView = mockk(relaxed = true)
            val errorMessage = "fail testing delete"

            Given("fail delete") {
                val deleteCartData = DeleteCartData(isSuccess = false, message = errorMessage)
                every { deleteCartListUseCase.createObservable(any()) } returns Observable.just(deleteCartData)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process delete cart item") {
                val cartItemData = CartItemData()
                cartItemData.originData = CartItemData.OriginData()
                cartListPresenter.processDeleteCartItem(arrayListOf(cartItemData), arrayListOf(cartItemData), arrayListOf(), false, false)
            }

            Then("should show error message") {
                verify {
                    view.showToastMessageRed(errorMessage)
                }
            }
        }

        Scenario("fail remove cart data with CartResponseErrorException") {

            val view: ICartListView = mockk(relaxed = true)
            val errorMessage = "fail testing delete"

            Given("fail delete") {
                every { deleteCartListUseCase.createObservable(any()) } returns Observable.error(CartResponseErrorException(errorMessage))
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process delete cart item") {
                val cartItemData = CartItemData()
                cartItemData.originData = CartItemData.OriginData()
                cartListPresenter.processDeleteCartItem(arrayListOf(cartItemData), arrayListOf(cartItemData), arrayListOf(), false, false)
            }

            Then("should show error message") {
                verify {
                    view.showToastMessageRed(errorMessage)
                }
            }
        }

        Scenario("fail remove cart data with other exception") {

            val view: ICartListView = mockk(relaxed = true)

            Given("fail delete") {
                every { deleteCartListUseCase.createObservable(any()) } returns Observable.error(IllegalStateException())
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process delete cart item") {
                val cartItemData = CartItemData()
                cartItemData.originData = CartItemData.OriginData()
                cartListPresenter.processDeleteCartItem(arrayListOf(cartItemData), arrayListOf(cartItemData), arrayListOf(), false, false)
            }

            Then("should show error message") {
                verify {
                    view.showToastMessageRed(any())
                }
            }
        }

    }

})