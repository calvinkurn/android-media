package com.tokopedia.purchase_platform.features.cart.view.presenter

import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.domain.schedulers.TestSchedulers
import com.tokopedia.purchase_platform.common.domain.usecase.GetInsuranceCartUseCase
import com.tokopedia.purchase_platform.common.domain.usecase.RemoveInsuranceProductUsecase
import com.tokopedia.purchase_platform.common.domain.usecase.UpdateInsuranceProductDataUsecase
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartItemData
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
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.subscriptions.CompositeSubscription

/**
 * Created by Irfan Khoirul on 2020-01-31.
 */

object CartListPresenterLocalDataChangeTest : Spek({

    val getCartListSimplifiedUseCase: GetCartListSimplifiedUseCase = mockk()
    val deleteCartListUseCase: DeleteCartUseCase = mockk()
    val updateCartUseCase: UpdateCartUseCase = mockk()
    val checkPromoStackingCodeUseCase: CheckPromoStackingCodeUseCase = mockk()
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

    Feature("Local data changes") {

        val cartListPresenter by memoized {
            CartListPresenter(
                    getCartListSimplifiedUseCase, deleteCartListUseCase, updateCartUseCase,
                    checkPromoStackingCodeUseCase, compositeSubscription, addWishListUseCase,
                    removeWishListUseCase, updateAndReloadCartUseCase, userSessionInterface,
                    clearCacheAutoApplyStackUseCase, getRecentViewUseCase, getWishlistUseCase,
                    getRecommendationUseCase, addToCartUseCase, getInsuranceCartUseCase,
                    removeInsuranceProductUsecase, updateInsuranceProductDataUsecase,
                    seamlessLoginUsecase, TestSchedulers
            )
        }

        beforeEachTest {
            cartListPresenter.attachView(view)
        }

        Scenario("Quantity changed") {

            var result = false

            val cartDataList = mutableListOf<CartItemData>().apply {
                add(CartItemData().apply {
                    originData = CartItemData.OriginData(originalQty = 1)
                    updatedData = CartItemData.UpdatedData(quantity = 2)
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

            val cartDataList = mutableListOf<CartItemData>().apply {
                add(CartItemData().apply {
                    originData = CartItemData.OriginData(originalRemark = "note")
                    updatedData = CartItemData.UpdatedData(remark = "note note")
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

            val cartDataList = mutableListOf<CartItemData>().apply {
                add(CartItemData().apply {
                    originData = CartItemData.OriginData(originalQty = 1, originalRemark = "note")
                    updatedData = CartItemData.UpdatedData(quantity = 2, remark = "note note")
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

            val cartDataList = mutableListOf<CartItemData>().apply {
                add(CartItemData().apply {
                    originData = CartItemData.OriginData(originalQty = 1, originalRemark = "note")
                    updatedData = CartItemData.UpdatedData(quantity = 1, remark = "note")
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