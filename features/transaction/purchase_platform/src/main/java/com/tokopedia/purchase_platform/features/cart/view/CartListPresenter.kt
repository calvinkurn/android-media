package com.tokopedia.purchase_platform.features.cart.view

import android.os.Build
import android.text.TextUtils
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.*
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.request.*
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartDigitalProduct
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartShops
import com.tokopedia.purchase_platform.common.domain.schedulers.ExecutorSchedulers
import com.tokopedia.purchase_platform.common.domain.usecase.GetInsuranceCartUseCase
import com.tokopedia.purchase_platform.common.domain.usecase.RemoveInsuranceProductUsecase
import com.tokopedia.purchase_platform.common.domain.usecase.UpdateInsuranceProductDataUsecase
import com.tokopedia.purchase_platform.features.cart.data.model.request.RemoveCartRequest
import com.tokopedia.purchase_platform.features.cart.data.model.request.UpdateCartRequest
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartItemData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData
import com.tokopedia.purchase_platform.features.cart.domain.usecase.*
import com.tokopedia.purchase_platform.features.cart.view.analytics.EnhancedECommerceActionFieldData
import com.tokopedia.purchase_platform.features.cart.view.analytics.EnhancedECommerceClickData
import com.tokopedia.purchase_platform.features.cart.view.analytics.EnhancedECommerceData
import com.tokopedia.purchase_platform.features.cart.view.analytics.EnhancedECommerceProductData
import com.tokopedia.purchase_platform.features.cart.view.subscriber.*
import com.tokopedia.purchase_platform.features.cart.view.uimodel.*
import com.tokopedia.purchase_platform.features.promo.data.request.validate_use.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.features.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use.ValidateUsePromoRevampUiModel
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
import rx.subscriptions.CompositeSubscription
import java.util.*
import javax.inject.Inject

/**
 * @author anggaprasetiyo on 18/01/18.
 */

class CartListPresenter @Inject constructor(private val getCartListSimplifiedUseCase: GetCartListSimplifiedUseCase?,
                                            private val deleteCartUseCase: DeleteCartUseCase?,
                                            private val updateCartUseCase: UpdateCartUseCase?,
                                            private val compositeSubscription: CompositeSubscription,
                                            private val addWishListUseCase: AddWishListUseCase?,
                                            private val removeWishListUseCase: RemoveWishListUseCase?,
                                            private val updateAndReloadCartUseCase: UpdateAndReloadCartUseCase?,
                                            private val userSessionInterface: UserSessionInterface,
                                            private val clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase?,
                                            private val getRecentViewUseCase: GetRecentViewUseCase?,
                                            private val getWishlistUseCase: GetWishlistUseCase?,
                                            private val getRecommendationUseCase: GetRecommendationUseCase?,
                                            private val addToCartUseCase: AddToCartUseCase?,
                                            private val getInsuranceCartUseCase: GetInsuranceCartUseCase?,
                                            private val removeInsuranceProductUsecase: RemoveInsuranceProductUsecase?,
                                            private val updateInsuranceProductDataUsecase: UpdateInsuranceProductDataUsecase?,
                                            private val seamlessLoginUsecase: SeamlessLoginUsecase,
                                            private val updateCartCounterUseCase: UpdateCartCounterUseCase,
                                            private val updateCartAndValidateUseUseCase: UpdateCartAndValidateUseUseCase,
                                            private val validateUsePromoRevampUseCase: ValidateUsePromoRevampUseCase,
                                            private val schedulers: ExecutorSchedulers) : ICartListPresenter {

    private var view: ICartListView? = null
    private var cartListData: CartListData? = null
    private var hasPerformChecklistChange: Boolean = false
    private var insuranceChecked = true
    var lastValidateUseResponse: ValidateUsePromoRevampUiModel? = null
    var isLastApplyResponseStillValid = true

    companion object {
        private val PERCENTAGE = 100.0f

        private val PARAM_GLOBAL = "global"

        val ITEM_CHECKED_ALL_WITHOUT_CHANGES = 0
        val ITEM_CHECKED_ALL_WITH_CHANGES = 1
        val ITEM_CHECKED_PARTIAL_SHOP = 3
        val ITEM_CHECKED_PARTIAL_ITEM = 4
        val ITEM_CHECKED_PARTIAL_SHOP_AND_ITEM = 5

        private val QUERY_APP_CLIENT_ID = "{app_client_id}"
    }

    override fun attachView(view: ICartListView) {
        this.view = view
    }

    override fun detachView() {
        compositeSubscription.unsubscribe()
        addWishListUseCase?.unsubscribe()
        removeWishListUseCase?.unsubscribe()
        getRecommendationUseCase?.unsubscribe()
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
        view?.let {
            if (initialLoad) {
                it.renderLoadGetCartData()
            } else if (!isLoadingTypeRefresh) {
                it.showProgressLoading()
            }

            val requestParams = RequestParams.create()
            requestParams.putString(GetCartListSimplifiedUseCase.PARAM_SELECTED_CART_ID, cartId)

            compositeSubscription.add(getCartListSimplifiedUseCase?.createObservable(requestParams)
                    ?.subscribe(GetCartListDataSubscriber(view, this, initialLoad))
            )
        }
    }

    override fun getInsuranceTechCart() {
        view?.let {
            getInsuranceCartUseCase?.execute(GetInsuranceCartSubscriber(it))
        }
    }

    override fun setAllInsuranceProductsChecked(insuranceCartShopsArrayList: ArrayList<InsuranceCartShops>, isChecked: Boolean) {
        for (insuranceCartShops in insuranceCartShopsArrayList) {
            if (insuranceCartShops.shopItemsList.size > 0 && insuranceCartShops.shopItemsList[0].digitalProductList.size > 0) {
                insuranceCartShops.shopItemsList[0].digitalProductList[0].optIn = isChecked
            }
        }
    }

    override fun processDeleteCartInsurance(insuranceCartShopsArrayList: ArrayList<InsuranceCartDigitalProduct>, showToaster: Boolean) {
        view?.let {
            if (insuranceCartShopsArrayList.isNotEmpty()) {
                view?.showProgressLoading()
                val cartIdList = ArrayList<String>()
                val removeInsuranceDataArrayList = ArrayList<RemoveInsuranceData>()
                val productIdArrayList = ArrayList<Long>()
                for (insuranceCartDigitalProduct in insuranceCartShopsArrayList) {
                    var shopid: Long
                    var productId: Long
                    try {
                        shopid = java.lang.Long.parseLong(insuranceCartDigitalProduct.shopId)
                    } catch (e: Exception) {
                        shopid = 0
                    }

                    try {
                        productId = java.lang.Long.parseLong(insuranceCartDigitalProduct.productId)
                    } catch (e: Exception) {
                        productId = 0
                    }

                    cartIdList.add(insuranceCartDigitalProduct.cartItemId.toString())
                    val removeInsuranceData = RemoveInsuranceData(insuranceCartDigitalProduct.cartItemId, shopid, productId)
                    removeInsuranceDataArrayList.add(removeInsuranceData)
                    productIdArrayList.add(productId)
                }

                removeInsuranceProductUsecase?.setRequestParams(removeInsuranceDataArrayList, "cart", Build.VERSION.SDK_INT.toString(), cartIdList)
                removeInsuranceProductUsecase?.execute(GetRemoveMacroInsuranceProductSubscriber(it, productIdArrayList, showToaster))
            }
        }
    }

    override fun updateInsuranceProductData(insuranceCartShops: InsuranceCartShops,
                                            updateInsuranceProductApplicationDetailsArrayList: ArrayList<UpdateInsuranceProductApplicationDetails>) {
        view?.let {
            it.showProgressLoading()

            val cartIdList = ArrayList<UpdateInsuranceDataCart>()
            val updateInsuranceDataArrayList = ArrayList<UpdateInsuranceData>()
            val updateInsuranceProductItemsArrayList = ArrayList<UpdateInsuranceProductItems>()

            val shopid = insuranceCartShops.shopId

            var productId: Long = 0

            if (insuranceCartShops.shopItemsList.isNotEmpty()) {
                for (insuranceCartShopItem in insuranceCartShops.shopItemsList) {
                    val updateInsuranceProductArrayList = ArrayList<UpdateInsuranceProduct>()
                    for (insuranceCartDigitalProduct in insuranceCartShopItem.digitalProductList) {
                        if (!insuranceCartDigitalProduct.isProductLevel) {
                            val cartId = insuranceCartShopItem.digitalProductList[0].cartItemId
                            productId = insuranceCartShopItem.productId
                            cartIdList.add(UpdateInsuranceDataCart(cartId.toString(), 1))

                            val updateInsuranceProduct = UpdateInsuranceProduct(insuranceCartDigitalProduct.digitalProductId,
                                    insuranceCartDigitalProduct.cartItemId,
                                    insuranceCartDigitalProduct.typeId,
                                    updateInsuranceProductApplicationDetailsArrayList)
                            updateInsuranceProductArrayList.add(updateInsuranceProduct)

                            val updateInsuranceProductItems = UpdateInsuranceProductItems(insuranceCartShopItem.productId, 1, updateInsuranceProductArrayList)
                            updateInsuranceProductItemsArrayList.add(updateInsuranceProductItems)
                            val updateInsuranceData = UpdateInsuranceData(shopid, updateInsuranceProductItemsArrayList)
                            updateInsuranceDataArrayList.add(updateInsuranceData)
                        }
                    }
                }
            }

            updateInsuranceProductDataUsecase?.setRequestParams(updateInsuranceDataArrayList, "cart", Build.VERSION.SDK_INT.toString(), cartIdList)
            updateInsuranceProductDataUsecase?.execute(GetSubscriberUpdateInsuranceProductData(it, this, productId))
        }
    }

    override fun processDeleteCartItem(allCartItemData: List<CartItemData>,
                                       removedCartItems: List<CartItemData>,
                                       appliedPromoCodeList: ArrayList<String>?,
                                       addWishList: Boolean,
                                       removeInsurance: Boolean) {
        view?.let {
            it.showProgressLoading()

            val removeAllItem = allCartItemData.size == removedCartItems.size

            var tmpAppliedPromoCodeList = appliedPromoCodeList
            if (tmpAppliedPromoCodeList == null) {
                tmpAppliedPromoCodeList = ArrayList()
            }

            val toBeDeletedCartIds = ArrayList<String>()
            for (cartItemData in removedCartItems) {
                toBeDeletedCartIds.add(cartItemData.originData?.cartId?.toString() ?: "0")
            }

            val removeCartRequest = RemoveCartRequest()
            removeCartRequest.addWishlist = if (addWishList) 1 else 0
            removeCartRequest.cartIds = toBeDeletedCartIds

            val requestParams = RequestParams.create()
            requestParams.putObject(DeleteCartUseCase.PARAM_REMOVE_CART_REQUEST, removeCartRequest)
            requestParams.putObject(DeleteCartUseCase.PARAM_TO_BE_REMOVED_PROMO_CODES, tmpAppliedPromoCodeList)

            compositeSubscription.add(deleteCartUseCase?.createObservable(requestParams)
                    ?.subscribe(DeleteCartItemSubscriber(view, this, toBeDeletedCartIds,
                            removeAllItem, removeInsurance)))
        }
    }

    override fun processUpdateCartData(fireAndForget: Boolean) {
        view?.let {
            if (!fireAndForget) {
                it.showProgressLoading()
            }

            val updateCartRequestList = getUpdateCartRequest(it.getAllSelectedCartDataList()
                    ?: emptyList())
            val requestParams = RequestParams.create()
            requestParams.putObject(UpdateCartUseCase.PARAM_UPDATE_CART_REQUEST, updateCartRequestList)

            compositeSubscription.add(
                    updateCartUseCase?.createObservable(requestParams)
                            ?.subscribe(UpdateCartSubscriber(view, this, fireAndForget))
            )
        }
    }

    override fun processToUpdateAndReloadCartData(cartId: String) {
        view?.let {
            val cartItemDataList = ArrayList<CartItemData>()
            for (data in it.getAllAvailableCartDataList()) {
                if (!data.isError) {
                    cartItemDataList.add(data)
                }
            }

            val updateCartRequestList = getUpdateCartRequest(cartItemDataList)
            val requestParams = RequestParams.create()
            requestParams.putObject(UpdateCartUseCase.PARAM_UPDATE_CART_REQUEST, updateCartRequestList)
            requestParams.putString(GetCartListSimplifiedUseCase.PARAM_SELECTED_CART_ID, cartId)

            compositeSubscription.add(
                    updateAndReloadCartUseCase?.createObservable(requestParams)
                            ?.subscribe(UpdateAndReloadCartSubscriber(it, this))
            )
        }
    }

    private fun getUpdateCartRequest(cartItemDataList: List<CartItemData>): ArrayList<UpdateCartRequest> {
        val updateCartRequestList = ArrayList<UpdateCartRequest>()
        for (cartItemData in cartItemDataList) {
            val updateCartRequest = UpdateCartRequest()
            updateCartRequest.cartId = cartItemData.originData?.cartId?.toString() ?: "0"
            updateCartRequest.notes = cartItemData.updatedData?.remark
            updateCartRequest.quantity = cartItemData.updatedData?.quantity ?: 0
            updateCartRequestList.add(updateCartRequest)
        }
        return updateCartRequestList
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
                        cartShopHolderData.shopGroupAvailableData.cartItemDataList?.let {
                            for (cartItemHolderData in it) {
                                if (cartItemHolderData.cartItemData?.isError == false) {
                                    if (cartItemHolderData.isSelected) {
                                        allCartItemDataList.add(cartItemHolderData)
                                    }
                                } else {
                                    errorProductCount++
                                }
                            }
                        }
                    }
                } else {
                    errorProductCount += cartShopHolderData.shopGroupAvailableData.cartItemDataList?.size
                            ?: 0
                }
            }
        }

        // Set cart item parent id if current value is 0
        for (i in allCartItemDataList.indices) {
            val cartItemData = allCartItemDataList[i].cartItemData
            if (cartItemData?.originData?.parentId == "0") {
                cartItemData.originData?.parentId = (i + 1).toString()
            }
        }

        // Calculate total price, total item, and wholesale price (if any)
        val cashbackWholesalePriceMap = HashMap<String, Double>()
        val subtotalWholesalePriceMap = HashMap<String, Double>()
        val cartItemParentIdMap = HashMap<String, CartItemData>()
        for (cartItemHolderData in allCartItemDataList) {
            cartItemHolderData.cartItemData?.originData?.let {
                val parentId = it.parentId ?: "0"
                val productId = it.productId
                var itemQty = cartItemHolderData.cartItemData?.updatedData?.quantity ?: 0
                totalItemQty += itemQty
                if (!TextUtils.isEmpty(parentId) && parentId != "0") {
                    for (cartItemHolderDataTmp in allCartItemDataList) {
                        if (productId != cartItemHolderDataTmp.cartItemData?.originData?.productId &&
                                parentId == cartItemHolderDataTmp.cartItemData?.originData?.parentId &&
                                cartItemHolderDataTmp.cartItemData?.originData?.pricePlan == cartItemHolderData.cartItemData?.originData?.pricePlan) {
                            itemQty += cartItemHolderDataTmp.cartItemData?.updatedData?.quantity
                                    ?: 0
                        }
                    }
                }

                var hasCalculateWholesalePrice = false
                val wholesalePriceDataList = it.wholesalePriceData
                if (wholesalePriceDataList != null && wholesalePriceDataList.isNotEmpty()) {
                    var subTotalWholesalePrice = 0.0
                    var itemCashback = 0.0
                    for (wholesalePriceData in wholesalePriceDataList) {
                        if (itemQty >= wholesalePriceData.qtyMin) {
                            subTotalWholesalePrice = (itemQty * wholesalePriceData.prdPrc).toDouble()
                            hasCalculateWholesalePrice = true
                            val wholesalePriceFormatted = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                                    wholesalePriceData.prdPrc, false)
                            it.wholesalePriceFormatted = wholesalePriceFormatted
                            break
                        }
                    }
                    if (!hasCalculateWholesalePrice) {
                        if (itemQty > wholesalePriceDataList[wholesalePriceDataList.size - 1].prdPrc) {
                            subTotalWholesalePrice = (itemQty * wholesalePriceDataList[wholesalePriceDataList.size - 1].prdPrc).toDouble()
                            val wholesalePriceFormatted = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                                    wholesalePriceDataList[wholesalePriceDataList.size - 1].prdPrc, false)
                            it.wholesalePriceFormatted = wholesalePriceFormatted
                        } else {
                            subTotalWholesalePrice = itemQty * it.pricePlan
                            it.wholesalePriceFormatted = null
                        }
                    }
                    if (it.isCashBack) {
                        val cashbackPercentageString = it.productCashBack?.replace("%", "")
                        val cashbackPercentage = cashbackPercentageString?.toDouble()
                                ?: 0.toDouble()
                        itemCashback = cashbackPercentage / PERCENTAGE * subTotalWholesalePrice
                    }
                    if (!subtotalWholesalePriceMap.containsKey(parentId)) {
                        subtotalWholesalePriceMap[parentId] = subTotalWholesalePrice
                    }
                    if (!cashbackWholesalePriceMap.containsKey(parentId)) {
                        cashbackWholesalePriceMap[parentId] = itemCashback
                    }
                    it
                } else {
                    if (!cartItemParentIdMap.containsKey(parentId)) {
                        val itemPrice = itemQty * (it.pricePlan
                                ?: 0.toDouble())
                        if (it.isCashBack) {
                            val cashbackPercentageString = it.productCashBack?.replace("%", "")
                            val cashbackPercentage = cashbackPercentageString?.toDouble()
                                    ?: 0.toDouble()
                            val itemCashback = cashbackPercentage / PERCENTAGE * itemPrice
                            totalCashback += itemCashback
                        }
                        totalPrice += itemPrice
                        it.wholesalePriceFormatted = null
                        cartItemHolderData.cartItemData?.let {
                            cartItemParentIdMap[parentId] = it
                        }
                        it
                    } else {
                        val calculatedHolderData = cartItemParentIdMap[parentId]
                        if (calculatedHolderData?.originData?.pricePlan != it.pricePlan) {
                            val itemPrice = itemQty * it.pricePlan
                            if (it.isCashBack) {
                                val cashbackPercentageString = it.productCashBack?.replace("%", "")
                                val cashbackPercentage = cashbackPercentageString?.toDouble()
                                        ?: 0.toDouble()
                                val itemCashback = cashbackPercentage / PERCENTAGE * itemPrice
                                totalCashback += itemCashback
                            }
                            totalPrice += itemPrice
                            it.wholesalePriceFormatted = null
                        }
                        it
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
            if (insuranceCartShops.shopItemsList.size > 0) {
                for (insuranceCartShopItem in insuranceCartShops.shopItemsList) {
                    for (insuranceCartDigitalProduct in insuranceCartShopItem.digitalProductList) {
                        if (insuranceCartDigitalProduct.optIn) {
                            totalPrice += insuranceCartDigitalProduct.pricePerProduct.toDouble()
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
        view?.updateCashback(totalCashback)
        val selectAllItem = view?.getAllAvailableCartDataList()?.size == allCartItemDataList.size + errorProductCount &&
                allCartItemDataList.size > 0 && insuranceChecked
        val unSelectAllItem = allCartItemDataList.size == 0
        view?.renderDetailInfoSubTotal(totalItemQty.toString(), totalPriceString, selectAllItem, unSelectAllItem, dataList.isEmpty())
    }

    override fun processAddToWishlist(productId: String, userId: String, wishListActionListener: WishListActionListener) {
        addWishListUseCase?.createObservable(productId, userId, wishListActionListener)
    }

    override fun processRemoveFromWishlist(productId: String, userId: String, wishListActionListener: WishListActionListener) {
        removeWishListUseCase?.createObservable(productId, userId, wishListActionListener)
    }

    // ANALYTICS COMMON
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

    // ANALYTICS DELETE ACTION
    override fun generateDeleteCartDataAnalytics(cartItemDataList: List<CartItemData>): Map<String, Any> {
        val enhancedECommerceCartMapData = EnhancedECommerceCartMapData().apply {
            for (cartItemData in cartItemDataList) {
                val enhancedECommerceProductCartMapData = getEnhancedECommerceProductCartMapData(cartItemData)
                addProduct(enhancedECommerceProductCartMapData.product)
            }

            setCurrencyCode(EnhancedECommerceCartMapData.VALUE_CURRENCY_IDR)
            setAction(EnhancedECommerceCartMapData.REMOVE_ACTION)
        }
        return enhancedECommerceCartMapData.cartMap
    }

    private fun getEnhancedECommerceProductCartMapData(cartItemData: CartItemData): EnhancedECommerceProductCartMapData {
        return EnhancedECommerceProductCartMapData().apply {
            setCartId(cartItemData.originData?.cartId.toString())
            setDimension45(cartItemData.originData?.cartId.toString())
            setProductName(cartItemData.originData?.productName)
            setProductID(cartItemData.originData?.productId.toString())
            setPrice(cartItemData.originData?.pricePlanInt.toString())
            setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setCategory(
                    if (TextUtils.isEmpty(cartItemData.originData?.categoryForAnalytics)) {
                        EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                    } else {
                        cartItemData.originData?.categoryForAnalytics
                    }
            )
            setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setQty(cartItemData.updatedData?.quantity ?: 0)
            setShopId(cartItemData.originData?.shopId)
            setShopType(cartItemData.originData?.shopType)
            setShopName(cartItemData.originData?.shopName)
            setCategoryId(cartItemData.originData?.categoryId)
            setAttribution(
                    if (TextUtils.isEmpty(cartItemData.originData?.trackerAttribution)) {
                        EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                    } else {
                        cartItemData.originData?.trackerAttribution
                    }
            )
            setDimension38(
                    if (TextUtils.isEmpty(cartItemData.originData?.trackerAttribution)) {
                        EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                    } else {
                        cartItemData.originData?.trackerAttribution
                    }
            )
            setListName(
                    if (TextUtils.isEmpty(cartItemData.originData?.trackerListName)) {
                        EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                    } else {
                        cartItemData.originData?.trackerListName
                    }
            )
            setDimension40(
                    if (TextUtils.isEmpty(cartItemData.originData?.trackerListName)) {
                        EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                    } else {
                        cartItemData.originData?.trackerListName
                    }
            )
        }
    }

    // ANALYTICS IMPRESSION
    override fun generateRecommendationImpressionDataAnalytics(cartRecommendationItemHolderDataList: List<CartRecommendationItemHolderData>, isEmptyCart: Boolean): Map<String, Any> {
        val enhancedECommerceCartMapData = EnhancedECommerceCartMapData().apply {
            var position = 1
            for (cartRecommendationItemHolderData in cartRecommendationItemHolderDataList) {
                val enhancedECommerceProductCartMapData = getEnhancedECommerceProductRecommendationMapData(cartRecommendationItemHolderData.recommendationItem, isEmptyCart, position)
                addImpression(enhancedECommerceProductCartMapData.product)
                position++
            }

            setCurrencyCode(EnhancedECommerceCartMapData.VALUE_CURRENCY_IDR)
        }
        return enhancedECommerceCartMapData.cartMap
    }

    private fun getEnhancedECommerceProductRecommendationMapData(recommendationItem: RecommendationItem, isEmptyCart: Boolean, position: Int): EnhancedECommerceProductCartMapData {
        val enhancedECommerceProductCartMapData = EnhancedECommerceProductCartMapData().apply {
            setProductID(recommendationItem.productId.toString())
            setProductName(recommendationItem.name)
            setPrice(recommendationItem.price.replace("[^0-9]".toRegex(), ""))
            setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setCategory(if (TextUtils.isEmpty(recommendationItem.categoryBreadcrumbs))
                EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
            else
                recommendationItem.categoryBreadcrumbs)
            setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setListName(getActionFieldListStr(isEmptyCart, recommendationItem))
            setPosition(position.toString())
            if (recommendationItem.isFreeOngkirActive) {
                setDimension83(EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR)
            } else {
                setDimension83(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            }
        }
        return enhancedECommerceProductCartMapData
    }

    override fun generateWishlistDataImpressionAnalytics(cartWishlistItemHolderDataList: List<CartWishlistItemHolderData>, isEmptyCart: Boolean): Map<String, Any> {
        val enhancedECommerceCartMapData = EnhancedECommerceCartMapData().apply {
            var position = 0
            for (cartWishlistItemHolderData in cartWishlistItemHolderDataList) {
                val enhancedECommerceProductCartMapData = getProductWishlistImpressionMapData(cartWishlistItemHolderData, isEmptyCart, position)
                addImpression(enhancedECommerceProductCartMapData.product)
                position++
            }

            setCurrencyCode(EnhancedECommerceCartMapData.VALUE_CURRENCY_IDR)
        }
        return enhancedECommerceCartMapData.cartMap
    }

    private fun getProductWishlistImpressionMapData(wishlistItemHolderData: CartWishlistItemHolderData, isEmptyCart: Boolean, position: Int): EnhancedECommerceProductCartMapData {
        return EnhancedECommerceProductCartMapData().apply {
            setProductID(wishlistItemHolderData.id)
            setProductName(wishlistItemHolderData.name)
            setPrice(wishlistItemHolderData.price.replace("[^0-9]".toRegex(), ""))
            setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setCategory(wishlistItemHolderData.category)
            setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)

            if (isEmptyCart) {
                setListName(EnhancedECommerceActionFieldData.VALUE_SECTION_NAME_WISHLIST_EMPTY_CART)
            } else {
                setListName(EnhancedECommerceActionFieldData.VALUE_SECTION_NAME_WISHLIST)
            }

            setPosition(position.toString())
        }
    }

    override fun generateRecentViewDataImpressionAnalytics(cartRecentViewItemHolderDataList: List<CartRecentViewItemHolderData>, isEmptyCart: Boolean): Map<String, Any> {
        val enhancedECommerceCartMapData = EnhancedECommerceCartMapData().apply {
            var position = 0
            for (cartRecentViewItemHolderData in cartRecentViewItemHolderDataList) {
                val enhancedECommerceProductCartMapData = getProductRecentViewImpressionMapData(cartRecentViewItemHolderData, isEmptyCart, position)
                addImpression(enhancedECommerceProductCartMapData.product)
                position++
            }

            setCurrencyCode(EnhancedECommerceCartMapData.VALUE_CURRENCY_IDR)
        }
        return enhancedECommerceCartMapData.cartMap
    }

    private fun getProductRecentViewImpressionMapData(recentViewItemHolderData: CartRecentViewItemHolderData, isEmptyCart: Boolean, position: Int): EnhancedECommerceProductCartMapData {
        return EnhancedECommerceProductCartMapData().apply {
            setProductID(recentViewItemHolderData.id)
            setProductName(recentViewItemHolderData.name)
            setPrice(recentViewItemHolderData.price.replace("[^0-9]".toRegex(), ""))
            setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setCategory(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)

            if (isEmptyCart) {
                setListName(EnhancedECommerceActionFieldData.VALUE_SECTION_NAME_RECENT_VIEW_EMPTY_CART)
            } else {
                setListName(EnhancedECommerceActionFieldData.VALUE_SECTION_NAME_RECENT_VIEW)
            }

            setPosition(position.toString())
        }
    }

    // ANALYTICS CLICK ACTION
    override fun generateRecommendationDataOnClickAnalytics(recommendationItem: RecommendationItem, isEmptyCart: Boolean, position: Int): Map<String, Any> {
        val enhancedECommerceProductCartMapData = EnhancedECommerceProductCartMapData().apply {
            setProductID(recommendationItem.productId.toString())
            setProductName(recommendationItem.name)
            setPrice(recommendationItem.price.replace("[^0-9]".toRegex(), ""))
            setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setCategory(if (TextUtils.isEmpty(recommendationItem.categoryBreadcrumbs))
                EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
            else
                recommendationItem.categoryBreadcrumbs)
            setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setPosition(position.toString())
            setAttribution(EnhancedECommerceProductCartMapData.RECOMMENDATION_ATTRIBUTION)
            if (recommendationItem.isFreeOngkirActive) {
                setDimension83(EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR)
            } else {
                setDimension83(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            }
        }
        val productsData = ArrayList<Map<String, Any>>().apply {
            add(enhancedECommerceProductCartMapData.product)
        }
        return getEnhancedECommerceOnClickData(productsData, getActionFieldListStr(isEmptyCart, recommendationItem)).getData()
    }

    private fun getEnhancedECommerceOnClickData(productsData: List<Map<String, Any>>, valueSectionName: String): EnhancedECommerceData {
        val enhancedECommerceActionFieldData = EnhancedECommerceActionFieldData().apply {
            setList(valueSectionName)
        }

        val enhancedECommerceClickData = EnhancedECommerceClickData().apply {
            setActionField(enhancedECommerceActionFieldData.getData())
            setProducts(productsData)
        }

        val enhancedECommerce = EnhancedECommerceData().apply {
            setClickData(enhancedECommerceClickData.getData())
        }
        return enhancedECommerce
    }

    override fun generateRecentViewProductClickDataLayer(cartRecentViewItemHolderData: CartRecentViewItemHolderData, position: Int): Map<String, Any> {
        val stringObjectMap = HashMap<String, Any>()
        val enhancedECommerceActionField = EnhancedECommerceActionField().apply {
            setList(EnhancedECommerceActionFieldData.VALUE_SECTION_NAME_RECENT_VIEW)
        }
        val enhancedECommerceProductCartMapData = EnhancedECommerceProductCartMapData().apply {
            setProductName(cartRecentViewItemHolderData.name)
            setProductID(cartRecentViewItemHolderData.id)
            setPrice(cartRecentViewItemHolderData.price.replace("[^0-9]".toRegex(), ""))
            setCategory(EnhancedECommerceProductData.DEFAULT_VALUE_NONE_OTHER)
            setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setPosition(position.toString())
        }
        val enhancedECommerceAdd = EnhancedECommerceAdd().apply {
            setActionField(enhancedECommerceActionField.actionFieldMap)
            addProduct(enhancedECommerceProductCartMapData.product)
        }
        stringObjectMap["currencyCode"] = "IDR"
        stringObjectMap[EnhancedECommerceAdd.KEY_ADD] = enhancedECommerceAdd.getAddMap()
        return stringObjectMap
    }

    override fun generateRecentViewProductClickEmptyCartDataLayer(cartRecentViewItemHolderData: CartRecentViewItemHolderData, position: Int): Map<String, Any> {
        val enhancedECommerceProductData = EnhancedECommerceProductData().apply {
            setProductID(cartRecentViewItemHolderData.id)
            setProductName(cartRecentViewItemHolderData.name)
            setPrice(cartRecentViewItemHolderData.price.replace("[^0-9]".toRegex(), ""))
            setBrand(EnhancedECommerceProductData.DEFAULT_VALUE_NONE_OTHER)
            setCategory(EnhancedECommerceProductData.DEFAULT_VALUE_NONE_OTHER)
            setPosition(position.toString())
            setVariant(EnhancedECommerceProductData.DEFAULT_VALUE_NONE_OTHER)
        }

        val productsData = ArrayList<Map<String, Any>>().apply {
            add(enhancedECommerceProductData.getProduct())
        }

        val enhancedECommerceEmptyCart = getEnhancedECommerceOnClickData(
                productsData, EnhancedECommerceActionFieldData.VALUE_SECTION_NAME_RECENT_VIEW_EMPTY_CART)

        return enhancedECommerceEmptyCart.getData()
    }

    override fun generateWishlistProductClickDataLayer(cartWishlistItemHolderData: CartWishlistItemHolderData, position: Int): Map<String, Any> {
        val stringObjectMap = HashMap<String, Any>()
        val enhancedECommerceActionField = EnhancedECommerceActionField().apply {
            setList(EnhancedECommerceActionFieldData.VALUE_SECTION_NAME_WISHLIST)
        }
        val enhancedECommerceProductCartMapData = EnhancedECommerceProductCartMapData().apply {
            setProductName(cartWishlistItemHolderData.name)
            setProductID(cartWishlistItemHolderData.id)
            setPrice(cartWishlistItemHolderData.price.replace("[^0-9]".toRegex(), ""))
            setCategory(cartWishlistItemHolderData.category)
            setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setPosition(position.toString())
        }
        val enhancedECommerceAdd = EnhancedECommerceAdd().apply {
            setActionField(enhancedECommerceActionField.actionFieldMap)
            addProduct(enhancedECommerceProductCartMapData.product)
        }
        stringObjectMap["currencyCode"] = "IDR"
        stringObjectMap[EnhancedECommerceAdd.KEY_ADD] = enhancedECommerceAdd.getAddMap()
        return stringObjectMap
    }

    override fun generateWishlistProductClickEmptyCartDataLayer(cartWishlistItemHolderData: CartWishlistItemHolderData, position: Int): Map<String, Any> {
        val enhancedECommerceEmptyCartProductData = EnhancedECommerceProductData().apply {
            setProductID(cartWishlistItemHolderData.id)
            setProductName(cartWishlistItemHolderData.name)
            setPrice(cartWishlistItemHolderData.price.replace("[^0-9]".toRegex(), ""))
            setBrand(EnhancedECommerceProductData.DEFAULT_VALUE_NONE_OTHER)
            setCategory(cartWishlistItemHolderData.category)
            setPosition(position.toString())
            setVariant(EnhancedECommerceProductData.DEFAULT_VALUE_NONE_OTHER)
        }

        val productsData = ArrayList<Map<String, Any>>().apply {
            add(enhancedECommerceEmptyCartProductData.getProduct())
        }

        val enhancedECommerceEmptyCart = getEnhancedECommerceOnClickData(
                productsData, EnhancedECommerceActionFieldData.VALUE_SECTION_NAME_WISHLIST_EMPTY_CART)

        return enhancedECommerceEmptyCart.getData()
    }

    // ANALYTICS STEP 0 / STEP 1
    override fun generateCheckoutDataAnalytics(cartItemDataList: List<CartItemData>, step: String): Map<String, Any> {
        val checkoutMapData = HashMap<String, Any>()
        val enhancedECommerceActionField = EnhancedECommerceActionField().apply {
            setStep(step)
            if (step == EnhancedECommerceActionField.STEP_0) {
                setOption(EnhancedECommerceActionField.STEP_0_OPTION_VIEW_CART_PAGE)
            } else if (step == EnhancedECommerceActionField.STEP_1) {
                setOption(EnhancedECommerceActionField.STEP_1_OPTION_CART_PAGE_LOADED)
            }
        }
        val enhancedECommerceCheckout = EnhancedECommerceCheckout().apply {
            for (cartItemData in cartItemDataList) {
                val enhancedECommerceProductCartMapData = getCheckoutEnhancedECommerceProductCartMapData(cartItemData)
                addProduct(enhancedECommerceProductCartMapData.product)
            }
            setCurrencyCode(EnhancedECommerceCartMapData.VALUE_CURRENCY_IDR)
            setActionField(enhancedECommerceActionField.actionFieldMap)
        }
        checkoutMapData[EnhancedECommerceCheckout.KEY_CHECKOUT] = enhancedECommerceCheckout.checkoutMap
        return checkoutMapData
    }

    private fun getCheckoutEnhancedECommerceProductCartMapData(cartItemData: CartItemData): EnhancedECommerceProductCartMapData {
        val enhancedECommerceProductCartMapData = EnhancedECommerceProductCartMapData().apply {
            setDimension80(
                    if (TextUtils.isEmpty(cartItemData.originData?.trackerAttribution)) {
                        EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                    } else {
                        cartItemData.originData?.trackerAttribution
                    }
            )
            setDimension45(cartItemData.originData?.cartId.toString())
            setDimension54(cartItemData.isFulfillment)
            setDimension53(cartItemData.originData?.priceOriginal ?: 0 > 0)
            setProductName(cartItemData.originData?.productName)
            setProductID(cartItemData.originData?.productId.toString())
            setPrice(cartItemData.originData?.pricePlanInt.toString())
            setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setCategory(
                    if (TextUtils.isEmpty(cartItemData.originData?.category)) {
                        EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                    } else {
                        cartItemData.originData?.category
                    }
            )
            setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setQty(cartItemData.updatedData?.quantity ?: 0)
            setShopId(cartItemData.originData?.shopId)
            setShopType(cartItemData.originData?.shopType)
            setShopName(cartItemData.originData?.shopName)
            setCategoryId(cartItemData.originData?.categoryId)
            setWarehouseId(cartItemData.originData?.warehouseId.toString())
            setProductWeight(cartItemData.originData?.weightPlan.toString())
            setCartId(cartItemData.originData?.cartId.toString())
            setPromoCode(cartItemData.originData?.promoCodes)
            setPromoDetails(cartItemData.originData?.promoDetails)
            setDimension83(
                    if (cartItemData.originData?.isFreeShipping == true) {
                        EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR
                    } else {
                        EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                    }
            )
        }
        return enhancedECommerceProductCartMapData
    }

    // ANALYTICS ATC
    override fun generateAddToCartEnhanceEcommerceDataLayer(cartWishlistItemHolderData: CartWishlistItemHolderData,
                                                            addToCartDataResponseModel: AddToCartDataModel, isCartEmpty: Boolean): Map<String, Any> {
        val stringObjectMap = HashMap<String, Any>()
        val enhancedECommerceActionField = EnhancedECommerceActionField().apply {
            setList(if (isCartEmpty) EnhancedECommerceActionField.LIST_WISHLIST_ON_EMPTY_CART else EnhancedECommerceActionField.LIST_WISHLIST)
        }
        val enhancedECommerceProductCartMapData = EnhancedECommerceProductCartMapData().apply {
            setProductName(cartWishlistItemHolderData.name)
            setProductID(cartWishlistItemHolderData.id)
            setPrice(cartWishlistItemHolderData.rawPrice)
            setCategory(cartWishlistItemHolderData.category)
            setQty(cartWishlistItemHolderData.minOrder)
            setShopId(cartWishlistItemHolderData.shopId)
            setShopType(cartWishlistItemHolderData.shopType)
            setShopName(cartWishlistItemHolderData.shopName)
            setPicture(cartWishlistItemHolderData.imageUrl)
            setUrl(cartWishlistItemHolderData.url)
            setDimension45(addToCartDataResponseModel.data.cartId.toString())
            setBrand("")
            setCategoryId("")
            setVariant("")
        }
        val enhancedECommerceAdd = EnhancedECommerceAdd().apply {
            setActionField(enhancedECommerceActionField.actionFieldMap)
            addProduct(enhancedECommerceProductCartMapData.product)
        }
        stringObjectMap["currencyCode"] = "IDR"
        stringObjectMap[EnhancedECommerceAdd.KEY_ADD] = enhancedECommerceAdd.getAddMap()
        return stringObjectMap
    }

    override fun generateAddToCartEnhanceEcommerceDataLayer(cartRecentViewItemHolderData: CartRecentViewItemHolderData,
                                                            addToCartDataResponseModel: AddToCartDataModel, isCartEmpty: Boolean): Map<String, Any> {
        val stringObjectMap = HashMap<String, Any>()
        val enhancedECommerceActionField = EnhancedECommerceActionField().apply {
            setList(if (isCartEmpty) EnhancedECommerceActionField.LIST_RECENT_VIEW_ON_EMPTY_CART else EnhancedECommerceActionField.LIST_RECENT_VIEW)
        }
        val enhancedECommerceProductCartMapData = EnhancedECommerceProductCartMapData().apply {
            setProductName(cartRecentViewItemHolderData.name)
            setProductID(cartRecentViewItemHolderData.id)
            setPrice(cartRecentViewItemHolderData.price)
            setQty(cartRecentViewItemHolderData.minOrder)
            setDimension52(cartRecentViewItemHolderData.shopId)
            setDimension57(cartRecentViewItemHolderData.shopName)
            setDimension59(cartRecentViewItemHolderData.shopType)
            setDimension77(addToCartDataResponseModel.data.cartId.toString())
            setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setCategoryId("")
            setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
        }
        val enhancedECommerceAdd = EnhancedECommerceAdd().apply {
            setActionField(enhancedECommerceActionField.actionFieldMap)
            addProduct(enhancedECommerceProductCartMapData.product)
        }
        stringObjectMap["currencyCode"] = "IDR"
        stringObjectMap[EnhancedECommerceAdd.KEY_ADD] = enhancedECommerceAdd.getAddMap()
        return stringObjectMap
    }

    override fun generateAddToCartEnhanceEcommerceDataLayer(cartRecommendationItemHolderData: CartRecommendationItemHolderData,
                                                            addToCartDataResponseModel: AddToCartDataModel, isCartEmpty: Boolean): Map<String, Any> {
        val stringObjectMap = HashMap<String, Any>()
        val enhancedECommerceActionField = EnhancedECommerceActionField().apply {
            setList(getActionFieldListStr(isCartEmpty, cartRecommendationItemHolderData.recommendationItem))
        }
        val enhancedECommerceProductCartMapData = EnhancedECommerceRecomProductCartMapData().apply {
            setProductName(cartRecommendationItemHolderData.recommendationItem.name)
            setProductID(cartRecommendationItemHolderData.recommendationItem.productId.toString())
            setPrice(cartRecommendationItemHolderData.recommendationItem.price.replace("[^0-9]".toRegex(), ""))
            setCategory(cartRecommendationItemHolderData.recommendationItem.categoryBreadcrumbs)
            setQty(cartRecommendationItemHolderData.recommendationItem.minOrder)
            setShopId(cartRecommendationItemHolderData.recommendationItem.shopId.toString())
            setShopType(cartRecommendationItemHolderData.recommendationItem.shopType)
            setShopName(cartRecommendationItemHolderData.recommendationItem.shopName)
            setDimension45(addToCartDataResponseModel.data.cartId.toString())
            setDimension53(cartRecommendationItemHolderData.recommendationItem.discountPercentageInt > 0)
            setDimension40(addToCartDataResponseModel.data.trackerListName)
            setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setCategoryId("")
            setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            if (cartRecommendationItemHolderData.recommendationItem.isFreeOngkirActive) {
                setDimension83(EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR)
            } else {
                setDimension83(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            }
        }

        val enhancedECommerceAdd = EnhancedECommerceAdd().apply {
            setActionField(enhancedECommerceActionField.actionFieldMap)
            addProduct(enhancedECommerceProductCartMapData.getProduct())
        }
        stringObjectMap["currencyCode"] = "IDR"
        stringObjectMap[EnhancedECommerceAdd.KEY_ADD] = enhancedECommerceAdd.getAddMap()
        return stringObjectMap
    }

    override fun setHasPerformChecklistChange(hasChangeState: Boolean) {
        hasPerformChecklistChange = hasChangeState
    }

    override fun getHasPerformChecklistChange(): Boolean {
        return hasPerformChecklistChange
    }

    override fun dataHasChanged(): Boolean {
        var hasChanges = false
        view?.getAllCartDataList()?.let {
            for ((originData, updatedData) in it) {
                if (updatedData?.quantity != originData?.originalQty || updatedData?.remark != originData?.originalRemark) {
                    hasChanges = true
                    break
                }
            }
            if (hasChanges) {
                for ((originData, updatedData) in it) {
                    originData?.originalQty = updatedData?.quantity ?: 0
                    originData?.originalRemark = updatedData?.remark
                }
            }
        }

        return hasChanges
    }

    override fun processGetRecentViewData() {
        try {
            val userId = Integer.parseInt(userSessionInterface.userId)
            val requestParams = RequestParams.create()
            requestParams.putInt(GetRecentViewUseCase.PARAM_USER_ID, userId)
            compositeSubscription.add(
                    getRecentViewUseCase?.createObservable(requestParams)
                            ?.subscribe(GetRecentViewSubscriber(view))
            )
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }

    override fun processGetWishlistData() {
        compositeSubscription.add(
                getWishlistUseCase?.createObservable(RequestParams.EMPTY)
                        ?.subscribe(GetWishlistSubscriber(view, this))
        )
    }

    override fun processGetRecommendationData(page: Int, allProductIds: List<String>) {
        view?.showItemLoading()
        val requestParam = getRecommendationUseCase?.getRecomParams(
                page, "recom_widget", "cart", allProductIds, "")
        getRecommendationUseCase?.createObservable(requestParam ?: RequestParams.EMPTY)
                ?.subscribeOn(schedulers.io)
                ?.observeOn(schedulers.main)
                ?.subscribe(GetRecommendationSubscriber(view))
    }

    override fun processAddToCart(productModel: Any) {
        view?.showProgressLoading()

        var productId = 0
        var shopId = 0
        var externalSource = ""
        if (productModel is CartWishlistItemHolderData) {
            productId = Integer.parseInt(productModel.id)
            shopId = Integer.parseInt(productModel.shopId)
            externalSource = AddToCartRequestParams.ATC_FROM_WISHLIST
        } else if (productModel is CartRecentViewItemHolderData) {
            productId = Integer.parseInt(productModel.id)
            shopId = Integer.parseInt(productModel.shopId)
            externalSource = AddToCartRequestParams.ATC_FROM_RECENT_VIEW
        } else if (productModel is CartRecommendationItemHolderData) {
            val (recommendationItem) = productModel
            productId = recommendationItem.productId
            shopId = recommendationItem.shopId
            externalSource = AddToCartRequestParams.ATC_FROM_RECOMMENDATION
        }

        val addToCartRequestParams = AddToCartRequestParams().apply {
            this.productId = productId.toLong()
            this.shopId = shopId
            this.quantity = 0
            this.notes = ""
            this.warehouseId = 0
            this.atcFromExternalSource = externalSource
        }

        val requestParams = RequestParams.create()
        requestParams.putObject(AddToCartUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, addToCartRequestParams)
        compositeSubscription.add(
                addToCartUseCase?.createObservable(requestParams)
                        ?.subscribeOn(schedulers.io)
                        ?.unsubscribeOn(schedulers.io)
                        ?.observeOn(schedulers.main)
                        ?.subscribe(AddToCartSubscriber(view, this, productModel))
        )
    }

    override fun redirectToLite(url: String) {
        view?.let {
            it.showProgressLoading()
            val adsId = it.getAdsId()
            if (adsId != null && !adsId.trim { it <= ' ' }.isEmpty()) {
                seamlessLoginUsecase.generateSeamlessUrl(url.replace(QUERY_APP_CLIENT_ID, adsId), object : SeamlessLoginSubscriber {
                    override fun onUrlGenerated(url: String) {
                        view?.let { currentView ->
                            currentView.hideProgressLoading()
                            currentView.goToLite(url)
                        }
                    }

                    override fun onError(msg: String) {
                        view?.let { currentView ->
                            currentView.hideProgressLoading()
                            currentView.showToastMessageRed(msg)
                        }
                    }
                })
            } else {
                it.hideProgressLoading()
                it.showToastMessageRed(ResponseErrorException())
            }
        }
    }

    override fun processUpdateCartCounter() {
        compositeSubscription.add(
                updateCartCounterUseCase.createObservable(RequestParams.create())
                        .subscribeOn(schedulers.io)
                        .unsubscribeOn(schedulers.io)
                        .observeOn(schedulers.main)
                        .subscribe(UpdateCartCounterSubscriber(view))
        )
    }

    override fun doUpdateCartForPromo() {
        view?.let {
            it.showProgressLoading()

            val updateCartRequestList = getUpdateCartRequest(it.getAllSelectedCartDataList()
                    ?: emptyList())
            val requestParams = RequestParams.create()
            requestParams.putObject(UpdateCartUseCase.PARAM_UPDATE_CART_REQUEST, updateCartRequestList)

            compositeSubscription.add(
                    updateCartUseCase?.createObservable(requestParams)
                            ?.subscribe(UpdateCartForPromoSubscriber(view))
            )
        }

    }

    override fun doValidateUse(promoRequestValidateUse: ValidateUsePromoRequest) {
        val requestParams = RequestParams.create()
        requestParams.putObject(ValidateUsePromoRevampUseCase.PARAM_VALIDATE_USE, promoRequestValidateUse)
        validateUsePromoRevampUseCase.createObservable(requestParams)
                .subscribeOn(schedulers.io)
                .unsubscribeOn(schedulers.io)
                .observeOn(schedulers.main)
                .subscribe(ValidateUseSubscriber(view, this))
    }

    override fun doUpdateCartAndValidateUse(promoRequestValidateUse: ValidateUsePromoRequest) {
        view?.let { cartListView ->
            val cartItemDataList = ArrayList<CartItemData>()
            cartListView.getAllSelectedCartDataList()?.let { listCartItemData ->
                for (data in listCartItemData) {
                    if (!data.isError) {
                        cartItemDataList.add(data)
                    }
                }
            }

            val updateCartRequestList = getUpdateCartRequest(cartItemDataList)
            val requestParams = RequestParams.create()
            requestParams.putObject(UpdateCartUseCase.PARAM_UPDATE_CART_REQUEST, updateCartRequestList)
            requestParams.putObject(ValidateUsePromoRevampUseCase.PARAM_VALIDATE_USE, promoRequestValidateUse)

            compositeSubscription.add(
                    updateCartAndValidateUseUseCase.createObservable(requestParams)
                            .subscribe(UpdateCartAndValidateUseSubscriber(cartListView, this))
            )
        }
    }

    override fun doClearRedPromosBeforeGoToCheckout(promoCodeList: ArrayList<String>) {
        view?.showItemLoading()
        clearCacheAutoApplyStackUseCase?.setParams(ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE, promoCodeList)
        compositeSubscription.add(
                clearCacheAutoApplyStackUseCase?.createObservable(RequestParams.create())
                        ?.subscribe(ClearRedPromosBeforeGoToCheckoutSubscriber(view))
        )
    }

    override fun getValidateUseLastResponse(): ValidateUsePromoRevampUiModel? {
        return lastValidateUseResponse
    }

    override fun setValidateUseLastResponse(response: ValidateUsePromoRevampUiModel?) {
        lastValidateUseResponse = response
    }

    override fun isLastApplyValid(): Boolean {
        return isLastApplyResponseStillValid
    }

    override fun setLastApplyNotValid() {
        isLastApplyResponseStillValid = false
    }

    override fun setLastApplyValid() {
        isLastApplyResponseStillValid = true
    }
}