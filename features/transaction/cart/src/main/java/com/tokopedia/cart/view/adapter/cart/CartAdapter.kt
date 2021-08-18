package com.tokopedia.cart.view.adapter.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.collection.ArraySet
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.*
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.adapter.recentview.CartRecentViewAdapter
import com.tokopedia.cart.view.adapter.wishlist.CartWishlistAdapter
import com.tokopedia.cart.view.uimodel.*
import com.tokopedia.cart.view.viewholder.*
import com.tokopedia.purchase_platform.common.feature.sellercashback.SellerCashbackListener
import com.tokopedia.purchase_platform.common.feature.sellercashback.ShipmentSellerCashbackModel
import com.tokopedia.purchase_platform.common.feature.sellercashback.ShipmentSellerCashbackViewHolder
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementActionListener
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementViewHolder
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.currency.CurrencyFormatUtil
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject
import kotlin.math.min

class CartAdapter @Inject constructor(private val actionListener: ActionListener,
                                      private val cartItemActionListener: CartItemAdapter.ActionListener,
                                      private val tickerAnnouncementActionListener: TickerAnnouncementActionListener,
                                      private val sellerCashbackListener: SellerCashbackListener,
                                      private val userSession: UserSessionInterface) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val cartDataList = ArrayList<Any>()
    private var compositeSubscription = CompositeSubscription()

    private var shipmentSellerCashbackModel: ShipmentSellerCashbackModel? = null
    private var cartEmptyHolderData: CartEmptyHolderData? = null
    private var cartLoadingHolderData: CartLoadingHolderData? = null
    private var cartWishlistAdapter: CartWishlistAdapter? = null
    private var cartRecentViewAdapter: CartRecentViewAdapter? = null
    private var cartTopAdsHeadlineData: CartTopAdsHeadlineData? = null
    private var tmpCollapsedUnavailableItem = ArrayList<Any>()

    var firstCartSectionHeaderPosition: Int = -1

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

    val allAvailableShopGroupDataList: List<CartShopHolderData>
        get() {
            val cartShopHolderDataFinalList = ArrayList<CartShopHolderData>()
            for (i in cartDataList.indices) {
                val obj = cartDataList[i]
                if (obj is CartShopHolderData && !obj.isError) {
                    cartShopHolderDataFinalList.add(obj)
                }
            }
            return cartShopHolderDataFinalList
        }

    val selectedCartItemData: List<CartItemHolderData>
        get() {
            val cartItemDataList = ArrayList<CartItemHolderData>()
            for (data in cartDataList) {
                if (data is CartShopHolderData) {
                    if ((data.isPartialSelected || data.isAllSelected)) {
                        for (cartItemHolderData in data.productUiModelList) {
                            if (cartItemHolderData.isSelected && !cartItemHolderData.isError) {
                                cartItemDataList.add(cartItemHolderData)
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
                    if (data.isPartialSelected || data.isAllSelected) {
                        cartShopHolderDataList.add(data)
                    }
                }
            }

            return cartShopHolderDataList
        }

    val allCartItemData: List<CartItemHolderData>
        get() {
            val cartItemDataList = ArrayList<CartItemHolderData>()
            loop@ for (data in cartDataList) {
                when (data) {
                    is CartShopHolderData -> {
                        val cartItemHolderDataList = data.productUiModelList
                        cartItemDataList.addAll(cartItemHolderDataList)
                    }
                    is CartWishlistHolderData, is CartRecentViewHolderData, is CartRecommendationItemHolderData -> break@loop
                }
            }

            cartItemDataList.addAll(collapsedUnavailableCartItemData)

            return cartItemDataList
        }

    val allAvailableCartItemData: List<CartItemHolderData>
        get() {
            val cartItemDataList = ArrayList<CartItemHolderData>()
            loop@ for (data in cartDataList) {
                when (data) {
                    is CartShopHolderData -> {
                        if (!data.isError) {
                            cartItemDataList.addAll(data.productUiModelList)
                        }
                    }
                    is CartWishlistHolderData, is CartRecentViewHolderData, is CartRecommendationItemHolderData -> break@loop
                }
            }

            return cartItemDataList
        }

    val allDisabledCartItemData: List<CartItemHolderData>
        get() {
            val cartItemDataList = ArrayList<CartItemHolderData>()
            loop@ for (data in cartDataList) {
                when (data) {
                    is CartShopHolderData -> {
                        if (data.isError) {
                            cartItemDataList.addAll(data.productUiModelList)
                        }
                    }
                    is CartWishlistHolderData, is CartRecentViewHolderData, is CartRecommendationItemHolderData -> break@loop
                }
            }

            cartItemDataList.addAll(collapsedUnavailableCartItemData)

            return cartItemDataList
        }

    val collapsedUnavailableCartItemData: List<CartItemHolderData>
        get() {
            val cartItemDataList = ArrayList<CartItemHolderData>()
            loop@ for (data in tmpCollapsedUnavailableItem) {
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
                        data.productUiModelList.let {
                            for (cartItemHolderData in it) {
                                productIdList.add(cartItemHolderData.productId)
                            }
                        }
                    }
                    is DisabledCartItemHolderData -> productIdList.add(data.productId)
                }
            }

            return productIdList
        }

    val allAvailableCartItemHolderData: List<CartItemHolderData>
        get() {
            val cartItemDataList = ArrayList<CartItemHolderData>()
            for (data in cartDataList) {
                if (data is CartShopHolderData) {
                    data.productUiModelList.let {
                        cartItemDataList.addAll(it)
                    }
                }
            }

            return cartItemDataList
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
            is CartChooseAddressHolderData -> CartChooseAddressViewHolder.LAYOUT
            is CartShopHolderData -> CartShopViewHolder.LAYOUT
            is CartItemTickerErrorHolderData -> CartTickerErrorViewHolder.TYPE_VIEW_TICKER_CART_ERROR
            is ShipmentSellerCashbackModel -> ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK
            is CartEmptyHolderData -> CartEmptyViewHolder.LAYOUT
            is CartRecentViewHolderData -> CartRecentViewViewHolder.LAYOUT
            is CartWishlistHolderData -> CartWishlistViewHolder.LAYOUT
            is CartSectionHeaderHolderData -> CartSectionHeaderViewHolder.LAYOUT
            is CartTopAdsHeadlineData -> CartTopAdsHeadlineViewHolder.LAYOUT
            is CartRecommendationItemHolderData -> CartRecommendationViewHolder.LAYOUT
            is CartLoadingHolderData -> CartLoadingViewHolder.LAYOUT
            is TickerAnnouncementHolderData -> TickerAnnouncementViewHolder.LAYOUT
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
                val binding = ItemSelectAllBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return CartSelectAllViewHolder(binding, actionListener, compositeSubscription)
            }
            CartChooseAddressViewHolder.LAYOUT -> {
                val binding = ItemCartChooseAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return CartChooseAddressViewHolder(binding, actionListener)
            }
            CartShopViewHolder.LAYOUT -> {
                val binding = ItemShopBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return CartShopViewHolder(binding, actionListener, cartItemActionListener, compositeSubscription)
            }
            CartTickerErrorViewHolder.TYPE_VIEW_TICKER_CART_ERROR -> {
                val binding = HolderItemCartTickerErrorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return CartTickerErrorViewHolder(binding, actionListener)
            }
            ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK, parent, false)
                return ShipmentSellerCashbackViewHolder(view, sellerCashbackListener)
            }
            CartEmptyViewHolder.LAYOUT -> {
                val binding = ItemEmptyCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return CartEmptyViewHolder(binding, actionListener)
            }
            CartRecentViewViewHolder.LAYOUT -> {
                val binding = ItemCartRecentViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return CartRecentViewViewHolder(binding, actionListener)
            }
            CartWishlistViewHolder.LAYOUT -> {
                val binding = ItemCartWishlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return CartWishlistViewHolder(binding, actionListener)
            }
            CartSectionHeaderViewHolder.LAYOUT -> {
                val binding = ItemCartSectionHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return CartSectionHeaderViewHolder(binding, actionListener)
            }
            CartTopAdsHeadlineViewHolder.LAYOUT -> {
                val binding = ItemCartTopAdsHeadlineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return CartTopAdsHeadlineViewHolder(binding, actionListener, userSession)
            }
            CartRecommendationViewHolder.LAYOUT -> {
                val binding = ItemCartRecommendationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return CartRecommendationViewHolder(binding, actionListener)
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
            DisabledCartItemViewHolder.LAYOUT -> {
                val binding = HolderItemCartErrorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return DisabledCartItemViewHolder(binding, actionListener)
            }
            DisabledItemHeaderViewHolder.LAYOUT -> {
                val binding = ItemCartDisabledHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return DisabledItemHeaderViewHolder(binding, actionListener)
            }
            DisabledReasonViewHolder.LAYOUT -> {
                val binding = ItemCartDisabledReasonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return DisabledReasonViewHolder(binding, actionListener)
            }
            DisabledShopViewHolder.LAYOUT -> {
                val binding = ItemCartDisabledShopBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return DisabledShopViewHolder(binding, actionListener)
            }
            DisabledAccordionViewHolder.LAYOUT -> {
                val binding = ItemCartDisabledAccordionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return DisabledAccordionViewHolder(binding, actionListener)
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
        when (viewType) {
            CartSelectAllViewHolder.LAYOUT -> {
                val data = cartDataList[position] as CartSelectAllHolderData
                (holder as CartSelectAllViewHolder).bind(data)
            }
            CartChooseAddressViewHolder.LAYOUT -> {
                val data = cartDataList[position] as CartChooseAddressHolderData
                (holder as CartChooseAddressViewHolder).bind(data)
            }
            CartShopViewHolder.LAYOUT -> {
                val data = cartDataList[position] as CartShopHolderData
                if (data.isNeedToRefreshWeight) {
                    (holder as CartShopViewHolder).bindUpdatedWeight(data)
                } else {
                    (holder as CartShopViewHolder).bindData(data)
                }
            }
            CartTickerErrorViewHolder.TYPE_VIEW_TICKER_CART_ERROR -> {
                val data = cartDataList[position] as CartItemTickerErrorHolderData
                (holder as CartTickerErrorViewHolder).bindData(data)
            }
            ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK -> {
                val data = cartDataList[position] as ShipmentSellerCashbackModel
                (holder as ShipmentSellerCashbackViewHolder).bindViewHolder(data)
            }
            CartEmptyViewHolder.LAYOUT -> {
                val data = cartDataList[position] as CartEmptyHolderData
                (holder as CartEmptyViewHolder).bind(data)
            }
            CartRecentViewViewHolder.LAYOUT -> {
                val data = cartDataList[position] as CartRecentViewHolderData
                (holder as CartRecentViewViewHolder).bind(data)
                cartRecentViewAdapter = holder.recentViewAdapter
            }
            CartWishlistViewHolder.LAYOUT -> {
                val data = cartDataList[position] as CartWishlistHolderData
                (holder as CartWishlistViewHolder).bind(data)
                cartWishlistAdapter = holder.wishlistAdapter
            }
            CartSectionHeaderViewHolder.LAYOUT -> {
                val data = cartDataList[position] as CartSectionHeaderHolderData
                (holder as CartSectionHeaderViewHolder).bind(data)
            }
            CartTopAdsHeadlineViewHolder.LAYOUT -> {
                val data = cartDataList[position] as CartTopAdsHeadlineData
                (holder as CartTopAdsHeadlineViewHolder).bind(data)
            }
            CartRecommendationViewHolder.LAYOUT -> {
                val data = cartDataList[position] as CartRecommendationItemHolderData
                (holder as CartRecommendationViewHolder).bind(data)
            }
            CartLoadingViewHolder.LAYOUT -> {
                val data = cartDataList[position] as CartLoadingHolderData
                (holder as CartLoadingViewHolder).bind(data)
            }
            TickerAnnouncementViewHolder.LAYOUT -> {
                val cartTickerData = cartDataList[position] as TickerAnnouncementHolderData
                (holder as TickerAnnouncementViewHolder).bind(cartTickerData)
            }
            DisabledItemHeaderViewHolder.LAYOUT -> {
                val data = cartDataList[position] as DisabledItemHeaderHolderData
                (holder as DisabledItemHeaderViewHolder).bind(data)
            }
            DisabledReasonViewHolder.LAYOUT -> {
                val data = cartDataList[position] as DisabledReasonHolderData
                (holder as DisabledReasonViewHolder).bind(data)
            }
            DisabledShopViewHolder.LAYOUT -> {
                val data = cartDataList[position] as DisabledShopHolderData
                (holder as DisabledShopViewHolder).bind(data)
            }
            DisabledCartItemViewHolder.LAYOUT -> {
                val data = cartDataList[position] as DisabledCartItemHolderData
                (holder as DisabledCartItemViewHolder).bind(data)
            }
            DisabledAccordionViewHolder.LAYOUT -> {
                val data = cartDataList[position] as DisabledAccordionHolderData
                (holder as DisabledAccordionViewHolder).bind(data)
            }
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

    fun addItem(any: Any) {
        cartDataList.add(any)
    }

    fun addItem(index: Int, any: Any) {
        cartDataList.add(index, any)
    }

    fun addItems(anyList: List<Any>) {
        cartDataList.addAll(anyList)
    }

    fun addItems(index: Int = -1, anyList: List<Any>) {
        cartDataList.addAll(index, anyList)
    }

    fun addNotAvailableShop(disabledShopHolderData: DisabledShopHolderData) {
        var showDivider = false
        if (cartDataList.size > 0 && cartDataList[cartDataList.size - 1] !is DisabledReasonHolderData) {
            showDivider = true
        }
        disabledShopHolderData.showDivider = showDivider
        cartDataList.add(disabledShopHolderData)
    }

    fun addCartWishlistData(cartSectionHeaderHolderData: CartSectionHeaderHolderData,
                            cartWishlistHolderData: CartWishlistHolderData) {
        var wishlistIndex = 0
        for ((index, item) in cartDataList.withIndex()) {
            if (item is CartEmptyHolderData ||
                    item is CartShopHolderData ||
                    item is ShipmentSellerCashbackModel ||
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

    fun addCartRecentViewData(cartSectionHeaderHolderData: CartSectionHeaderHolderData,
                              cartRecentViewHolderData: CartRecentViewHolderData) {
        var recentViewIndex = 0
        for ((index, item) in cartDataList.withIndex()) {
            if (item is CartEmptyHolderData ||
                    item is CartShopHolderData ||
                    item is ShipmentSellerCashbackModel ||
                    item is CartWishlistHolderData ||
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

    fun addCartTopAdsHeadlineData(cartSectionHeaderHolderData: CartSectionHeaderHolderData?, recommendationPage: Int) {
        if (recommendationPage == 1) {
            var recommendationIndex = 0
            for ((index, item) in cartDataList.withIndex()) {
                if (item is CartEmptyHolderData ||
                        item is CartShopHolderData ||
                        item is ShipmentSellerCashbackModel ||
                        item is DisabledItemHeaderHolderData ||
                        item is DisabledReasonHolderData ||
                        item is DisabledShopHolderData ||
                        item is DisabledCartItemHolderData ||
                        item is DisabledAccordionHolderData ||
                        item is CartRecentViewHolderData ||
                        item is CartWishlistHolderData) {
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

            addCartTopAdsHeadlineData(++recommendationIndex)
            notifyItemRangeInserted(recommendationIndex, 2)
        }
    }

    fun addCartRecommendationData(cartSectionHeaderHolderData: CartSectionHeaderHolderData?,
                                  cartRecommendationItemHolderDataList: List<CartRecommendationItemHolderData>,
                                  recommendationPage: Int) {
        var recommendationIndex = 0
        for ((index, item) in cartDataList.withIndex()) {
            if (item is CartEmptyHolderData ||
                    item is CartShopHolderData ||
                    item is ShipmentSellerCashbackModel ||
                    item is DisabledItemHeaderHolderData ||
                    item is DisabledReasonHolderData ||
                    item is DisabledShopHolderData ||
                    item is DisabledCartItemHolderData ||
                    item is DisabledAccordionHolderData ||
                    item is CartRecentViewHolderData ||
                    item is CartWishlistHolderData ||
                    item is CartTopAdsHeadlineData ||
                    item is CartRecommendationItemHolderData) {
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

        if (recommendationPage == 1) {
            addCartTopAdsHeadlineData(++recommendationIndex)
        }
        cartDataList.addAll(++recommendationIndex, cartRecommendationItemHolderDataList)
        notifyItemRangeInserted(recommendationIndex, cartRecommendationItemHolderDataList.size)
    }

    private fun addCartTopAdsHeadlineData(index: Int) {
        val cartTopAdsHeadlineData = CartTopAdsHeadlineData()
        this.cartTopAdsHeadlineData = cartTopAdsHeadlineData
        cartDataList.add(index, cartTopAdsHeadlineData)
    }

    fun setShopSelected(position: Int, selected: Boolean) {
        val any = cartDataList[position]
        if (any is CartShopHolderData) {
            any.isAllSelected = selected
            any.productUiModelList.let {
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
            any.productUiModelList.let {
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

    fun resetQuantity(position: Int, parentPosition: Int) {
        if (getItemViewType(parentPosition) == CartShopViewHolder.LAYOUT) {
            (cartDataList[parentPosition] as CartShopHolderData)?.productUiModelList?.get(position)?.quantity = 0
        }
        checkForShipmentForm()
    }

    fun resetData() {
        cartDataList.clear()
        firstCartSectionHeaderPosition = -1
        notifyDataSetChanged()
        checkForShipmentForm()
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

    fun removeChooseAddressWidget(): Int {
        var index = -1
        cartDataList.forEachIndexed { i, data ->
            if (data is CartChooseAddressHolderData) {
                index = i
            }
        }

        if (index != -1) {
            cartDataList.removeAt(index)
        }

        return index
    }

    fun checkForShipmentForm() {
        var hasCheckedAvailableItem = false
        loop@ for (any in cartDataList) {
            if (hasCheckedAvailableItem) break@loop
            if (any is CartShopHolderData) {
                if (any.isAllSelected) {
                    hasCheckedAvailableItem = true
                } else if (any.isPartialSelected) {
                    any.productUiModelList.let {
                        innerLoop@ for (cartItemHolderData in it) {
                            if (cartItemHolderData.isSelected) {
                                hasCheckedAvailableItem = true
                                break@innerLoop
                            }
                        }
                    }
                }
            }
        }

        if (hasCheckedAvailableItem) {
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
                    if (index != -1) {
                        notifyItemChanged(index)
                    }
                }
            }
        } else {
            shipmentSellerCashbackModel?.let {
                val index = cartDataList.indexOf(it)
                if (index != -1) {
                    cartDataList.remove(it)
                    notifyItemRemoved(index)
                    shipmentSellerCashbackModel = null
                }
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
                cartShopHolderData.productUiModelList.let {
                    for (cartItemHolderData in it) {
                        if (cartItemHolderData.productId == productId) {
                            cartItemHolderData.isWishlisted = isWishlisted
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

    fun getCartShopHolderIndexByCartId(cartId: String): Int {
        loop@ for ((index, any) in cartDataList.withIndex()) {
            if (any is CartShopHolderData) {
                any.productUiModelList.let { cartItemHolderDataList ->
                    innerLoop@ for (cartItemHolderData in cartItemHolderDataList) {
                        if (cartItemHolderData.cartId == cartId) {
                            return index
                        }
                    }
                }
            }
        }

        return RecyclerView.NO_POSITION
    }

    fun getCartShopHolderIndexByCartString(cartString: String): Int {
        loop@ for ((index, any) in cartDataList.withIndex()) {
            if (any is CartShopHolderData && any.cartString.equals(cartString)) {
                return index
            }
        }

        return RecyclerView.NO_POSITION
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
                if (headerIndex > -1) {
                    cartDataList.removeAt(headerIndex)
                    notifyItemRangeRemoved(headerIndex, 2)
                }
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
            if (index != -1) {
                cartDataList.remove(it)
                notifyItemRemoved(index)
            }
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
        // Todo : refactor delete mechanism
/*
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
                    obj.shopGroupAvailableData?.cartItemDataList?.let {
                        for (cartItemHolderData in it) {
                            cartItemHolderData.cartItemData.originData.let { data ->
                                if (cartIds.contains(data.cartId)) {
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
                is DisabledCartItemHolderData -> if (cartIds.contains(obj.cartId)) {
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
            tmpCollapsedUnavailableItem.clear()
        }

        // Determine to remove error ticker and unavailable item header
        if (cartItemTickerErrorHolderData != null || disabledItemHeaderHolderData != null) {
            // Count available item and unavailable items
            var errorItemCount = 0
            var normalItemCount = 0
            loop@ for (any in cartDataList) {
                when (any) {
                    is CartShopHolderData -> any.shopGroupAvailableData?.cartItemDataList?.let {
                        for (cartItemHolderData in it) {
                            normalItemCount++
                        }
                    }
                    is DisabledCartItemHolderData -> errorItemCount++
                    is CartRecentViewHolderData, is CartWishlistHolderData, is CartRecommendationItemHolderData -> break@loop
                }
            }

            errorItemCount += collapsedUnavailableCartItemData.size

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
                            it.cartTickerErrorData.errorInfo = String.format(context.getString(R.string.cart_error_message), errorItemCount)
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
*/
        return Pair(ArrayList(), ArrayList())
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

        this.tmpCollapsedUnavailableItem = tmpCollapsedItem

        cartDataList.removeAll(tmpCollapsedItem)
        notifyItemRangeRemoved(firstIndex, tmpCollapsedItem.size)
    }

    fun expandDisabledItems() {
        if (tmpCollapsedUnavailableItem.isNotEmpty()) {
            var headerIndex = 0
            for ((index, item) in cartDataList.withIndex()) {
                if (item is DisabledCartItemHolderData) {
                    headerIndex = index
                    break
                }
            }
            cartDataList.addAll(++headerIndex, tmpCollapsedUnavailableItem)
            notifyItemRangeInserted(headerIndex, tmpCollapsedUnavailableItem.size)
            tmpCollapsedUnavailableItem.clear()
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
                    any.productUiModelList.forEach {
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
                        any.productUiModelList.forEach {
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
                    if (it.productUiModelList.isNotEmpty()) {
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
                    data.productUiModelList.forEach {
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

    fun isAllAvailableItemCheked(): Boolean {
        getData().forEach {
            when (it) {
                is CartShopHolderData -> {
                    it.productUiModelList.forEach {
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
                    it.productUiModelList.forEach {
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

}
