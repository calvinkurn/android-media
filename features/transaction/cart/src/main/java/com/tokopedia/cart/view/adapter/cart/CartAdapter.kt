package com.tokopedia.cart.view.adapter.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.collection.ArraySet
import androidx.recyclerview.widget.RecyclerView
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

    companion object {
        const val SELLER_CASHBACK_ACTION_INSERT = 1
        const val SELLER_CASHBACK_ACTION_UPDATE = 2
        const val SELLER_CASHBACK_ACTION_DELETE = 3
    }

    private val cartDataList = ArrayList<Any>()
    private var compositeSubscription = CompositeSubscription()

    private var shipmentSellerCashbackModel: ShipmentSellerCashbackModel? = null
    private var cartEmptyHolderData: CartEmptyHolderData? = null
    private var cartLoadingHolderData: CartLoadingHolderData? = null
    private var cartWishlistAdapter: CartWishlistAdapter? = null
    private var cartRecentViewAdapter: CartRecentViewAdapter? = null
    private var cartTopAdsHeadlineData: CartTopAdsHeadlineData? = null
    private var tmpAllUnavailableShop: MutableList<Any>? = null
    private var tmpCollapsedUnavailableShop: MutableList<CartShopHolderData>? = null

    var firstCartSectionHeaderPosition: Int = -1

    val allShopGroupDataList: List<CartShopHolderData>
        get() {
            val shopGroupList = mutableListOf<CartShopHolderData>()
            loop@ for (data in cartDataList) {
                when (data) {
                    is CartShopHolderData -> {
                        shopGroupList.add(data)
                    }
                    hasReachAllShopItems(data) -> break@loop
                }
            }
            return shopGroupList
        }

    val allAvailableShopGroupDataList: List<CartShopHolderData>
        get() {
            val availableShopGroupList = mutableListOf<CartShopHolderData>()
            loop@ for (data in cartDataList) {
                when (data) {
                    is CartShopHolderData -> {
                        if (!data.isError) {
                            availableShopGroupList.add(data)
                        }
                    }
                    hasReachAllShopItems(data) -> break@loop
                }
            }
            return availableShopGroupList
        }

    val allUnavailableShopGroupDataList: List<CartShopHolderData>
        get() {
            val availableShopGroupList = mutableListOf<CartShopHolderData>()
            loop@ for (data in cartDataList) {
                when (data) {
                    is CartShopHolderData -> {
                        if (data.isError) {
                            availableShopGroupList.add(data)
                        }
                    }
                    hasReachAllShopItems(data) -> break@loop
                }
            }
            return availableShopGroupList
        }

    val selectedCartItemData: List<CartItemHolderData>
        get() {
            val cartItemDataList = ArrayList<CartItemHolderData>()
            loop@ for (data in cartDataList) {
                when (data) {
                    is CartShopHolderData -> {
                        if ((data.isPartialSelected || data.isAllSelected)) {
                            for (cartItemHolderData in data.productUiModelList) {
                                if (cartItemHolderData.isSelected && !cartItemHolderData.isError) {
                                    cartItemHolderData.shopBoMetadata = data.boMetadata
                                    cartItemHolderData.shopBoAffordabilityData = data.boAffordability
                                    cartItemDataList.add(cartItemHolderData)
                                }
                            }
                        }
                    }
                    hasReachAllShopItems(data) -> break@loop
                }
            }

            return cartItemDataList
        }

    val selectedCartShopHolderData: List<CartShopHolderData>
        get() {
            val cartShopHolderDataList = ArrayList<CartShopHolderData>()
            loop@ for (data in cartDataList) {
                when (data) {
                    is CartShopHolderData -> {
                        if (data.isPartialSelected || data.isAllSelected) {
                            cartShopHolderDataList.add(data)
                        }
                    }
                    hasReachAllShopItems(data) -> break@loop
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
                        for (cartItemHolderData in cartItemHolderDataList) {
                            cartItemHolderData.shopBoMetadata = data.boMetadata
                            cartItemHolderData.shopBoAffordabilityData = data.boAffordability
                            cartItemDataList.add(cartItemHolderData)
                        }
                    }
                    hasReachAllShopItems(data) -> break@loop
                }
            }

            if (tmpAllUnavailableShop?.isNotEmpty() == true) {
                cartItemDataList.addAll(collapsedUnavailableCartItemData)
            }

            return cartItemDataList
        }

    val allAvailableCartItemData: List<CartItemHolderData>
        get() {
            val cartItemDataList = ArrayList<CartItemHolderData>()
            loop@ for (data in cartDataList) {
                when (data) {
                    is CartShopHolderData -> {
                        if (!data.isError) {
                            val cartItemHolderDataList = data.productUiModelList
                            for (cartItemHolderData in cartItemHolderDataList) {
                                cartItemHolderData.shopBoMetadata = data.boMetadata
                                cartItemHolderData.shopBoAffordabilityData = data.boAffordability
                                cartItemDataList.add(cartItemHolderData)
                            }
                        }
                    }
                    hasReachAllShopItems(data) -> break@loop
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
                    hasReachAllShopItems(data) -> break@loop
                }
            }

            if (tmpAllUnavailableShop?.isNotEmpty() == true) {
                cartItemDataList.addAll(collapsedUnavailableCartItemData)
            }

            return cartItemDataList
        }

    private val collapsedUnavailableCartItemData: List<CartItemHolderData>
        get() {
            val cartItemDataList = mutableListOf<CartItemHolderData>()
            tmpAllUnavailableShop?.let {
                loop@ for (data in it) {
                    when (data) {
                        is CartShopHolderData -> {
                            cartItemDataList.addAll(data.productUiModelList)
                        }
                    }
                }
            }
            return cartItemDataList
        }

    val allCartItemProductId: List<String>
        get() {
            val productIdList = ArrayList<String>()
            loop@ for (data in cartDataList) {
                when (data) {
                    is CartShopHolderData -> {
                        data.productUiModelList.let {
                            for (cartItemHolderData in it) {
                                productIdList.add(cartItemHolderData.productId)
                            }
                        }
                    }
                    hasReachAllShopItems(data) -> break@loop
                }
            }

            return productIdList
        }

    val allAvailableCartItemHolderData: List<CartItemHolderData>
        get() {
            val cartItemDataList = ArrayList<CartItemHolderData>()
            loop@ for (data in cartDataList) {
                when (data) {
                    is CartShopHolderData -> {
                        data.productUiModelList.let {
                            cartItemDataList.addAll(it)
                        }
                    }
                    hasReachAllShopItems(data) -> break@loop
                }
            }

            return cartItemDataList
        }

    val allCartShopHolderData: List<CartShopHolderData>
        get() {
            val cartShopHolderDataList = mutableListOf<CartShopHolderData>()
            loop@ for (data in cartDataList) {
                when (data) {
                    is CartShopHolderData -> {
                        cartShopHolderDataList.add(data)
                    }
                    hasReachAllShopItems(data) -> break@loop
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
            is DisabledItemHeaderHolderData -> DisabledItemHeaderViewHolder.LAYOUT
            is DisabledReasonHolderData -> DisabledReasonViewHolder.LAYOUT
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
            DisabledItemHeaderViewHolder.LAYOUT -> {
                val binding = ItemCartDisabledHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return DisabledItemHeaderViewHolder(binding, actionListener)
            }
            DisabledReasonViewHolder.LAYOUT -> {
                val binding = ItemCartDisabledReasonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return DisabledReasonViewHolder(binding, actionListener)
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

    fun addCartWishlistData(cartSectionHeaderHolderData: CartSectionHeaderHolderData,
                            cartWishlistHolderData: CartWishlistHolderData) {
        var wishlistIndex = 0
        for ((index, item) in cartDataList.withIndex()) {
            if (item is CartEmptyHolderData ||
                    item is CartShopHolderData ||
                    item is ShipmentSellerCashbackModel ||
                    item is DisabledItemHeaderHolderData ||
                    item is DisabledReasonHolderData ||
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
        val cartProductIds = mutableListOf<String>()
        loop@ for (item in cartDataList) {
            when (item) {
                is CartShopHolderData -> {
                    item.productUiModelList.forEach { cartItem ->
                        cartProductIds.add(cartItem.productId)
                    }
                }
                is CartRecentViewHolderData, is CartWishlistHolderData, is CartRecommendationItemHolderData -> {
                    break@loop
                }
            }
        }
        val cartTopAdsHeadlineData = CartTopAdsHeadlineData(cartProductIds = cartProductIds)
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

    fun setItemSelected(position: Int, cartItemHolderData: CartItemHolderData) {
        val cartShopHolderData = getCartShopHolderDataByCartItemHolderData(cartItemHolderData)
        cartShopHolderData?.let {
            cartShopHolderData.productUiModelList.forEachIndexed { index, cartItemHolderData ->
                if (index == position) {
                    cartItemHolderData.isSelected = !cartItemHolderData.isSelected
                }
            }

            var selectedCount = 0
            cartShopHolderData.productUiModelList.forEach {
                if (it.isSelected) {
                    selectedCount++
                }
            }

            if (selectedCount == 0) {
                cartShopHolderData.isAllSelected = false
                cartShopHolderData.isPartialSelected = false
            } else if (selectedCount > 0 && selectedCount < cartShopHolderData.productUiModelList.size) {
                cartShopHolderData.isAllSelected = false
                cartShopHolderData.isPartialSelected = true
            } else {
                cartShopHolderData.isAllSelected = true
                cartShopHolderData.isPartialSelected = false
            }
        }
    }

    fun resetQuantity(position: Int, cartItemHolderData: CartItemHolderData) {
        val cartShopHolderData = getCartShopHolderDataByCartItemHolderData(cartItemHolderData)
        cartShopHolderData?.productUiModelList?.let {
            if (position < it.size) {
                it[position].quantity = 0
                checkForShipmentForm()
            }
        }
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

    fun updateShipmentSellerCashback(cashback: Double): Pair<Int, Int>? {
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
                    return Pair(SELLER_CASHBACK_ACTION_INSERT, index)
                }
            } else {
                shipmentSellerCashbackModel?.let {
                    it.sellerCashback = cashback.toInt()
                    it.isVisible = true
                    it.sellerCashbackFmt = CurrencyFormatUtil.convertPriceValueToIdrFormat(cashback.toLong(), false).removeDecimalSuffix()
                    val index = cartDataList.indexOf(it)
                    return Pair(SELLER_CASHBACK_ACTION_UPDATE, index)
                }
            }
        } else {
            shipmentSellerCashbackModel?.let {
                val index = cartDataList.indexOf(it)
                cartDataList.remove(it)
                shipmentSellerCashbackModel = null
                return Pair(SELLER_CASHBACK_ACTION_DELETE, index)
            }
        }

        return null
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
            }
        }
    }

    fun getCartItemByBundleId(bundleId: String): List<CartItemHolderData> {
        val cartItemHolderDataList = mutableListOf<CartItemHolderData>()
        loop@ for (data in cartDataList) {
            if (cartItemHolderDataList.isNotEmpty()) {
                break@loop
            }
            when (data) {
                is CartShopHolderData -> {
                    data.productUiModelList.forEach { cartItemHolderData ->
                        if (cartItemHolderData.isBundlingItem && cartItemHolderData.bundleId == bundleId) {
                            cartItemHolderDataList.add(cartItemHolderData)
                        }
                    }
                }
                hasReachAllShopItems(data) -> break@loop
            }
        }

        return cartItemHolderDataList
    }

    fun getCartShopHolderDataByIndex(index: Int): CartShopHolderData? {
        return if (cartDataList[index] is CartShopHolderData) {
            cartDataList[index] as CartShopHolderData
        } else null
    }

    fun getCartShopHolderDataByCartItemHolderData(cartItemHolderData: CartItemHolderData): CartShopHolderData? {
        loop@ for (data in cartDataList) {
            if (data is CartShopHolderData && data.cartString == cartItemHolderData.cartString && data.isError == cartItemHolderData.isError) {
                return data
            }
        }

        return null
    }

    fun getCartShopHolderDataAndIndexByCartString(cartString: String): Pair<CartShopHolderData?, Int> {
        loop@ for ((index, data) in cartDataList.withIndex()) {
            if (data is CartShopHolderData && data.cartString == cartString) {
                return Pair(data, index)
            }
        }

        return Pair(null, RecyclerView.NO_POSITION)
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

    private fun hasReachAllShopItems(data: Any): Boolean {
        return data is CartRecentViewHolderData ||
                data is CartWishlistHolderData ||
                data is CartTopAdsHeadlineData ||
                data is CartRecommendationItemHolderData
    }

    fun removeProductByCartId(cartIds: List<String>, needRefresh: Boolean, isFromGlobalCheckbox: Boolean): Pair<List<Int>, List<Int>> {
        val toBeRemovedItems = mutableListOf<Any>()
        val toBeRemovedIndices = mutableListOf<Int>()
        val toBeUpdatedIndices = mutableListOf<Int>()

        var cartSelectAllHolderDataIndexPair: Pair<CartSelectAllHolderData, Int>? = null
        var cartItemTickerErrorHolderDataIndexPair: Pair<CartItemTickerErrorHolderData, Int>? = null
        var disabledItemHeaderHolderDataIndexPair: Pair<DisabledItemHeaderHolderData, Int>? = null
        var disabledAccordionHolderDataIndexPair: Pair<DisabledAccordionHolderData, Int>? = null
        loop@ for ((index, data) in cartDataList.withIndex()) {
            when {
                data is CartSelectAllHolderData -> cartSelectAllHolderDataIndexPair = Pair(data, index)
                data is CartItemTickerErrorHolderData -> cartItemTickerErrorHolderDataIndexPair = Pair(data, index)
                data is DisabledItemHeaderHolderData -> disabledItemHeaderHolderDataIndexPair = Pair(data, index)
                data is DisabledAccordionHolderData -> disabledAccordionHolderDataIndexPair = Pair(data, index)
                data is CartShopHolderData -> {
                    val toBeDeletedProducts = mutableListOf<CartItemHolderData>()
                    var hasSelectDeletedProducts = false
                    data.productUiModelList.forEach { cartItemHolderData ->
                        if (cartIds.contains(cartItemHolderData.cartId)) {
                            toBeDeletedProducts.add(cartItemHolderData)
                            if (cartItemHolderData.isSelected) {
                                hasSelectDeletedProducts = true
                            }
                        }
                    }
                    if (toBeDeletedProducts.isNotEmpty()) {
                        data.productUiModelList.removeAll(toBeDeletedProducts)
                        if (data.productUiModelList.isEmpty()) {
                            val previousIndex = index - 1
                            if (data.isError && previousIndex < cartDataList.size) {
                                val previousData = cartDataList[previousIndex]
                                if (previousData is DisabledReasonHolderData) {
                                    toBeRemovedItems.add(previousData)
                                    toBeRemovedIndices.add(previousIndex)
                                }
                            }
                            toBeRemovedItems.add(data)
                            toBeRemovedIndices.add(index)
                        } else {
                            if (!needRefresh && (isFromGlobalCheckbox || hasSelectDeletedProducts)) {
                                actionListener.checkBoAffordability(data)
                            }
                            toBeUpdatedIndices.add(index)
                        }
                    }
                }
                hasReachAllShopItems(data) -> break@loop
            }
        }

        cartDataList.removeAll(toBeRemovedItems)

        if (allAvailableCartItemData.isEmpty()) {
            cartSelectAllHolderDataIndexPair?.let {
                cartDataList.remove(it.first)
                toBeRemovedItems.add(it.second)
            }
        }

        val disabledCartItems = allDisabledCartItemData
        if (disabledCartItems.isEmpty()) {
            cartItemTickerErrorHolderDataIndexPair?.let {
                cartDataList.remove(it.first)
                toBeRemovedItems.add(it.second)
                toBeRemovedIndices.add(it.second)
            }
            disabledItemHeaderHolderDataIndexPair?.let {
                cartDataList.remove(it.first)
                toBeRemovedItems.add(it.second)
                toBeRemovedIndices.add(it.second)
            }
            disabledAccordionHolderDataIndexPair?.let {
                cartDataList.remove(it.first)
                toBeRemovedItems.add(it.second)
                toBeRemovedIndices.add(it.second)
            }
        } else {
            val errorItemCount = disabledCartItems.size
            cartItemTickerErrorHolderDataIndexPair?.let {
                it.first.errorProductCount = errorItemCount
                toBeUpdatedIndices.add(it.second)
            }
            disabledItemHeaderHolderDataIndexPair?.let {
                it.first.disabledItemCount = errorItemCount
                toBeUpdatedIndices.add(it.second)
            }

            var removeAccordion = false
            if (errorItemCount == 1) {
                removeAccordion = true
            } else {
                val bundleIdCartIdSet = mutableSetOf<String>()
                disabledCartItems.forEach {
                    if (it.isBundlingItem) {
                        bundleIdCartIdSet.add(it.bundleId)
                    } else {
                        bundleIdCartIdSet.add(it.cartId)
                    }
                }
                if (bundleIdCartIdSet.size <= 1) {
                    removeAccordion = true
                }
            }

            if (removeAccordion) {
                disabledAccordionHolderDataIndexPair?.let {
                    cartDataList.remove(it.first)
                    toBeRemovedItems.add(it.second)
                    toBeRemovedIndices.add(it.second)
                }
            }
        }
        return Pair(toBeRemovedIndices, toBeUpdatedIndices)
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

    private fun getNonCollapsibleUnavailableProduct(shop: CartShopHolderData): List<CartItemHolderData> {
        val nonCollapsibleProducts = mutableListOf<CartItemHolderData>()
        var previousBundlingId = ""
        var previousBundlingGroupId = ""
        loop@ for (product in shop.productUiModelList) {
            if (product.isBundlingItem) {
                if (nonCollapsibleProducts.isNotEmpty() && nonCollapsibleProducts.firstOrNull()?.isBundlingItem == false) {
                    break@loop
                } else {
                    if (previousBundlingId.isBlank() && previousBundlingGroupId.isBlank()) {
                        previousBundlingId = product.bundleId
                        previousBundlingGroupId = product.bundleGroupId
                    }
                    if (product.bundleId == previousBundlingId && product.bundleGroupId == previousBundlingGroupId) {
                        nonCollapsibleProducts.add(product)
                    }
                }
            } else {
                if (nonCollapsibleProducts.isEmpty()) {
                    nonCollapsibleProducts.add(product)
                } else {
                    break@loop
                }
            }
        }

        return nonCollapsibleProducts
    }

    fun collapseDisabledItems() {
        var firstIndex = RecyclerView.NO_POSITION
        var lastIndex = 0
        for ((index, item) in cartDataList.withIndex()) {
            if (firstIndex == RecyclerView.NO_POSITION && item is CartShopHolderData && item.isError) {
                firstIndex = index
            } else if (item is DisabledAccordionHolderData) {
                lastIndex = index
                break
            }
        }
        if (firstIndex > RecyclerView.NO_POSITION && lastIndex >= firstIndex && lastIndex <= cartDataList.size) {
            val tmpAllUnavailableShop = cartDataList.subList(firstIndex, lastIndex).toMutableList()
            this.tmpAllUnavailableShop = tmpAllUnavailableShop

            val tmpCollapsedUnavailableShop = mutableListOf<CartShopHolderData>()
            val firstUnavailableShop = tmpAllUnavailableShop.firstOrNull()
            firstUnavailableShop?.let { shop ->
                if (shop is CartShopHolderData) {
                    val tmpProducts = getNonCollapsibleUnavailableProduct(shop)
                    val cartShopHolderData = shop.copy()
                    cartShopHolderData.productUiModelList.clear()
                    cartShopHolderData.productUiModelList.addAll(tmpProducts)

                    tmpCollapsedUnavailableShop.add(cartShopHolderData)
                    this.tmpCollapsedUnavailableShop = tmpCollapsedUnavailableShop

                    cartDataList.removeAll(tmpAllUnavailableShop)
                    cartDataList.addAll(firstIndex, tmpCollapsedUnavailableShop)

                    notifyItemRangeRemoved(firstIndex, tmpAllUnavailableShop.size)
                    notifyItemInserted(firstIndex)
                }
            }
        }
    }

    fun expandDisabledItems() {
        tmpCollapsedUnavailableShop?.let { collapsedUnavailableItems ->
            if (collapsedUnavailableItems.isNotEmpty()) {
                val firstIndex = cartDataList.indexOf(collapsedUnavailableItems.first())
                if (firstIndex != -1) {
                    cartDataList.removeAll(collapsedUnavailableItems)
                    collapsedUnavailableItems.clear()

                    tmpAllUnavailableShop?.let { unavailableItems ->
                        cartDataList.addAll(firstIndex, unavailableItems)
                        unavailableItems.clear()
                        notifyItemRemoved(firstIndex)
                        notifyItemRangeInserted(firstIndex, unavailableItems.size)
                    }
                }
            }
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

    fun iterateAllAvailableShop(func: (CartShopHolderData) -> Unit): Pair<Int, Int> {
        var firstIndex = 0
        var count = 0
        cartDataList.forEachIndexed { index, any ->
            when (any) {
                is CartShopHolderData -> {
                    count++
                    if (firstIndex == 0) {
                        firstIndex = index
                    }
                    func(any)
                }
                is DisabledItemHeaderHolderData, is ShipmentSellerCashbackModel, is CartSectionHeaderHolderData -> {
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
                        actionListener.checkBoAffordability(data)
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
