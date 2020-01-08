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
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateAndReloadCartListData
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
 * Created by Irfan Khoirul on 2020-01-08.
 */

class CartListPresenterUpdateAndReloadCartTest : Spek({

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
    val view: ICartListView = mockk(relaxed = true)

    Feature("update and reload cart list") {

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

        Scenario("success update and reload empty cart") {

            val emptyCartListData = UpdateAndReloadCartListData()

            Given("empty data") {
                every { updateAndReloadCartUseCase.createObservable(any()) } returns Observable.just(emptyCartListData)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
                every { view.getAllAvailableCartDataList() } returns arrayListOf()
            }

            When("process to update and reload cart data") {
                cartListPresenter.processToUpdateAndReloadCartData("0")
            }

            Then("should hide loading") {
                verify {
                    view.hideProgressLoading()
                }
            }
        }

        Scenario("success update and reload cart") {

            val cartListData = UpdateAndReloadCartListData()

            Given("cart data") {
                cartListData.cartListData = CartListData()
                every { updateAndReloadCartUseCase.createObservable(any()) } returns Observable.just(cartListData)
            }

            Given("attach view") {
                val cartItemData = CartItemData()
                val updatedData = CartItemData.UpdatedData()
                cartItemData.originData = CartItemData.OriginData()
                updatedData.remark = ""
                cartItemData.updatedData = updatedData

                cartListPresenter.attachView(view)
                every { view.getAllAvailableCartDataList() } returns arrayListOf(cartItemData)
            }

            When("process to update and reload cart data") {
                cartListPresenter.processToUpdateAndReloadCartData("0")
            }

            Then("should render success") {
                verify {
                    view.renderInitialGetCartListDataSuccess(cartListData.cartListData)
                }
            }
        }

        Scenario("failed update and reload cart with exception") {

            val errorMessage = "Error"

            Given("cart data") {
                every { updateAndReloadCartUseCase.createObservable(any()) } returns Observable.error(CartResponseErrorException(errorMessage))
            }

            Given("attach view") {
                val cartItemData = CartItemData()
                val updatedData = CartItemData.UpdatedData()
                cartItemData.originData = CartItemData.OriginData()
                updatedData.remark = ""
                cartItemData.updatedData = updatedData

                cartListPresenter.attachView(view)
                every { view.getAllAvailableCartDataList() } returns arrayListOf(cartItemData)
            }

            When("process to update and reload cart data") {
                cartListPresenter.processToUpdateAndReloadCartData("0")
            }

            Then("should render success") {
                verify {
                    view.hideProgressLoading()
                    view.showToastMessageRed(errorMessage)
                }
            }
        }

        Scenario("failed update and reload cart with exception") {

            val errorMessage = "Error"

            Given("cart data") {
                every { updateAndReloadCartUseCase.createObservable(any()) } returns Observable.error(IllegalStateException(errorMessage))
            }

            Given("attach view") {
                val cartItemData = CartItemData()
                val updatedData = CartItemData.UpdatedData()
                cartItemData.originData = CartItemData.OriginData()
                updatedData.remark = ""
                cartItemData.updatedData = updatedData

                cartListPresenter.attachView(view)
                every { view.getAllAvailableCartDataList() } returns arrayListOf(cartItemData)
            }

            When("process to update and reload cart data") {
                cartListPresenter.processToUpdateAndReloadCartData("0")
            }

            Then("should render success") {
                verify {
                    view.hideProgressLoading()
                    view.showToastMessageRed(errorMessage)
                }
            }
        }

    }

})