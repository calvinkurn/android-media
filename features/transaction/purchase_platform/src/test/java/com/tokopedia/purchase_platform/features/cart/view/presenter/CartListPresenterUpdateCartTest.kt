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
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.ShopGroupAvailableData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateCartData
import com.tokopedia.purchase_platform.features.cart.domain.usecase.*
import com.tokopedia.purchase_platform.features.cart.view.CartListPresenter
import com.tokopedia.purchase_platform.features.cart.view.ICartListView
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
import rx.Observable
import rx.subscriptions.CompositeSubscription

/**
 * Created by Irfan Khoirul on 2020-01-07.
 */

class CartListPresenterUpdateCartTest : Spek({

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

    Feature("update cart list") {

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

        Scenario("success update cart") {

            val view: ICartListView = mockk(relaxed = true)
            val updateCartData = UpdateCartData()

            Given("update cart data") {
                updateCartData.isSuccess = true
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartData()
            }

            Then("should render success") {
                verify {
                    view.hideProgressLoading()
                    view.renderToShipmentFormSuccess(any(), any(), any(), any())
                }
            }
        }

        Scenario("success update cart with eligible COD") {

            val view: ICartListView = mockk(relaxed = true)
            val updateCartData = UpdateCartData()

            Given("update cart data") {
                updateCartData.isSuccess = true
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            Given("shop data list") {
                every { view.getAllSelectedCartDataList() } answers {
                    val cartItemDataList = mutableListOf<CartItemData>().apply {
                        add(CartItemData().let {
                            it.originData = CartItemData.OriginData().let { originData ->
                                originData.isCod = true
                                originData.pricePlan = 1000.0
                                originData
                            }
                            it.updatedData = CartItemData.UpdatedData().let { updatedData ->
                                updatedData.quantity = 10
                                updatedData
                            }
                            it
                        })
                    }
                    cartItemDataList
                }
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartData()
            }

            Then("should render success with eligible COD") {
                verify {
                    view.hideProgressLoading()
                    view.renderToShipmentFormSuccess(any(), any(), true, any())
                }
            }
        }

        Scenario("success update cart with not eligible COD") {

            val view: ICartListView = mockk(relaxed = true)
            val updateCartData = UpdateCartData()

            Given("update cart data") {
                updateCartData.isSuccess = true
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            Given("shop data list") {
                every { view.getAllSelectedCartDataList() } answers {
                    val cartItemDataList = mutableListOf<CartItemData>().apply {
                        add(CartItemData().let {
                            it.originData = CartItemData.OriginData().let { originData ->
                                originData.isCod = false
                                originData.pricePlan = 1000000.0
                                originData
                            }
                            it.updatedData = CartItemData.UpdatedData().let { updatedData ->
                                updatedData.quantity = 10
                                updatedData
                            }
                            it
                        })
                    }
                    cartItemDataList
                }
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartData()
            }

            Then("should render success with not eligible COD") {
                verify {
                    view.hideProgressLoading()
                    view.renderToShipmentFormSuccess(any(), any(), false, any())
                }
            }
        }

        Scenario("success update cart with item change state ITEM_CHECKED_ALL_WITHOUT_CHANGES") {

            val view: ICartListView = mockk(relaxed = true)
            val updateCartData = UpdateCartData()

            Given("update cart data") {
                updateCartData.isSuccess = true
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartData()
            }

            Then("should render success with item change state ITEM_CHECKED_ALL_WITHOUT_CHANGES") {
                verify {
                    view.hideProgressLoading()
                    view.renderToShipmentFormSuccess(any(), any(), any(), CartListPresenter.ITEM_CHECKED_ALL_WITHOUT_CHANGES)
                }
            }
        }

        Scenario("success update cart with item change state ITEM_CHECKED_PARTIAL_ITEM") {

            val view: ICartListView = mockk(relaxed = true)
            val updateCartData = UpdateCartData()

            Given("update cart data") {
                updateCartData.isSuccess = true
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            Given("shop data list") {
                every { view.getAllShopDataList() } answers {
                    val shopDataList = mutableListOf<CartShopHolderData>().apply {
                        add(CartShopHolderData().apply {
                            shopGroupAvailableData = ShopGroupAvailableData().apply {
                                cartItemHolderDataList = mutableListOf<CartItemHolderData>().apply {
                                    add(CartItemHolderData(
                                            cartItemData = null,
                                            isSelected = false
                                    ))
                                }
                            }
                        })
                    }
                    shopDataList
                }
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartData()
            }

            Then("should render success with item change state ITEM_CHECKED_PARTIAL_ITEM") {
                verify {
                    view.hideProgressLoading()
                    view.renderToShipmentFormSuccess(any(), any(), any(), CartListPresenter.ITEM_CHECKED_PARTIAL_ITEM)
                }
            }
        }

        Scenario("success update cart with item change state ITEM_CHECKED_PARTIAL_SHOP") {

            val view: ICartListView = mockk(relaxed = true)
            val updateCartData = UpdateCartData()

            Given("update cart data") {
                updateCartData.isSuccess = true
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            Given("shop data list") {
                every { view.getAllShopDataList() } answers {
                    val shopDataList = mutableListOf<CartShopHolderData>().apply {
                        add(CartShopHolderData().apply {
                            shopGroupAvailableData = ShopGroupAvailableData()
                            isAllSelected = true
                        })
                        add(CartShopHolderData().apply {
                            shopGroupAvailableData = ShopGroupAvailableData()
                            isAllSelected = false
                        })
                    }
                    shopDataList
                }
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartData()
            }

            Then("should render success with item change state ITEM_CHECKED_PARTIAL_SHOP") {
                verify {
                    view.hideProgressLoading()
                    view.renderToShipmentFormSuccess(any(), any(), any(), CartListPresenter.ITEM_CHECKED_PARTIAL_SHOP)
                }
            }
        }

        Scenario("success update cart with item change state ITEM_CHECKED_PARTIAL_ITEM") {

            val view: ICartListView = mockk(relaxed = true)
            val updateCartData = UpdateCartData()

            Given("update cart data") {
                updateCartData.isSuccess = true
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            Given("shop data list") {
                every { view.getAllShopDataList() } answers {
                    val shopDataList = mutableListOf<CartShopHolderData>().apply {
                        add(CartShopHolderData().apply {
                            shopGroupAvailableData = ShopGroupAvailableData().apply {
                                cartItemHolderDataList = mutableListOf<CartItemHolderData>().apply {
                                    add(CartItemHolderData(
                                            cartItemData = null,
                                            isSelected = true
                                    ))
                                }
                            }
                            isAllSelected = true
                        })
                        add(CartShopHolderData().apply {
                            shopGroupAvailableData = ShopGroupAvailableData().apply {
                                cartItemHolderDataList = mutableListOf<CartItemHolderData>().apply {
                                    add(CartItemHolderData(
                                            cartItemData = null,
                                            isSelected = false
                                    ))
                                }
                            }
                            isAllSelected = false
                        })
                    }
                    shopDataList
                }
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartData()
            }

            Then("should render success with item change state ITEM_CHECKED_PARTIAL_ITEM") {
                verify {
                    view.hideProgressLoading()
                    view.renderToShipmentFormSuccess(any(), any(), any(), CartListPresenter.ITEM_CHECKED_PARTIAL_SHOP_AND_ITEM)
                }
            }
        }

        Scenario("failed update cart") {

            val view: ICartListView = mockk(relaxed = true)
            val updateCartData = UpdateCartData()

            Given("update cart data") {
                updateCartData.isSuccess = false
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartData()
            }

            Then("should render error") {
                verify {
                    view.hideProgressLoading()
                    view.renderErrorToShipmentForm(any())
                }
            }
        }

        Scenario("failed update cart with CartResponseErrorException") {

            val view: ICartListView = mockk(relaxed = true)
            val errorMessage = "Error"

            Given("update cart data") {
                every { updateCartUseCase.createObservable(any()) } returns Observable.error(CartResponseErrorException(errorMessage))
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartData()
            }

            Then("should render error") {
                verify {
                    view.hideProgressLoading()
                    view.renderErrorToShipmentForm(errorMessage)
                }
            }
        }

        Scenario("failed update cart with other exception") {

            val view: ICartListView = mockk(relaxed = true)
            Given("update cart data") {
                every { updateCartUseCase.createObservable(any()) } returns Observable.error(IllegalStateException())
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartData()
            }

            Then("should render error") {
                verify {
                    view.hideProgressLoading()
                    view.renderErrorToShipmentForm(any())
                }
            }
        }

    }

})