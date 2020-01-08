package com.tokopedia.purchase_platform.features.cart.view

import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.purchase_platform.common.domain.schedulers.TestSchedulers
import com.tokopedia.purchase_platform.common.domain.usecase.GetInsuranceCartUseCase
import com.tokopedia.purchase_platform.common.domain.usecase.RemoveInsuranceProductUsecase
import com.tokopedia.purchase_platform.common.domain.usecase.UpdateInsuranceProductDataUsecase
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartItemData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.ShopGroupAvailableData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.WholesalePriceData
import com.tokopedia.purchase_platform.features.cart.domain.usecase.*
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartItemHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartShopHolderData
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
import rx.subscriptions.CompositeSubscription

class CartListPresenterTest : Spek({

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

    Feature("calculate subtotal") {

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

        Scenario("no item selected") {

            val view: ICartListView = mockk(relaxed = true)

            Given("attach view") {
                cartListPresenter.attachView(view)
                every { view.getAllAvailableCartDataList() } answers {
                    cartShops.flatMap {
                        it.shopGroupAvailableData.cartItemDataList ?: mutableListOf()
                    }.map {
                        it.cartItemData ?: CartItemData()
                    }
                }
            }

            When("recalculate subtotal") {
                cartListPresenter.reCalculateSubTotal(cartShops, arrayListOf())
            }

            Then("should have no subtotal and no cashback") {
                verify {
                    view.updateCashback(0.0)
                    view.renderDetailInfoSubTotal("0", "-", false, true, false)
                }
            }
        }

        Scenario("some item selected") {

            val view: ICartListView = mockk(relaxed = true)

            Given("check some items") {
                firstItemFirst.isSelected = true
                firstShop.isPartialSelected = true

                secondItemSecond.isSelected = true
                secondShop.isPartialSelected = true
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
                every { view.getAllAvailableCartDataList() } answers {
                    cartShops.flatMap {
                        it.shopGroupAvailableData.cartItemDataList ?: mutableListOf()
                    }.map {
                        it.cartItemData ?: CartItemData()
                    }
                }
            }

            When("recalculate subtotal") {
                cartListPresenter.reCalculateSubTotal(cartShops, arrayListOf())
            }

            Then("should have 1004 subtotal and 100 cashback") {
                verify {
                    view.updateCashback(100.0)
                    view.renderDetailInfoSubTotal("5", "Rp1.004", false, false, false)
                }
            }
        }

        Scenario("some item error") {

            val view: ICartListView = mockk(relaxed = true)

            Given("error in unselected items") {
                firstItemFirst.isSelected = true
                secondItemFirstData.isError = true
                firstShop.isPartialSelected = true

                secondShopData.isError = true
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
                every { view.getAllAvailableCartDataList() } answers {
                    cartShops.flatMap {
                        it.shopGroupAvailableData.cartItemDataList ?: mutableListOf()
                    }.map {
                        it.cartItemData ?: CartItemData()
                    }
                }
            }

            When("recalculate subtotal") {
                cartListPresenter.reCalculateSubTotal(cartShops, arrayListOf())
            }

            Then("should have 1000 subtotal, 100 cashback and selected all item") {
                verify {
                    view.updateCashback(100.0)
                    view.renderDetailInfoSubTotal("1", "Rp1.000", true, false, false)
                }
            }
        }

        Scenario("all item selected") {

            val view: ICartListView = mockk(relaxed = true)

            Given("check all items") {
                firstItemFirst.isSelected = true
                secondItemFirst.isSelected = true
                firstShop.isAllSelected = true

                firstItemSecond.isSelected = true
                secondItemSecond.isSelected = true
                secondShop.isAllSelected = true
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
                every { view.getAllAvailableCartDataList() } answers {
                    cartShops.flatMap {
                        it.shopGroupAvailableData.cartItemDataList ?: mutableListOf()
                    }.map {
                        it.cartItemData ?: CartItemData()
                    }
                }
            }

            When("recalculate subtotal") {
                cartListPresenter.reCalculateSubTotal(cartShops, arrayListOf())
            }

            Then("should have 1684 subtotal and 100 cashback") {
                verify {
                    view.updateCashback(100.0)
                    view.renderDetailInfoSubTotal("10", "Rp1.684", true, false, false)
                }
            }
        }

        Scenario("all item selected with wholesale price") {

            val view: ICartListView = mockk(relaxed = true)

            Given("check all items") {
                firstItemFirst.isSelected = true
                secondItemFirst.isSelected = true
                firstShop.isAllSelected = true

                firstItemSecond.isSelected = true
                secondItemSecond.isSelected = true
                secondShop.isAllSelected = true
            }

            Given("wholesale price") {
                val wholesalePriceData = WholesalePriceData().apply {
                    qtyMin = 5
                    prdPrc = 100
                }
                firstItemFirstOriginData.wholesalePriceData = arrayListOf(wholesalePriceData)
                firstItemFirstUpdatedData.quantity = 10
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
                every { view.getAllAvailableCartDataList() } answers {
                    cartShops.flatMap {
                        it.shopGroupAvailableData.cartItemDataList ?: mutableListOf()
                    }.map {
                        it.cartItemData ?: CartItemData()
                    }
                }
            }

            When("recalculate subtotal") {
                cartListPresenter.reCalculateSubTotal(cartShops, arrayListOf())
            }

            Then("should have 1684 subtotal from 19 items and 100 cashback") {
                verify {
                    view.updateCashback(100.0)
                    view.renderDetailInfoSubTotal("19", "Rp1.684", true, false, false)
                }
            }
        }

        Scenario("all item selected with invalid wholesale price") {

            val view: ICartListView = mockk(relaxed = true)

            Given("check all items") {
                firstItemFirst.isSelected = true
                secondItemFirst.isSelected = true
                firstShop.isAllSelected = true

                firstItemSecond.isSelected = true
                secondItemSecond.isSelected = true
                secondShop.isAllSelected = true
            }

            Given("wholesale price") {
                val wholesalePriceData = WholesalePriceData().apply {
                    qtyMin = 10
                    prdPrc = 100
                }
                firstItemFirstOriginData.wholesalePriceData = arrayListOf(wholesalePriceData)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
                every { view.getAllAvailableCartDataList() } answers {
                    cartShops.flatMap {
                        it.shopGroupAvailableData.cartItemDataList ?: mutableListOf()
                    }.map {
                        it.cartItemData ?: CartItemData()
                    }
                }
            }

            When("recalculate subtotal") {
                cartListPresenter.reCalculateSubTotal(cartShops, arrayListOf())
            }

            Then("should have 1684 subtotal and 100 cashback") {
                verify {
                    view.updateCashback(100.0)
                    view.renderDetailInfoSubTotal("10", "Rp1.684", true, false, false)
                }
            }
        }

        Scenario("all item selected with product variant") {

            val view: ICartListView = mockk(relaxed = true)

            Given("check all items") {
                firstItemFirst.isSelected = true
                secondItemFirst.isSelected = true
                firstShop.isAllSelected = true

                firstItemSecond.isSelected = true
                secondItemSecond.isSelected = true
                secondShop.isAllSelected = true
            }

            Given("product variant") {
                firstItemFirstOriginData.parentId = "9"
                secondItemFirstOriginData.parentId = "9"
                secondItemFirstOriginData.isCashBack = firstItemFirstOriginData.isCashBack
                secondItemFirstOriginData.productCashBack = firstItemFirstOriginData.productCashBack
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
                every { view.getAllAvailableCartDataList() } answers {
                    cartShops.flatMap {
                        it.shopGroupAvailableData.cartItemDataList ?: mutableListOf()
                    }.map {
                        it.cartItemData ?: CartItemData()
                    }
                }
            }

            When("recalculate subtotal") {
                cartListPresenter.reCalculateSubTotal(cartShops, arrayListOf())
            }

            Then("should have 1684 subtotal and 160 cashback") {
                verify {
                    view.updateCashback(160.0)
                    view.renderDetailInfoSubTotal("10", "Rp1.684", true, false, false)
                }
            }
        }

        Scenario("all item selected with same priced product variant") {

            val view: ICartListView = mockk(relaxed = true)

            Given("check all items") {
                firstItemFirst.isSelected = true
                secondItemFirst.isSelected = true
                firstShop.isAllSelected = true

                firstItemSecond.isSelected = true
                secondItemSecond.isSelected = true
                secondShop.isAllSelected = true
            }

            Given("product variant with same price") {
                firstItemFirstOriginData.parentId = "9"
                secondItemFirstOriginData.parentId = "9"
                secondItemFirstOriginData.pricePlan = firstItemFirstOriginData.pricePlan
                secondItemFirstOriginData.isCashBack = firstItemFirstOriginData.isCashBack
                secondItemFirstOriginData.productCashBack = firstItemFirstOriginData.productCashBack
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
                every { view.getAllAvailableCartDataList() } answers {
                    cartShops.flatMap {
                        it.shopGroupAvailableData.cartItemDataList ?: mutableListOf()
                    }.map {
                        it.cartItemData ?: CartItemData()
                    }
                }
            }

            When("recalculate subtotal") {
                cartListPresenter.reCalculateSubTotal(cartShops, arrayListOf())
            }

            Then("should have 4084 subtotal and 400 cashback") {
                verify {
                    view.updateCashback(400.0)
                    view.renderDetailInfoSubTotal("10", "Rp4.084", true, false, false)
                }
            }
        }
    }

})