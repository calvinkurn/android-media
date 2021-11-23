package com.tokopedia.cart.bundle.view.presenter

import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.bundle.data.model.response.shopgroupsimplified.WholesalePrice
import com.tokopedia.cart.bundle.domain.usecase.*
import com.tokopedia.cart.bundle.view.CartListPresenter
import com.tokopedia.cart.bundle.view.ICartListView
import com.tokopedia.cart.bundle.view.uimodel.CartItemHolderData
import com.tokopedia.cart.bundle.view.uimodel.CartShopHolderData
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
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.subscriptions.CompositeSubscription

object CartListPresenterCalculateSubTotalTest : Spek({

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

    Feature("calculate subtotal") {

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

        //region First Item In First Shop
        val firstProductFirstShop by memoized {
            CartItemHolderData().apply {
                productPrice = 1000
                parentId = "0"
                productId = "1"
                productCashBack = "10%"
                quantity = 1
            }
        }
        //endregion

        //region First Item In Second Shop
        val firstProductSecondShop by memoized {
            CartItemHolderData().apply {
                productPrice = 40
                parentId = "0"
                productId = "2"
                quantity = 2
            }
        }
        //endregion

        //region Second Item In First Shop
        val secondProductFirstShop by memoized {
            CartItemHolderData().apply {
                productPrice = 200
                parentId = "0"
                productId = "3"
                quantity = 3
            }
        }
        //endregion

        //region Second Item In Second Shop
        val secondProductSecondShop by memoized {
            CartItemHolderData().apply {
                productPrice = 1
                parentId = "0"
                productId = "4"
                quantity = 4
            }
        }
        //endregion

        //region First Item In Third Shop, bundling product
        val firstProductThirdShop by memoized {
            CartItemHolderData().apply {
                isBundlingItem = true
                bundleId = "123"
                bundleGroupId = "123-abc"
                bundlePrice = 50000
                bundleQuantity = 2
                quantity = 1
            }
        }
        //endregion

        //region First Shop
        val firstShop by memoized {
            CartShopHolderData().apply {
                productUiModelList = arrayListOf(firstProductFirstShop, secondProductFirstShop)
            }
        }
        //endregion

        //region Second Shop
        val secondShop by memoized {
            CartShopHolderData().apply {
                productUiModelList = arrayListOf(firstProductSecondShop, secondProductSecondShop)
            }
        }
        //endregion

        //region Second Shop
        val thirdShop by memoized {
            CartShopHolderData().apply {
                productUiModelList = arrayListOf(firstProductThirdShop)
            }
        }
        //endregion

        val cartShops by memoized { arrayListOf(firstShop, secondShop) }
        val cartShopsIncludingBundleItems by memoized { arrayListOf(firstShop, secondShop, thirdShop) }

        beforeEachTest {
            cartListPresenter.attachView(view)
        }

        Scenario("no item selected") {

            Given("cart data list") {
                every { view.getAllAvailableCartDataList() } answers {
                    cartShops.flatMap {
                        it.productUiModelList
                    }
                }
            }

            When("recalculate subtotal") {
                cartListPresenter.reCalculateSubTotal(cartShops)
            }

            Then("should have no subtotal and no cashback") {
                verify {
                    view.updateCashback(0.0)
                    view.renderDetailInfoSubTotal("0", 0.0, false)
                }
            }
        }

        Scenario("some item selected") {

            Given("check some items") {
                firstProductFirstShop.isSelected = true
                firstShop.isPartialSelected = true

                secondProductSecondShop.isSelected = true
                secondShop.isPartialSelected = true
            }

            Given("cart data list") {
                every { view.getAllAvailableCartDataList() } answers {
                    cartShops.flatMap {
                        it.productUiModelList
                    }
                }
            }

            When("recalculate subtotal") {
                cartListPresenter.reCalculateSubTotal(cartShops)
            }

            Then("should have 1004 subtotal and 100 cashback") {
                verify {
                    view.updateCashback(100.0)
                    view.renderDetailInfoSubTotal("5", 1004.0, false)
                }
            }
        }

        Scenario("all item selected") {

            Given("check all items") {
                firstProductFirstShop.isSelected = true
                secondProductFirstShop.isSelected = true
                firstShop.isAllSelected = true

                firstProductSecondShop.isSelected = true
                secondProductSecondShop.isSelected = true
                secondShop.isAllSelected = true
            }

            Given("cart data list") {
                every { view.getAllAvailableCartDataList() } answers {
                    cartShops.flatMap {
                        it.productUiModelList
                    }
                }
            }

            When("recalculate subtotal") {
                cartListPresenter.reCalculateSubTotal(cartShops)
            }

            Then("should have 1684 subtotal and 100 cashback") {
                verify {
                    view.updateCashback(100.0)
                    view.renderDetailInfoSubTotal("10", 1684.0, false)
                }
            }
        }

        Scenario("all item selected with wholesale price") {

            Given("check all items") {
                firstProductFirstShop.isSelected = true
                secondProductFirstShop.isSelected = true
                firstShop.isAllSelected = true

                firstProductSecondShop.isSelected = true
                secondProductSecondShop.isSelected = true
                secondShop.isAllSelected = true
            }

            Given("wholesale price") {
                val wholesalePriceData = WholesalePrice(qtyMin = 5, prdPrc = 100)
                firstProductFirstShop.wholesalePriceData = arrayListOf(wholesalePriceData)
                firstProductFirstShop.quantity = 10
            }

            Given("cart data list") {
                every { view.getAllAvailableCartDataList() } answers {
                    cartShops.flatMap {
                        it.productUiModelList
                    }
                }
            }

            When("recalculate subtotal") {
                cartListPresenter.reCalculateSubTotal(cartShops)
            }

            Then("should have 1684 subtotal from 19 items and 100 cashback") {
                verify {
                    view.updateCashback(100.0)
                    view.renderDetailInfoSubTotal("19", 1684.0, false)
                }
            }
        }

        Scenario("all item selected with invalid wholesale price") {

            Given("check all items") {
                firstProductFirstShop.isSelected = true
                secondProductFirstShop.isSelected = true
                firstShop.isAllSelected = true

                firstProductSecondShop.isSelected = true
                secondProductSecondShop.isSelected = true
                secondShop.isAllSelected = true
            }

            Given("wholesale price") {
                val wholesalePriceData = WholesalePrice(qtyMin = 10, prdPrc = 100)
                firstProductFirstShop.wholesalePriceData = arrayListOf(wholesalePriceData)
            }

            Given("cart data list") {
                every { view.getAllAvailableCartDataList() } answers {
                    cartShops.flatMap {
                        it.productUiModelList
                    }
                }
            }

            When("recalculate subtotal") {
                cartListPresenter.reCalculateSubTotal(cartShops)
            }

            Then("should have 1684 subtotal and 100 cashback") {
                verify {
                    view.updateCashback(100.0)
                    view.renderDetailInfoSubTotal("10", 1684.0, false)
                }
            }
        }

        Scenario("all item selected with product variant") {

            Given("check all items") {
                firstProductFirstShop.isSelected = true
                secondProductFirstShop.isSelected = true
                firstShop.isAllSelected = true

                firstProductSecondShop.isSelected = true
                secondProductSecondShop.isSelected = true
                secondShop.isAllSelected = true
            }

            Given("product variant") {
                firstProductFirstShop.parentId = "9"
                secondProductFirstShop.parentId = "9"
                secondProductFirstShop.productCashBack = firstProductFirstShop.productCashBack
            }

            Given("cart data list") {
                every { view.getAllAvailableCartDataList() } answers {
                    cartShops.flatMap {
                        it.productUiModelList
                    }
                }
            }

            When("recalculate subtotal") {
                cartListPresenter.reCalculateSubTotal(cartShops)
            }

            Then("should have 1684 subtotal and 160 cashback") {
                verify {
                    view.updateCashback(160.0)
                    view.renderDetailInfoSubTotal("10", 1684.0, false)
                }
            }
        }

        Scenario("all item selected with same priced product variant") {

            Given("check all items") {
                firstProductFirstShop.isSelected = true
                secondProductFirstShop.isSelected = true
                firstShop.isAllSelected = true

                firstProductSecondShop.isSelected = true
                secondProductSecondShop.isSelected = true
                secondShop.isAllSelected = true
            }

            Given("product variant with same price") {
                firstProductSecondShop.quantity = 1
                firstProductSecondShop.productPrice = 100
                secondProductSecondShop.quantity = 1
                secondProductSecondShop.productPrice = 200

                firstProductFirstShop.parentId = "9"
                firstProductFirstShop.productPrice = 1000
                firstProductFirstShop.productCashBack = "10%"
                firstProductFirstShop.quantity = 2

                secondProductFirstShop.parentId = "9"
                secondProductFirstShop.productPrice = 1000
                secondProductFirstShop.productCashBack = "10%"
                secondProductFirstShop.quantity = 2
            }

            Given("cart data list") {
                every { view.getAllAvailableCartDataList() } answers {
                    cartShops.flatMap {
                        it.productUiModelList
                    }
                }
            }

            When("recalculate subtotal") {
                cartListPresenter.reCalculateSubTotal(cartShops)
            }

            Then("should have 4084 subtotal and 400 cashback") {
                verify {
                    view.updateCashback(400.0)
                    view.renderDetailInfoSubTotal("6", 4300.0, false)
                }
            }
        }

        Scenario("bundling item selected") {

            Given("check all items") {
                firstProductFirstShop.isSelected = true
                secondProductFirstShop.isSelected = true
                firstShop.isAllSelected = true

                firstProductSecondShop.isSelected = true
                secondProductSecondShop.isSelected = true
                secondShop.isAllSelected = true

                firstProductThirdShop.isSelected = true
                thirdShop.isAllSelected = true
            }

            Given("cart data list") {
                every { view.getAllAvailableCartDataList() } answers {
                    cartShopsIncludingBundleItems.flatMap {
                        it.productUiModelList
                    }
                }
            }

            When("recalculate subtotal") {
                cartListPresenter.reCalculateSubTotal(cartShopsIncludingBundleItems)
            }

            Then("should have 4084 subtotal and 400 cashback") {
                verify {
                    view.renderDetailInfoSubTotal("12", 101684.0, false)
                }
            }
        }
    }

})