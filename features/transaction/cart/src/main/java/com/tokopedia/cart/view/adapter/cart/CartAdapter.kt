package com.tokopedia.cart.view.adapter.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.collection.ArraySet
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.domain.model.cartlist.CartItemData
import com.tokopedia.cart.domain.model.cartlist.ShopGroupAvailableData
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.adapter.recentview.CartRecentViewAdapter
import com.tokopedia.cart.view.adapter.wishlist.CartWishlistAdapter
import com.tokopedia.cart.view.uimodel.*
import com.tokopedia.cart.view.viewholder.*
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.promocheckout.common.view.model.PromoStackingData
import com.tokopedia.purchase_platform.common.feature.insurance.InsuranceCartShopViewHolder
import com.tokopedia.purchase_platform.common.feature.insurance.InsuranceItemActionListener
import com.tokopedia.purchase_platform.common.feature.insurance.PAGE_TYPE_CART
import com.tokopedia.purchase_platform.common.feature.insurance.response.InsuranceCartDigitalProduct
import com.tokopedia.purchase_platform.common.feature.insurance.response.InsuranceCartShops
import com.tokopedia.purchase_platform.common.feature.sellercashback.SellerCashbackListener
import com.tokopedia.purchase_platform.common.feature.sellercashback.ShipmentSellerCashbackModel
import com.tokopedia.purchase_platform.common.feature.sellercashback.ShipmentSellerCashbackViewHolder
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementActionListener
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementViewHolder
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject
import kotlin.math.min

/**
 * @author anggaprasetiyo on 18/01/18.
 */

class CartAdapter @Inject constructor(private val actionListener: ActionListener,
                                      private val cartItemActionListener: CartItemAdapter.ActionListener,
                                      private val insuranceItemActionlistener: InsuranceItemActionListener,
                                      private val tickerAnnouncementActionListener: TickerAnnouncementActionListener,
                                      private val sellerCashbackListener: SellerCashbackListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val cartDataList = ArrayList<Any>()
    private var compositeSubscription = CompositeSubscription()

    val insuranceCartShops = ArrayList<InsuranceCartShops>()
    private val insuranceRecommendationList = ArrayList<InsuranceCartShops>()
    private val insuranceCartList = ArrayList<InsuranceCartShops>()
    private var shipmentSellerCashbackModel: ShipmentSellerCashbackModel? = null
    private var cartEmptyHolderData: CartEmptyHolderData? = null
    private var cartLoadingHolderData: CartLoadingHolderData? = null
    private var cartWishlistAdapter: CartWishlistAdapter? = null
    private var cartRecentViewAdapter: CartRecentViewAdapter? = null
    private var sendInsuranceImpressionEvent = false
    private var tmpCollapsedItem = ArrayList<Any>()

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

            cartItemDataList.addAll(collapsedCartItemData)

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

            cartItemDataList.addAll(collapsedCartItemData)

            return cartItemDataList
        }

    val collapsedCartItemData: List<CartItemData>
        get() {
            val cartItemDataList = ArrayList<CartItemData>()
            loop@ for (data in tmpCollapsedItem) {
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
            is CartSelectAllHolderData -> CartSelectAllViewHolder.LAYOUT
            is CartShopHolderData -> CartShopViewHolder.TYPE_VIEW_ITEM_SHOP
            is CartItemTickerErrorHolderData -> CartTickerErrorViewHolder.TYPE_VIEW_TICKER_CART_ERROR
            is ShipmentSellerCashbackModel -> ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK
            is CartEmptyHolderData -> CartEmptyViewHolder.LAYOUT
            is CartRecentViewHolderData -> CartRecentViewViewHolder.LAYOUT
            is CartWishlistHolderData -> CartWishlistViewHolder.LAYOUT
            is CartSectionHeaderHolderData -> CartSectionHeaderViewHolder.LAYOUT
            is CartRecommendationItemHolderData -> CartRecommendationViewHolder.LAYOUT
            is CartLoadingHolderData -> CartLoadingViewHolder.LAYOUT
            is TickerAnnouncementHolderData -> TickerAnnouncementViewHolder.LAYOUT
            is InsuranceCartShops -> InsuranceCartShopViewHolder.TYPE_VIEW_INSURANCE_CART_SHOP
            is DisabledCartItemHolderData -> DisabledCartItemViewHolder.LAYOUT
            is DisabledItemHeaderHolderData -> DisabledItemHeaderViewHolder.LAYOUT
            is DisabledReasonHolderData -> DisabledReasonViewHolder.LAYOUT
            is DisabledShopHolderData -> DisabledShopViewHolder.LAYOUT
            is DisabledAccordionHolderData -> DisabledAccordionViewHolder.LAYOUT
            else -> super.getItemViewType(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        initializeCompositeSubscription()
        when (viewType) {
            CartSelectAllViewHolder.LAYOUT -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(CartSelectAllViewHolder.LAYOUT, parent, false)
                return CartSelectAllViewHolder(view, actionListener, compositeSubscription)
            }
            CartShopViewHolder.TYPE_VIEW_ITEM_SHOP -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(CartShopViewHolder.TYPE_VIEW_ITEM_SHOP, parent, false)
                return CartShopViewHolder(view, actionListener, cartItemActionListener, compositeSubscription)
            }
            CartTickerErrorViewHolder.TYPE_VIEW_TICKER_CART_ERROR -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(CartTickerErrorViewHolder.TYPE_VIEW_TICKER_CART_ERROR, parent, false)
                return CartTickerErrorViewHolder(view, actionListener)
            }
            ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK, parent, false)
                return ShipmentSellerCashbackViewHolder(view, sellerCashbackListener)
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
            DisabledReasonViewHolder.LAYOUT -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(DisabledReasonViewHolder.LAYOUT, parent, false)
                return DisabledReasonViewHolder(view, actionListener)
            }
            DisabledShopViewHolder.LAYOUT -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(DisabledShopViewHolder.LAYOUT, parent, false)
                return DisabledShopViewHolder(view, actionListener)
            }
            DisabledAccordionViewHolder.LAYOUT -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(DisabledAccordionViewHolder.LAYOUT, parent, false)
                return DisabledAccordionViewHolder(view, actionListener)
            }

            else -> throw RuntimeException("No view holder type found")
        }

    }

    private fun initializeCompositeSubscription() {
        if (compositeSubscription.isUnsubscribed) {
            compositeSubscription = CompositeSubscription()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        when {
            viewType == CartSelectAllViewHolder.LAYOUT -> {
                val data = cartDataList[position] as CartSelectAllHolderData
                (holder as CartSelectAllViewHolder).bind(data)
            }
            viewType == CartShopViewHolder.TYPE_VIEW_ITEM_SHOP -> {
                val data = cartDataList[position] as CartShopHolderData
                (holder as CartShopViewHolder).bindData(data)
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
            getItemViewType(position) == InsuranceCartShopViewHolder.TYPE_VIEW_INSURANCE_CART_SHOP -> {
                val insuranceCartShops = cartDataList[position] as InsuranceCartShops
                (holder as InsuranceCartShopViewHolder).bindData(insuranceCartShops, position, PAGE_TYPE_CART)
            }
            viewType == DisabledItemHeaderViewHolder.LAYOUT -> {
                val data = cartDataList[position] as DisabledItemHeaderHolderData
                (holder as DisabledItemHeaderViewHolder).bind(data)
            }
            viewType == DisabledReasonViewHolder.LAYOUT -> {
                val data = cartDataList[position] as DisabledReasonHolderData
                (holder as DisabledReasonViewHolder).bind(data)
            }
            viewType == DisabledShopViewHolder.LAYOUT -> {
                val data = cartDataList[position] as DisabledShopHolderData
                (holder as DisabledShopViewHolder).bind(data)
            }
            viewType == DisabledCartItemViewHolder.LAYOUT -> {
                val data = cartDataList[position] as DisabledCartItemHolderData
                (holder as DisabledCartItemViewHolder).bind(data)
            }
            viewType == DisabledAccordionViewHolder.LAYOUT -> {
                val data = cartDataList[position] as DisabledAccordionHolderData
                (holder as DisabledAccordionViewHolder).bind(data)
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

    fun clearCompositeSubscription() {
        compositeSubscription.clear()
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

    fun addNotAvailableReason(disabledReasonHolderData: DisabledReasonHolderData) {
        cartDataList.add(disabledReasonHolderData)
    }

    fun addNotAvailableShop(disabledShopHolderData: DisabledShopHolderData) {
        var showDivider = false
        if (cartDataList.size > 0 && cartDataList[cartDataList.size - 1] !is DisabledReasonHolderData) {
            showDivider = true
        }
        disabledShopHolderData.showDivider = showDivider
        cartDataList.add(disabledShopHolderData)
    }

    fun addNotAvailableProduct(disabledCartItemHolderData: DisabledCartItemHolderData) {
        cartDataList.add(disabledCartItemHolderData)
    }

    fun addNotAvailableAccordion(accordionHolderData: DisabledAccordionHolderData) {
        cartDataList.add(accordionHolderData)
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

    fun setItemSelected(position: Int, parentPosition: Int, selected: Boolean) {
        val any = cartDataList[parentPosition]
        if (any is CartShopHolderData) {
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
                } else if (selectedCount > 0 && selectedCount < it.size) {
                    any.isAllSelected = false
                    any.isPartialSelected = true
                } else {
                    any.isAllSelected = true
                    any.isPartialSelected = false
                }
            }
        }
    }

    fun increaseQuantity(position: Int, parentPosition: Int) {
        if (parentPosition < cartDataList.size) {
            val data = cartDataList[parentPosition]
            if (data is CartShopHolderData) {
                data.shopGroupAvailableData.cartItemDataList?.let {
                    if (position < it.size) {
                        it[position].cartItemData?.updatedData?.increaseQuantity()
                    }
                }
            }
        }
        checkForShipmentForm()
    }

    fun decreaseQuantity(position: Int, parentPosition: Int) {
        if (parentPosition < cartDataList.size) {
            val data = cartDataList[parentPosition]
            if (data is CartShopHolderData) {
                data.shopGroupAvailableData.cartItemDataList?.let {
                    if (position < it.size) {
                        it[position].cartItemData?.updatedData?.decreaseQuantity()
                    }
                }
            }
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

    fun addCartTickerError(cartItemTickerErrorHolderData: CartItemTickerErrorHolderData) {
        cartDataList.add(cartItemTickerErrorHolderData)
        checkForShipmentForm()
    }

    fun addCartEmptyData(cartEmptyHolderData: CartEmptyHolderData) {
        cartDataList.add(cartEmptyHolderData)
    }

    fun addCartRecentViewData(cartSectionHeaderHolderData: CartSectionHeaderHolderData,
                              cartRecentViewHolderData: CartRecentViewHolderData) {
        var recentViewIndex = 0
        for ((index, item) in cartDataList.withIndex()) {
            if (item is CartEmptyHolderData ||
                    item is CartShopHolderData ||
                    item is ShipmentSellerCashbackModel ||
                    item is CartWishlistHolderData ||
                    item is InsuranceCartShops ||
                    item is DisabledItemHeaderHolderData ||
                    item is DisabledReasonHolderData ||
                    item is DisabledShopHolderData ||
                    item is DisabledCartItemHolderData ||
                    item is DisabledAccordionHolderData) {
                recentViewIndex = index
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
        for ((index, item) in cartDataList.withIndex()) {
            if (item is CartEmptyHolderData ||
                    item is CartShopHolderData ||
                    item is ShipmentSellerCashbackModel ||
                    item is InsuranceCartShops ||
                    item is DisabledItemHeaderHolderData ||
                    item is DisabledReasonHolderData ||
                    item is DisabledShopHolderData ||
                    item is DisabledCartItemHolderData ||
                    item is DisabledAccordionHolderData
            ) {
                wishlistIndex = index
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

    fun updateCartWishlistData(cartWishlistHolderData: CartWishlistHolderData): Int {
        var wishlistIndex = 0
        for ((index, item) in cartDataList.withIndex()) {
            if (item is CartWishlistHolderData) {
                wishlistIndex = index
                break
            }
        }

        if (wishlistIndex != 0) {
            cartDataList[wishlistIndex] = cartWishlistHolderData
        }

        return wishlistIndex
    }

    fun getCartWishlistHolderData(): CartWishlistHolderData {
        cartDataList.forEach {
            if (it is CartWishlistHolderData) {
                return it
            }
        }

        return CartWishlistHolderData()
    }

    fun addCartRecommendationData(cartSectionHeaderHolderData: CartSectionHeaderHolderData?,
                                  cartRecommendationItemHolderDataList: List<CartRecommendationItemHolderData>) {
        var recommendationIndex = 0
        for ((index, item) in cartDataList.withIndex()) {
            if (item is CartEmptyHolderData ||
                    item is CartShopHolderData ||
                    item is ShipmentSellerCashbackModel ||
                    item is CartRecentViewHolderData ||
                    item is CartWishlistHolderData ||
                    item is CartRecommendationItemHolderData ||
                    item is InsuranceCartShops ||
                    item is DisabledItemHeaderHolderData ||
                    item is DisabledReasonHolderData ||
                    item is DisabledShopHolderData ||
                    item is DisabledCartItemHolderData ||
                    item is DisabledAccordionHolderData) {
                recommendationIndex = index
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
        notifyItemRangeInserted(recommendationIndex, cartRecommendationItemHolderDataList.size)
    }

    fun removeCartEmptyData() {
        cartEmptyHolderData?.let {
            val index = cartDataList.indexOf(it)
            cartDataList.remove(it)
            notifyItemRemoved(index)

            cartEmptyHolderData = null
        }
    }

    fun renderEmptyCart(cartEmptyHolderData: CartEmptyHolderData) {
        if (cartDataList.isNotEmpty() && cartDataList[0] is TickerAnnouncementHolderData) {
            cartDataList.set(0, cartEmptyHolderData)
            notifyItemChanged(0)
        } else {
            cartDataList.add(0, cartEmptyHolderData)
            notifyItemInserted(0)
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

        for (any in cartDataList) {
            if (any is CartShopHolderData && (any.isAllSelected || any.isPartialSelected)) {
                any.shopGroupAvailableData.cartItemDataList?.forEach {
                    if (!it.errorFormItemValidationMessage.isNullOrBlank()) {
                        canProcess = false
                        return@forEach
                    }
                }
            }
        }

        if (canProcess && checkedCount > 0) {
            actionListener.onCartDataEnableToCheckout()
        } else {
            actionListener.onCartDataDisableToCheckout()
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
                    it.sellerCashback = cashback.toInt()
                    it.isVisible = true
                    it.sellerCashbackFmt = CurrencyFormatUtil.convertPriceValueToIdrFormat(cashback.toLong(), false).removeDecimalSuffix()
                    cartDataList.add(++index, it)
                    notifyItemInserted(index)
                }
            } else {
                shipmentSellerCashbackModel?.let {
                    it.sellerCashback = cashback.toInt()
                    it.isVisible = true
                    it.sellerCashbackFmt = CurrencyFormatUtil.convertPriceValueToIdrFormat(cashback.toLong(), false).removeDecimalSuffix()
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

    fun removeWishlist(productId: String) {
        var wishlistIndex = 0
        var wishlistItemIndex = 0
        var cartWishlistHolderData: CartWishlistHolderData? = null
        for ((i, any) in cartDataList.withIndex()) {
            if (any is CartWishlistHolderData) {
                wishlistIndex = i
                val wishlist = any.wishList
                for ((j, data) in wishlist.withIndex()) {
                    if (data.id == productId) {
                        wishlistItemIndex = j
                        cartWishlistHolderData = any
                        break
                    }
                }
                break
            }
        }

        if (cartWishlistHolderData != null) {
            if (cartWishlistHolderData.wishList.size > 1) {
                cartWishlistAdapter?.let {
                    cartWishlistHolderData.wishList.removeAt(wishlistItemIndex)
                    cartWishlistAdapter?.updateWishlistItems(cartWishlistHolderData.wishList)
                }
            } else {
                // Remove wishlist holder & wishlist header
                cartDataList.removeAt(wishlistIndex)
                val headerIndex = wishlistIndex - 1
                cartDataList.removeAt(headerIndex)
                notifyItemRangeRemoved(headerIndex, 2)
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

    fun removeAccordionDisabledItem() {
        var item: DisabledAccordionHolderData? = null
        cartDataList.forEach {
            if (it is DisabledAccordionHolderData) {
                item = it
            }
        }

        item?.let {
            cartDataList.remove(it)
        }
    }

    fun removeCartItemById(cartIds: List<String>, context: Context?): Pair<ArrayList<Int>, ArrayList<Int>> {
        // Store item first before remove item to prevent ConcurrentModificationException
        val toBeRemovedData = ArrayList<Any>()
        val toBeRemovedIndex = ArrayList<Int>()
        val toBeUpdatedIndex = ArrayList<Int>()
        var selectAllHolderData: CartSelectAllHolderData? = null
        var disabledItemHeaderHolderData: DisabledItemHeaderHolderData? = null
        var cartItemTickerErrorHolderData: CartItemTickerErrorHolderData? = null
        var disabledAccordionHolderData: DisabledAccordionHolderData? = null

        // Get to be deleted items
        var deletedDisabledItemCount = 0
        loop@ for (i in cartDataList.indices) {
            val obj = cartDataList[i]
            when (obj) {
                // For enable / available item
                is CartShopHolderData -> {
                    val toBeRemovedCartItemHolderData = ArrayList<CartItemHolderData>()
                    obj.shopGroupAvailableData.cartItemDataList?.let {
                        for (cartItemHolderData in it) {
                            cartItemHolderData.cartItemData?.originData?.let { data ->
                                if (cartIds.contains(data.cartId.toString())) {
                                    toBeRemovedCartItemHolderData.add(cartItemHolderData)
                                    if (!toBeUpdatedIndex.contains(i)) {
                                        toBeUpdatedIndex.add(i)
                                    }
                                }
                            }
                        }
                        for (cartItemHolderData in toBeRemovedCartItemHolderData) {
                            it.remove(cartItemHolderData)
                        }
                        if (it.size == 0) {
                            toBeRemovedData.add(obj)
                            toBeRemovedIndex.add(i)
                        }
                    }
                }

                // For disabled / unavailable item, also delete other item (shop, unavailable reason, unavailable header, accordion) if needed
                is DisabledCartItemHolderData -> if (cartIds.contains(obj.cartId.toString())) {
                    if (i < 1) {
                        continue@loop
                    }
                    val indexBefore = i - 1
                    val before = cartDataList[indexBefore]
                    var after: Any? = null
                    if (i + 1 < cartDataList.size) {
                        after = cartDataList[i + 1]
                    }

                    if (before is DisabledShopHolderData) {
                        // If item before `obj` is shop, then remove it since the shop only has one item
                        if (after !is DisabledCartItemHolderData) {
                            toBeRemovedData.add(before)
                            toBeRemovedIndex.add(indexBefore)
                            // Adjust divider visibility
                            if (after is DisabledShopHolderData) {
                                after.showDivider = false
                            }
                        }
                        toBeRemovedData.add(obj)
                        toBeRemovedIndex.add(i)
                    } else if (before is DisabledCartItemHolderData) {
                        // If item before `obj` is cart item, then don't remove it since the shop has more than one item
                        // Adjust divider visibility
                        if (after !is DisabledCartItemHolderData) {
                            before.showDivider = false
                        }
                        toBeRemovedData.add(obj)
                        toBeRemovedIndex.add(i)
                    }

                    // If two item before `obj` is reason item, then remove it since the reason has only one shop and the shop has only one item
                    if (i < 2) {
                        continue@loop
                    }
                    val indexTwoBefore = i - 2
                    val twoBefore = cartDataList[indexTwoBefore]
                    if (twoBefore is DisabledReasonHolderData) {
                        if (after !is DisabledCartItemHolderData && after !is DisabledShopHolderData) {
                            toBeRemovedData.add(twoBefore)
                            toBeRemovedIndex.add(indexTwoBefore)
                        }
                    }

                    deletedDisabledItemCount++
                }
                is CartSelectAllHolderData -> selectAllHolderData = obj
                is DisabledItemHeaderHolderData -> disabledItemHeaderHolderData = obj
                is CartItemTickerErrorHolderData -> cartItemTickerErrorHolderData = obj
                is DisabledAccordionHolderData -> disabledAccordionHolderData = obj
                is CartRecentViewHolderData, is CartWishlistHolderData, is CartRecommendationItemHolderData -> break@loop
            }
        }

        // Remove all collected items from previous loop
        for (cartShopHolderData in toBeRemovedData) {
            cartDataList.remove(cartShopHolderData)
        }

        // Remove select all item if all available item removed
        if (selectAllHolderData != null && allAvailableCartItemData.isEmpty()) {
            toBeRemovedIndex.add(cartDataList.indexOf(selectAllHolderData))
            cartDataList.remove(selectAllHolderData)
        }

        // Check if delete action is bulk delete on unavailable section. If true, remove accordion
        if (deletedDisabledItemCount != 0 && cartIds.size > 1) {
            disabledAccordionHolderData?.let {
                toBeRemovedData.add(it)
            }
            tmpCollapsedItem.clear()
        }

        // Determine to remove error ticker and unavailable item header
        if (cartItemTickerErrorHolderData != null || disabledItemHeaderHolderData != null) {
            // Count available item and unavailable items
            var errorItemCount = 0
            var normalItemCount = 0
            loop@ for (any in cartDataList) {
                when (any) {
                    is CartShopHolderData -> any.shopGroupAvailableData.cartItemDataList?.let {
                        for (cartItemHolderData in it) {
                            normalItemCount++
                        }
                    }
                    is DisabledCartItemHolderData -> errorItemCount++
                    is CartRecentViewHolderData, is CartWishlistHolderData, is CartRecommendationItemHolderData -> break@loop
                }
            }

            errorItemCount += collapsedCartItemData.size

            if (errorItemCount > 0) {
                // Goes here if unavailable item still exist
                if (context != null) {
                    if (normalItemCount == 0) {
                        // If normal / non error item empty, remove unavailable item error ticker
                        cartItemTickerErrorHolderData?.let {
                            toBeRemovedIndex.add(cartDataList.indexOf(it))
                            cartDataList.remove(it)
                        }
                    } else {
                        // If normal / non error item not empty, adjust error ticker item wording count
                        cartItemTickerErrorHolderData?.let {
                            it.cartTickerErrorData?.errorInfo = String.format(context.getString(R.string.cart_error_message), errorItemCount)
                            toBeUpdatedIndex.add(cartDataList.indexOf(it))
                        }
                    }
                    // Adjust unavailable item header wording
                    disabledItemHeaderHolderData?.let {
                        it.disabledItemCount = errorItemCount
                        toBeUpdatedIndex.add(cartDataList.indexOf(it))
                    }
                }
            } else {
                // Goes here if unavailable item is not exist
                // Remove unavailable item error ticker
                cartItemTickerErrorHolderData?.let {
                    toBeRemovedIndex.add(cartDataList.indexOf(it))
                    cartDataList.remove(it)
                }
                // Remove unavailable item header
                disabledItemHeaderHolderData?.let {
                    toBeRemovedIndex.add(cartDataList.indexOf(it))
                    cartDataList.remove(it)
                }
            }
        }

        cartItemTickerErrorHolderData?.let {
            toBeUpdatedIndex.add(cartDataList.indexOf(it))
        }

        disabledItemHeaderHolderData?.let {
            toBeUpdatedIndex.add(cartDataList.indexOf(it))
        }

        return Pair(toBeRemovedIndex, toBeUpdatedIndex)
    }

    fun addCartTicker(tickerAnnouncementHolderData: TickerAnnouncementHolderData) {
        cartDataList.add(0, tickerAnnouncementHolderData)
    }

    fun addSelectAll(data: CartSelectAllHolderData) {
        cartDataList.add(data)
    }

    fun getItemCountBeforeCartItem(): Int {
        var count = 0
        cartDataList.forEach {
            if (it is CartShopHolderData) return@forEach
            count++
        }

        return count
    }

    fun getRecommendationItem(): List<CartRecommendationItemHolderData> {
        var firstRecommendationItemIndex = 0
        for ((index, item) in cartDataList.withIndex()) {
            if (item is CartRecommendationItemHolderData) {
                firstRecommendationItemIndex = index
                break
            }
        }

        var lastIndex = itemCount
        // Check if last item is not loading view type
        if (cartDataList[itemCount - 1] !is CartRecommendationItemHolderData) {
            lastIndex = itemCount - 1
        }
        val recommendationList = cartDataList.subList(firstRecommendationItemIndex, lastIndex)

        return recommendationList as List<CartRecommendationItemHolderData>
    }

    fun collapseOrExpandDisabledItemAccordion(data: DisabledAccordionHolderData) {
        val index = cartDataList.indexOf(data)
        if (index > 0) {
            notifyItemChanged(index)
        }
    }

    fun collapseDisabledItems() {
        val tmpCollapsedItem = ArrayList<Any>()
        var firstIndex = 0
        for ((index, item) in cartDataList.withIndex()) {
            if (item is DisabledCartItemHolderData) {
                firstIndex = index
                break
            }
        }
        firstIndex += 1

        for (index in firstIndex until cartDataList.size) {
            val item = cartDataList[index]
            if (item is DisabledReasonHolderData ||
                    item is DisabledShopHolderData ||
                    item is DisabledCartItemHolderData) {
                tmpCollapsedItem.add(cartDataList[index])
            }
        }

        this.tmpCollapsedItem = tmpCollapsedItem

        cartDataList.removeAll(tmpCollapsedItem)
        notifyItemRangeRemoved(firstIndex, tmpCollapsedItem.size)
    }

    fun expandDisabledItems() {
        if (tmpCollapsedItem.isNotEmpty()) {
            var headerIndex = 0
            for ((index, item) in cartDataList.withIndex()) {
                if (item is DisabledCartItemHolderData) {
                    headerIndex = index
                    break
                }
            }
            cartDataList.addAll(++headerIndex, tmpCollapsedItem)
            notifyItemRangeInserted(headerIndex, tmpCollapsedItem.size)
            tmpCollapsedItem.clear()
        }
    }

    fun getDisabledAccordionHolderData(): DisabledAccordionHolderData? {
        cartDataList.forEach {
            if (it is DisabledAccordionHolderData) {
                return it
            }
        }

        return null
    }

    fun getFirstShopAndShopCount(): Pair<Int, Int> {
        var firstIndex = 0
        var count = 0
        cartDataList.forEachIndexed { index, any ->
            when (any) {
                is CartShopHolderData -> {
                    count++
                    if (firstIndex == 0) {
                        firstIndex = index
                    }
                }
                is ShipmentSellerCashbackModel, is CartSectionHeaderHolderData -> {
                    return@forEachIndexed
                }
            }
        }

        return Pair(firstIndex, count)
    }

    fun setLastItemAlwaysSelected(): Boolean {
        var cartItemCount = 0
        getData().forEach outer@{ any ->
            when (any) {
                is CartShopHolderData -> {
                    any.shopGroupAvailableData.cartItemDataList?.forEach {
                        cartItemCount++

                        if (cartItemCount > 1) {
                            return@outer
                        }
                    }
                }
            }
        }

        if (cartItemCount == 1) {
            var tmpIndex = 0
            getData().forEachIndexed { index, any ->
                when (any) {
                    is CartShopHolderData -> {
                        tmpIndex = index
                        any.isAllSelected = true
                        any.shopGroupAvailableData.cartItemDataList?.forEach {
                            it.isSelected = true
                        }
                    }
                    is DisabledItemHeaderHolderData, is CartSectionHeaderHolderData -> {
                        return@forEachIndexed
                    }
                }
            }

            notifyItemChanged(tmpIndex)

            return true
        }

        return false
    }

    fun hasAvailableItemLeft(): Boolean {
        getData().forEach {
            when (it) {
                is CartShopHolderData -> {
                    if (it.shopGroupAvailableData.cartItemDataList?.isNotEmpty() == true) {
                        return true
                    }
                }
                is DisabledItemHeaderHolderData, is CartSectionHeaderHolderData -> {
                    return false
                }
            }
        }

        return false
    }

    fun setAllAvailableItemCheck(cheked: Boolean) {
        val indices = ArraySet<Int>()
        getData().forEachIndexed { index, data ->
            when (data) {
                is CartShopHolderData -> {
                    var changeShopLevelCheckboxState = false
                    data.shopGroupAvailableData.cartItemDataList?.forEach {
                        if (it.isSelected != cheked) {
                            it.isSelected = cheked
                            indices.add(index)
                            changeShopLevelCheckboxState = true
                        }
                    }

                    if (changeShopLevelCheckboxState) {
                        data.isAllSelected = cheked
                        data.isPartialSelected = false
                    }
                }
                is DisabledItemHeaderHolderData, is CartSectionHeaderHolderData -> {
                    return@forEachIndexed
                }
            }
        }

        indices.forEach {
            notifyItemChanged(it)
        }
    }

    fun setCheckboxGlobalItemState(cheked: Boolean, isCheckUncheckDirectAction: Boolean) {
        var tmpIndex = -1
        getData().forEachIndexed { index, data ->
            when (data) {
                is CartSelectAllHolderData -> {
                    data.isCheked = cheked
                    data.isCheckUncheckDirectAction = isCheckUncheckDirectAction
                    tmpIndex = index
                    return@forEachIndexed
                }
            }
        }

        if (tmpIndex != -1) {
            notifyItemChanged(tmpIndex)
        }
    }

    fun resetGlobalItemLock() {
        getData().forEach { data ->
            when (data) {
                is CartSelectAllHolderData -> {
                    data.isCheckUncheckDirectAction = true
                    return@forEach
                }
            }
        }
    }

    fun isAllAvailableItemCheked(): Boolean {
        getData().forEach {
            when (it) {
                is CartShopHolderData -> {
                    it.shopGroupAvailableData.cartItemDataList?.forEach {
                        if (!it.isSelected) {
                            return false
                        }
                    }
                }
                is DisabledItemHeaderHolderData, is CartSectionHeaderHolderData -> {
                    return true
                }
            }
        }

        return true
    }

    fun getData(): ArrayList<Any> {
        return cartDataList
    }

    fun hasSelectedCartItem(): Boolean {
        getData().forEach {
            when (it) {
                is CartShopHolderData -> {
                    it.shopGroupAvailableData.cartItemDataList?.forEach {
                        if (it.isSelected) return true
                    }
                }
                is DisabledItemHeaderHolderData, is CartSectionHeaderHolderData -> {
                    return false
                }
            }
        }

        return false
    }

    fun setGlobalDeleteVisibility(isShow: Boolean) {
        var tmpIndex = -1
        getData().forEachIndexed { index, data ->
            when (data) {
                is CartSelectAllHolderData -> {
                    data.isShowDeleteButton = isShow
                    tmpIndex = index
                    return@forEachIndexed
                }
            }
        }

        if (tmpIndex != -1) {
            notifyItemChanged(tmpIndex)
        }
    }
}
