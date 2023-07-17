package com.tokopedia.cartrevamp.view.adapter.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.databinding.HolderItemCartTickerErrorBinding
import com.tokopedia.cart.databinding.ItemCartChooseAddressBinding
import com.tokopedia.cart.databinding.ItemCartDisabledAccordionBinding
import com.tokopedia.cart.databinding.ItemCartDisabledHeaderBinding
import com.tokopedia.cart.databinding.ItemCartDisabledReasonBinding
import com.tokopedia.cart.databinding.ItemCartProductRevampBinding
import com.tokopedia.cart.databinding.ItemCartRecentViewBinding
import com.tokopedia.cart.databinding.ItemCartRecommendationBinding
import com.tokopedia.cart.databinding.ItemCartSectionHeaderBinding
import com.tokopedia.cart.databinding.ItemCartShopBottomBinding
import com.tokopedia.cart.databinding.ItemCartTopAdsHeadlineBinding
import com.tokopedia.cart.databinding.ItemCartWishlistBinding
import com.tokopedia.cart.databinding.ItemEmptyCartBinding
import com.tokopedia.cart.databinding.ItemGroupRevampBinding
import com.tokopedia.cart.databinding.ItemSelectedAmountBinding
import com.tokopedia.cartrevamp.view.ActionListener
import com.tokopedia.cartrevamp.view.adapter.diffutil.CartDiffUtilCallback
import com.tokopedia.cartrevamp.view.adapter.recentview.CartRecentViewAdapter
import com.tokopedia.cartrevamp.view.adapter.wishlist.CartWishlistAdapter
import com.tokopedia.cartrevamp.view.uimodel.CartChooseAddressHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartEmptyHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartGroupHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartItemTickerErrorHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartLoadingHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartRecentViewHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartSectionHeaderHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartSelectedAmountHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartShopBottomHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartTopAdsHeadlineData
import com.tokopedia.cartrevamp.view.uimodel.CartWishlistHolderData
import com.tokopedia.cartrevamp.view.uimodel.DisabledAccordionHolderData
import com.tokopedia.cartrevamp.view.uimodel.DisabledItemHeaderHolderData
import com.tokopedia.cartrevamp.view.uimodel.DisabledReasonHolderData
import com.tokopedia.cartrevamp.view.viewholder.CartChooseAddressViewHolder
import com.tokopedia.cartrevamp.view.viewholder.CartEmptyViewHolder
import com.tokopedia.cartrevamp.view.viewholder.CartGroupViewHolder
import com.tokopedia.cartrevamp.view.viewholder.CartItemViewHolder
import com.tokopedia.cartrevamp.view.viewholder.CartLoadingViewHolder
import com.tokopedia.cartrevamp.view.viewholder.CartRecentViewViewHolder
import com.tokopedia.cartrevamp.view.viewholder.CartRecommendationViewHolder
import com.tokopedia.cartrevamp.view.viewholder.CartSectionHeaderViewHolder
import com.tokopedia.cartrevamp.view.viewholder.CartSelectedAmountViewHolder
import com.tokopedia.cartrevamp.view.viewholder.CartShopBottomViewHolder
import com.tokopedia.cartrevamp.view.viewholder.CartTickerErrorViewHolder
import com.tokopedia.cartrevamp.view.viewholder.CartTopAdsHeadlineViewHolder
import com.tokopedia.cartrevamp.view.viewholder.CartWishlistViewHolder
import com.tokopedia.cartrevamp.view.viewholder.DisabledAccordionViewHolder
import com.tokopedia.cartrevamp.view.viewholder.DisabledItemHeaderViewHolder
import com.tokopedia.cartrevamp.view.viewholder.DisabledReasonViewHolder
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.purchase_platform.common.feature.sellercashback.SellerCashbackListener
import com.tokopedia.purchase_platform.common.feature.sellercashback.ShipmentSellerCashbackModel
import com.tokopedia.purchase_platform.common.feature.sellercashback.ShipmentSellerCashbackViewHolder
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementViewHolder
import com.tokopedia.user.session.UserSessionInterface
import rx.subscriptions.CompositeSubscription

class CartAdapter constructor(
    private val actionListener: ActionListener,
    private val cartItemActionListener: CartItemAdapter.ActionListener,
    private val sellerCashbackListener: SellerCashbackListener,
    private val userSession: UserSessionInterface
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), CartItemViewHolder.ViewHolderListener {

    private val cartDataList = ArrayList<Any>()
    private var compositeSubscription = CompositeSubscription()

    private var cartWishlistAdapter: CartWishlistAdapter? = null
    private var cartRecentViewAdapter: CartRecentViewAdapter? = null
    private var tmpAllUnavailableShop: MutableList<Any>? = null

    var plusCoachMark: CoachMark2? = null

    companion object {
        const val SELLER_CASHBACK_ACTION_INSERT = 1
        const val SELLER_CASHBACK_ACTION_UPDATE = 2
        const val SELLER_CASHBACK_ACTION_DELETE = 3
    }

    fun updateList(newList: ArrayList<Any>) {
        val diffResult = DiffUtil.calculateDiff(CartDiffUtilCallback(cartDataList, newList))

        cartDataList.clear()
        cartDataList.addAll(newList)

        diffResult.dispatchUpdatesTo(this)
    }

    val allDisabledCartItemData: List<CartItemHolderData>
        get() {
            val cartItemDataList = ArrayList<CartItemHolderData>()
            if (tmpAllUnavailableShop?.isNotEmpty() == true) {
                cartItemDataList.addAll(collapsedUnavailableCartItemData)
            } else {
                loop@ for (data in cartDataList) {
                    when (data) {
                        is CartGroupHolderData -> {
                            if (data.isError) {
                                cartItemDataList.addAll(data.productUiModelList)
                            }
                        }

                        hasReachAllShopItems(data) -> break@loop
                    }
                }
            }

            return cartItemDataList
        }

    private val collapsedUnavailableCartItemData: List<CartItemHolderData>
        get() {
            val cartItemDataList = mutableListOf<CartItemHolderData>()
            tmpAllUnavailableShop?.let {
                loop@ for (data in it) {
                    when (data) {
                        is CartGroupHolderData -> {
                            cartItemDataList.addAll(data.productUiModelList)
                        }
                    }
                }
            }
            return cartItemDataList
        }

    override fun getItemViewType(position: Int): Int {
        val data = cartDataList[position]
        return when (data) {
            is CartSelectedAmountHolderData -> CartSelectedAmountViewHolder.LAYOUT
            is CartChooseAddressHolderData -> CartChooseAddressViewHolder.LAYOUT
            is CartGroupHolderData -> CartGroupViewHolder.LAYOUT
            is CartShopBottomHolderData -> CartShopBottomViewHolder.LAYOUT
            is CartItemHolderData -> CartItemViewHolder.TYPE_VIEW_ITEM_CART
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
            CartSelectedAmountViewHolder.LAYOUT -> {
                val binding =
                    ItemSelectedAmountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return CartSelectedAmountViewHolder(binding, actionListener)
            }

            CartChooseAddressViewHolder.LAYOUT -> {
                val binding = ItemCartChooseAddressBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return CartChooseAddressViewHolder(binding, actionListener)
            }

            CartGroupViewHolder.LAYOUT -> {
                val binding =
                    ItemGroupRevampBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return CartGroupViewHolder(
                    binding,
                    actionListener,
                    compositeSubscription,
                    plusCoachMark
                )
            }

            CartItemViewHolder.TYPE_VIEW_ITEM_CART -> {
                val binding = ItemCartProductRevampBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return CartItemViewHolder(binding, cartItemActionListener)
            }

            CartShopBottomViewHolder.LAYOUT -> {
                val binding = ItemCartShopBottomBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return CartShopBottomViewHolder(binding, actionListener)
            }

            CartTickerErrorViewHolder.TYPE_VIEW_TICKER_CART_ERROR -> {
                val binding = HolderItemCartTickerErrorBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return CartTickerErrorViewHolder(binding, actionListener)
            }

            ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(
                        ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK,
                        parent,
                        false
                    )
                return ShipmentSellerCashbackViewHolder(view, sellerCashbackListener)
            }

            CartEmptyViewHolder.LAYOUT -> {
                val binding =
                    ItemEmptyCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return CartEmptyViewHolder(binding, actionListener)
            }

            CartRecentViewViewHolder.LAYOUT -> {
                val binding = ItemCartRecentViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return CartRecentViewViewHolder(binding, actionListener)
            }

            CartWishlistViewHolder.LAYOUT -> {
                val binding = ItemCartWishlistBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return CartWishlistViewHolder(binding, actionListener)
            }

            CartSectionHeaderViewHolder.LAYOUT -> {
                val binding = ItemCartSectionHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return CartSectionHeaderViewHolder(binding, actionListener)
            }

            CartTopAdsHeadlineViewHolder.LAYOUT -> {
                val binding = ItemCartTopAdsHeadlineBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return CartTopAdsHeadlineViewHolder(binding, actionListener, userSession)
            }

            CartRecommendationViewHolder.LAYOUT -> {
                val binding = ItemCartRecommendationBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
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
                return TickerAnnouncementViewHolder(view)
            }

            DisabledItemHeaderViewHolder.LAYOUT -> {
                val binding = ItemCartDisabledHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return DisabledItemHeaderViewHolder(binding, actionListener)
            }

            DisabledReasonViewHolder.LAYOUT -> {
                val binding = ItemCartDisabledReasonBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return DisabledReasonViewHolder(binding, actionListener)
            }

            DisabledAccordionViewHolder.LAYOUT -> {
                val binding = ItemCartDisabledAccordionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
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
            CartSelectedAmountViewHolder.LAYOUT -> {
                val data = cartDataList[position] as CartSelectedAmountHolderData
                (holder as CartSelectedAmountViewHolder).bind(data)
            }

            CartChooseAddressViewHolder.LAYOUT -> {
                val data = cartDataList[position] as CartChooseAddressHolderData
                (holder as CartChooseAddressViewHolder).bind(data)
            }

            CartGroupViewHolder.LAYOUT -> {
                val data = cartDataList[position] as CartGroupHolderData
                (holder as CartGroupViewHolder).bindData(data)
            }

            CartItemViewHolder.TYPE_VIEW_ITEM_CART -> {
                val data = cartDataList[position] as CartItemHolderData
                (holder as CartItemViewHolder).bindData(data, this, 1)
            }

            CartShopBottomViewHolder.LAYOUT -> {
                val data = cartDataList[position] as CartShopBottomHolderData
                (holder as CartShopBottomViewHolder).bindData(data)
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

    fun setShopSelected(position: Int, selected: Boolean) {
        val any = cartDataList[position]
        if (any is CartGroupHolderData) {
            any.isAllSelected = selected
            any.productUiModelList.let {
                for (cartItemHolderData in it) {
                    cartItemHolderData.isSelected = selected
                }
            }
        }
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

    fun notifyByProductId(productId: String, isWishlisted: Boolean) {
        outerloop@ for (i in cartDataList.indices) {
            val obj = cartDataList[i]
            if (obj is CartGroupHolderData) {
                val cartGroupHolderData = cartDataList[i] as CartGroupHolderData
                innerloop@ for (cartItemHolderData in cartGroupHolderData.productUiModelList) {
                    if (cartItemHolderData.productId == productId) {
                        cartItemHolderData.isWishlisted = isWishlisted
                        if (cartGroupHolderData.isCollapsed) {
                            notifyItemChanged(i)
                            break@outerloop
                        }
                        break@innerloop
                    }
                }
            } else if (obj is CartItemHolderData && obj.productId == productId) {
                obj.isWishlisted = isWishlisted
                notifyItemChanged(i)
                break@outerloop
            }
        }
    }

    fun getCartItemByBundleGroupId(
        bundleId: String,
        bundleGroupId: String
    ): List<CartItemHolderData> {
        val cartItemHolderDataList = mutableListOf<CartItemHolderData>()
        loop@ for (data in cartDataList) {
            if (cartItemHolderDataList.isNotEmpty()) {
                break@loop
            }
            when (data) {
                is CartGroupHolderData -> {
                    data.productUiModelList.forEach { cartItemHolderData ->
                        if (cartItemHolderData.isBundlingItem && cartItemHolderData.bundleId == bundleId && cartItemHolderData.bundleGroupId == bundleGroupId) {
                            cartItemHolderDataList.add(cartItemHolderData)
                        }
                    }
                }

                hasReachAllShopItems(data) -> break@loop
            }
        }

        return cartItemHolderDataList
    }

    fun getCartShopBottomHolderDataFromIndex(shopBottomIndex: Int): CartShopBottomHolderData? {
        val bottomItem = cartDataList[shopBottomIndex]
        return if (bottomItem is CartShopBottomHolderData) {
            bottomItem
        } else {
            null
        }
    }

    fun getCartGroupHolderDataByCartItemHolderData(cartItemHolderData: CartItemHolderData): CartGroupHolderData? {
        loop@ for (data in cartDataList) {
            if (data is CartGroupHolderData && data.cartString == cartItemHolderData.cartString && data.isError == cartItemHolderData.isError) {
                return data
            }
        }

        return null
    }

    fun getCartGroupHolderDataAndIndexByCartString(
        cartString: String,
        isUnavailableGroup: Boolean
    ): Pair<Int, List<Any>> {
        val cartGroupList = arrayListOf<Any>()
        var startingIndex = RecyclerView.NO_POSITION
        for ((index, data) in cartDataList.withIndex()) {
            if (data is CartGroupHolderData && data.cartString == cartString && data.isError == isUnavailableGroup) {
                startingIndex = index
                cartGroupList.add(data)
            } else if (data is CartItemHolderData && startingIndex >= 0 && data.cartString == cartString) {
                cartGroupList.add(data)
            } else if (data is CartShopBottomHolderData && startingIndex >= 0 && data.shopData.cartString == cartString) {
                cartGroupList.add(data)
                break
            }
        }
        return Pair(startingIndex, cartGroupList)
    }

    private fun updateShopShownByCartGroup(cartGroupHolderData: CartGroupHolderData) {
        if (cartGroupHolderData.isUsingOWOCDesign()) {
            val groupPromoHolderDataMap = hashMapOf<String, MutableList<CartItemHolderData>>()
            cartGroupHolderData.productUiModelList.forEach {
                it.isShopShown = false
                val cartStringOrder = it.cartStringOrder
                if (!groupPromoHolderDataMap.containsKey(cartStringOrder)) {
                    groupPromoHolderDataMap[cartStringOrder] = arrayListOf()
                }
                groupPromoHolderDataMap[cartStringOrder]?.add(it)
            }
            groupPromoHolderDataMap.forEach { (_, value) ->
                value.firstOrNull()?.isShopShown = true
            }
        }
    }

    fun getCartShopHolderIndexByCartId(cartId: String): Int {
        loop@ for ((index, any) in cartDataList.withIndex()) {
            if (any is CartGroupHolderData) {
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

    fun notifyWishlist(productId: String, isWishlist: Boolean) {
        outerloop@ for (any in cartDataList) {
            if (any is CartWishlistHolderData) {
                val wishlist = any.wishList
                for (data in wishlist) {
                    if (data.id == productId) {
                        data.isWishlist = isWishlist
                        cartWishlistAdapter?.let {
                            cartWishlistAdapter?.notifyItemChanged(wishlist.indexOf(data))
                        }
                        break@outerloop
                    }
                }
                break@outerloop
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
        outerloop@ for (any in cartDataList) {
            if (any is CartRecentViewHolderData) {
                val recentViews = any.recentViewList
                for (data in recentViews) {
                    if (data.id == productId) {
                        data.isWishlist = isWishlist
                        cartRecentViewAdapter?.let {
                            cartRecentViewAdapter?.notifyItemChanged(recentViews.indexOf(data))
                        }
                        break@outerloop
                    }
                }
                break@outerloop
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

    // TODO: remove
    private fun hasReachAllShopItems(data: Any): Boolean {
        return data is CartRecentViewHolderData ||
            data is CartWishlistHolderData ||
            data is CartTopAdsHeadlineData ||
            data is CartRecommendationItemHolderData
    }

//    fun removeProductByCartId(cartIds: List<String>, needRefresh: Boolean, isFromGlobalCheckbox: Boolean): Pair<List<Int>, List<Int>> {
//        val toBeRemovedItems = mutableListOf<Any>()
//        val toBeRemovedIndices = mutableListOf<Int>()
//        val toBeUpdatedIndices = mutableListOf<Int>()
//
//        var cartSelectAllHolderDataIndexPair: Pair<CartSelectAllHolderData, Int>? = null
//        var cartItemTickerErrorHolderDataIndexPair: Pair<CartItemTickerErrorHolderData, Int>? = null
//        var disabledItemHeaderHolderDataIndexPair: Pair<DisabledItemHeaderHolderData, Int>? = null
//        var disabledAccordionHolderDataIndexPair: Pair<DisabledAccordionHolderData, Int>? = null
//        loop@ for ((index, data) in cartDataList.withIndex()) {
//            when {
//                data is CartSelectAllHolderData -> cartSelectAllHolderDataIndexPair = Pair(data, index)
//                data is CartItemTickerErrorHolderData -> cartItemTickerErrorHolderDataIndexPair = Pair(data, index)
//                data is DisabledItemHeaderHolderData -> disabledItemHeaderHolderDataIndexPair = Pair(data, index)
//                data is DisabledAccordionHolderData -> disabledAccordionHolderDataIndexPair = Pair(data, index)
//                data is CartGroupHolderData -> {
//                    val toBeDeletedProducts = mutableListOf<CartItemHolderData>()
//                    var hasSelectDeletedProducts = false
//                    var selectedNonDeletedProducts = 0
//                    data.productUiModelList.forEach { cartItemHolderData ->
//                        if (cartIds.contains(cartItemHolderData.cartId)) {
//                            toBeDeletedProducts.add(cartItemHolderData)
//                            if (cartItemHolderData.isSelected) {
//                                hasSelectDeletedProducts = true
//                            }
//                        } else if (cartItemHolderData.isSelected) {
//                            selectedNonDeletedProducts += 1
//                        }
//                    }
//                    if (toBeDeletedProducts.isNotEmpty()) {
//                        data.productUiModelList.removeAll(toBeDeletedProducts)
//                        if (data.productUiModelList.isEmpty()) {
//                            val previousIndex = index - 1
//                            if (data.isError && previousIndex < cartDataList.size) {
//                                val previousData = cartDataList[previousIndex]
//                                if (previousData is DisabledReasonHolderData) {
//                                    toBeRemovedItems.add(previousData)
//                                    toBeRemovedIndices.add(previousIndex)
//                                }
//                            }
//                            toBeRemovedItems.add(data)
//                            toBeRemovedIndices.add(index)
//                        } else {
//                            // update selection
//                            data.productUiModelList.last().isFinalItem = true
//                            updateShopShownByCartGroup(data)
//                            data.isAllSelected = selectedNonDeletedProducts > 0 && data.productUiModelList.size == selectedNonDeletedProducts
//                            data.isPartialSelected = selectedNonDeletedProducts > 0 && data.productUiModelList.size > selectedNonDeletedProducts
//                            if (!needRefresh && (isFromGlobalCheckbox || hasSelectDeletedProducts) && selectedNonDeletedProducts > 0) {
//                                actionListener.checkCartShopGroupTicker(data)
//                            }
//                            toBeUpdatedIndices.add(index)
//                        }
//                    }
//                }
//                data is CartItemHolderData -> {
//                    if (cartIds.contains(data.cartId)) {
//                        toBeRemovedItems.add(data)
//                        toBeRemovedIndices.add(index)
//                    }
//                }
//                hasReachAllShopItems(data) -> break@loop
//            }
//        }
//
//        cartDataList.removeAll(toBeRemovedItems)
//
//        if (allAvailableCartItemData.isEmpty()) {
//            cartSelectAllHolderDataIndexPair?.let {
//                cartDataList.remove(it.first)
//                toBeRemovedItems.add(it.second)
//            }
//        }
//
//        val disabledCartItems = allDisabledCartItemData
//        if (disabledCartItems.isEmpty()) {
//            cartItemTickerErrorHolderDataIndexPair?.let {
//                cartDataList.remove(it.first)
//                toBeRemovedItems.add(it.second)
//                toBeRemovedIndices.add(it.second)
//            }
//            disabledItemHeaderHolderDataIndexPair?.let {
//                cartDataList.remove(it.first)
//                toBeRemovedItems.add(it.second)
//                toBeRemovedIndices.add(it.second)
//            }
//            disabledAccordionHolderDataIndexPair?.let {
//                cartDataList.remove(it.first)
//                toBeRemovedItems.add(it.second)
//                toBeRemovedIndices.add(it.second)
//            }
//        } else {
//            val errorItemCount = disabledCartItems.size
//            cartItemTickerErrorHolderDataIndexPair?.let {
//                it.first.errorProductCount = errorItemCount
//                toBeUpdatedIndices.add(it.second)
//            }
//            disabledItemHeaderHolderDataIndexPair?.let {
//                it.first.disabledItemCount = errorItemCount
//                toBeUpdatedIndices.add(it.second)
//            }
//
//            var removeAccordion = false
//            if (errorItemCount == 1) {
//                removeAccordion = true
//            } else {
//                val bundleIdCartIdSet = mutableSetOf<String>()
//                disabledCartItems.forEach {
//                    if (it.isBundlingItem) {
//                        bundleIdCartIdSet.add(it.bundleId)
//                    } else {
//                        bundleIdCartIdSet.add(it.cartId)
//                    }
//                }
//                if (bundleIdCartIdSet.size <= 1) {
//                    removeAccordion = true
//                }
//            }
//
//            if (removeAccordion) {
//                disabledAccordionHolderDataIndexPair?.let {
//                    cartDataList.remove(it.first)
//                    toBeRemovedItems.add(it.second)
//                    toBeRemovedIndices.add(it.second)
//                }
//            }
//        }
//        return Pair(toBeRemovedIndices, toBeUpdatedIndices)
//    }

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

    private fun getNonCollapsibleUnavailableProduct(shop: CartGroupHolderData): List<CartItemHolderData> {
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

    fun setLastItemAlwaysSelected(): Boolean {
        var cartItemCount = 0
        getData().forEach outer@{ any ->
            when (any) {
                is CartGroupHolderData -> {
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
                    is CartGroupHolderData -> {
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
                is CartGroupHolderData -> {
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

    fun isAllAvailableItemCheked(): Boolean {
        getData().forEach {
            when (it) {
                is CartGroupHolderData -> {
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

    fun setCoachMark(coachMark: CoachMark2) {
        plusCoachMark = coachMark
    }

    fun updateAddOnByCartId(cartId: String, newAddOnWording: String) {
        var position = 0
        loop@ for ((index, any) in cartDataList.withIndex()) {
            if (any is CartGroupHolderData) {
                any.productUiModelList.let { cartItemHolderDataList ->
                    innerLoop@ for (cartItemHolderData in cartItemHolderDataList) {
                        if (cartItemHolderData.cartId == cartId) {
                            position = index
                            cartItemHolderData.addOnsProduct.widget.wording = newAddOnWording
                        }
                    }
                }
            }
        }
        notifyItemChanged(position)
    }

    override fun onNeedToRefreshSingleProduct(childPosition: Int) {
        notifyItemChanged(childPosition)
        cartItemActionListener.onNeedToRecalculate()
    }

    override fun onNeedToRefreshSingleShop(
        cartItemHolderData: CartItemHolderData,
        itemPosition: Int
    ) {
        cartItemActionListener.onNeedToRecalculate()
        cartItemActionListener.onNeedToRefreshSingleShop(cartItemHolderData, itemPosition)
    }

    override fun onNeedToRefreshWeight(cartItemHolderData: CartItemHolderData) {
        cartItemActionListener.onNeedToRecalculate()
        cartItemActionListener.onNeedToRefreshWeight(cartItemHolderData)
    }

    override fun onNeedToRefreshBoAffordability(cartItemHolderData: CartItemHolderData) {
        cartItemActionListener.onNeedToRefreshWeight(cartItemHolderData)
    }
}
