package com.tokopedia.cart.view.presenter

import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.domain.model.cartlist.CartItemData
import com.tokopedia.cart.domain.model.cartlist.ShopGroupAvailableData
import com.tokopedia.cart.domain.model.cartlist.WholesalePriceData
import com.tokopedia.cart.domain.usecase.*
import com.tokopedia.cart.view.CartListPresenter
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cart.view.uimodel.CartShopHolderData
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.subscriptions.CompositeSubscription

object CartListPresenterCalculateSubTotalTest : Spek({

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

    Feature("calculate subtotal") {

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

        //region First Item In First Shop
        val firstItemFirstOriginData by memoized {
            CartItemData.OriginData().apply {
                pricePlan = 1000.0
                parentId = "0"
                productId = "1"
                isCashBack = true
                productCashBack = "10%"
            }
        }
        val firstItemFirstUpdatedData by memoized {
            CartItemData.UpdatedData().apply {
                quantity = 1
            }
        }
        val firstItemFirstData by memoized {
            CartItemData().apply {
                originData = firstItemFirstOriginData
                updatedData = firstItemFirstUpdatedData
            }
        }
        val firstItemFirst by memoized {
            CartItemHolderData(cartItemData = firstItemFirstData)
        }
        //endregion

        //region First Item In Second Shop
        val firstItemSecondOriginData by memoized {
            CartItemData.OriginData().apply {
                pricePlan = 40.0
                parentId = "0"
                productId = "2"
            }
        }
        val firstItemSecondUpdatedData by memoized {
            CartItemData.UpdatedData().apply {
                quantity = 2
            }
        }
        val firstItemSecondData by memoized {
            CartItemData().apply {
                originData = firstItemSecondOriginData
                updatedData = firstItemSecondUpdatedData
            }
        }
        val firstItemSecond by memoized {
            CartItemHolderData(cartItemData = firstItemSecondData)
        }
        //endregion

        //region Second Item In First Shop
        val secondItemFirstOriginData by memoized {
            CartItemData.OriginData().apply {
                pricePlan = 200.0
                parentId = "0"
                productId = "3"
            }
        }
        val secondItemFirstUpdatedData by memoized {
            CartItemData.UpdatedData().apply {
                quantity = 3
            }
        }
        val secondItemFirstData by memoized {
            CartItemData().apply {
                originData = secondItemFirstOriginData
                updatedData = secondItemFirstUpdatedData
            }
        }
        val secondItemFirst by memoized {
            CartItemHolderData(cartItemData = secondItemFirstData)
        }
        //endregion

        //region Second Item In Second Shop
        val secondItemSecondOriginData by memoized {
            CartItemData.OriginData().apply {
                pricePlan = 1.0
                parentId = "0"
                productId = "4"
            }
        }
        val secondItemSecondUpdatedData by memoized {
            CartItemData.UpdatedData().apply {
                quantity = 4
            }
        }
        val secondItemSecondData by memoized {
            CartItemData().apply {
                originData = secondItemSecondOriginData
                updatedData = secondItemSecondUpdatedData
            }
        }
        val secondItemSecond by memoized {
            CartItemHolderData(cartItemData = secondItemSecondData)
        }
        //endregion

        //region First Shop
        val firstShopData by memoized {
            ShopGroupAvailableData().apply {
                cartItemHolderDataList = arrayListOf(firstItemFirst, secondItemFirst)
            }
        }
        val firstShop by memoized {
            CartShopHolderData().apply {
                shopGroupAvailableData = firstShopData
            }
        }
        //endregion

        //region Second Shop
        val secondShopData by memoized {
            ShopGroupAvailableData().apply {
                cartItemHolderDataList = arrayListOf(firstItemSecond, secondItemSecond)
            }
        }
        val secondShop by memoized {
            CartShopHolderData().apply {
                shopGroupAvailableData = secondShopData
            }
        }
        //endregion

        val cartShops by memoized { arrayListOf(firstShop, secondShop) }

        beforeEachTest {
            cartListPresenter.attachView(view)
        }

        Scenario("no item selected") {

            Given("cart data list") {
                every { view.getAllAvailableCartDataList() } answers {
                    cartShops.flatMap {
                        it.shopGroupAvailableData?.cartItemDataList ?: mutableListOf()
                    }.map {
                        it.cartItemData ?: CartItemData()
                    }
                }
            }

            When("recalculate subtotal") {
                cartListPresenter.reCalculateSubTotal(cartShops)
            }

            Then("should have no subtotal and no cashback") {
                verify {
                    view.updateCashback(0.0)
                    view.renderDetailInfoSubTotal("0", 0.0, 0.0, false, true, false)
                }
            }
        }

        Scenario("some item selected") {

            Given("check some items") {
                firstItemFirst.isSelected = true
                firstShop.isPartialSelected = true

                secondItemSecond.isSelected = true
                secondShop.isPartialSelected = true
            }

            Given("cart data list") {
                every { view.getAllAvailableCartDataList() } answers {
                    cartShops.flatMap {
                        it.shopGroupAvailableData?.cartItemDataList ?: mutableListOf()
                    }.map {
                        it.cartItemData ?: CartItemData()
                    }
                }
            }

            When("recalculate subtotal") {
                cartListPresenter.reCalculateSubTotal(cartShops)
            }

            Then("should have 1004 subtotal and 100 cashback") {
                verify {
                    view.updateCashback(100.0)
                    view.renderDetailInfoSubTotal("5", 1004.0, 1004.0, false, false, false)
                }
            }
        }

        Scenario("some item error") {

            Given("error in unselected items") {
                firstItemFirst.isSelected = true
                secondItemFirstData.isError = true
                firstShop.isPartialSelected = true

                secondShopData.isError = true
            }

            Given("cart data list") {
                every { view.getAllAvailableCartDataList() } answers {
                    cartShops.flatMap {
                        it.shopGroupAvailableData?.cartItemDataList ?: mutableListOf()
                    }.map {
                        it.cartItemData ?: CartItemData()
                    }
                }
            }

            When("recalculate subtotal") {
                cartListPresenter.reCalculateSubTotal(cartShops)
            }

            Then("should have 1000 subtotal, 100 cashback and selected all item") {
                verify {
                    view.updateCashback(100.0)
                    view.renderDetailInfoSubTotal("1", 1000.0, 1000.0, true, false, false)
                }
            }
        }

        Scenario("all item selected") {

            Given("check all items") {
                firstItemFirst.isSelected = true
                secondItemFirst.isSelected = true
                firstShop.setAllItemSelected(true)

                firstItemSecond.isSelected = true
                secondItemSecond.isSelected = true
                secondShop.setAllItemSelected(true)
            }

            Given("cart data list") {
                every { view.getAllAvailableCartDataList() } answers {
                    cartShops.flatMap {
                        it.shopGroupAvailableData?.cartItemDataList ?: mutableListOf()
                    }.map {
                        it.cartItemData ?: CartItemData()
                    }
                }
            }

            When("recalculate subtotal") {
                cartListPresenter.reCalculateSubTotal(cartShops)
            }

            Then("should have 1684 subtotal and 100 cashback") {
                verify {
                    view.updateCashback(100.0)
                    view.renderDetailInfoSubTotal("10", 1684.0, 1684.0, true, false, false)
                }
            }
        }

        Scenario("all item selected with wholesale price") {

            Given("check all items") {
                firstItemFirst.isSelected = true
                secondItemFirst.isSelected = true
                firstShop.setAllItemSelected(true)

                firstItemSecond.isSelected = true
                secondItemSecond.isSelected = true
                secondShop.setAllItemSelected(true)
            }

            Given("wholesale price") {
                val wholesalePriceData = WholesalePriceData().apply {
                    qtyMin = 5
                    prdPrc = 100
                }
                firstItemFirstOriginData.wholesalePriceData = arrayListOf(wholesalePriceData)
                firstItemFirstUpdatedData.quantity = 10
            }

            Given("cart data list") {
                every { view.getAllAvailableCartDataList() } answers {
                    cartShops.flatMap {
                        it.shopGroupAvailableData?.cartItemDataList ?: mutableListOf()
                    }.map {
                        it.cartItemData ?: CartItemData()
                    }
                }
            }

            When("recalculate subtotal") {
                cartListPresenter.reCalculateSubTotal(cartShops)
            }

            Then("should have 1684 subtotal from 19 items and 100 cashback") {
                verify {
                    view.updateCashback(100.0)
                    view.renderDetailInfoSubTotal("19", 1684.0, 1684.0, true, false, false)
                }
            }
        }

        Scenario("all item selected with invalid wholesale price") {

            Given("check all items") {
                firstItemFirst.isSelected = true
                secondItemFirst.isSelected = true
                firstShop.setAllItemSelected(true)

                firstItemSecond.isSelected = true
                secondItemSecond.isSelected = true
                secondShop.setAllItemSelected(true)
            }

            Given("wholesale price") {
                val wholesalePriceData = WholesalePriceData().apply {
                    qtyMin = 10
                    prdPrc = 100
                }
                firstItemFirstOriginData.wholesalePriceData = arrayListOf(wholesalePriceData)
            }

            Given("cart data list") {
                every { view.getAllAvailableCartDataList() } answers {
                    cartShops.flatMap {
                        it.shopGroupAvailableData?.cartItemDataList ?: mutableListOf()
                    }.map {
                        it.cartItemData ?: CartItemData()
                    }
                }
            }

            When("recalculate subtotal") {
                cartListPresenter.reCalculateSubTotal(cartShops)
            }

            Then("should have 1684 subtotal and 100 cashback") {
                verify {
                    view.updateCashback(100.0)
                    view.renderDetailInfoSubTotal("10", 1684.0, 1684.0, true, false, false)
                }
            }
        }

        Scenario("all item selected with product variant") {

            Given("check all items") {
                firstItemFirst.isSelected = true
                secondItemFirst.isSelected = true
                firstShop.setAllItemSelected(true)

                firstItemSecond.isSelected = true
                secondItemSecond.isSelected = true
                secondShop.setAllItemSelected(true)
            }

            Given("product variant") {
                firstItemFirstOriginData.parentId = "9"
                secondItemFirstOriginData.parentId = "9"
                secondItemFirstOriginData.isCashBack = firstItemFirstOriginData.isCashBack
                secondItemFirstOriginData.productCashBack = firstItemFirstOriginData.productCashBack
            }

            Given("cart data list") {
                every { view.getAllAvailableCartDataList() } answers {
                    cartShops.flatMap {
                        it.shopGroupAvailableData?.cartItemDataList ?: mutableListOf()
                    }.map {
                        it.cartItemData ?: CartItemData()
                    }
                }
            }

            When("recalculate subtotal") {
                cartListPresenter.reCalculateSubTotal(cartShops)
            }

            Then("should have 1684 subtotal and 160 cashback") {
                verify {
                    view.updateCashback(160.0)
                    view.renderDetailInfoSubTotal("10", 1684.0, 1684.0, true, false, false)
                }
            }
        }

        Scenario("all item selected with same priced product variant") {

            Given("check all items") {
                firstItemFirst.isSelected = true
                secondItemFirst.isSelected = true
                firstShop.setAllItemSelected(true)

                firstItemSecond.isSelected = true
                secondItemSecond.isSelected = true
                secondShop.setAllItemSelected(true)
            }

            Given("product variant with same price") {
                firstItemFirstOriginData.parentId = "9"
                secondItemFirstOriginData.parentId = "9"
                secondItemFirstOriginData.pricePlan = firstItemFirstOriginData.pricePlan
                secondItemFirstOriginData.isCashBack = firstItemFirstOriginData.isCashBack
                secondItemFirstOriginData.productCashBack = firstItemFirstOriginData.productCashBack
            }

            Given("cart data list") {
                every { view.getAllAvailableCartDataList() } answers {
                    cartShops.flatMap {
                        it.shopGroupAvailableData?.cartItemDataList ?: mutableListOf()
                    }.map {
                        it.cartItemData ?: CartItemData()
                    }
                }
            }

            When("recalculate subtotal") {
                cartListPresenter.reCalculateSubTotal(cartShops)
            }

            Then("should have 4084 subtotal and 400 cashback") {
                verify {
                    view.updateCashback(400.0)
                    view.renderDetailInfoSubTotal("10", 4084.0, 4084.0, true, false, false)
                }
            }
        }
    }

})