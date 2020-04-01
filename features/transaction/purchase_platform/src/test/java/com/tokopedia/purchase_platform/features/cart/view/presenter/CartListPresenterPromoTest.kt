package com.tokopedia.purchase_platform.features.cart.view.presenter

import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.view.model.PromoStackingData
import com.tokopedia.promocheckout.common.view.uimodel.*
import com.tokopedia.purchase_platform.common.domain.schedulers.TestSchedulers
import com.tokopedia.purchase_platform.common.domain.usecase.GetInsuranceCartUseCase
import com.tokopedia.purchase_platform.common.domain.usecase.RemoveInsuranceProductUsecase
import com.tokopedia.purchase_platform.common.domain.usecase.UpdateInsuranceProductDataUsecase
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.domain.model.VoucherOrdersItemData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartItemData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.ShopGroupAvailableData
import com.tokopedia.purchase_platform.features.cart.domain.usecase.*
import com.tokopedia.purchase_platform.features.cart.view.CartListPresenter
import com.tokopedia.purchase_platform.features.cart.view.ICartListView
import com.tokopedia.purchase_platform.features.cart.view.uimodel.CartItemHolderData
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.seamless_login.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.subscriptions.CompositeSubscription

/**
 * Created by Irfan Khoirul on 2020-01-09.
 */

object CartListPresenterPromoTest : Spek({

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

    Feature("check promo first step after clash") {

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

        Scenario("apply promo global after clash success and grey state") {

            val promoStackUiModel = ResponseGetPromoStackUiModel().apply {
                status = "OK"
                data = DataUiModel().apply {
                    clashings = ClashingInfoDetailUiModel().apply {
                        isClashedPromos = false
                    }
                    message = MessageUiModel().apply {
                        state = "grey"
                    }
                }
            }
            val cartListData = CartListData().apply {
                shopGroupAvailableDataList = mutableListOf<ShopGroupAvailableData>().apply {
                    add(ShopGroupAvailableData().apply {
                        cartItemHolderDataList = mutableListOf<CartItemHolderData>().apply {
                            add(CartItemHolderData(cartItemData = CartItemData().apply {
                                originData = CartItemData.OriginData().apply {
                                    productId = "1"
                                }
                                updatedData = CartItemData.UpdatedData().apply {
                                    quantity = 1
                                }
                            }))
                        }
                        cartString = "12345-abcde"
                        shopId = "99999"
                    })
                }
            }
            val promoList = arrayListOf<ClashingVoucherOrderUiModel>().apply {
                add(ClashingVoucherOrderUiModel(uniqueId = "", code = "codeGlobal"))
            }

            Given("success check first step") {
                every { checkPromoStackingCodeUseCase.createObservable(any()) } returns Observable.just(promoStackUiModel)
            }

            Given("data promo stack") {
                every { checkPromoStackingCodeUseCase.setParams(any()) } just Runs
            }

            Given("cart list data") {
                cartListPresenter.setCartListData(cartListData)
            }

            When("process apply promo") {
                cartListPresenter.processApplyPromoStackAfterClash(PromoStackingData(), promoList, "")
            }

            Then("should render success") {
                verify {
                    view.onSuccessCheckPromoFirstStep(promoStackUiModel)
                }
            }
        }

        Scenario("apply promo global after clash success and red state") {

            val cartListData = CartListData().apply {
                shopGroupAvailableDataList = mutableListOf<ShopGroupAvailableData>().apply {
                    add(ShopGroupAvailableData().apply {
                        cartItemHolderDataList = mutableListOf<CartItemHolderData>().apply {
                            add(CartItemHolderData(cartItemData = CartItemData().apply {
                                originData = CartItemData.OriginData().apply {
                                    productId = "X"
                                }
                                updatedData = CartItemData.UpdatedData().apply {
                                    quantity = 1
                                }
                            }))
                        }
                        cartString = "12345-abcde"
                        shopId = "X"
                    })
                }
            }
            val promoStackUiModel = ResponseGetPromoStackUiModel().apply {
                status = "OK"
                data = DataUiModel().apply {
                    clashings = ClashingInfoDetailUiModel().apply {
                        isClashedPromos = false
                    }
                    message = MessageUiModel().apply {
                        state = "red"
                        text = "error"
                    }
                }
            }
            val promoList = arrayListOf<ClashingVoucherOrderUiModel>().apply {
                add(ClashingVoucherOrderUiModel(uniqueId = "", code = "codeGlobal"))
            }

            Given("success check first step") {
                every { checkPromoStackingCodeUseCase.createObservable(any()) } returns Observable.just(promoStackUiModel)
            }

            Given("data promo stack") {
                every { checkPromoStackingCodeUseCase.setParams(any()) } just Runs
            }

            Given("cart list data") {
                cartListPresenter.setCartListData(cartListData)
            }

            When("process apply promo") {
                cartListPresenter.processApplyPromoStackAfterClash(PromoStackingData(), promoList, "")
            }

            Then("should render error") {
                verify {
                    view.showToastMessageRed(promoStackUiModel.data.message.text)
                }
            }
        }

        Scenario("apply promo merchant after clash success and grey state") {

            val cartListData = CartListData().apply {
                shopGroupAvailableDataList = mutableListOf<ShopGroupAvailableData>().apply {
                    add(ShopGroupAvailableData().apply {
                        cartItemHolderDataList = mutableListOf<CartItemHolderData>().apply {
                            add(CartItemHolderData(cartItemData = CartItemData().apply {
                                originData = CartItemData.OriginData().apply {
                                    productId = "1"
                                }
                                updatedData = CartItemData.UpdatedData().apply {
                                    quantity = 1
                                }
                            }))
                        }
                        voucherOrdersItemData = VoucherOrdersItemData().apply {
                            code = "codeMerchant"
                        }
                        cartString = "12345-abcde"
                        shopId = "99999"
                    })
                }
            }
            val promoStackUiModel = ResponseGetPromoStackUiModel().apply {
                status = "OK"
                data = DataUiModel().apply {
                    clashings = ClashingInfoDetailUiModel().apply {
                        isClashedPromos = false
                    }
                    voucherOrders = mutableListOf<VoucherOrdersItemUiModel>().apply {
                        add(VoucherOrdersItemUiModel().apply {
                            message = MessageUiModel().apply {
                                state = "grey"
                            }
                        })
                    }
                }
            }
            val promoList = arrayListOf<ClashingVoucherOrderUiModel>().apply {
                add(ClashingVoucherOrderUiModel().apply {
                    code = "codeMerchant"
                    uniqueId = "12345-abcde"
                })
            }


            Given("success check first step") {
                every { checkPromoStackingCodeUseCase.createObservable(any()) } returns Observable.just(promoStackUiModel)
            }

            Given("data promo stack") {
                every { checkPromoStackingCodeUseCase.setParams(any()) } just Runs
            }

            Given("cart list data") {
                cartListPresenter.setCartListData(cartListData)
            }

            When("process apply promo") {
                cartListPresenter.processApplyPromoStackAfterClash(PromoStackingData(), promoList, "")
            }

            Then("should render success") {
                verify {
                    view.onSuccessCheckPromoFirstStep(promoStackUiModel)
                }
            }
        }

        Scenario("apply promo merchant after clash success and red state") {

            val promoStackUiModel = ResponseGetPromoStackUiModel().apply {
                status = "OK"
                data = DataUiModel().apply {
                    clashings = ClashingInfoDetailUiModel().apply {
                        isClashedPromos = false
                    }
                    voucherOrders = mutableListOf<VoucherOrdersItemUiModel>().apply {
                        add(VoucherOrdersItemUiModel().apply {
                            message = MessageUiModel().apply {
                                state = "red"
                                text = "red state"
                            }
                        })
                    }
                }
            }
            val cartListData = CartListData().apply {
                shopGroupAvailableDataList = mutableListOf<ShopGroupAvailableData>().apply {
                    add(ShopGroupAvailableData().apply {
                        cartItemHolderDataList = mutableListOf<CartItemHolderData>().apply {
                            add(CartItemHolderData(cartItemData = CartItemData().apply {
                                originData = CartItemData.OriginData().apply {
                                    productId = "1"
                                }
                                updatedData = CartItemData.UpdatedData().apply {
                                    quantity = 1
                                }
                            }))
                        }
                        voucherOrdersItemData = VoucherOrdersItemData().apply {
                            code = "codeMerchant"
                        }
                        cartString = "12345-abcde"
                        shopId = "99999"
                    })
                }
            }
            val promoList = arrayListOf<ClashingVoucherOrderUiModel>().apply {
                add(ClashingVoucherOrderUiModel(uniqueId = "12345-abcde", code = "codeMerchant"))
            }

            Given("success check first step") {
                every { checkPromoStackingCodeUseCase.createObservable(any()) } returns Observable.just(promoStackUiModel)
            }

            Given("data promo stack") {
                every { checkPromoStackingCodeUseCase.setParams(any()) } just Runs
            }

            Given("cart list data") {
                cartListPresenter.setCartListData(cartListData)
            }

            When("process apply promo") {
                cartListPresenter.processApplyPromoStackAfterClash(PromoStackingData(), promoList, "")
            }

            Then("should render success") {
                verify {
                    view.showToastMessageRed(promoStackUiModel.data.voucherOrders[0].message.text)
                }
            }
        }

        Scenario("apply promo global after clash success but still clashing") {

            val promoStackUiModel = ResponseGetPromoStackUiModel().apply {
                status = "OK"
                data = DataUiModel().apply {
                    clashings = ClashingInfoDetailUiModel().apply {
                        isClashedPromos = true
                    }
                }
            }
            val cartListData = CartListData().apply {
                shopGroupAvailableDataList = mutableListOf<ShopGroupAvailableData>().apply {
                    add(ShopGroupAvailableData().apply {
                        cartItemHolderDataList = mutableListOf<CartItemHolderData>().apply {
                            add(CartItemHolderData(cartItemData = CartItemData().apply {
                                originData = CartItemData.OriginData().apply {
                                    productId = "1"
                                }
                                updatedData = CartItemData.UpdatedData().apply {
                                    quantity = 1
                                }
                            }))
                        }
                        cartString = "12345-abcde"
                        shopId = "99999"
                    })
                }
            }
            val type = "merchant"
            val promoList = arrayListOf<ClashingVoucherOrderUiModel>().apply {
                add(ClashingVoucherOrderUiModel(uniqueId = "", code = "codeGlobal"))
            }

            Given("success check first step") {
                every { checkPromoStackingCodeUseCase.createObservable(any()) } returns Observable.just(promoStackUiModel)
            }

            Given("data promo stack") {
                every { checkPromoStackingCodeUseCase.setParams(any()) } just Runs
            }

            Given("cart list data") {
                cartListPresenter.setCartListData(cartListData)
            }

            When("process apply promo") {
                cartListPresenter.processApplyPromoStackAfterClash(PromoStackingData(), promoList, type)
            }

            Then("should render clash") {
                verify {
                    view.onClashCheckPromo(promoStackUiModel.data.clashings, type)
                }
            }
        }

        Scenario("apply promo global after clash error") {

            val promoStackUiModel = ResponseGetPromoStackUiModel().apply {
                status = "ERROR"
                data = DataUiModel().apply {
                    message = MessageUiModel().apply {
                        text = "error"
                    }
                }
            }
            val promoList = arrayListOf<ClashingVoucherOrderUiModel>().apply {
                add(ClashingVoucherOrderUiModel(uniqueId = "", code = "codeGlobal"))
            }

            Given("success check first step") {
                every { checkPromoStackingCodeUseCase.createObservable(any()) } returns Observable.just(promoStackUiModel)
            }

            Given("data promo stack") {
                every { checkPromoStackingCodeUseCase.setParams(any()) } just Runs
            }

            When("process apply promo") {
                cartListPresenter.processApplyPromoStackAfterClash(PromoStackingData(), promoList, "")
            }

            Then("should render error") {
                verify {
                    view.showToastMessageRed(promoStackUiModel.data.message.text)
                }
            }
        }

        Scenario("apply promo global after clash error with exception") {

            val exception = IllegalStateException()
            val promoList = arrayListOf<ClashingVoucherOrderUiModel>().apply {
                add(ClashingVoucherOrderUiModel(uniqueId = "", code = "codeGlobal"))
            }

            Given("success check first step") {
                every { checkPromoStackingCodeUseCase.createObservable(any()) } returns Observable.error(exception)
            }

            Given("data promo stack") {
                every { checkPromoStackingCodeUseCase.setParams(any()) } just Runs
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process apply promo") {
                cartListPresenter.processApplyPromoStackAfterClash(PromoStackingData(), promoList, "")
            }

            Then("should render error") {
                verify {
                    view.showToastMessageRed(exception)
                }
            }
        }
    }

})