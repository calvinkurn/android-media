package com.tokopedia.purchase_platform.features.cart.view.presenter

import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckout.common.data.entity.request.Promo
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.promocheckout.common.view.model.PromoStackingData
import com.tokopedia.promocheckout.common.view.uimodel.*
import com.tokopedia.purchase_platform.common.domain.schedulers.TestSchedulers
import com.tokopedia.purchase_platform.common.domain.usecase.GetInsuranceCartUseCase
import com.tokopedia.purchase_platform.common.domain.usecase.RemoveInsuranceProductUsecase
import com.tokopedia.purchase_platform.common.domain.usecase.UpdateInsuranceProductDataUsecase
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.domain.model.VoucherOrdersItemData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartItemData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.DeleteCartData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.ShopGroupAvailableData
import com.tokopedia.purchase_platform.features.cart.domain.usecase.*
import com.tokopedia.purchase_platform.features.cart.view.CartListPresenter
import com.tokopedia.purchase_platform.features.cart.view.ICartListView
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartItemHolderData
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

class CartListPresenterPromoTest : Spek({

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

    Feature("check promo first step after clash") {

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

        Scenario("apply promo global after clash success and grey state") {

            val view: ICartListView = mockk(relaxed = true)

            Given("success check first step") {
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
                every { checkPromoStackingCodeUseCase.createObservable(any()) } returns Observable.just(promoStackUiModel)
            }

            Given("data promo stack") {
                every { checkPromoStackingCodeUseCase.setParams(any()) } just Runs
            }

            Given("cart list data") {
                cartListPresenter.setCartListData(CartListData().apply {
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
                })
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process apply promo") {
                val promoList = arrayListOf<ClashingVoucherOrderUiModel>()
                promoList.add(ClashingVoucherOrderUiModel(uniqueId = "", code = "codeGlobal"))

                cartListPresenter.processApplyPromoStackAfterClash(PromoStackingData(), promoList, "")
            }

            Then("should render success") {
                verify {
                    view.hideProgressLoading()
                    view.onSuccessCheckPromoFirstStep(any())
                }
            }
        }

        Scenario("apply promo global after clash success and red state") {

            val view: ICartListView = mockk(relaxed = true)

            Given("success check first step") {
                val promoStackUiModel = ResponseGetPromoStackUiModel().apply {
                    status = "OK"
                    data = DataUiModel().apply {
                        clashings = ClashingInfoDetailUiModel().apply {
                            isClashedPromos = false
                        }
                        message = MessageUiModel().apply {
                            state = "red"
                        }
                    }
                }
                every { checkPromoStackingCodeUseCase.createObservable(any()) } returns Observable.just(promoStackUiModel)
            }

            Given("data promo stack") {
                every { checkPromoStackingCodeUseCase.setParams(any()) } just Runs
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process apply promo") {
                val promoList = arrayListOf<ClashingVoucherOrderUiModel>()
                promoList.add(ClashingVoucherOrderUiModel(uniqueId = "", code = "codeGlobal"))
                cartListPresenter.processApplyPromoStackAfterClash(PromoStackingData(), promoList, "")
            }

            Then("should render success") {
                verify {
                    view.hideProgressLoading()
                    view.showToastMessageRed(any())
                }
            }
        }

        Scenario("apply promo merchant after clash success and grey state") {

            val view: ICartListView = mockk(relaxed = true)

            Given("success check first step") {
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
                every { checkPromoStackingCodeUseCase.createObservable(any()) } returns Observable.just(promoStackUiModel)
            }

            Given("data promo stack") {
                every { checkPromoStackingCodeUseCase.setParams(any()) } just Runs
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process apply promo") {
                val promoList = arrayListOf<ClashingVoucherOrderUiModel>()
                promoList.add(ClashingVoucherOrderUiModel().apply {
                    uniqueId = "12345"
                })
                cartListPresenter.processApplyPromoStackAfterClash(PromoStackingData(), promoList, "")
            }

            Then("should render success") {
                verify {
                    view.hideProgressLoading()
                    view.onSuccessCheckPromoFirstStep(any())
                }
            }
        }

        Scenario("apply promo merchant after clash success and red state") {

            val view: ICartListView = mockk(relaxed = true)

            Given("success check first step") {
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
                                }
                            })
                        }
                    }
                }
                every { checkPromoStackingCodeUseCase.createObservable(any()) } returns Observable.just(promoStackUiModel)
            }

            Given("data promo stack") {
                every { checkPromoStackingCodeUseCase.setParams(any()) } just Runs
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process apply promo") {
                val promoList = arrayListOf<ClashingVoucherOrderUiModel>()
                promoList.add(ClashingVoucherOrderUiModel(uniqueId = ""))
                cartListPresenter.processApplyPromoStackAfterClash(PromoStackingData(), promoList, "")
            }

            Then("should render success") {
                verify {
                    view.hideProgressLoading()
                    view.showToastMessageRed(any())
                }
            }
        }

/*
        Scenario("check promo first step after clash still clash") {

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

        Scenario("check promo first step after clash error") {

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

        Scenario("check promo first step after clash error with exception") {

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
*/

    }

})