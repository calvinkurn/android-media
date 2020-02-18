package com.tokopedia.purchase_platform.features.cart.view.presenter

import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
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
import com.tokopedia.purchase_platform.features.cart.view.uimodel.CartItemHolderData
import com.tokopedia.purchase_platform.features.cart.view.uimodel.CartShopHolderData
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

object CartListPresenterUpdateCartTest : Spek({

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
    val updateCartCounterUseCase: UpdateCartCounterUseCase = mockk()
    val view: ICartListView = mockk(relaxed = true)

    Feature("update cart list") {

        val cartListPresenter by memoized {
            CartListPresenter(
                    getCartListSimplifiedUseCase, deleteCartListUseCase, updateCartUseCase,
                    checkPromoStackingCodeUseCase, compositeSubscription, addWishListUseCase,
                    removeWishListUseCase, updateAndReloadCartUseCase, userSessionInterface,
                    clearCacheAutoApplyStackUseCase, getRecentViewUseCase, getWishlistUseCase,
                    getRecommendationUseCase, addToCartUseCase, getInsuranceCartUseCase,
                    removeInsuranceProductUsecase, updateInsuranceProductDataUsecase,
                    seamlessLoginUsecase, updateCartCounterUseCase, TestSchedulers
            )
        }

        beforeEachTest {
            cartListPresenter.attachView(view)
        }

        Scenario("success update cart") {

            val updateCartData = UpdateCartData().apply {
                isSuccess = true
            }
            val cartItemDataList = mutableListOf<CartItemData>().apply {
                add(CartItemData().apply {
                    originData = CartItemData.OriginData().apply {
                        isCod = true
                        pricePlan = 1000.0
                    }
                    updatedData = CartItemData.UpdatedData().apply {
                        quantity = 10
                    }
                })
            }

            Given("update cart data") {
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            Given("shop data list") {
                every { view.getAllSelectedCartDataList() } answers { cartItemDataList }
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartData(false)
            }

            Then("should render success") {
                verify {
                    view.renderToShipmentFormSuccess(any(), cartItemDataList, any(), any())
                }
            }
        }

        Scenario("success update cart with eligible COD") {

            val updateCartData = UpdateCartData().apply {
                isSuccess = true
            }
            val cartItemDataList = mutableListOf<CartItemData>().apply {
                add(CartItemData().apply {
                    originData = CartItemData.OriginData().apply {
                        isCod = true
                        pricePlan = 1000.0
                    }
                    updatedData = CartItemData.UpdatedData().apply {
                        quantity = 10
                    }
                })
            }

            Given("update cart data") {
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            Given("shop data list") {
                every { view.getAllSelectedCartDataList() } answers { cartItemDataList }
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartData(false)
            }

            Then("should render success with eligible COD") {
                verify {
                    view.renderToShipmentFormSuccess(any(), any(), true, any())
                }
            }
        }

        Scenario("success update cart with not eligible COD") {

            val updateCartData = UpdateCartData().apply {
                isSuccess = true
            }
            val cartItemDataList = mutableListOf<CartItemData>().apply {
                add(CartItemData().apply {
                    originData = CartItemData.OriginData().apply {
                        isCod = false
                        pricePlan = 1000000.0
                    }
                    updatedData = CartItemData.UpdatedData().apply {
                        quantity = 10
                    }
                })
            }

            Given("update cart data") {
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            Given("shop data list") {
                every { view.getAllSelectedCartDataList() } answers { cartItemDataList }
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartData(false)
            }

            Then("should render success with not eligible COD") {
                verify {
                    view.renderToShipmentFormSuccess(any(), any(), false, any())
                }
            }
        }

        Scenario("success update cart with item change state ITEM_CHECKED_ALL_WITHOUT_CHANGES") {

            val updateCartData = UpdateCartData().apply {
                isSuccess = true
            }

            Given("update cart data") {
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            Given("state user have not uncheck and recheck item") {
                cartListPresenter.setHasPerformChecklistChange(false)
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartData(false)
            }

            Then("should render success with item change state ITEM_CHECKED_ALL_WITHOUT_CHANGES") {
                verify {
                    view.renderToShipmentFormSuccess(any(), any(), any(), CartListPresenter.ITEM_CHECKED_ALL_WITHOUT_CHANGES)
                }
            }
        }

        Scenario("success update cart with item change state ITEM_CHECKED_ALL_WITH_CHANGES") {

            val updateCartData = UpdateCartData().apply {
                isSuccess = true
            }

            Given("update cart data") {
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            Given("state user have uncheck and recheck item") {
                cartListPresenter.setHasPerformChecklistChange(true)
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartData(false)
            }

            Then("should render success with item change state ITEM_CHECKED_ALL_WITH_CHANGES") {
                verify {
                    view.renderToShipmentFormSuccess(any(), any(), any(), CartListPresenter.ITEM_CHECKED_ALL_WITH_CHANGES)
                }
            }
        }

        Scenario("success update cart with item change state ITEM_CHECKED_PARTIAL_ITEM") {

            val updateCartData = UpdateCartData().apply {
                isSuccess = true
            }
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

            Given("update cart data") {
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            Given("shop data list") {
                every { view.getAllShopDataList() } answers { shopDataList }
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartData(false)
            }

            Then("should render success with item change state ITEM_CHECKED_PARTIAL_ITEM") {
                verify {
                    view.renderToShipmentFormSuccess(any(), any(), any(), CartListPresenter.ITEM_CHECKED_PARTIAL_ITEM)
                }
            }
        }

        Scenario("success update cart with item change state ITEM_CHECKED_PARTIAL_SHOP") {

            val updateCartData = UpdateCartData().apply {
                isSuccess = true
            }
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

            Given("update cart data") {
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            Given("shop data list") {
                every { view.getAllShopDataList() } answers { shopDataList }
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartData(false)
            }

            Then("should render success with item change state ITEM_CHECKED_PARTIAL_SHOP") {
                verify {
                    view.renderToShipmentFormSuccess(any(), any(), any(), CartListPresenter.ITEM_CHECKED_PARTIAL_SHOP)
                }
            }
        }

        Scenario("success update cart with item change state ITEM_CHECKED_PARTIAL_SHOP_AND_ITEM") {

            val updateCartData = UpdateCartData().apply {
                isSuccess = true
            }
            val shopDataList = mutableListOf<CartShopHolderData>().apply {
                add(CartShopHolderData().apply {
                    shopGroupAvailableData = ShopGroupAvailableData().apply {
                        cartItemHolderDataList = mutableListOf<CartItemHolderData>().apply {
                            add(CartItemHolderData(
                                    cartItemData = null,
                                    isSelected = true
                            ))
                            add(CartItemHolderData(
                                    cartItemData = null,
                                    isSelected = false
                            ))
                        }
                    }
                    isAllSelected = false
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

            Given("update cart data") {
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            Given("shop data list") {
                every { view.getAllShopDataList() } answers { shopDataList }
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartData(false)
            }

            Then("should render success with item change state ITEM_CHECKED_PARTIAL_SHOP_AND_ITEM") {
                verify {
                    view.renderToShipmentFormSuccess(any(), any(), any(), CartListPresenter.ITEM_CHECKED_PARTIAL_SHOP_AND_ITEM)
                }
            }
        }

        Scenario("failed update cart") {

            val updateCartData = UpdateCartData().apply {
                isSuccess = false
                message = "Error message"
            }

            Given("update cart data") {
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartData(false)
            }

            Then("should render error") {
                verify {
                    view.renderErrorToShipmentForm(updateCartData.message)
                }
            }
        }

        Scenario("failed update cart with exception") {

            val exception = CartResponseErrorException("Error message")

            Given("update cart data") {
                every { updateCartUseCase.createObservable(any()) } returns Observable.error(exception)
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartData(false)
            }

            Then("should render error") {
                verify {
                    view.renderErrorToShipmentForm(exception)
                }
            }
        }

    }

})