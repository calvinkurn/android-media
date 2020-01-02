package com.tokopedia.purchase_platform.features.cart.view

import androidx.fragment.app.FragmentActivity
import com.tokopedia.abstraction.R
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.purchase_platform.common.domain.usecase.GetInsuranceCartUseCase
import com.tokopedia.purchase_platform.common.domain.usecase.RemoveInsuranceProductUsecase
import com.tokopedia.purchase_platform.common.domain.usecase.UpdateInsuranceProductDataUsecase
import com.tokopedia.purchase_platform.features.cart.data.model.response.recentview.GqlRecentViewResponse
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.*
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
import io.mockk.verifyOrder
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.Subscriber
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
    val view: ICartListView = mockk(relaxed = true)

    Feature("get cart list") {

        val cartListPresenter by memoized {
            CartListPresenter(
                    getCartListSimplifiedUseCase, deleteCartListUseCase, updateCartUseCase,
                    checkPromoStackingCodeUseCase, checkPromoStackingCodeMapper, compositeSubscription,
                    addWishListUseCase, removeWishListUseCase, updateAndReloadCartUseCase,
                    userSessionInterface, clearCacheAutoApplyStackUseCase, getRecentViewUseCase,
                    getWishlistUseCase, getRecommendationUseCase, addToCartUseCase, getInsuranceCartUseCase,
                    removeInsuranceProductUsecase, updateInsuranceProductDataUsecase, seamlessLoginUsecase
            )
        }
        val emptyCartListData = CartListData()

        Scenario("initial load") {

            Given("empty response") {
                every { getCartListSimplifiedUseCase.createObservable(any()) } returns Observable.just(emptyCartListData)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process initial get cart data") {
                cartListPresenter.processInitialGetCartData("", true, false)
            }

            Then("should render success") {
                verify {
                    view.renderInitialGetCartListDataSuccess(emptyCartListData)
                }
            }

            Then("should render then finish loading") {
                verify {
                    view.renderLoadGetCartData()
                    view.renderLoadGetCartDataFinish()
                }
            }
        }

        Scenario("refresh load") {

            Given("empty response") {
                every { getCartListSimplifiedUseCase.createObservable(any()) } returns Observable.just(emptyCartListData)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process initial get cart data") {
                cartListPresenter.processInitialGetCartData("", false, false)
            }

            Then("should render success") {
                verify {
                    view.renderInitialGetCartListDataSuccess(emptyCartListData)
                }
            }

            Then("should show then hide progress loading") {
                verifyOrder {
                    view.showProgressLoading()
                    view.hideProgressLoading()
                }
            }
        }

        Scenario("error load") {

            val context: FragmentActivity = mockk()
            val errorMessage = "Terjadi kesalahan pada server. Ulangi beberapa saat lagi"

            Given("throw error") {
                every { getCartListSimplifiedUseCase.createObservable(any()) } returns Observable.error(ResponseErrorException("testing"))
                every { getRecentViewUseCase.createObservable(any(), any()) } answers {
                    Observable.just(GraphqlResponse(
                            mapOf(GqlRecentViewResponse::class.java to GqlRecentViewResponse()), emptyMap(), false))
                            .subscribe(secondArg() as Subscriber<GraphqlResponse>)
                }
            }

            Given("attach view") {
                every { view.getActivityObject() } returns context
                every { context.getString(R.string.default_request_error_internal_server) } returns errorMessage
                cartListPresenter.attachView(view)
            }

            When("process initial get cart data") {
                cartListPresenter.processInitialGetCartData("", true, true)
            }

            Then("should render error") {
                verify {
                    view.renderErrorInitialGetCartListData(errorMessage)
                }
            }
        }
    }

    Feature("update cart list") {

        val cartListPresenter by memoized {
            CartListPresenter(
                    getCartListSimplifiedUseCase, deleteCartListUseCase, updateCartUseCase,
                    checkPromoStackingCodeUseCase, checkPromoStackingCodeMapper, compositeSubscription,
                    addWishListUseCase, removeWishListUseCase, updateAndReloadCartUseCase,
                    userSessionInterface, clearCacheAutoApplyStackUseCase, getRecentViewUseCase,
                    getWishlistUseCase, getRecommendationUseCase, addToCartUseCase, getInsuranceCartUseCase,
                    removeInsuranceProductUsecase, updateInsuranceProductDataUsecase, seamlessLoginUsecase
            )
        }

        Scenario("success update") {

            val updateCartData = UpdateCartData()

            Given("update cart data") {
                updateCartData.isSuccess = true
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processToUpdateCartData(arrayListOf())
            }

            Then("should render success") {
                verify {
                    view.hideProgressLoading()
                    view.renderToShipmentFormSuccess(any(), any(), any(), any())
                }
            }
        }

        Scenario("failed update") {

            val updateCartData = UpdateCartData()

            Given("update cart data") {
                updateCartData.isSuccess = false
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processToUpdateCartData(arrayListOf())
            }

            Then("should render success") {
                verify {
                    view.hideProgressLoading()
                    view.renderErrorToShipmentForm(any())
                }
            }
        }

    }

    Feature("update cart list for promo global") {

        val cartListPresenter by memoized {
            CartListPresenter(
                    getCartListSimplifiedUseCase, deleteCartListUseCase, updateCartUseCase,
                    checkPromoStackingCodeUseCase, checkPromoStackingCodeMapper, compositeSubscription,
                    addWishListUseCase, removeWishListUseCase, updateAndReloadCartUseCase,
                    userSessionInterface, clearCacheAutoApplyStackUseCase, getRecentViewUseCase,
                    getWishlistUseCase, getRecommendationUseCase, addToCartUseCase, getInsuranceCartUseCase,
                    removeInsuranceProductUsecase, updateInsuranceProductDataUsecase, seamlessLoginUsecase
            )
        }

        Scenario("success update and redirect to coupon list") {

            val updateCartData = UpdateCartData()

            Given("update cart data") {
                updateCartData.isSuccess = true
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartDataPromoStacking(arrayListOf(), any(), CartFragment.GO_TO_LIST)
            }

            Then("should render success and redirect to coupon list") {
                verify {
                    view.hideProgressLoading()
                    view.goToCouponList()
                }
            }
        }

        Scenario("success update and redirect to coupon detail") {

            val updateCartData = UpdateCartData()

            Given("update cart data") {
                updateCartData.isSuccess = true
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartDataPromoStacking(arrayListOf(), any(), CartFragment.GO_TO_DETAIL)
            }

            Then("should render success and redirect to coupon list") {
                verify {
                    view.hideProgressLoading()
                    view.goToDetailPromoStacking()
                }
            }
        }

        Scenario("failed update") {

            val updateCartData = UpdateCartData()

            Given("update cart data") {
                updateCartData.isSuccess = false
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartDataPromoStacking(arrayListOf())
            }

            Then("should show error") {
                verify {
                    view.hideProgressLoading()
                    view.showToastMessageRed(any())
                }
            }
        }

    }

    Feature("update cart list for promo merchant") {

        val cartListPresenter by memoized {
            CartListPresenter(
                    getCartListSimplifiedUseCase, deleteCartListUseCase, updateCartUseCase,
                    checkPromoStackingCodeUseCase, checkPromoStackingCodeMapper, compositeSubscription,
                    addWishListUseCase, removeWishListUseCase, updateAndReloadCartUseCase,
                    userSessionInterface, clearCacheAutoApplyStackUseCase, getRecentViewUseCase,
                    getWishlistUseCase, getRecommendationUseCase, addToCartUseCase, getInsuranceCartUseCase,
                    removeInsuranceProductUsecase, updateInsuranceProductDataUsecase, seamlessLoginUsecase
            )
        }

        Scenario("success update") {

            val updateCartData = UpdateCartData()

            Given("update cart data") {
                updateCartData.isSuccess = true
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartDataPromoMerchant(arrayListOf(), any())
            }

            Then("should render success and show promo merchant bottomsheet") {
                verify {
                    view.hideProgressLoading()
                    view.showMerchantVoucherListBottomsheet()
                }
            }
        }

        Scenario("failed update") {

            val updateCartData = UpdateCartData()

            Given("update cart data") {
                updateCartData.isSuccess = false
                every { updateCartUseCase.createObservable(any()) } returns Observable.just(updateCartData)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartDataPromoStacking(arrayListOf())
            }

            Then("should show error") {
                verify {
                    view.hideProgressLoading()
                    view.showToastMessageRed(any())
                }
            }
        }

    }

    Feature("update and reload cart list") {

        val cartListPresenter by memoized {
            CartListPresenter(
                    getCartListSimplifiedUseCase, deleteCartListUseCase, updateCartUseCase,
                    checkPromoStackingCodeUseCase, checkPromoStackingCodeMapper, compositeSubscription,
                    addWishListUseCase, removeWishListUseCase, updateAndReloadCartUseCase,
                    userSessionInterface, clearCacheAutoApplyStackUseCase, getRecentViewUseCase,
                    getWishlistUseCase, getRecommendationUseCase, addToCartUseCase, getInsuranceCartUseCase,
                    removeInsuranceProductUsecase, updateInsuranceProductDataUsecase, seamlessLoginUsecase
            )
        }

        Scenario("success update and reload empty") {

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

        Scenario("success update and reload") {

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
    }

    Feature("delete cart item") {

        val cartListPresenter by memoized {
            CartListPresenter(
                    getCartListSimplifiedUseCase, deleteCartListUseCase, updateCartUseCase,
                    checkPromoStackingCodeUseCase, checkPromoStackingCodeMapper, compositeSubscription,
                    addWishListUseCase, removeWishListUseCase, updateAndReloadCartUseCase,
                    userSessionInterface, clearCacheAutoApplyStackUseCase, getRecentViewUseCase,
                    getWishlistUseCase, getRecommendationUseCase, addToCartUseCase, getInsuranceCartUseCase,
                    removeInsuranceProductUsecase, updateInsuranceProductDataUsecase, seamlessLoginUsecase
            )
        }

        Scenario("remove all cart data") {

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
    }

    Feature("add to cart") {

        val cartListPresenter by memoized {
            CartListPresenter(
                    getCartListSimplifiedUseCase, deleteCartListUseCase, updateCartUseCase,
                    checkPromoStackingCodeUseCase, checkPromoStackingCodeMapper, compositeSubscription,
                    addWishListUseCase, removeWishListUseCase, updateAndReloadCartUseCase,
                    userSessionInterface, clearCacheAutoApplyStackUseCase, getRecentViewUseCase,
                    getWishlistUseCase, getRecommendationUseCase, addToCartUseCase, getInsuranceCartUseCase,
                    removeInsuranceProductUsecase, updateInsuranceProductDataUsecase, seamlessLoginUsecase
            )
        }

        Scenario("success add to cart") {

            val addToCartDataModel = AddToCartDataModel()

            Given("add to cart data") {
                val dataModel = DataModel()
                val messages = arrayListOf()
                messages.add("Success message")
                dataModel.message = messages
                addToCartDataModel.status = AddToCartDataModel.STATUS_OK
                addToCartDataModel.data = dataModel
                every { addToCartUseCase.createObservable(any()) } returns Observable.just(addToCartDataModel)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processAddToCart(any())
            }

            Then("should render success") {
                verify {
                    view.hideProgressLoading()
                    view.triggerSendEnhancedEcommerceAddToCartSuccess()
                    view.processInitialGetCartData()
                    view.showToastMessageGreen(addToCartDataModel.data.message[0])
                }
            }
        }

        Scenario("failed add to cart") {

            val addToCartDataModel = AddToCartDataModel()

            Given("add to cart data") {
                val errorMessages = arrayListOf()
                errorMessages.add("Error message")
                addToCartDataModel.errorMessage = errorMessages
                addToCartDataModel.status = AddToCartDataModel.STATUS_ERROR
                every { addToCartUseCase.createObservable(any()) } returns Observable.just(addToCartDataModel)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processAddToCart(any())
            }

            Then("should render success") {
                verify {
                    view.hideProgressLoading()
                    view.showToastMessageRed(addToCartDataModel.errorMessage[0])
                }
            }
        }

    }

    Feature("calculate subtotal") {

        val cartListPresenter by memoized {
            CartListPresenter(
                    getCartListSimplifiedUseCase, deleteCartListUseCase, updateCartUseCase,
                    checkPromoStackingCodeUseCase, checkPromoStackingCodeMapper, compositeSubscription,
                    addWishListUseCase, removeWishListUseCase, updateAndReloadCartUseCase,
                    userSessionInterface, clearCacheAutoApplyStackUseCase, getRecentViewUseCase,
                    getWishlistUseCase, getRecommendationUseCase, addToCartUseCase, getInsuranceCartUseCase,
                    removeInsuranceProductUsecase, updateInsuranceProductDataUsecase, seamlessLoginUsecase
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