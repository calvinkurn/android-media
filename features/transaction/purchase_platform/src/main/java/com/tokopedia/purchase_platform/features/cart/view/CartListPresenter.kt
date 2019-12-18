package com.tokopedia.purchase_platform.features.cart.view

import android.os.Build
import android.text.TextUtils

import com.google.gson.Gson
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.TKPDMapParam
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.promocheckout.common.data.entity.request.CurrentApplyCode
import com.tokopedia.promocheckout.common.data.entity.request.Order
import com.tokopedia.promocheckout.common.data.entity.request.ProductDetail
import com.tokopedia.promocheckout.common.data.entity.request.Promo
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.promocheckout.common.view.model.PromoStackingData
import com.tokopedia.promocheckout.common.view.uimodel.ClashingVoucherOrderUiModel
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceAdd
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCartMapData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCheckout
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceRecomProductCartMapData
import com.tokopedia.purchase_platform.common.data.api.CartResponseErrorException
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.request.RemoveInsuranceData
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.request.UpdateInsuranceData
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.request.UpdateInsuranceDataCart
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.request.UpdateInsuranceProduct
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.request.UpdateInsuranceProductApplicationDetails
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.request.UpdateInsuranceProductItems
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartDigitalProduct
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartShopItems
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartShops
import com.tokopedia.purchase_platform.common.domain.usecase.GetInsuranceCartUseCase
import com.tokopedia.purchase_platform.common.domain.usecase.RemoveInsuranceProductUsecase
import com.tokopedia.purchase_platform.common.domain.usecase.UpdateInsuranceProductDataUsecase
import com.tokopedia.purchase_platform.features.cart.data.model.request.RemoveCartRequest
import com.tokopedia.purchase_platform.features.cart.data.model.request.UpdateCartRequest
import com.tokopedia.purchase_platform.features.cart.domain.model.DeleteAndRefreshCartListData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartItemData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.ShopGroupAvailableData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateAndRefreshCartListData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateCartData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.WholesalePriceData
import com.tokopedia.purchase_platform.features.cart.domain.usecase.DeleteCartListUseCase
import com.tokopedia.purchase_platform.features.cart.domain.usecase.GetCartListSimplifiedUseCase
import com.tokopedia.purchase_platform.features.cart.domain.usecase.GetRecentViewUseCase
import com.tokopedia.purchase_platform.features.cart.domain.usecase.UpdateAndReloadCartUseCase
import com.tokopedia.purchase_platform.features.cart.domain.usecase.UpdateCartUseCase
import com.tokopedia.purchase_platform.features.cart.view.analytics.EnhancedECommerceEmptyCartActionFieldData
import com.tokopedia.purchase_platform.features.cart.view.analytics.EnhancedECommerceEmptyCartClickData
import com.tokopedia.purchase_platform.features.cart.view.analytics.EnhancedECommerceEmptyCartData
import com.tokopedia.purchase_platform.features.cart.view.analytics.EnhancedECommerceEmptyCartProductData
import com.tokopedia.purchase_platform.features.cart.view.subscriber.AddToCartSubscriber
import com.tokopedia.purchase_platform.features.cart.view.subscriber.CheckPromoFirstStepAfterClashSubscriber
import com.tokopedia.purchase_platform.features.cart.view.subscriber.ClearCacheAutoApplyAfterClashSubscriber
import com.tokopedia.purchase_platform.features.cart.view.subscriber.ClearCacheAutoApplySubscriber
import com.tokopedia.purchase_platform.features.cart.view.subscriber.GetInsuranceCartSubscriber
import com.tokopedia.purchase_platform.features.cart.view.subscriber.GetRecentViewSubscriber
import com.tokopedia.purchase_platform.features.cart.view.subscriber.GetRecommendationSubscriber
import com.tokopedia.purchase_platform.features.cart.view.subscriber.GetRemoveMacroInsuranceProductSubscriber
import com.tokopedia.purchase_platform.features.cart.view.subscriber.GetSubscriberUpdateInsuranceProductData
import com.tokopedia.purchase_platform.features.cart.view.subscriber.GetWishlistSubscriber
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartItemHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartRecentViewItemHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartRecommendationItemHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartShopHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartWishlistItemHolderData
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.seamless_login.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.seamless_login.subscriber.SeamlessLoginSubscriber
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase

import java.util.ArrayList
import java.util.HashMap

import javax.inject.Inject

import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

/**
 * @author anggaprasetiyo on 18/01/18.
 */

class CartListPresenter @Inject constructor(private val getCartListSimplifiedUseCase: GetCartListSimplifiedUseCase,
                                            private val deleteCartListUseCase: DeleteCartListUseCase,
                                            private val updateCartUseCase: UpdateCartUseCase,
                                            private val checkPromoStackingCodeUseCase: CheckPromoStackingCodeUseCase?,
                                            private val checkPromoStackingCodeMapper: CheckPromoStackingCodeMapper,
                                            private val compositeSubscription: CompositeSubscription,
                                            private val addWishListUseCase: AddWishListUseCase?,
                                            private val removeWishListUseCase: RemoveWishListUseCase?,
                                            private val updateAndReloadCartUseCase: UpdateAndReloadCartUseCase,
                                            private val userSessionInterface: UserSessionInterface,
                                            private val clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase?,
                                            private val getRecentViewUseCase: GetRecentViewUseCase?,
                                            private val getWishlistUseCase: GetWishlistUseCase?,
                                            private val getRecommendationUseCase: GetRecommendationUseCase?,
                                            private val addToCartUseCase: AddToCartUseCase?,
                                            private val getInsuranceCartUseCase: GetInsuranceCartUseCase?,
                                            private val removeInsuranceProductUsecase: RemoveInsuranceProductUsecase?,
                                            private val updateInsuranceProductDataUsecase: UpdateInsuranceProductDataUsecase?,
                                            private val seamlessLoginUsecase: SeamlessLoginUsecase) : ICartListPresenter {

    private var view: ICartListView? = null
    private var cartListData: CartListData? = null
    private var hasPerformChecklistChange: Boolean = false
    private var insuranceChecked = true

    private fun getChecklistCondition(): Int {
        var checklistCondition = ITEM_CHECKED_ALL_WITHOUT_CHANGES
        val cartShopHolderDataList = view?.getAllShopDataList()

        if (cartShopHolderDataList?.size ?: 0 == 1) {
            cartShopHolderDataList?.get(0)?.shopGroupAvailableData?.cartItemDataList?.let {
                for (cartShopHolderData in it) {
                    if (!cartShopHolderData.isSelected) {
                        checklistCondition = ITEM_CHECKED_PARTIAL_ITEM
                        break
                    }
                }
            }
        } else if (cartShopHolderDataList?.size ?: 0 > 1) {
            var allSelectedItemShopCount = 0
            var selectPartialShopAndItem = false
            cartShopHolderDataList?.let {
                for (cartShopHolderData in it) {
                    if (cartShopHolderData.isAllSelected) {
                        allSelectedItemShopCount++
                    } else {
                        var selectedItem = 0
                        cartShopHolderData.shopGroupAvailableData.cartItemDataList?.let {
                            for (cartItemHolderData in it) {
                                if (!cartItemHolderData.isSelected) {
                                    selectedItem++
                                }
                            }
                            if (!selectPartialShopAndItem && selectedItem != it.size) {
                                selectPartialShopAndItem = true
                            }
                        }
                    }
                }
                if (selectPartialShopAndItem) {
                    checklistCondition = ITEM_CHECKED_PARTIAL_SHOP_AND_ITEM
                } else if (allSelectedItemShopCount < cartShopHolderDataList.size) {
                    checklistCondition = ITEM_CHECKED_PARTIAL_SHOP
                }
            }
        }

        if (checklistCondition == ITEM_CHECKED_ALL_WITHOUT_CHANGES && hasPerformChecklistChange) {
            checklistCondition = ITEM_CHECKED_ALL_WITH_CHANGES
        }
        return checklistCondition
    }

    override fun attachView(view: ICartListView) {
        this.view = view
    }

    override fun detachView() {
        compositeSubscription.unsubscribe()
        addWishListUseCase?.unsubscribe()
        removeWishListUseCase?.unsubscribe()
        clearCacheAutoApplyStackUseCase?.unsubscribe()
        checkPromoStackingCodeUseCase?.unsubscribe()
        getRecentViewUseCase?.unsubscribe()
        getWishlistUseCase?.unsubscribe()
        getRecommendationUseCase?.unsubscribe()
        addToCartUseCase?.unsubscribe()
        getInsuranceCartUseCase?.unsubscribe()
        removeInsuranceProductUsecase?.unsubscribe()
        updateInsuranceProductDataUsecase?.unsubscribe()
        view = null
    }

    override fun getCartListData(): CartListData? {
        return cartListData
    }

    override fun setCartListData(cartListData: CartListData) {
        this.cartListData = cartListData
    }

    override fun processInitialGetCartData(cartId: String, initialLoad: Boolean, isLoadingTypeRefresh: Boolean) {
        if (initialLoad) {
            view!!.renderLoadGetCartData()
        } else if (!isLoadingTypeRefresh) {
            view!!.showProgressLoading()
        }

        val requestParams = RequestParams.create()
        requestParams.putString(GetCartListSimplifiedUseCase.PARAM_SELECTED_CART_ID, cartId)

        compositeSubscription.add(getCartListSimplifiedUseCase.createObservable(requestParams)
                .subscribe(getSubscriberInitialCartListData(initialLoad))
        )
    }

    override fun getInsuranceTechCart() {
        getInsuranceCartUseCase!!.execute(GetInsuranceCartSubscriber(view!!))
    }

    override fun setAllInsuranceProductsChecked(insuranceCartShopsArrayList: ArrayList<InsuranceCartShops>, isChecked: Boolean) {
        for (insuranceCartShops in insuranceCartShopsArrayList) {
            if (insuranceCartShops != null &&
                    insuranceCartShops.shopItemsList != null &&
                    insuranceCartShops.shopItemsList.size > 0 &&
                    insuranceCartShops.shopItemsList[0] != null &&
                    insuranceCartShops.shopItemsList[0].digitalProductList != null &&
                    insuranceCartShops.shopItemsList[0].digitalProductList.size > 0 &&
                    insuranceCartShops.shopItemsList[0].digitalProductList[0] != null) {

                insuranceCartShops.shopItemsList[0].digitalProductList[0].optIn = isChecked
            }
        }
    }

    override fun processDeleteCartInsurance(insuranceCartShopsArrayList: ArrayList<InsuranceCartDigitalProduct>, showToaster: Boolean) {

        if (insuranceCartShopsArrayList != null && !insuranceCartShopsArrayList.isEmpty()) {
            view!!.showProgressLoading()
            val cartIdList = ArrayList<String>()
            val removeInsuranceDataArrayList = ArrayList<RemoveInsuranceData>()
            val productIdArrayList = ArrayList<Long>()
            for ((_, cartId, _, _, _, _, _, _, _, _, _, _, _, shopId, productId1) in insuranceCartShopsArrayList) {
                var shopid: Long
                var productId: Long
                try {
                    shopid = java.lang.Long.parseLong(shopId)
                } catch (e: Exception) {
                    shopid = 0
                }

                try {
                    productId = java.lang.Long.parseLong(productId1)
                } catch (e: Exception) {
                    productId = 0
                }

                cartIdList.add(cartId.toString())
                val removeInsuranceData = RemoveInsuranceData(cartId, shopid, productId)
                removeInsuranceDataArrayList.add(removeInsuranceData)
                productIdArrayList.add(productId)
            }


            removeInsuranceProductUsecase!!.setRequestParams(removeInsuranceDataArrayList, "cart", Build.VERSION.SDK_INT.toString(), cartIdList)
            removeInsuranceProductUsecase.execute(GetRemoveMacroInsuranceProductSubscriber(view!!, productIdArrayList, showToaster))
        }
    }

    override fun updateInsuranceProductData(insuranceCartShops: InsuranceCartShops,
                                            updateInsuranceProductApplicationDetailsArrayList: ArrayList<UpdateInsuranceProductApplicationDetails>) {
        view!!.showProgressLoading()

        val cartIdList = ArrayList<UpdateInsuranceDataCart>()
        val updateInsuranceDataArrayList = ArrayList<UpdateInsuranceData>()
        val updateInsuranceProductItemsArrayList = ArrayList<UpdateInsuranceProductItems>()

        val shopid = insuranceCartShops.shopId

        var productId: Long = 0

        if (insuranceCartShops.shopItemsList != null && !insuranceCartShops.shopItemsList.isEmpty()) {
            for ((productId1, digitalProductList) in insuranceCartShops.shopItemsList) {


                val updateInsuranceProductArrayList = ArrayList<UpdateInsuranceProduct>()
                for ((digitalProductId, cartItemId, typeId, _, _, _, isProductLevel) in digitalProductList) {

                    if (!isProductLevel) {

                        val cartId = digitalProductList[0].cartItemId
                        productId = productId1
                        cartIdList.add(UpdateInsuranceDataCart(cartId.toString(), 1))

                        val updateInsuranceProduct = UpdateInsuranceProduct(digitalProductId,
                                cartItemId,
                                typeId,
                                updateInsuranceProductApplicationDetailsArrayList)
                        updateInsuranceProductArrayList.add(updateInsuranceProduct)

                        val updateInsuranceProductItems = UpdateInsuranceProductItems(productId1, 1, updateInsuranceProductArrayList)
                        updateInsuranceProductItemsArrayList.add(updateInsuranceProductItems)
                        val updateInsuranceData = UpdateInsuranceData(shopid, updateInsuranceProductItemsArrayList)
                        updateInsuranceDataArrayList.add(updateInsuranceData)

                    }
                }
            }
        }


        updateInsuranceProductDataUsecase!!.setRequestParams(updateInsuranceDataArrayList, "cart", Build.VERSION.SDK_INT.toString(), cartIdList)
        updateInsuranceProductDataUsecase.execute(GetSubscriberUpdateInsuranceProductData(view!!, this, productId))
    }

    override fun processDeleteCartItem(allCartItemData: List<CartItemData>,
                                       removedCartItems: List<CartItemData>,
                                       appliedPromoOnDeletedProductList: ArrayList<String>?,
                                       addWishList: Boolean,
                                       removeInsurance: Boolean) {
        var appliedPromoOnDeletedProductList = appliedPromoOnDeletedProductList
        view!!.showProgressLoading()
        val removeAllItem = allCartItemData.size == removedCartItems.size
        if (appliedPromoOnDeletedProductList == null) {
            appliedPromoOnDeletedProductList = ArrayList()
        }

        val toBeDeletedCartIds = ArrayList<Int>()
        for ((originData) in removedCartItems) {
            toBeDeletedCartIds.add(originData!!.cartId)
        }
        val removeCartRequest = RemoveCartRequest()
        removeCartRequest.addWishlist = if (addWishList) 1 else 0
        removeCartRequest.cartIds = toBeDeletedCartIds
        val paramDelete = TKPDMapParam<String, String>()
        paramDelete[DeleteCartListUseCase.PARAM_PARAMS] = Gson().toJson(removeCartRequest)

        val requestParams = RequestParams.create()
        requestParams.putObject(DeleteCartListUseCase.PARAM_REQUEST_AUTH_MAP_STRING_DELETE_CART,
                AuthHelper.generateParamsNetwork(userSessionInterface.userId, userSessionInterface.deviceId, paramDelete))
        requestParams.putBoolean(DeleteCartListUseCase.PARAM_IS_DELETE_ALL_DATA, removeAllItem)
        requestParams.putObject(DeleteCartListUseCase.PARAM_TO_BE_REMOVED_PROMO_CODES, appliedPromoOnDeletedProductList)

        compositeSubscription.add(deleteCartListUseCase.createObservable(requestParams)
                .subscribe(getSubscriberDeleteAndRefreshCart(toBeDeletedCartIds, removedCartItems, removeAllItem, removeInsurance)))
    }

    override fun processToUpdateCartData(cartItemDataList: List<CartItemData>) {
        view!!.showProgressLoading()
        val updateCartRequestList = ArrayList<UpdateCartRequest>()
        for ((originData, updatedData) in cartItemDataList) {
            val updateCartRequest = UpdateCartRequest()
            updateCartRequest.cartId = originData!!.cartId
            updateCartRequest.notes = updatedData!!.remark
            updateCartRequest.quantity = updatedData.quantity
            updateCartRequestList.add(updateCartRequest)
        }
        val paramUpdate = TKPDMapParam<String, String>()
        paramUpdate[UpdateCartUseCase.PARAM_CARTS] = Gson().toJson(updateCartRequestList)

        val requestParams = RequestParams.create()
        requestParams.putObject(UpdateCartUseCase.PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART,
                AuthHelper.generateParamsNetwork(userSessionInterface.userId, userSessionInterface.deviceId, paramUpdate))

        compositeSubscription.add(
                updateCartUseCase.createObservable(requestParams)
                        .subscribe(getSubscriberToShipmentSingleAddress(cartItemDataList))
        )
    }

    override fun processUpdateCartDataPromoMerchant(cartItemDataList: List<CartItemData>,
                                                    shopGroupAvailableData: ShopGroupAvailableData) {
        view!!.showProgressLoading()
        val updateCartRequestList = ArrayList<UpdateCartRequest>()
        for ((originData, updatedData) in cartItemDataList) {
            val updateCartRequest = UpdateCartRequest()
            updateCartRequest.cartId = originData!!.cartId
            updateCartRequest.notes = updatedData!!.remark
            updateCartRequest.quantity = updatedData.quantity
            updateCartRequestList.add(updateCartRequest)
        }
        val paramUpdate = TKPDMapParam<String, String>()
        paramUpdate[UpdateCartUseCase.PARAM_CARTS] = Gson().toJson(updateCartRequestList)

        val requestParams = RequestParams.create()
        requestParams.putObject(UpdateCartUseCase.PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART,
                AuthHelper.generateParamsNetwork(userSessionInterface.userId, userSessionInterface.deviceId, paramUpdate))

        compositeSubscription.add(
                updateCartUseCase.createObservable(requestParams)
                        .subscribe(getSubscriberUpdateCartPromoMerchant(shopGroupAvailableData))
        )
    }

    override fun processUpdateCartDataPromoStacking(cartItemDataList: List<CartItemData>,
                                                    promoStackingData: PromoStackingData,
                                                    goToDetail: Int) {
        view!!.showProgressLoading()
        val updateCartRequestList = ArrayList<UpdateCartRequest>()
        for ((originData, updatedData) in cartItemDataList) {
            val updateCartRequest = UpdateCartRequest()
            updateCartRequest.cartId = originData!!.cartId
            updateCartRequest.notes = updatedData!!.remark
            updateCartRequest.quantity = updatedData.quantity
            updateCartRequestList.add(updateCartRequest)
        }
        val paramUpdate = TKPDMapParam<String, String>()
        paramUpdate[UpdateCartUseCase.PARAM_CARTS] = Gson().toJson(updateCartRequestList)

        val requestParams = RequestParams.create()
        requestParams.putObject(UpdateCartUseCase.PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART,
                AuthHelper.generateParamsNetwork(userSessionInterface.userId, userSessionInterface.deviceId, paramUpdate))

        compositeSubscription.add(
                updateCartUseCase.createObservable(requestParams)
                        .subscribe(getSubscriberUpdateCartPromoGlobal(promoStackingData, goToDetail))
        )
    }

    private fun getSubscriberUpdateCartPromoMerchant(shopGroupAvailableData: ShopGroupAvailableData): Subscriber<UpdateCartData> {
        return object : Subscriber<UpdateCartData>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                if (view != null) {
                    view!!.hideProgressLoading()
                    var errorMessage = e.message
                    if (e !is CartResponseErrorException) {
                        errorMessage = ErrorHandler.getErrorMessage(view!!.getActivityObject(), e)
                    }
                    view!!.showToastMessageRed(errorMessage)
                    processInitialGetCartData(view!!.getCartId(), cartListData == null, false)
                }
            }

            override fun onNext(data: UpdateCartData) {
                if (view != null) {
                    view!!.hideProgressLoading()
                    if (!data.isSuccess) {
                        view!!.showToastMessageRed(data.message!!)
                    } else {
                        view!!.showMerchantVoucherListBottomsheet(shopGroupAvailableData)
                    }
                }
            }
        }
    }

    private fun getSubscriberUpdateCartPromoGlobal(promoStackingData: PromoStackingData,
                                                   stateGoTo: Int): Subscriber<UpdateCartData> {
        return object : Subscriber<UpdateCartData>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                if (view != null) {
                    view!!.hideProgressLoading()
                    var errorMessage = e.message
                    if (e !is CartResponseErrorException) {
                        errorMessage = ErrorHandler.getErrorMessage(view!!.getActivityObject(), e)
                    }
                    view!!.showToastMessageRed(errorMessage)
                    processInitialGetCartData(view!!.getCartId(), cartListData == null, false)
                }
            }

            override fun onNext(data: UpdateCartData) {
                if (view != null) {
                    view!!.hideProgressLoading()
                    if (!data.isSuccess) {
                        view!!.showToastMessageRed(data.message!!)
                    } else {
                        if (stateGoTo == CartFragment.GO_TO_LIST) {
                            view!!.goToCouponList()
                        } else {
                            view!!.goToDetailPromoStacking(promoStackingData)
                        }
                    }
                }
            }
        }
    }

    override fun processToUpdateAndReloadCartData() {
        val cartItemDataList = ArrayList<CartItemData>()
        for (data in view!!.getAllAvailableCartDataList()) {
            if (!data.isError) {
                cartItemDataList.add(data)
            }
        }
        val updateCartRequestList = ArrayList<UpdateCartRequest>()
        for ((originData, updatedData) in cartItemDataList) {
            val updateCartRequest = UpdateCartRequest()
            updateCartRequest.cartId = originData!!.cartId
            updateCartRequest.notes = updatedData!!.remark
            updateCartRequest.quantity = updatedData.quantity
            updateCartRequestList.add(updateCartRequest)
        }
        val paramUpdate = TKPDMapParam<String, String>()
        paramUpdate[UpdateAndReloadCartUseCase.PARAM_CARTS] = Gson().toJson(updateCartRequestList)

        val requestParams = RequestParams.create()
        requestParams.putObject(UpdateAndReloadCartUseCase.PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART,
                AuthHelper.generateParamsNetwork(userSessionInterface.userId, userSessionInterface.deviceId, paramUpdate))

        compositeSubscription.add(
                updateAndReloadCartUseCase.createObservable(requestParams)
                        .subscribe(object : Subscriber<UpdateAndRefreshCartListData>() {
                            override fun onCompleted() {

                            }

                            override fun onError(e: Throwable) {
                                if (view != null) {
                                    view!!.hideProgressLoading()
                                    var errorMessage = e.message
                                    if (e !is CartResponseErrorException) {
                                        errorMessage = ErrorHandler.getErrorMessage(view!!.getActivityObject(), e)
                                    }
                                    view!!.showToastMessageRed(errorMessage)
                                }
                            }

                            override fun onNext(updateAndRefreshCartListData: UpdateAndRefreshCartListData) {
                                if (view != null) {
                                    view!!.hideProgressLoading()
                                    if (updateAndRefreshCartListData.cartListData != null) {
                                        this@CartListPresenter.cartListData = updateAndRefreshCartListData.cartListData
                                        view!!.renderLoadGetCartDataFinish()
                                        view!!.renderInitialGetCartListDataSuccess(cartListData)
                                    }
                                }
                            }
                        })
        )

    }

    override fun reCalculateSubTotal(dataList: List<CartShopHolderData>, insuranceCartShopsArrayList: ArrayList<InsuranceCartShops>) {
        var totalCashback = 0.0
        var totalPrice = 0.0
        var totalItemQty = 0
        var errorProductCount = 0

        // Collect all Cart Item, if has no error and selected
        val allCartItemDataList = ArrayList<CartItemHolderData>()
        for (cartShopHolderData in dataList) {
            if (cartShopHolderData.shopGroupAvailableData.cartItemDataList != null) {
                if (!cartShopHolderData.shopGroupAvailableData.isError) {
                    if (cartShopHolderData.isAllSelected || cartShopHolderData.isPartialSelected) {
                        for (cartItemHolderData in cartShopHolderData.shopGroupAvailableData.cartItemDataList!!) {
                            if (!cartItemHolderData.cartItemData.isError) {
                                if (cartItemHolderData.isSelected) {
                                    allCartItemDataList.add(cartItemHolderData)
                                }
                            } else {
                                errorProductCount++
                            }
                        }
                    }
                } else {
                    errorProductCount += cartShopHolderData.shopGroupAvailableData.cartItemDataList!!.size
                }
            }
        }

        // Set cart item parent id if current value is 0
        for (i in allCartItemDataList.indices) {
            val cartItemData = allCartItemDataList[i].cartItemData
            if (cartItemData != null && cartItemData.originData != null) {
                if (cartItemData.originData!!.parentId == "0") {
                    cartItemData.originData!!.parentId = (i + 1).toString()
                }
            }
        }

        // Calculate total price, total item, and wholesale price (if any)
        val cashbackWholesalePriceMap = HashMap<String, Double>()
        val subtotalWholesalePriceMap = HashMap<String, Double>()
        val cartItemParentIdMap = HashMap<String, CartItemData>()
        for ((cartItemData) in allCartItemDataList) {
            if (cartItemData.originData != null) {
                val parentId = cartItemData.originData!!.parentId
                val productId = cartItemData.originData!!.productId
                var itemQty = cartItemData.updatedData!!.quantity
                totalItemQty += itemQty
                if (!TextUtils.isEmpty(parentId) && parentId != "0") {
                    for ((cartItemData1) in allCartItemDataList) {
                        if (productId != cartItemData1.originData!!.productId &&
                                parentId == cartItemData1.originData!!.parentId &&
                                cartItemData1.originData!!.pricePlan == cartItemData.originData!!.pricePlan) {
                            itemQty += cartItemData1.updatedData!!.quantity
                        }
                    }
                }

                val wholesalePriceDataList = cartItemData.originData!!.wholesalePriceData
                var hasCalculateWholesalePrice = false
                if (wholesalePriceDataList != null && wholesalePriceDataList.size > 0) {
                    var subTotalWholesalePrice = 0.0
                    var itemCashback = 0.0
                    for ((_, _, _, qtyMin, _, prdPrc) in wholesalePriceDataList) {
                        if (itemQty >= qtyMin) {
                            subTotalWholesalePrice = (itemQty * prdPrc).toDouble()
                            hasCalculateWholesalePrice = true
                            val wholesalePriceFormatted = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                                    prdPrc, false)
                            cartItemData.originData!!.wholesalePriceFormatted = wholesalePriceFormatted
                            break
                        }
                    }
                    if (!hasCalculateWholesalePrice) {
                        if (itemQty > wholesalePriceDataList[wholesalePriceDataList.size - 1].prdPrc) {
                            subTotalWholesalePrice = (itemQty * wholesalePriceDataList[wholesalePriceDataList.size - 1].prdPrc).toDouble()
                            val wholesalePriceFormatted = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                                    wholesalePriceDataList[wholesalePriceDataList.size - 1].prdPrc, false)
                            cartItemData.originData!!.wholesalePriceFormatted = wholesalePriceFormatted
                        } else {
                            subTotalWholesalePrice = itemQty * cartItemData.originData!!.pricePlan
                            cartItemData.originData!!.wholesalePriceFormatted = null
                        }
                    }
                    if (cartItemData.originData!!.isCashBack) {
                        val cashbackPercentageString = cartItemData.originData!!.productCashBack!!.replace("%", "")
                        val cashbackPercentage = java.lang.Double.parseDouble(cashbackPercentageString)
                        itemCashback = cashbackPercentage / PERCENTAGE * subTotalWholesalePrice
                    }
                    if (!subtotalWholesalePriceMap.containsKey(parentId)) {
                        subtotalWholesalePriceMap[parentId!!] = subTotalWholesalePrice
                    }
                    if (!cashbackWholesalePriceMap.containsKey(parentId)) {
                        cashbackWholesalePriceMap[parentId!!] = itemCashback
                    }
                } else {
                    if (!cartItemParentIdMap.containsKey(cartItemData.originData!!.parentId)) {
                        val itemPrice = itemQty * cartItemData.originData!!.pricePlan
                        if (cartItemData.originData!!.isCashBack) {
                            val cashbackPercentageString = cartItemData.originData!!.productCashBack!!.replace("%", "")
                            val cashbackPercentage = java.lang.Double.parseDouble(cashbackPercentageString)
                            val itemCashback = cashbackPercentage / PERCENTAGE * itemPrice
                            totalCashback = totalCashback + itemCashback
                        }
                        totalPrice = totalPrice + itemPrice
                        cartItemData.originData!!.wholesalePriceFormatted = null
                        cartItemParentIdMap[cartItemData.originData!!.parentId!!] = cartItemData
                    } else {
                        val calculatedHolderData = cartItemParentIdMap[cartItemData.originData!!.parentId]
                        if (calculatedHolderData!!.originData!!.pricePlan != cartItemData.originData!!.pricePlan) {
                            val itemPrice = itemQty * cartItemData.originData!!.pricePlan
                            if (cartItemData.originData!!.isCashBack) {
                                val cashbackPercentageString = cartItemData.originData!!.productCashBack!!.replace("%", "")
                                val cashbackPercentage = java.lang.Double.parseDouble(cashbackPercentageString)
                                val itemCashback = cashbackPercentage / PERCENTAGE * itemPrice
                                totalCashback = totalCashback + itemCashback
                            }
                            totalPrice = totalPrice + itemPrice
                            cartItemData.originData!!.wholesalePriceFormatted = null
                        }
                    }
                }
            }
        }

        if (!subtotalWholesalePriceMap.isEmpty()) {
            for ((_, value) in subtotalWholesalePriceMap) {
                totalPrice += value
            }
        }

        if (!cashbackWholesalePriceMap.isEmpty()) {
            for ((_, value) in cashbackWholesalePriceMap) {
                totalCashback += value
            }
        }

        insuranceChecked = true
        for (insuranceCartShops in insuranceCartShopsArrayList) {

            if (insuranceCartShops != null &&
                    insuranceCartShops.shopItemsList != null &&
                    insuranceCartShops.shopItemsList[0] != null &&
                    insuranceCartShops.shopItemsList.size > 0 &&
                    insuranceCartShops.shopItemsList[0].digitalProductList[0] != null) {

                for ((_, digitalProductList) in insuranceCartShops.shopItemsList) {
                    for ((_, _, _, pricePerProduct, _, optIn) in digitalProductList) {
                        if (optIn) {
                            totalPrice += pricePerProduct.toDouble()
                            totalItemQty += 1
                        } else {
                            insuranceChecked = false
                        }
                    }
                }
            }
        }

        var totalPriceString = "-"
        if (totalPrice > 0) {
            totalPriceString = CurrencyFormatUtil.convertPriceValueToIdrFormat(totalPrice.toLong(), false)
        }
        view!!.updateCashback(totalCashback)
        val selectAllItem = view!!.getAllAvailableCartDataList().size == allCartItemDataList.size + errorProductCount &&
                allCartItemDataList.size > 0 && insuranceChecked
        val unselectAllItem = allCartItemDataList.size == 0
        view!!.renderDetailInfoSubTotal(totalItemQty.toString(), totalPriceString, selectAllItem, unselectAllItem, dataList.isEmpty())

    }

    private fun getSubscriberInitialCartListData(initialLoad: Boolean): Subscriber<CartListData> {
        return object : Subscriber<CartListData>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                e.printStackTrace()
                if (view != null) {
                    if (!initialLoad) {
                        view!!.hideProgressLoading()
                    }
                    view!!.renderLoadGetCartDataFinish()
                    var errorMessage = e.message
                    if (e !is CartResponseErrorException) {
                        errorMessage = ErrorHandler.getErrorMessage(view!!.getActivityObject(), e)
                    }
                    view!!.renderErrorInitialGetCartListData(errorMessage)
                    view!!.stopCartPerformanceTrace()
                }
            }

            override fun onNext(cartListData: CartListData) {
                if (view != null) {
                    if (!initialLoad) {
                        view!!.hideProgressLoading()
                    }
                    this@CartListPresenter.cartListData = cartListData
                    view!!.renderLoadGetCartDataFinish()
                    view!!.renderInitialGetCartListDataSuccess(cartListData)
                    view!!.stopCartPerformanceTrace()
                }
            }
        }
    }

    private fun getSubscriberDeleteAndRefreshCart(toBeDeletedCartIds: List<Int>,
                                                  removedCartItems: List<CartItemData>,
                                                  removeAllItems: Boolean,
                                                  removeInsurance: Boolean): Subscriber<DeleteAndRefreshCartListData> {
        return object : Subscriber<DeleteAndRefreshCartListData>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (view != null) {
                    view!!.hideProgressLoading()
                    e.printStackTrace()
                    var errorMessage = e.message
                    if (e !is CartResponseErrorException) {
                        errorMessage = ErrorHandler.getErrorMessage(view!!.getActivityObject(), e)
                    }
                    view!!.showToastMessageRed(errorMessage)
                }
            }

            override fun onNext(deleteAndRefreshCartListData: DeleteAndRefreshCartListData) {
                if (view != null) {
                    view!!.hideProgressLoading()
                    view!!.renderLoadGetCartDataFinish()

                    if (deleteAndRefreshCartListData.deleteCartData!!.isSuccess) {

                        if (removeInsurance) {
                            processDeleteCartInsurance(view!!.getInsuranceCartShopData()!!, false)
                        }

                        if (removeAllItems) {
                            processInitialGetCartData(view!!.getCartId(), false, false)
                        } else {
                            view!!.onDeleteCartDataSuccess(toBeDeletedCartIds)
                        }

                    } else {
                        view!!.showToastMessageRed(
                                deleteAndRefreshCartListData.deleteCartData!!.message!!
                        )
                    }
                }
            }
        }
    }

    private fun getSubscriberToShipmentSingleAddress(cartItemDataList: List<CartItemData>): Subscriber<UpdateCartData> {
        return object : Subscriber<UpdateCartData>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (view != null) {
                    e.printStackTrace()
                    view!!.hideProgressLoading()
                    var errorMessage = e.message
                    if (e !is CartResponseErrorException) {
                        errorMessage = ErrorHandler.getErrorMessage(view!!.getActivityObject(), e)
                    }
                    view!!.renderErrorToShipmentForm(errorMessage)
                    processInitialGetCartData(view!!.getCartId(), cartListData == null, false)
                }
            }

            override fun onNext(data: UpdateCartData) {
                if (view != null) {
                    view!!.hideProgressLoading()
                    if (!data.isSuccess) {
                        view!!.renderErrorToShipmentForm(data.message!!)
                    } else {
                        val checklistCondition = getChecklistCondition()
                        view!!.renderToShipmentFormSuccess(
                                generateCheckoutDataAnalytics(cartItemDataList, EnhancedECommerceActionField.STEP_1),
                                cartItemDataList,
                                isCheckoutProductEligibleForCashOnDelivery(cartItemDataList),
                                checklistCondition)
                    }
                }
            }
        }
    }

    private fun isCheckoutProductEligibleForCashOnDelivery(cartItemDataList: List<CartItemData>): Boolean {
        var totalAmount = 0.0
        val maximalTotalAmountEligible = 1000000.0
        for ((originData, updatedData) in cartItemDataList) {
            val itemPriceAmount = originData!!.pricePlan * updatedData!!.quantity
            totalAmount = totalAmount + itemPriceAmount
            if (!originData.isCod)
                return false
        }
        return totalAmount <= maximalTotalAmountEligible
    }

    override fun processCancelAutoApplyPromoStack(shopIndex: Int, promoCodeList: ArrayList<String>, ignoreAPIResponse: Boolean) {
        if (promoCodeList.size > 0) {
            if (!ignoreAPIResponse) {
                view!!.showProgressLoading()
            }
            clearCacheAutoApplyStackUseCase!!.setParams(ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE, promoCodeList)
            clearCacheAutoApplyStackUseCase.execute(RequestParams.create(), ClearCacheAutoApplySubscriber(view, this, shopIndex, ignoreAPIResponse))
        }
    }

    override fun processCancelAutoApplyPromoStackAfterClash(promoStackingGlobalData: PromoStackingData,
                                                            oldPromoList: ArrayList<String>,
                                                            newPromoList: ArrayList<ClashingVoucherOrderUiModel>,
                                                            type: String) {
        view!!.showProgressLoading()
        clearCacheAutoApplyStackUseCase!!.setParams(ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE, oldPromoList)
        clearCacheAutoApplyStackUseCase.execute(RequestParams.create(), ClearCacheAutoApplyAfterClashSubscriber(view, this, promoStackingGlobalData, newPromoList, type))
    }

    override fun processApplyPromoStackAfterClash(promoStackingGlobalData: PromoStackingData,
                                                  newPromoList: ArrayList<ClashingVoucherOrderUiModel>,
                                                  type: String) {
        val promo = generateCheckPromoFirstStepParam(promoStackingGlobalData)
        promo.codes = ArrayList()
        if (promo.orders != null) {
            for (order in promo.orders!!) {
                order.codes = ArrayList()
            }
        }

        // New promo list is array, but it will always be 1 item
        if (newPromoList != null && newPromoList.size > 0) {
            val (code, uniqueId) = newPromoList[0]
            if (TextUtils.isEmpty(uniqueId)) {
                // This promo is global promo
                val codes = ArrayList<String>()
                codes.add(code)
                promo.codes = codes

                val currentApplyCode = CurrentApplyCode()
                if (!code.isEmpty()) {
                    currentApplyCode.code = code
                    currentApplyCode.type = PARAM_GLOBAL
                }
                promo.currentApplyCode = currentApplyCode
            } else {
                // This promo is merchant/logistic promo
                if (promo.orders != null) {
                    for (order in promo.orders!!) {
                        if (uniqueId == order.uniqueId) {
                            val codes = ArrayList<String>()
                            codes.add(code)
                            order.codes = codes

                            val currentApplyCode = CurrentApplyCode()
                            if (!code.isEmpty()) {
                                currentApplyCode.code = code
                                currentApplyCode.type = type
                            }
                            promo.currentApplyCode = currentApplyCode
                            break
                        }
                    }
                }
            }
            view!!.showProgressLoading()
            checkPromoStackingCodeUseCase!!.setParams(promo)
            checkPromoStackingCodeUseCase.execute(RequestParams.create(),
                    CheckPromoFirstStepAfterClashSubscriber(view, this, checkPromoStackingCodeMapper, type))
        }
    }

    override fun processAddToWishlist(productId: String, userId: String, listener: WishListActionListener) {
        addWishListUseCase!!.createObservable(productId, userId, listener)
    }

    override fun processRemoveFromWishlist(productId: String, userId: String, listener: WishListActionListener) {
        removeWishListUseCase!!.createObservable(productId, userId, listener)
    }

    override fun generateCartDataAnalytics(cartItemDataList: List<CartItemData>, enhancedECommerceAction: String): Map<String, Any> {

        val enhancedECommerceCartMapData = EnhancedECommerceCartMapData()

        for (cartItemData in cartItemDataList) {
            val enhancedECommerceProductCartMapData = getEnhancedECommerceProductCartMapData(cartItemData)
            enhancedECommerceCartMapData.addProduct(enhancedECommerceProductCartMapData.product)
        }

        enhancedECommerceCartMapData.setCurrencyCode(EnhancedECommerceCartMapData.VALUE_CURRENCY_IDR)
        enhancedECommerceCartMapData.setAction(enhancedECommerceAction)

        return enhancedECommerceCartMapData.cartMap
    }

    override fun generateRecommendationDataAnalytics(cartRecommendationItemHolderDataList: List<CartRecommendationItemHolderData>, isEmptyCart: Boolean): Map<String, Any> {
        val enhancedECommerceCartMapData = EnhancedECommerceCartMapData()

        var position = 1
        for ((recommendationItem) in cartRecommendationItemHolderDataList) {
            val enhancedECommerceProductCartMapData = getEnhancedECommerceProductRecommendationMapData(recommendationItem, isEmptyCart, position)
            enhancedECommerceCartMapData.addImpression(enhancedECommerceProductCartMapData.product)
            position++
        }

        enhancedECommerceCartMapData.setCurrencyCode(EnhancedECommerceCartMapData.VALUE_CURRENCY_IDR)
        return enhancedECommerceCartMapData.cartMap
    }

    override fun generateWishlistDataImpressionAnalytics(cartWishlistItemHolderDataList: List<CartWishlistItemHolderData>, isEmptyCart: Boolean): Map<String, Any> {
        val enhancedECommerceCartMapData = EnhancedECommerceCartMapData()

        var position = 0
        for (cartWishlistItemHolderData in cartWishlistItemHolderDataList) {
            val enhancedECommerceProductCartMapData = getProductWishlistImpressionMapData(cartWishlistItemHolderData, isEmptyCart, position)
            enhancedECommerceCartMapData.addImpression(enhancedECommerceProductCartMapData.product)
            position++
        }

        enhancedECommerceCartMapData.setCurrencyCode(EnhancedECommerceCartMapData.VALUE_CURRENCY_IDR)
        return enhancedECommerceCartMapData.cartMap
    }

    override fun generateRecentViewDataImpressionAnalytics(cartRecentViewItemHolderDataList: List<CartRecentViewItemHolderData>, isEmptyCart: Boolean): Map<String, Any> {
        val enhancedECommerceCartMapData = EnhancedECommerceCartMapData()

        var position = 0
        for (cartRecentViewItemHolderData in cartRecentViewItemHolderDataList) {
            val enhancedECommerceProductCartMapData = getProductRecentViewImpressionMapData(cartRecentViewItemHolderData, isEmptyCart, position)
            enhancedECommerceCartMapData.addImpression(enhancedECommerceProductCartMapData.product)
            position++
        }

        enhancedECommerceCartMapData.setCurrencyCode(EnhancedECommerceCartMapData.VALUE_CURRENCY_IDR)
        return enhancedECommerceCartMapData.cartMap
    }

    override fun generateRecommendationDataOnClickAnalytics(recommendationItem: RecommendationItem, isEmptyCart: Boolean, position: Int): Map<String, Any> {
        val enhancedECommerceProductCartMapData = EnhancedECommerceProductCartMapData()
        enhancedECommerceProductCartMapData.setProductID(recommendationItem.productId.toString())
        enhancedECommerceProductCartMapData.setProductName(recommendationItem.name)
        enhancedECommerceProductCartMapData.setPrice(recommendationItem.price.replace("[^0-9]".toRegex(), ""))
        enhancedECommerceProductCartMapData.setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
        enhancedECommerceProductCartMapData.setCategory(if (TextUtils.isEmpty(recommendationItem.categoryBreadcrumbs))
            EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
        else
            recommendationItem.categoryBreadcrumbs)
        enhancedECommerceProductCartMapData.setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
        enhancedECommerceProductCartMapData.setPosition(position.toString())
        enhancedECommerceProductCartMapData.setAttribution(EnhancedECommerceProductCartMapData.RECOMMENDATION_ATTRIBUTION)
        if (recommendationItem.isFreeOngkirActive) {
            enhancedECommerceProductCartMapData.setDimension83(EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR)
        } else {
            enhancedECommerceProductCartMapData.setDimension83(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
        }
        val productsData = ArrayList<Map<String, Any>>()
        productsData.add(enhancedECommerceProductCartMapData.product)
        return getEnhancedECommerceOnClickEmptyCartData(productsData, getActionFieldListStr(isEmptyCart, recommendationItem)).getData()
    }

    private fun getEnhancedECommerceProductRecommendationMapData(recommendationItem: RecommendationItem, isEmptyCart: Boolean, position: Int): EnhancedECommerceProductCartMapData {
        val enhancedECommerceProductCartMapData = EnhancedECommerceProductCartMapData()
        enhancedECommerceProductCartMapData.setProductID(recommendationItem.productId.toString())
        enhancedECommerceProductCartMapData.setProductName(recommendationItem.name)
        enhancedECommerceProductCartMapData.setPrice(recommendationItem.price.replace("[^0-9]".toRegex(), ""))
        enhancedECommerceProductCartMapData.setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
        enhancedECommerceProductCartMapData.setCategory(if (TextUtils.isEmpty(recommendationItem.categoryBreadcrumbs))
            EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
        else
            recommendationItem.categoryBreadcrumbs)
        enhancedECommerceProductCartMapData.setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
        enhancedECommerceProductCartMapData.setListName(getActionFieldListStr(isEmptyCart, recommendationItem))
        enhancedECommerceProductCartMapData.setPosition(position.toString())
        if (recommendationItem.isFreeOngkirActive) {
            enhancedECommerceProductCartMapData.setDimension83(EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR)
        } else {
            enhancedECommerceProductCartMapData.setDimension83(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
        }
        return enhancedECommerceProductCartMapData
    }

    private fun getEnhancedECommerceOnClickEmptyCartData(productsData: List<Map<String, Any>>, valueSectionName: String): EnhancedECommerceEmptyCartData {
        val enhancedECommerceEmptyCartActionFieldData = EnhancedECommerceEmptyCartActionFieldData()
        enhancedECommerceEmptyCartActionFieldData.setList(valueSectionName)

        val enhancedECommerceEmptyCartClickData = EnhancedECommerceEmptyCartClickData()
        enhancedECommerceEmptyCartClickData.setActionField(enhancedECommerceEmptyCartActionFieldData.getData())
        enhancedECommerceEmptyCartClickData.setProducts(productsData)

        val enhancedECommerceEmptyCart = EnhancedECommerceEmptyCartData()
        enhancedECommerceEmptyCart.setClickData(enhancedECommerceEmptyCartClickData.getData())
        return enhancedECommerceEmptyCart
    }

    private fun getActionFieldListStr(isCartEmpty: Boolean, recommendationItem: RecommendationItem): String {
        var listName: String
        if (isCartEmpty) {
            listName = EnhancedECommerceActionField.LIST_CART_RECOMMENDATION_ON_EMPTY_CART
        } else {
            listName = EnhancedECommerceActionField.LIST_CART_RECOMMENDATION
        }
        listName += recommendationItem.recommendationType
        if (recommendationItem.isTopAds) {
            listName += EnhancedECommerceActionField.LIST_CART_RECOMMENDATION_TOPADS_TYPE
        }
        return listName
    }

    private fun getEnhancedECommerceProductCartMapData(cartItemData: CartItemData): EnhancedECommerceProductCartMapData {
        val enhancedECommerceProductCartMapData = EnhancedECommerceProductCartMapData()
        enhancedECommerceProductCartMapData.setCartId(cartItemData.originData!!.cartId.toString())
        enhancedECommerceProductCartMapData.setDimension45(cartItemData.originData!!.cartId.toString())
        enhancedECommerceProductCartMapData.setProductName(cartItemData.originData!!.productName)
        enhancedECommerceProductCartMapData.setProductID(cartItemData.originData!!.productId.toString())
        enhancedECommerceProductCartMapData.setPrice(cartItemData.originData!!.pricePlanInt.toString())
        enhancedECommerceProductCartMapData.setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
        enhancedECommerceProductCartMapData.setCategory(if (TextUtils.isEmpty(cartItemData.originData!!.categoryForAnalytics))
            EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
        else
            cartItemData.originData!!.categoryForAnalytics)
        enhancedECommerceProductCartMapData.setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
        enhancedECommerceProductCartMapData.setQty(cartItemData.updatedData!!.quantity)
        enhancedECommerceProductCartMapData.setShopId(cartItemData.originData!!.shopId)
        enhancedECommerceProductCartMapData.setShopType(cartItemData.originData!!.shopType)
        enhancedECommerceProductCartMapData.setShopName(cartItemData.originData!!.shopName)
        enhancedECommerceProductCartMapData.setCategoryId(cartItemData.originData!!.categoryId)
        enhancedECommerceProductCartMapData.setAttribution(
                if (TextUtils.isEmpty(cartItemData.originData!!.trackerAttribution))
                    EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                else
                    cartItemData.originData!!.trackerAttribution
        )
        enhancedECommerceProductCartMapData.setDimension38(
                if (TextUtils.isEmpty(cartItemData.originData!!.trackerAttribution))
                    EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                else
                    cartItemData.originData!!.trackerAttribution
        )
        enhancedECommerceProductCartMapData.setListName(
                if (TextUtils.isEmpty(cartItemData.originData!!.trackerListName))
                    EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                else
                    cartItemData.originData!!.trackerListName
        )
        enhancedECommerceProductCartMapData.setDimension40(
                if (TextUtils.isEmpty(cartItemData.originData!!.trackerListName))
                    EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                else
                    cartItemData.originData!!.trackerListName
        )
        return enhancedECommerceProductCartMapData
    }

    private fun getCheckoutEnhancedECommerceProductCartMapData(cartItemData: CartItemData): EnhancedECommerceProductCartMapData {
        val enhancedECommerceProductCartMapData = EnhancedECommerceProductCartMapData()
        enhancedECommerceProductCartMapData.setDimension80(
                if (TextUtils.isEmpty(cartItemData.originData!!.trackerAttribution))
                    EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                else
                    cartItemData.originData!!.trackerAttribution
        )
        enhancedECommerceProductCartMapData.setDimension45(cartItemData.originData!!.cartId.toString())
        enhancedECommerceProductCartMapData.setDimension54(cartItemData.isFulfillment)
        enhancedECommerceProductCartMapData.setDimension53(cartItemData.originData!!.priceOriginal > 0)
        enhancedECommerceProductCartMapData.setProductName(cartItemData.originData!!.productName)
        enhancedECommerceProductCartMapData.setProductID(cartItemData.originData!!.productId.toString())
        enhancedECommerceProductCartMapData.setPrice(cartItemData.originData!!.pricePlanInt.toString())
        enhancedECommerceProductCartMapData.setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
        enhancedECommerceProductCartMapData.setCategory(if (TextUtils.isEmpty(cartItemData.originData!!.category))
            EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
        else
            cartItemData.originData!!.category)
        enhancedECommerceProductCartMapData.setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
        enhancedECommerceProductCartMapData.setQty(cartItemData.updatedData!!.quantity)
        enhancedECommerceProductCartMapData.setShopId(cartItemData.originData!!.shopId)
        enhancedECommerceProductCartMapData.setShopType(cartItemData.originData!!.shopType)
        enhancedECommerceProductCartMapData.setShopName(cartItemData.originData!!.shopName)
        enhancedECommerceProductCartMapData.setCategoryId(cartItemData.originData!!.categoryId)
        enhancedECommerceProductCartMapData.setWarehouseId(cartItemData.originData!!.warehouseId.toString())
        enhancedECommerceProductCartMapData.setProductWeight(cartItemData.originData!!.weightPlan.toString())
        enhancedECommerceProductCartMapData.setCartId(cartItemData.originData!!.cartId.toString())
        enhancedECommerceProductCartMapData.setPromoCode(cartItemData.originData!!.promoCodes)
        enhancedECommerceProductCartMapData.setPromoDetails(cartItemData.originData!!.promoDetails)
        enhancedECommerceProductCartMapData.setDimension83(if (cartItemData.originData!!.isFreeShipping)
            EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR
        else
            EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
        return enhancedECommerceProductCartMapData
    }

    override fun generateCheckoutDataAnalytics(cartItemDataList: List<CartItemData>, step: String): Map<String, Any> {
        val checkoutMapData = HashMap<String, Any>()
        val enhancedECommerceActionField = EnhancedECommerceActionField()
        enhancedECommerceActionField.setStep(step)
        if (step == EnhancedECommerceActionField.STEP_0) {
            enhancedECommerceActionField.setOption(EnhancedECommerceActionField.STEP_0_OPTION_VIEW_CART_PAGE)
        } else if (step == EnhancedECommerceActionField.STEP_1) {
            enhancedECommerceActionField.setOption(EnhancedECommerceActionField.STEP_1_OPTION_CART_PAGE_LOADED)
        }

        val enhancedECommerceCheckout = EnhancedECommerceCheckout()
        for (cartItemData in cartItemDataList) {
            val enhancedECommerceProductCartMapData = getCheckoutEnhancedECommerceProductCartMapData(cartItemData)
            enhancedECommerceCheckout.addProduct(enhancedECommerceProductCartMapData.product)
        }
        enhancedECommerceCheckout.setCurrencyCode(EnhancedECommerceCartMapData.VALUE_CURRENCY_IDR)
        enhancedECommerceCheckout.setActionField(enhancedECommerceActionField.actionFieldMap)

        checkoutMapData[EnhancedECommerceCheckout.KEY_CHECKOUT] = enhancedECommerceCheckout.checkoutMap

        return checkoutMapData
    }

    override fun setHasPerformChecklistChange() {
        hasPerformChecklistChange = true
    }

    override fun dataHasChanged(): Boolean {
        var hasChanges = false
        for ((originData, updatedData) in view!!.getAllCartDataList()) {
            if (updatedData!!.quantity != originData!!.originalQty || updatedData.remark != originData.originalRemark) {
                hasChanges = true
                break
            }
        }
        if (hasChanges) {
            for ((originData, updatedData) in view!!.getAllCartDataList()) {
                originData!!.originalQty = updatedData!!.quantity
                originData.originalRemark = updatedData.remark
            }
        }
        return hasChanges
    }

    override fun processGetRecentViewData() {
        try {
            val userId = Integer.parseInt(userSessionInterface.userId)
            getRecentViewUseCase!!.createObservable(userId, GetRecentViewSubscriber(view, this))
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }

    }

    override fun processGetWishlistData() {
        getWishlistUseCase!!.createObservable(GetWishlistSubscriber(view, this))
    }

    override fun processGetRecommendationData(page: Int, allProductIds: List<String>) {
        view!!.showItemLoading()
        val requestParam = getRecommendationUseCase!!.getRecomParams(
                page, "recom_widget", "cart", allProductIds, "")
        getRecommendationUseCase.execute(requestParam, GetRecommendationSubscriber(view, this))
    }

    override fun processAddToCart(productModel: Any) {
        view!!.showProgressLoading()

        var productId = 0
        var shopId = 0
        var externalSource = ""
        if (productModel is CartWishlistItemHolderData) {
            val (id, _, _, _, _, _, _, _, _, _, shopId1) = productModel
            productId = Integer.parseInt(id)
            shopId = Integer.parseInt(shopId1)
            externalSource = AddToCartRequestParams.ATC_FROM_WISHLIST
        } else if (productModel is CartRecentViewItemHolderData) {
            val (id, _, _, _, _, _, _, _, _, shopId1) = productModel
            productId = Integer.parseInt(id)
            shopId = Integer.parseInt(shopId1)
            externalSource = AddToCartRequestParams.ATC_FROM_RECENT_VIEW
        } else if (productModel is CartRecommendationItemHolderData) {
            val (recommendationItem) = productModel
            productId = recommendationItem.productId
            shopId = recommendationItem.shopId
            externalSource = AddToCartRequestParams.ATC_FROM_RECOMMENDATION
        }

        val addToCartRequestParams = AddToCartRequestParams()
        addToCartRequestParams.productId = productId
        addToCartRequestParams.shopId = shopId
        addToCartRequestParams.quantity = 0
        addToCartRequestParams.notes = ""
        addToCartRequestParams.warehouseId = 0
        addToCartRequestParams.atcFromExternalSource = externalSource

        val requestParams = RequestParams.create()
        requestParams.putObject(AddToCartUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, addToCartRequestParams)
        addToCartUseCase!!.createObservable(requestParams)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(AddToCartSubscriber(view, this, productModel))
    }

    override fun generateAddToCartEnhanceEcommerceDataLayer(cartWishlistItemHolderData: CartWishlistItemHolderData,
                                                            addToCartDataResponseModel: AddToCartDataModel, isCartEmpty: Boolean): Map<String, Any> {
        val stringObjectMap = HashMap<String, Any>()
        val enhancedECommerceActionField = EnhancedECommerceActionField()
        enhancedECommerceActionField.setList(if (isCartEmpty) EnhancedECommerceActionField.LIST_WISHLIST_ON_EMPTY_CART else EnhancedECommerceActionField.LIST_WISHLIST)
        val enhancedECommerceProductCartMapData = EnhancedECommerceProductCartMapData()
        enhancedECommerceProductCartMapData.setProductName(cartWishlistItemHolderData.name)
        enhancedECommerceProductCartMapData.setProductID(cartWishlistItemHolderData.id)
        enhancedECommerceProductCartMapData.setPrice(cartWishlistItemHolderData.rawPrice)
        enhancedECommerceProductCartMapData.setCategory(cartWishlistItemHolderData.category)
        enhancedECommerceProductCartMapData.setQty(cartWishlistItemHolderData.minOrder)
        enhancedECommerceProductCartMapData.setShopId(cartWishlistItemHolderData.shopId)
        enhancedECommerceProductCartMapData.setShopType(cartWishlistItemHolderData.shopType)
        enhancedECommerceProductCartMapData.setShopName(cartWishlistItemHolderData.shopName)
        enhancedECommerceProductCartMapData.setPicture(cartWishlistItemHolderData.imageUrl)
        enhancedECommerceProductCartMapData.setUrl(cartWishlistItemHolderData.url)
        enhancedECommerceProductCartMapData.setDimension45(addToCartDataResponseModel.data.cartId.toString())
        enhancedECommerceProductCartMapData.setBrand("")
        enhancedECommerceProductCartMapData.setCategoryId("")
        enhancedECommerceProductCartMapData.setVariant("")

        val enhancedECommerceAdd = EnhancedECommerceAdd()
        enhancedECommerceAdd.setActionField(enhancedECommerceActionField.actionFieldMap)
        enhancedECommerceAdd.addProduct(enhancedECommerceProductCartMapData.product)

        stringObjectMap["currencyCode"] = "IDR"
        stringObjectMap[EnhancedECommerceAdd.KEY_ADD] = enhancedECommerceAdd.getAddMap()
        return stringObjectMap
    }

    override fun generateAddToCartEnhanceEcommerceDataLayer(cartRecentViewItemHolderData: CartRecentViewItemHolderData,
                                                            addToCartDataResponseModel: AddToCartDataModel, isCartEmpty: Boolean): Map<String, Any> {
        val stringObjectMap = HashMap<String, Any>()
        val enhancedECommerceActionField = EnhancedECommerceActionField()
        enhancedECommerceActionField.setList(if (isCartEmpty) EnhancedECommerceActionField.LIST_RECENT_VIEW_ON_EMPTY_CART else EnhancedECommerceActionField.LIST_RECENT_VIEW)
        val enhancedECommerceProductCartMapData = EnhancedECommerceProductCartMapData()
        enhancedECommerceProductCartMapData.setProductName(cartRecentViewItemHolderData.name)
        enhancedECommerceProductCartMapData.setProductID(cartRecentViewItemHolderData.id)
        enhancedECommerceProductCartMapData.setPrice(cartRecentViewItemHolderData.price)
        enhancedECommerceProductCartMapData.setQty(cartRecentViewItemHolderData.minOrder)
        enhancedECommerceProductCartMapData.setDimension52(cartRecentViewItemHolderData.shopId)
        enhancedECommerceProductCartMapData.setDimension57(cartRecentViewItemHolderData.shopName)
        enhancedECommerceProductCartMapData.setDimension59(cartRecentViewItemHolderData.shopType)
        enhancedECommerceProductCartMapData.setDimension77(addToCartDataResponseModel.data.cartId.toString())
        enhancedECommerceProductCartMapData.setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
        enhancedECommerceProductCartMapData.setCategoryId("")
        enhancedECommerceProductCartMapData.setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)

        val enhancedECommerceAdd = EnhancedECommerceAdd()
        enhancedECommerceAdd.setActionField(enhancedECommerceActionField.actionFieldMap)
        enhancedECommerceAdd.addProduct(enhancedECommerceProductCartMapData.product)

        stringObjectMap["currencyCode"] = "IDR"
        stringObjectMap[EnhancedECommerceAdd.KEY_ADD] = enhancedECommerceAdd.getAddMap()
        return stringObjectMap
    }

    override fun generateAddToCartEnhanceEcommerceDataLayer(cartRecommendationItemHolderData: CartRecommendationItemHolderData,
                                                            addToCartDataResponseModel: AddToCartDataModel, isCartEmpty: Boolean): Map<String, Any> {
        val stringObjectMap = HashMap<String, Any>()
        val enhancedECommerceActionField = EnhancedECommerceActionField()
        enhancedECommerceActionField.setList(getActionFieldListStr(isCartEmpty, cartRecommendationItemHolderData.recommendationItem))
        val enhancedECommerceProductCartMapData = EnhancedECommerceRecomProductCartMapData()
        enhancedECommerceProductCartMapData.setProductName(cartRecommendationItemHolderData.recommendationItem.name)
        enhancedECommerceProductCartMapData.setProductID(cartRecommendationItemHolderData.recommendationItem.productId.toString())
        enhancedECommerceProductCartMapData.setPrice(cartRecommendationItemHolderData.recommendationItem.price.replace("[^0-9]".toRegex(), ""))
        enhancedECommerceProductCartMapData.setCategory(cartRecommendationItemHolderData.recommendationItem.categoryBreadcrumbs)
        enhancedECommerceProductCartMapData.setQty(cartRecommendationItemHolderData.recommendationItem.minOrder)
        enhancedECommerceProductCartMapData.setShopId(cartRecommendationItemHolderData.recommendationItem.shopId.toString())
        enhancedECommerceProductCartMapData.setShopType(cartRecommendationItemHolderData.recommendationItem.shopType)
        enhancedECommerceProductCartMapData.setShopName(cartRecommendationItemHolderData.recommendationItem.shopName)
        enhancedECommerceProductCartMapData.setDimension45(addToCartDataResponseModel.data.cartId.toString())
        enhancedECommerceProductCartMapData.setDimension53(cartRecommendationItemHolderData.recommendationItem.discountPercentageInt > 0)
        enhancedECommerceProductCartMapData.setDimension40(addToCartDataResponseModel.data.trackerListName)

        enhancedECommerceProductCartMapData.setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
        enhancedECommerceProductCartMapData.setCategoryId("")
        enhancedECommerceProductCartMapData.setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)

        if (cartRecommendationItemHolderData.recommendationItem.isFreeOngkirActive) {
            enhancedECommerceProductCartMapData.setDimension83(EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR)
        } else {
            enhancedECommerceProductCartMapData.setDimension83(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
        }

        val enhancedECommerceAdd = EnhancedECommerceAdd()
        enhancedECommerceAdd.setActionField(enhancedECommerceActionField.actionFieldMap)
        enhancedECommerceAdd.addProduct(enhancedECommerceProductCartMapData.getProduct())

        stringObjectMap["currencyCode"] = "IDR"
        stringObjectMap[EnhancedECommerceAdd.KEY_ADD] = enhancedECommerceAdd.getAddMap()
        return stringObjectMap
    }

    override fun generateRecentViewProductClickDataLayer(cartRecentViewItemHolderData: CartRecentViewItemHolderData, position: Int): Map<String, Any> {
        val stringObjectMap = HashMap<String, Any>()
        val enhancedECommerceActionField = EnhancedECommerceActionField()
        enhancedECommerceActionField.setList(EnhancedECommerceEmptyCartActionFieldData.VALUE_SECTION_NAME_RECENT_VIEW)
        val enhancedECommerceProductCartMapData = EnhancedECommerceProductCartMapData()

        enhancedECommerceProductCartMapData.setProductName(cartRecentViewItemHolderData.name)
        enhancedECommerceProductCartMapData.setProductID(cartRecentViewItemHolderData.id)
        enhancedECommerceProductCartMapData.setPrice(cartRecentViewItemHolderData.price.replace("[^0-9]".toRegex(), ""))
        enhancedECommerceProductCartMapData.setCategory(EnhancedECommerceEmptyCartProductData.DEFAULT_VALUE_NONE_OTHER)
        enhancedECommerceProductCartMapData.setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
        enhancedECommerceProductCartMapData.setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
        enhancedECommerceProductCartMapData.setPosition(position.toString())

        val enhancedECommerceAdd = EnhancedECommerceAdd()
        enhancedECommerceAdd.setActionField(enhancedECommerceActionField.actionFieldMap)
        enhancedECommerceAdd.addProduct(enhancedECommerceProductCartMapData.product)

        stringObjectMap["currencyCode"] = "IDR"
        stringObjectMap[EnhancedECommerceAdd.KEY_ADD] = enhancedECommerceAdd.getAddMap()
        return stringObjectMap
    }

    override fun generateWishlistProductClickDataLayer(cartWishlistItemHolderData: CartWishlistItemHolderData, position: Int): Map<String, Any> {
        val stringObjectMap = HashMap<String, Any>()
        val enhancedECommerceActionField = EnhancedECommerceActionField()
        enhancedECommerceActionField.setList(EnhancedECommerceEmptyCartActionFieldData.VALUE_SECTION_NAME_WISHLIST)
        val enhancedECommerceProductCartMapData = EnhancedECommerceProductCartMapData()

        enhancedECommerceProductCartMapData.setProductName(cartWishlistItemHolderData.name)
        enhancedECommerceProductCartMapData.setProductID(cartWishlistItemHolderData.id)
        enhancedECommerceProductCartMapData.setPrice(cartWishlistItemHolderData.price.replace("[^0-9]".toRegex(), ""))
        enhancedECommerceProductCartMapData.setCategory(cartWishlistItemHolderData.category)
        enhancedECommerceProductCartMapData.setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
        enhancedECommerceProductCartMapData.setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
        enhancedECommerceProductCartMapData.setPosition(position.toString())

        val enhancedECommerceAdd = EnhancedECommerceAdd()
        enhancedECommerceAdd.setActionField(enhancedECommerceActionField.actionFieldMap)
        enhancedECommerceAdd.addProduct(enhancedECommerceProductCartMapData.product)

        stringObjectMap["currencyCode"] = "IDR"
        stringObjectMap[EnhancedECommerceAdd.KEY_ADD] = enhancedECommerceAdd.getAddMap()
        return stringObjectMap
    }

    private fun getProductRecentViewImpressionMapData(recentViewItemHolderData: CartRecentViewItemHolderData, isEmptyCart: Boolean, position: Int): EnhancedECommerceProductCartMapData {
        val enhancedECommerceProductCartMapData = EnhancedECommerceProductCartMapData()
        enhancedECommerceProductCartMapData.setProductID(recentViewItemHolderData.id)
        enhancedECommerceProductCartMapData.setProductName(recentViewItemHolderData.name)
        enhancedECommerceProductCartMapData.setPrice(recentViewItemHolderData.price.replace("[^0-9]".toRegex(), ""))
        enhancedECommerceProductCartMapData.setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
        enhancedECommerceProductCartMapData.setCategory(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
        enhancedECommerceProductCartMapData.setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)

        if (isEmptyCart) {
            enhancedECommerceProductCartMapData.setListName(EnhancedECommerceEmptyCartActionFieldData.VALUE_SECTION_NAME_RECENT_VIEW_EMPTY_CART)
        } else {
            enhancedECommerceProductCartMapData.setListName(EnhancedECommerceEmptyCartActionFieldData.VALUE_SECTION_NAME_RECENT_VIEW)
        }

        enhancedECommerceProductCartMapData.setPosition(position.toString())
        return enhancedECommerceProductCartMapData
    }

    override fun generateRecentViewProductClickEmptyCartDataLayer(cartRecentViewItemHolderData: CartRecentViewItemHolderData, position: Int): Map<String, Any> {
        val enhancedECommerceEmptyCartProductData = EnhancedECommerceEmptyCartProductData()
        enhancedECommerceEmptyCartProductData.setProductID(cartRecentViewItemHolderData.id)
        enhancedECommerceEmptyCartProductData.setProductName(cartRecentViewItemHolderData.name)
        enhancedECommerceEmptyCartProductData.setPrice(cartRecentViewItemHolderData.price.replace("[^0-9]".toRegex(), ""))
        enhancedECommerceEmptyCartProductData.setBrand(EnhancedECommerceEmptyCartProductData.DEFAULT_VALUE_NONE_OTHER)
        enhancedECommerceEmptyCartProductData.setCategory(EnhancedECommerceEmptyCartProductData.DEFAULT_VALUE_NONE_OTHER)
        enhancedECommerceEmptyCartProductData.setPosition(position.toString())
        enhancedECommerceEmptyCartProductData.setVariant(EnhancedECommerceEmptyCartProductData.DEFAULT_VALUE_NONE_OTHER)
        val productsData = ArrayList<Map<String, Any>>()
        productsData.add(enhancedECommerceEmptyCartProductData.getProduct())

        val enhancedECommerceEmptyCart = getEnhancedECommerceOnClickEmptyCartData(
                productsData, EnhancedECommerceEmptyCartActionFieldData.VALUE_SECTION_NAME_RECENT_VIEW_EMPTY_CART)

        return enhancedECommerceEmptyCart.getData()
    }

    private fun getProductWishlistImpressionMapData(wishlistItemHolderData: CartWishlistItemHolderData, isEmptyCart: Boolean, position: Int): EnhancedECommerceProductCartMapData {
        val enhancedECommerceProductCartMapData = EnhancedECommerceProductCartMapData()
        enhancedECommerceProductCartMapData.setProductID(wishlistItemHolderData.id)
        enhancedECommerceProductCartMapData.setProductName(wishlistItemHolderData.name)
        enhancedECommerceProductCartMapData.setPrice(wishlistItemHolderData.price.replace("[^0-9]".toRegex(), ""))
        enhancedECommerceProductCartMapData.setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
        enhancedECommerceProductCartMapData.setCategory(wishlistItemHolderData.category)
        enhancedECommerceProductCartMapData.setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)

        if (isEmptyCart) {
            enhancedECommerceProductCartMapData.setListName(EnhancedECommerceEmptyCartActionFieldData.VALUE_SECTION_NAME_WISHLIST_EMPTY_CART)
        } else {
            enhancedECommerceProductCartMapData.setListName(EnhancedECommerceEmptyCartActionFieldData.VALUE_SECTION_NAME_WISHLIST)
        }

        enhancedECommerceProductCartMapData.setPosition(position.toString())
        return enhancedECommerceProductCartMapData
    }

    override fun generateWishlistProductClickEmptyCartDataLayer(cartWishlistItemHolderData: CartWishlistItemHolderData, position: Int): Map<String, Any> {
        val enhancedECommerceEmptyCartProductData = EnhancedECommerceEmptyCartProductData()
        enhancedECommerceEmptyCartProductData.setProductID(cartWishlistItemHolderData.id)
        enhancedECommerceEmptyCartProductData.setProductName(cartWishlistItemHolderData.name)
        enhancedECommerceEmptyCartProductData.setPrice(cartWishlistItemHolderData.price.replace("[^0-9]".toRegex(), ""))
        enhancedECommerceEmptyCartProductData.setBrand(EnhancedECommerceEmptyCartProductData.DEFAULT_VALUE_NONE_OTHER)
        enhancedECommerceEmptyCartProductData.setCategory(cartWishlistItemHolderData.category)
        enhancedECommerceEmptyCartProductData.setPosition(position.toString())
        enhancedECommerceEmptyCartProductData.setVariant(EnhancedECommerceEmptyCartProductData.DEFAULT_VALUE_NONE_OTHER)
        val productsData = ArrayList<Map<String, Any>>()
        productsData.add(enhancedECommerceEmptyCartProductData.getProduct())

        val enhancedECommerceEmptyCart = getEnhancedECommerceOnClickEmptyCartData(
                productsData, EnhancedECommerceEmptyCartActionFieldData.VALUE_SECTION_NAME_WISHLIST_EMPTY_CART)

        return enhancedECommerceEmptyCart.getData()
    }

    override fun redirectToLite(url: String) {
        if (view != null && view!!.getActivityObject() != null) {
            view!!.showProgressLoading()
            val localCacheHandler = LocalCacheHandler(view!!.getActivityObject()!!, ADVERTISINGID)
            val adsId = localCacheHandler.getString(KEY_ADVERTISINGID)
            if (adsId != null && !adsId.trim { it <= ' ' }.isEmpty()) {
                seamlessLoginUsecase.generateSeamlessUrl(url.replace(QUERY_APP_CLIENT_ID, adsId), object : SeamlessLoginSubscriber {
                    override fun onUrlGenerated(url: String) {
                        if (view != null) {
                            view!!.hideProgressLoading()
                            view!!.goToLite(url)
                        }
                    }

                    override fun onError(msg: String) {
                        if (view != null) {
                            view!!.hideProgressLoading()
                            view!!.showToastMessageRed(msg)
                        }
                    }
                })
            } else {
                view!!.hideProgressLoading()
                view!!.showToastMessageRed(ErrorHandler.getErrorMessage(view!!.getActivityObject(), null))
            }
        }
    }

    override fun generateCheckPromoFirstStepParam(promoStackingGlobalData: PromoStackingData): Promo {
        val shopGroupAvailableDataList = cartListData!!.shopGroupAvailableDataList
        val orders = ArrayList<Order>()
        for (shopGroupAvailableData in shopGroupAvailableDataList) {
            val order = Order()
            val productDetails = ArrayList<ProductDetail>()
            for ((cartItemData) in shopGroupAvailableData.cartItemDataList!!) {
                val productDetail = ProductDetail()
                try {
                    productDetail.productId = Integer.parseInt(cartItemData.originData!!.productId!!)
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                    productDetail.productId = 0
                }

                productDetail.quantity = cartItemData.updatedData!!.quantity
                productDetails.add(productDetail)
            }
            if (shopGroupAvailableData.voucherOrdersItemData != null && !TextUtils.isEmpty(shopGroupAvailableData.voucherOrdersItemData!!.code)) {
                val merchantPromoCodes = ArrayList<String>()
                merchantPromoCodes.add(shopGroupAvailableData.voucherOrdersItemData!!.code)
                if (merchantPromoCodes.size > 0) {
                    order.codes = merchantPromoCodes
                }
            }
            order.productDetails = productDetails
            order.uniqueId = shopGroupAvailableData.cartString
            try {
                order.shopId = Integer.parseInt(shopGroupAvailableData.shopId!!)
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                order.shopId = 0
            }

            orders.add(order)
        }
        val promo = Promo()
        promo.state = Promo.STATE_CART
        promo.cartType = Promo.CART_TYPE_DEFAULT
        if (promoStackingGlobalData != null) {
            val globalPromoCodes = ArrayList<String>()
            globalPromoCodes.add(promoStackingGlobalData.promoCode)
            promo.codes = globalPromoCodes
        }
        promo.orders = orders
        promo.skipApply = 0
        promo.isSuggested = 0
        return promo
    }

    companion object {
        private val PERCENTAGE = 100.0f

        private val PARAM_GLOBAL = "global"

        val ITEM_CHECKED_ALL_WITHOUT_CHANGES = 0
        val ITEM_CHECKED_ALL_WITH_CHANGES = 1
        val ITEM_CHECKED_PARTIAL_SHOP = 3
        val ITEM_CHECKED_PARTIAL_ITEM = 4
        val ITEM_CHECKED_PARTIAL_SHOP_AND_ITEM = 5

        private val ADVERTISINGID = "ADVERTISINGID"
        private val KEY_ADVERTISINGID = "KEY_ADVERTISINGID"
        private val QUERY_APP_CLIENT_ID = "{app_client_id}"
    }
}
