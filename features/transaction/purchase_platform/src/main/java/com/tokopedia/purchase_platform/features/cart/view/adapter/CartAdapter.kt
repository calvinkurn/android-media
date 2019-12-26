package com.tokopedia.purchase_platform.features.cart.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.view.common.TickerAnnouncementActionListener
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.TickerAnnouncementHolderData
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.promocheckout.common.view.model.PromoStackingData
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartDigitalProduct
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartShops
import com.tokopedia.purchase_platform.common.feature.promo_global.PromoActionListener
import com.tokopedia.purchase_platform.common.feature.promo_global.PromoGlobalViewHolder
import com.tokopedia.purchase_platform.common.feature.seller_cashback.ShipmentSellerCashbackModel
import com.tokopedia.purchase_platform.common.feature.seller_cashback.ShipmentSellerCashbackViewHolder
import com.tokopedia.purchase_platform.common.insurance.utils.PAGE_TYPE_CART
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartItemData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.ShopGroupAvailableData
import com.tokopedia.purchase_platform.features.cart.view.ActionListener
import com.tokopedia.purchase_platform.features.cart.view.InsuranceItemActionListener
import com.tokopedia.purchase_platform.features.cart.view.viewholder.*
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.*
import rx.subscriptions.CompositeSubscription
import java.util.*
import javax.inject.Inject
import kotlin.math.min

/**
 * @author anggaprasetiyo on 18/01/18.
 */

class CartAdapter @Inject constructor(private val actionListener: ActionListener?,
                                      private val promoActionListener: PromoActionListener?,
                                      private val cartItemActionListener: CartItemAdapter.ActionListener?,
                                      private val insuranceItemActionlistener: InsuranceItemActionListener?,
                                      private val tickerAnnouncementActionListener: TickerAnnouncementActionListener?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val cartDataList = ArrayList<Any>()
    private val compositeSubscription = CompositeSubscription()

    val insuranceCartShops = ArrayList<InsuranceCartShops>()
    private val insuranceRecommendationList = ArrayList<InsuranceCartShops>()
    private val insuranceCartList = ArrayList<InsuranceCartShops>()
    private var shipmentSellerCashbackModel: ShipmentSellerCashbackModel? = null
    private var cartEmptyHolderData: CartEmptyHolderData? = null
    private var cartLoadingHolderData: CartLoadingHolderData? = null
    private var cartWishlistAdapter: CartWishlistAdapter? = null
    private var cartRecentViewAdapter: CartRecentViewAdapter? = null
    private var sendInsuranceImpressionEvent = false

    var firstCartSectionHeaderPosition: Int = -1

    var isInsuranceSelected: Boolean = false
        private set
    var selectedInsuranceProductId = ""
        private set
    var selectedInsuranceProductTitle = ""
        private set

    val allShopGroupDataList: List<CartShopHolderData>
        get() {
            val cartShopHolderDataFinalList = ArrayList<CartShopHolderData>()
            for (i in cartDataList.indices) {
                val obj = cartDataList[i]
                if (obj is CartShopHolderData) {
                    cartShopHolderDataFinalList.add(obj)
                }
            }
            return cartShopHolderDataFinalList
        }

    val selectedCartItemData: List<CartItemData>
        get() {
            val cartItemDataList = ArrayList<CartItemData>()
            for (data in cartDataList) {
                if (data is CartShopHolderData) {
                    if ((data.isPartialSelected || data.isAllSelected)) {
                        data.shopGroupAvailableData.cartItemDataList?.let {
                            for (cartItemHolderData in it) {
                                if (cartItemHolderData.isSelected && cartItemHolderData.cartItemData?.isError == false) {
                                    cartItemHolderData.cartItemData?.let { cartItemData ->
                                        cartItemDataList.add(cartItemData)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return cartItemDataList
        }

    val selectedCartShopHolderData: List<CartShopHolderData>
        get() {
            val cartShopHolderDataList = ArrayList<CartShopHolderData>()
            for (data in cartDataList) {
                if (data is CartShopHolderData) {
                    if ((data.isPartialSelected || data.isAllSelected) && data.shopGroupAvailableData.cartItemDataList != null) {
                        cartShopHolderDataList.add(data)
                    }
                }
            }

            return cartShopHolderDataList
        }

    val allCartItemData: List<CartItemData>
        get() {
            val cartItemDataList = ArrayList<CartItemData>()
            loop@ for (data in cartDataList) {
                when (data) {
                    is CartShopHolderData -> {
                        val cartItemHolderDataList = data.shopGroupAvailableData.cartItemDataList
                        cartItemHolderDataList?.let {
                            for (cartItemHolderData in it) {
                                cartItemHolderData.cartItemData?.let { cartItemData ->
                                    cartItemDataList.add(cartItemData)
                                }
                            }
                        }
                    }
                    is DisabledCartItemHolderData -> {
                        data.data?.let {
                            cartItemDataList.add(it)
                        }
                    }
                    is CartWishlistHolderData, is CartRecentViewHolderData, is CartRecommendationItemHolderData -> break@loop
                }
            }

            return cartItemDataList
        }

    val allAvailableCartItemData: List<CartItemData>
        get() {
            val cartItemDataList = ArrayList<CartItemData>()
            loop@ for (data in cartDataList) {
                when (data) {
                    is CartShopHolderData -> {
                        data.shopGroupAvailableData.cartItemDataList?.let {
                            for (cartItemHolderData in it) {
                                cartItemHolderData.cartItemData?.let {
                                    cartItemDataList.add(it)
                                }
                            }
                        }
                    }
                    is CartWishlistHolderData, is CartRecentViewHolderData, is CartRecommendationItemHolderData -> break@loop
                }
            }

            return cartItemDataList
        }

    val allDisabledCartItemData: List<CartItemData>
        get() {
            val cartItemDataList = ArrayList<CartItemData>()
            loop@ for (data in cartDataList) {
                when (data) {
                    is DisabledCartItemHolderData -> {
                        data.data?.let {
                            cartItemDataList.add(it)
                        }
                    }
                    is CartWishlistHolderData, is CartRecentViewHolderData, is CartRecommendationItemHolderData -> break@loop
                }
            }

            return cartItemDataList
        }

    val allCartItemProductId: List<String>
        get() {
            val productIdList = ArrayList<String>()
            for (data in cartDataList) {
                when (data) {
                    is CartShopHolderData -> {
                        data.shopGroupAvailableData.cartItemDataList?.let {
                            for (cartItemHolderData in it) {
                                productIdList.add(cartItemHolderData.cartItemData?.originData?.productId
                                        ?: "")
                            }
                        }
                    }
                    is DisabledCartItemHolderData -> productIdList.add(data.productId)
                }
            }

            return productIdList
        }

    val allCartItemHolderData: List<CartItemHolderData>
        get() {
            val cartItemDataList = ArrayList<CartItemHolderData>()
            for (data in cartDataList) {
                if (data is CartShopHolderData) {
                    data.shopGroupAvailableData.cartItemDataList?.let {
                        cartItemDataList.addAll(it)
                    }
                }
            }

            return cartItemDataList
        }

    val selectedRecommendedInsuranceList: List<InsuranceCartShops>
        get() {
            val insuranceCartShopsList = ArrayList<InsuranceCartShops>()
            for (insuranceCartShops in insuranceRecommendationList) {
                if (insuranceCartShops.shopItemsList.size > 0 &&
                        insuranceCartShops.shopItemsList[0].digitalProductList.size > 0 &&
                        insuranceCartShops.shopItemsList[0].digitalProductList[0].optIn) {
                    insuranceCartShopsList.add(insuranceCartShops)
                }
            }

            return insuranceCartShopsList
        }

    val isInsuranceCartProductUnSelected: ArrayList<InsuranceCartDigitalProduct>
        get() {
            isInsuranceSelected = true
            val insuranceCartDigitalProductArrayList = ArrayList<InsuranceCartDigitalProduct>()
            for (insuranceCartShops in insuranceCartList) {
                if (insuranceCartShops.shopItemsList.isNotEmpty()) {
                    for (insuranceCartShopItem in insuranceCartShops.shopItemsList) {
                        if (insuranceCartShopItem.digitalProductList.isNotEmpty()) {
                            for (insuranceCartDigitalProduct in insuranceCartShopItem.digitalProductList) {
                                selectedInsuranceProductTitle = insuranceCartDigitalProduct.productInfo.title
                                selectedInsuranceProductId = insuranceCartDigitalProduct.productId
                                if (!insuranceCartDigitalProduct.optIn) {
                                    isInsuranceSelected = false
                                    insuranceCartDigitalProductArrayList.add(insuranceCartDigitalProduct)
                                }
                            }
                        } else {
                            isInsuranceSelected = false
                        }
                    }
                } else {
                    isInsuranceSelected = false
                }
            }

            return insuranceCartDigitalProductArrayList
        }

    val promoStackingGlobalData: PromoStackingData?
        get() {
            for (i in cartDataList.indices) {
                if (cartDataList[i] is PromoStackingData) {
                    return cartDataList[i] as PromoStackingData
                }
            }

            return null
        }

    val allCartShopHolderData: List<CartShopHolderData>
        get() {
            val cartShopHolderDataList = ArrayList<CartShopHolderData>()
            for (i in cartDataList.indices) {
                getCartShopHolderDataByIndex(i)?.let {
                    cartShopHolderDataList.add(it)
                }
            }

            return cartShopHolderDataList
        }

    val disabledItemHeaderPosition: Int
        get() {
            for (i in cartDataList.indices) {
                if (cartDataList[i] is DisabledItemHeaderHolderData) {
                    return i
                }
            }

            return 0
        }

    override fun getItemViewType(position: Int): Int {
        val data = cartDataList[position]
        return when (data) {
            is CartShopHolderData -> CartShopViewHolder.TYPE_VIEW_ITEM_SHOP
            is PromoStackingData -> PromoGlobalViewHolder.TYPE_VIEW_PROMO
            is CartItemTickerErrorHolderData -> CartTickerErrorViewHolder.TYPE_VIEW_TICKER_CART_ERROR
            is ShipmentSellerCashbackModel -> ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK
            is CartEmptyHolderData -> CartEmptyViewHolder.LAYOUT
            is CartRecentViewHolderData -> CartRecentViewViewHolder.LAYOUT
            is CartWishlistHolderData -> CartWishlistViewHolder.LAYOUT
            is CartSectionHeaderHolderData -> CartSectionHeaderViewHolder.LAYOUT
            is CartRecommendationItemHolderData -> CartRecommendationViewHolder.LAYOUT
            is CartLoadingHolderData -> CartLoadingViewHolder.LAYOUT
            is TickerAnnouncementHolderData -> TickerAnnouncementViewHolder.LAYOUT
            is Boolean -> CartSelectAllViewHolder.LAYOUT
            is InsuranceCartShops -> InsuranceCartShopViewHolder.TYPE_VIEW_INSURANCE_CART_SHOP
            is DisabledCartItemHolderData -> DisabledCartItemViewHolder.LAYOUT
            is DisabledItemHeaderHolderData -> DisabledItemHeaderViewHolder.LAYOUT
            is DisabledShopHolderData -> DisabledShopViewHolder.LAYOUT
            else -> super.getItemViewType(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            CartShopViewHolder.TYPE_VIEW_ITEM_SHOP -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(CartShopViewHolder.TYPE_VIEW_ITEM_SHOP, parent, false)
                return CartShopViewHolder(view, actionListener, cartItemActionListener, compositeSubscription)
            }
            PromoGlobalViewHolder.TYPE_VIEW_PROMO -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(PromoGlobalViewHolder.TYPE_VIEW_PROMO, parent, false)
                return PromoGlobalViewHolder(view, promoActionListener)
            }
            CartTickerErrorViewHolder.TYPE_VIEW_TICKER_CART_ERROR -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(CartTickerErrorViewHolder.TYPE_VIEW_TICKER_CART_ERROR, parent, false)
                return CartTickerErrorViewHolder(view, actionListener)
            }
            ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK, parent, false)
                return ShipmentSellerCashbackViewHolder(view)
            }
            CartEmptyViewHolder.LAYOUT -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(CartEmptyViewHolder.LAYOUT, parent, false)
                return CartEmptyViewHolder(view, actionListener)
            }
            CartRecentViewViewHolder.LAYOUT -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(CartRecentViewViewHolder.LAYOUT, parent, false)
                return CartRecentViewViewHolder(view, actionListener)
            }
            CartWishlistViewHolder.LAYOUT -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(CartWishlistViewHolder.LAYOUT, parent, false)
                return CartWishlistViewHolder(view, actionListener)
            }
            CartSectionHeaderViewHolder.LAYOUT -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(CartSectionHeaderViewHolder.LAYOUT, parent, false)
                return CartSectionHeaderViewHolder(view, actionListener)
            }
            CartRecommendationViewHolder.LAYOUT -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(CartRecommendationViewHolder.LAYOUT, parent, false)
                return CartRecommendationViewHolder(view, actionListener)
            }
            CartLoadingViewHolder.LAYOUT -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(CartLoadingViewHolder.LAYOUT, parent, false)
                return CartLoadingViewHolder(view)
            }
            CartSelectAllViewHolder.LAYOUT -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(CartSelectAllViewHolder.LAYOUT, parent, false)
                return CartSelectAllViewHolder(view, actionListener)
            }
            TickerAnnouncementViewHolder.LAYOUT -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(TickerAnnouncementViewHolder.LAYOUT, parent, false)
                return TickerAnnouncementViewHolder(view, tickerAnnouncementActionListener)
            }
            InsuranceCartShopViewHolder.TYPE_VIEW_INSURANCE_CART_SHOP -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(InsuranceCartShopViewHolder.TYPE_VIEW_INSURANCE_CART_SHOP, parent, false)
                return InsuranceCartShopViewHolder(view, insuranceItemActionlistener)
            }
            DisabledCartItemViewHolder.LAYOUT -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(DisabledCartItemViewHolder.LAYOUT, parent, false)
                return DisabledCartItemViewHolder(view, actionListener)
            }
            DisabledItemHeaderViewHolder.LAYOUT -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(DisabledItemHeaderViewHolder.LAYOUT, parent, false)
                return DisabledItemHeaderViewHolder(view, actionListener)
            }
            DisabledShopViewHolder.LAYOUT -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(DisabledShopViewHolder.LAYOUT, parent, false)
                return DisabledShopViewHolder(view)
            }
            else -> throw RuntimeException("No view holder type found")
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        when {
            viewType == CartShopViewHolder.TYPE_VIEW_ITEM_SHOP -> {
                val data = cartDataList[position] as CartShopHolderData
                (holder as CartShopViewHolder).bindData(data, position)
            }
            viewType == PromoGlobalViewHolder.TYPE_VIEW_PROMO -> {
                val data = cartDataList[position] as PromoStackingData
                (holder as PromoGlobalViewHolder).bindData(data, position)
            }
            viewType == CartTickerErrorViewHolder.TYPE_VIEW_TICKER_CART_ERROR -> {
                val data = cartDataList[position] as CartItemTickerErrorHolderData
                (holder as CartTickerErrorViewHolder).bindData(data, position)
            }
            viewType == ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK -> {
                val data = cartDataList[position] as ShipmentSellerCashbackModel
                (holder as ShipmentSellerCashbackViewHolder).bindViewHolder(data)
            }
            viewType == CartEmptyViewHolder.LAYOUT -> {
                val data = cartDataList[position] as CartEmptyHolderData
                (holder as CartEmptyViewHolder).bind(data)
            }
            viewType == CartRecentViewViewHolder.LAYOUT -> {
                val data = cartDataList[position] as CartRecentViewHolderData
                (holder as CartRecentViewViewHolder).bind(data)
                cartRecentViewAdapter = holder.recentViewAdapter
            }
            viewType == CartWishlistViewHolder.LAYOUT -> {
                val data = cartDataList[position] as CartWishlistHolderData
                (holder as CartWishlistViewHolder).bind(data)
                cartWishlistAdapter = holder.wishlistAdapter
            }
            viewType == CartSectionHeaderViewHolder.LAYOUT -> {
                val data = cartDataList[position] as CartSectionHeaderHolderData
                (holder as CartSectionHeaderViewHolder).bind(data)
            }
            viewType == CartRecommendationViewHolder.LAYOUT -> {
                val data = cartDataList[position] as CartRecommendationItemHolderData
                (holder as CartRecommendationViewHolder).bind(data)
            }
            viewType == CartLoadingViewHolder.LAYOUT -> {
                val data = cartDataList[position] as CartLoadingHolderData
                (holder as CartLoadingViewHolder).bind(data)
            }
            viewType == TickerAnnouncementViewHolder.LAYOUT -> {
                val cartTickerData = cartDataList[position] as TickerAnnouncementHolderData
                (holder as TickerAnnouncementViewHolder).bind(cartTickerData)
            }
            viewType == CartSelectAllViewHolder.LAYOUT -> {
                val isAllSelected = cartDataList[position] as Boolean
                (holder as CartSelectAllViewHolder).bind(isAllSelected)
            }
            getItemViewType(position) == InsuranceCartShopViewHolder.TYPE_VIEW_INSURANCE_CART_SHOP -> {
                val insuranceCartShops = cartDataList[position] as InsuranceCartShops
                (holder as InsuranceCartShopViewHolder).bindData(insuranceCartShops, position, PAGE_TYPE_CART)
            }
            viewType == DisabledItemHeaderViewHolder.LAYOUT -> {
                val data = cartDataList[position] as DisabledItemHeaderHolderData
                (holder as DisabledItemHeaderViewHolder).bind(data)
            }
            viewType == DisabledShopViewHolder.LAYOUT -> {
                val data = cartDataList[position] as DisabledShopHolderData
                (holder as DisabledShopViewHolder).bind(data)
            }
            viewType == DisabledCartItemViewHolder.LAYOUT -> {
                val data = cartDataList[position] as DisabledCartItemHolderData
                (holder as DisabledCartItemViewHolder).bind(data)
            }
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder is InsuranceCartShopViewHolder && !sendInsuranceImpressionEvent) {
            sendInsuranceImpressionEvent = true
            insuranceItemActionlistener?.sendEventInsuranceImpression(holder.getProductTitle())
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder.itemViewType == CartRecommendationViewHolder.LAYOUT) {
            (holder as CartRecommendationViewHolder).clearImage()
        }
    }

    override fun getItemCount(): Int {
        return cartDataList.size
    }

    fun unsubscribeSubscription() {
        compositeSubscription.unsubscribe()
    }

    fun addAvailableDataList(shopGroupAvailableDataList: List<ShopGroupAvailableData>) {
        for (shopGroupAvailableData in shopGroupAvailableDataList) {
            if (shopGroupAvailableData.cartItemDataList?.size ?: 0 > 0) {
                val cartShopHolderData = CartShopHolderData()
                cartShopHolderData.shopGroupAvailableData = shopGroupAvailableData
                if (shopGroupAvailableData.isError) {
                    cartShopHolderData.isAllSelected = false
                } else {
                    if (shopGroupAvailableData.isChecked) {
                        cartShopHolderData.isAllSelected = true
                    } else if (shopGroupAvailableData.cartItemDataList?.size ?: 0 > 1) {
                        shopGroupAvailableData.cartItemDataList?.let {
                            for (cartItemHolderData in it) {
                                if (cartItemHolderData.isSelected) {
                                    cartShopHolderData.isPartialSelected = true
                                    break
                                }
                            }
                        }
                    }
                }
                cartShopHolderData.shopGroupAvailableData = shopGroupAvailableData
                cartDataList.add(cartShopHolderData)
            }
        }
    }

    fun addNotAvailableHeader(disabledItemHeaderHolderData: DisabledItemHeaderHolderData) {
        cartDataList.add(disabledItemHeaderHolderData)
    }

    fun addNotAvailableShop(disabledShopHolderData: DisabledShopHolderData) {
        cartDataList.add(disabledShopHolderData)
    }

    fun addNotAvailableProduct(disabledCartItemHolderData: DisabledCartItemHolderData) {
        cartDataList.add(disabledCartItemHolderData)
    }

    fun setAllShopSelected(selected: Boolean) {
        for (i in cartDataList.indices) {
            if (cartDataList[i] is CartShopHolderData) {
                val cartShopHolderData = cartDataList[i] as CartShopHolderData
                if (cartShopHolderData.shopGroupAvailableData.cartItemDataList?.size ?: 0 == 1) {
                    cartShopHolderData.shopGroupAvailableData.cartItemDataList?.let {
                        for (cartItemHolderData in it) {
                            if (cartItemHolderData.cartItemData?.isError == true && cartItemHolderData.cartItemData?.isSingleChild == true) {
                                setShopSelected(i, false)
                            } else {
                                setShopSelected(i, selected)
                            }
                        }
                    }
                } else {
                    setShopSelected(i, selected)
                }
            }
        }
    }

    fun setShopSelected(position: Int, selected: Boolean) {
        val any = cartDataList[position]
        if (any is CartShopHolderData) {
            any.isAllSelected = selected
            any.shopGroupAvailableData.cartItemDataList?.let {
                for (cartItemHolderData in it) {
                    cartItemHolderData.isSelected = selected
                }
            }
        }
    }

    fun setItemSelected(position: Int, parentPosition: Int, selected: Boolean): Boolean {
        var needToUpdateParent = false
        val any = cartDataList[parentPosition]
        if (any is CartShopHolderData) {
            val shopAlreadySelected = any.isAllSelected || any.isPartialSelected
            var selectedCount = 0
            any.shopGroupAvailableData.cartItemDataList?.let {
                for (i in it.indices) {
                    val cartItemHolderData = it[i]
                    if (i == position) {
                        cartItemHolderData.isSelected = selected
                    }

                    if (cartItemHolderData.isSelected) {
                        selectedCount++
                    }
                }

                if (selectedCount == 0) {
                    any.isAllSelected = false
                    any.isPartialSelected = false
                    needToUpdateParent = shopAlreadySelected
                } else if (selectedCount > 0 && selectedCount < it.size) {
                    any.isAllSelected = false
                    any.isPartialSelected = true
                    needToUpdateParent = !shopAlreadySelected
                } else {
                    any.isAllSelected = true
                    any.isPartialSelected = false
                    needToUpdateParent = !shopAlreadySelected
                }

                // Check does has cash back item
                var hasCashbackItem = false
                for ((cartItemData) in it) {
                    if (cartItemData?.originData?.isCashBack == true) {
                        hasCashbackItem = true
                        break
                    }
                }

                // Check is it the last cash back item to uncheck & still contain non cash back item
                var isTheLastCashbackItem = true
                var hasUncheckedItem = false
                if (!selected && hasCashbackItem) {
                    // Check does it still contain unchecked item
                    for (cartItemHolderData in it) {
                        if (cartItemHolderData.isSelected && cartItemHolderData.cartItemData?.originData?.isCashBack == false) {
                            hasUncheckedItem = true
                            break
                        }
                    }
                    // Check is it the last cash back item to be unchecked
                    for (j in it.indices) {
                        val cartItemHolderData = it[j]
                        if (j != position && cartItemHolderData.isSelected && cartItemHolderData.cartItemData?.originData?.isCashBack == true) {
                            isTheLastCashbackItem = false
                            break
                        }
                    }
                }
                if (hasUncheckedItem && isTheLastCashbackItem) {
                    needToUpdateParent = true
                }
            }
        }

        return needToUpdateParent
    }

    fun increaseQuantity(position: Int, parentPosition: Int) {
        if (getItemViewType(parentPosition) == CartShopViewHolder.TYPE_VIEW_ITEM_SHOP) {
            (cartDataList[parentPosition] as CartShopHolderData).shopGroupAvailableData
                    .cartItemDataList?.get(position)?.cartItemData?.updatedData?.increaseQuantity()
        }
        checkForShipmentForm()
    }

    fun decreaseQuantity(position: Int, parentPosition: Int) {
        if (getItemViewType(parentPosition) == CartShopViewHolder.TYPE_VIEW_ITEM_SHOP) {
            (cartDataList[parentPosition] as CartShopHolderData).shopGroupAvailableData
                    .cartItemDataList?.get(position)?.cartItemData?.updatedData?.decreaseQuantity()
        }
        checkForShipmentForm()
    }

    fun resetQuantity(position: Int, parentPosition: Int) {
        if (getItemViewType(parentPosition) == CartShopViewHolder.TYPE_VIEW_ITEM_SHOP) {
            (cartDataList[parentPosition] as CartShopHolderData).shopGroupAvailableData
                    .cartItemDataList?.get(position)?.cartItemData?.updatedData?.resetQuantity()
        }
        checkForShipmentForm()
    }

    fun removeInsuranceDataItem(productIdList: List<Long>) {
        try {
            for (productId in productIdList) {
                var position = 0
                for (item in cartDataList) {
                    position++
                    if (item is InsuranceCartShops) {
                        for ((productId1) in item.shopItemsList) {
                            if (productId1 == productId) {
                                cartDataList.remove(item)
                                notifyItemRemoved(position)
                                break
                            }
                        }
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun addInsuranceDataList(insuranceCartShops: InsuranceCartShops, isRecommendation: Boolean) {
        this.insuranceCartShops.clear()
        this.insuranceCartShops.add(insuranceCartShops)

        if (isRecommendation) {
            insuranceRecommendationList.clear()
            insuranceRecommendationList.add(insuranceCartShops)
        } else {
            insuranceCartList.clear()
            insuranceCartList.add(insuranceCartShops)
        }

        var insuranceIndex = 0

        for (item in cartDataList) {
            if (item is CartEmptyHolderData || item is CartShopHolderData) {
                insuranceIndex = cartDataList.indexOf(item)
            }
        }

        cartDataList.add(++insuranceIndex, insuranceCartShops)

        notifyDataSetChanged()
    }

    fun resetData() {
        cartDataList.clear()
        firstCartSectionHeaderPosition = -1
        notifyDataSetChanged()
        checkForShipmentForm()
    }

    fun updateItemPromoStackVoucher(promoStackingData: PromoStackingData) {
        for (i in cartDataList.indices) {
            val any = cartDataList[i]
            if (any is PromoStackingData) {
                cartDataList[i] = promoStackingData
                notifyItemChanged(i)
            }
        }
    }

    fun addPromoStackingVoucherData(promoStackingData: PromoStackingData) {
        cartDataList.add(promoStackingData)
        checkForShipmentForm()
    }

    fun removePromoStackingVoucherData() {
        for (i in cartDataList.indices) {
            if (cartDataList[i] is PromoStackingData) {
                cartDataList.removeAt(i)
                notifyItemRemoved(i)
                break
            }
        }
    }

    fun addCartTickerError(cartItemTickerErrorHolderData: CartItemTickerErrorHolderData) {
        cartDataList.add(cartItemTickerErrorHolderData)
        checkForShipmentForm()
    }

    fun addCartEmptyData() {
        if (cartEmptyHolderData == null) {
            cartEmptyHolderData = CartEmptyHolderData()
        }
        cartEmptyHolderData?.let {
            cartDataList.add(it)
        }
    }

    fun addCartRecentViewData(cartSectionHeaderHolderData: CartSectionHeaderHolderData,
                              cartRecentViewHolderData: CartRecentViewHolderData) {
        var recentViewIndex = 0
        for (item in cartDataList) {
            if (item is CartEmptyHolderData ||
                    item is CartShopHolderData ||
                    item is ShipmentSellerCashbackModel ||
                    item is InsuranceCartShops ||
                    item is DisabledCartItemHolderData) {
                recentViewIndex = cartDataList.indexOf(item) // 3
            }
        }
        cartDataList.add(++recentViewIndex, cartSectionHeaderHolderData)
        firstCartSectionHeaderPosition = when (firstCartSectionHeaderPosition) {
            -1 -> recentViewIndex
            else -> min(firstCartSectionHeaderPosition, recentViewIndex)
        }
        cartDataList.add(++recentViewIndex, cartRecentViewHolderData)
        notifyDataSetChanged()
    }

    fun addCartWishlistData(cartSectionHeaderHolderData: CartSectionHeaderHolderData,
                            cartWishlistHolderData: CartWishlistHolderData) {
        var wishlistIndex = 0
        for (item in cartDataList) {
            if (item is CartEmptyHolderData ||
                    item is CartShopHolderData ||
                    item is ShipmentSellerCashbackModel ||
                    item is CartRecentViewHolderData ||
                    item is InsuranceCartShops ||
                    item is DisabledCartItemHolderData) {

                wishlistIndex = cartDataList.indexOf(item)
            }
        }
        cartDataList.add(++wishlistIndex, cartSectionHeaderHolderData)
        firstCartSectionHeaderPosition = when (firstCartSectionHeaderPosition) {
            -1 -> wishlistIndex
            else -> min(firstCartSectionHeaderPosition, wishlistIndex)
        }
        cartDataList.add(++wishlistIndex, cartWishlistHolderData)
        notifyDataSetChanged()
    }

    fun addCartRecommendationData(cartSectionHeaderHolderData: CartSectionHeaderHolderData?,
                                  cartRecommendationItemHolderDataList: List<CartRecommendationItemHolderData>) {
        var recommendationIndex = 0
        for (item in cartDataList) {
            if (item is CartEmptyHolderData ||
                    item is CartShopHolderData ||
                    item is ShipmentSellerCashbackModel ||
                    item is CartRecentViewHolderData ||
                    item is CartWishlistHolderData ||
                    item is CartRecommendationItemHolderData ||
                    item is InsuranceCartShops ||
                    item is DisabledCartItemHolderData) {
                recommendationIndex = cartDataList.indexOf(item)
            }
        }

        cartSectionHeaderHolderData?.let {
            cartDataList.add(++recommendationIndex, cartSectionHeaderHolderData)
            firstCartSectionHeaderPosition = when (firstCartSectionHeaderPosition) {
                -1 -> recommendationIndex
                else -> min(firstCartSectionHeaderPosition, recommendationIndex)
            }
        }

        cartDataList.addAll(++recommendationIndex, cartRecommendationItemHolderDataList)
        notifyDataSetChanged()
    }

    fun removeCartEmptyData() {
        cartEmptyHolderData?.let {
            cartDataList.remove(it)
            notifyDataSetChanged()

            cartEmptyHolderData = null
        }
    }

    fun checkForShipmentForm() {
        var canProcess = true
        var checkedCount = 0
        for (any in cartDataList) {
            if (any is CartShopHolderData) {
                if (!any.shopGroupAvailableData.isError) {
                    if (any.isAllSelected) {
                        checkedCount += any.shopGroupAvailableData.cartItemDataList?.size ?: 0
                    } else if (any.isPartialSelected) {
                        any.shopGroupAvailableData.cartItemDataList?.let {
                            for (cartItemHolderData in it) {
                                if (cartItemHolderData.isSelected) {
                                    checkedCount++
                                    if (cartItemHolderData.getErrorFormItemValidationTypeValue() != CartItemHolderData.ERROR_EMPTY || cartItemHolderData.cartItemData?.isError == true) {
                                        canProcess = false
                                        break
                                    }
                                }
                            }
                        }
                        if (!canProcess) {
                            break
                        }
                    }
                }
            }
        }

        if (canProcess && checkedCount > 0) {
            actionListener?.onCartDataEnableToCheckout()
        } else {
            actionListener?.onCartDataDisableToCheckout()
        }
    }

    fun updateShipmentSellerCashback(cashback: Double) {
        if (cashback > 0) {
            if (shipmentSellerCashbackModel == null || cartDataList.indexOf(shipmentSellerCashbackModel!!) == -1) {
                var index = 0
                for (item in cartDataList) {
                    if (item is CartShopHolderData) {
                        index = cartDataList.indexOf(item)
                    }
                }
                shipmentSellerCashbackModel = ShipmentSellerCashbackModel()
                shipmentSellerCashbackModel?.let {
                    it.isVisible = true
                    it.sellerCashback = CurrencyFormatUtil.convertPriceValueToIdrFormat(cashback.toLong(), false)
                    cartDataList.add(++index, it)
                    notifyItemInserted(index)
                }
            } else {
                shipmentSellerCashbackModel?.let {
                    it.isVisible = true
                    it.sellerCashback = CurrencyFormatUtil.convertPriceValueToIdrFormat(cashback.toLong(), false)
                    val index = cartDataList.indexOf(it)
                    notifyItemChanged(index)
                }
            }
        } else {
            shipmentSellerCashbackModel?.let {
                val index = cartDataList.indexOf(it)
                cartDataList.remove(it)
                notifyItemRemoved(index)
                shipmentSellerCashbackModel = null
            }
        }

        shipmentSellerCashbackModel?.let {
            val index = cartDataList.indexOf(it)
            if (index != -1) {
                notifyItemChanged(index)
            }
        }
    }

    fun notifyByProductId(productId: String, isWishlisted: Boolean) {
        for (i in cartDataList.indices) {
            val obj = cartDataList[i]
            if (obj is CartShopHolderData) {
                val cartShopHolderData = cartDataList[i] as CartShopHolderData
                cartShopHolderData.shopGroupAvailableData.cartItemDataList?.let {
                    for (cartItemHolderData in it) {
                        if (cartItemHolderData.cartItemData?.originData?.productId == productId) {
                            cartItemHolderData.cartItemData?.originData?.isWishlisted = isWishlisted
                            notifyItemChanged(i)
                            break
                        }
                    }
                }
            } else if (obj is DisabledCartItemHolderData) {
                if (obj.productId == productId) {
                    obj.isWishlisted = isWishlisted
                    notifyItemChanged(i)
                    break
                }
            }
        }
    }

    fun getCartShopHolderDataByIndex(index: Int): CartShopHolderData? {
        return if (cartDataList[index] is CartShopHolderData) {
            cartDataList[index] as CartShopHolderData
        } else null
    }

    fun notifyWishlist(productId: String, isWishlist: Boolean) {
        for (any in cartDataList) {
            if (any is CartWishlistHolderData) {
                val wishlist = any.wishList
                for (data in wishlist) {
                    if (data.id == productId) {
                        data.isWishlist = isWishlist
                        cartWishlistAdapter?.let {
                            cartWishlistAdapter?.notifyItemChanged(wishlist.indexOf(data))
                        }
                        break
                    }
                }
                break
            }
        }
    }

    fun notifyRecentView(productId: String, isWishlist: Boolean) {
        for (any in cartDataList) {
            if (any is CartRecentViewHolderData) {
                val recentViews = any.recentViewList
                for (data in recentViews) {
                    if (data.id == productId) {
                        data.isWishlist = isWishlist
                        cartRecentViewAdapter?.let {
                            cartRecentViewAdapter?.notifyItemChanged(recentViews.indexOf(data))
                        }
                        break
                    }
                }
                break
            }
        }
    }

    fun notifyRecommendation(productId: String, isWishlist: Boolean) {
        for (i in cartDataList.indices.reversed()) {
            val any = cartDataList[i]
            if (any is CartRecommendationItemHolderData) {
                if (any.recommendationItem.productId.toString() == productId) {
                    any.recommendationItem.isWishlist = isWishlist
                    notifyItemChanged(i)
                    break
                }
            }
        }
    }

    fun addCartLoadingData() {
        if (cartLoadingHolderData == null) {
            cartLoadingHolderData = CartLoadingHolderData()
        }
        cartLoadingHolderData?.let {
            cartDataList.add(it)
            notifyItemInserted(cartDataList.indexOf(it))
        }
    }

    fun removeCartLoadingData() {
        cartLoadingHolderData?.let {
            val index = cartDataList.indexOf(it)
            cartDataList.remove(it)
            notifyItemRemoved(index)
        }
    }

    fun removeCartItemById(cartIds: List<String>, context: Context?) {
        // Store item first before remove item to prevent ConcurrentModificationException
        val toBeRemovedData = ArrayList<Any>()
        var disabledItemHeaderHolderData: DisabledItemHeaderHolderData? = null
        var cartItemTickerErrorHolderData: CartItemTickerErrorHolderData? = null
        loop@ for (i in cartDataList.indices) {
            val obj = cartDataList[i]
            when (obj) {
                is CartShopHolderData -> {
                    val toBeRemovedCartItemHolderData = ArrayList<CartItemHolderData>()
                    obj.shopGroupAvailableData.cartItemDataList?.toMutableList()?.let {
                        for (cartItemHolderData in it) {
                            cartItemHolderData.cartItemData?.originData?.let {
                                if (cartIds.contains(it.cartId.toString())) {
                                    toBeRemovedCartItemHolderData.add(cartItemHolderData)
                                }
                            }
                        }
                        for (cartItemHolderData in toBeRemovedCartItemHolderData) {
                            it.remove(cartItemHolderData)
                        }
                        if (it.size == 0) {
                            toBeRemovedData.add(obj)
                        }
                    }
                }
                is DisabledCartItemHolderData -> if (cartIds.contains(obj.cartId.toString())) {
                    val before = cartDataList[i - 1]
                    var after: Any? = null
                    if (i + 1 < cartDataList.size) {
                        after = cartDataList[i + 1]
                    }
                    if (before is DisabledShopHolderData) {
                        if (after !is DisabledCartItemHolderData) {
                            toBeRemovedData.add(before)
                        }
                        toBeRemovedData.add(obj)
                    } else if (before is DisabledCartItemHolderData) {
                        if (after !is DisabledCartItemHolderData) {
                            before.showDivider = false
                        }
                        toBeRemovedData.add(obj)
                    }
                }
                is DisabledItemHeaderHolderData -> disabledItemHeaderHolderData = obj
                is CartItemTickerErrorHolderData -> cartItemTickerErrorHolderData = obj
                is CartRecentViewHolderData, is CartWishlistHolderData, is CartRecommendationItemHolderData -> break@loop
            }
        }

        for (cartShopHolderData in toBeRemovedData) {
            cartDataList.remove(cartShopHolderData)
        }

        if (cartItemTickerErrorHolderData != null || disabledItemHeaderHolderData != null) {
            var errorItemCount = 0
            loop@ for (any in cartDataList) {
                when (any) {
                    is CartShopHolderData -> any.shopGroupAvailableData.cartItemDataList?.let {
                        for (cartItemHolderData in it) {
                            if (cartItemHolderData.cartItemData?.isError == true) {
                                errorItemCount++
                            }
                        }
                    }
                    is DisabledCartItemHolderData -> errorItemCount++
                    is CartRecentViewHolderData, is CartWishlistHolderData, is CartRecommendationItemHolderData -> break@loop
                }
            }

            if (errorItemCount > 0) {
                if (context != null) {
                    cartItemTickerErrorHolderData?.let {
                        it.cartTickerErrorData?.errorInfo = String.format(context.getString(R.string.cart_error_message), errorItemCount)
                    }
                    disabledItemHeaderHolderData?.let {
                        it.disabledItemCount = errorItemCount
                    }
                }
            } else {
                cartItemTickerErrorHolderData?.let {
                    cartDataList.remove(it)
                }
                disabledItemHeaderHolderData?.let {
                    cartDataList.remove(it)
                }
            }
        }

        notifyDataSetChanged()
    }

    fun addCartTicker(tickerAnnouncementHolderData: TickerAnnouncementHolderData) {
        cartDataList.add(0, tickerAnnouncementHolderData)
    }

    fun addCartSelectAll() {
        if (cartDataList.size > 0 && cartDataList[0] !is Boolean) {
            cartDataList.add(0, true)
            notifyItemInserted(0)
        }
    }

    fun removeCartSelectAll() {
        if (cartDataList.size > 0) {
            if (cartDataList[0] is Boolean) {
                cartDataList.removeAt(0)
                notifyItemRemoved(0)
            }
        }
    }
}
