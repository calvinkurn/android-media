package com.tokopedia.cart.view.adapter.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.databinding.ItemCartBuyAgainBinding
import com.tokopedia.cart.databinding.ItemCartDisabledAccordionRevampBinding
import com.tokopedia.cart.databinding.ItemCartDisabledCollapsedBinding
import com.tokopedia.cart.databinding.ItemCartDisabledHeaderRevampBinding
import com.tokopedia.cart.databinding.ItemCartDisabledReasonRevampBinding
import com.tokopedia.cart.databinding.ItemCartProductRevampBinding
import com.tokopedia.cart.databinding.ItemCartRecentViewBinding
import com.tokopedia.cart.databinding.ItemCartRecommendationBinding
import com.tokopedia.cart.databinding.ItemCartSectionHeaderBinding
import com.tokopedia.cart.databinding.ItemCartShopBottomRevampBinding
import com.tokopedia.cart.databinding.ItemCartTopAdsHeadlineBinding
import com.tokopedia.cart.databinding.ItemCartWishlistBinding
import com.tokopedia.cart.databinding.ItemEmptyCartBinding
import com.tokopedia.cart.databinding.ItemGroupRevampBinding
import com.tokopedia.cart.databinding.ItemSelectedAmountBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.adapter.diffutil.CartDiffUtilCallback
import com.tokopedia.cart.view.adapter.wishlist.CartWishlistAdapter
import com.tokopedia.cart.view.customview.CartViewBinderHelper
import com.tokopedia.cart.view.uimodel.CartBuyAgainHolderData
import com.tokopedia.cart.view.uimodel.CartEmptyHolderData
import com.tokopedia.cart.view.uimodel.CartGroupHolderData
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cart.view.uimodel.CartLoadingHolderData
import com.tokopedia.cart.view.uimodel.CartMainCoachMarkUiModel
import com.tokopedia.cart.view.uimodel.CartRecentViewHolderData
import com.tokopedia.cart.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.cart.view.uimodel.CartSectionHeaderHolderData
import com.tokopedia.cart.view.uimodel.CartSelectedAmountHolderData
import com.tokopedia.cart.view.uimodel.CartShopBottomHolderData
import com.tokopedia.cart.view.uimodel.CartTopAdsHeadlineData
import com.tokopedia.cart.view.uimodel.CartWishlistHolderData
import com.tokopedia.cart.view.uimodel.DisabledAccordionHolderData
import com.tokopedia.cart.view.uimodel.DisabledCollapsedHolderData
import com.tokopedia.cart.view.uimodel.DisabledItemHeaderHolderData
import com.tokopedia.cart.view.uimodel.DisabledReasonHolderData
import com.tokopedia.cart.view.viewholder.CartBuyAgainViewHolder
import com.tokopedia.cart.view.viewholder.CartEmptyViewHolder
import com.tokopedia.cart.view.viewholder.CartGroupViewHolder
import com.tokopedia.cart.view.viewholder.CartItemViewHolder
import com.tokopedia.cart.view.viewholder.CartLoadingViewHolder
import com.tokopedia.cart.view.viewholder.CartRecentViewViewHolder
import com.tokopedia.cart.view.viewholder.CartRecommendationViewHolder
import com.tokopedia.cart.view.viewholder.CartSectionHeaderViewHolder
import com.tokopedia.cart.view.viewholder.CartSelectedAmountViewHolder
import com.tokopedia.cart.view.viewholder.CartShopBottomViewHolder
import com.tokopedia.cart.view.viewholder.CartTopAdsHeadlineViewHolder
import com.tokopedia.cart.view.viewholder.CartWishlistViewHolder
import com.tokopedia.cart.view.viewholder.DisabledAccordionViewHolder
import com.tokopedia.cart.view.viewholder.DisabledCollapsedViewHolder
import com.tokopedia.cart.view.viewholder.DisabledItemHeaderViewHolder
import com.tokopedia.cart.view.viewholder.DisabledReasonViewHolder
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.purchase_platform.common.feature.sellercashback.SellerCashbackListener
import com.tokopedia.purchase_platform.common.feature.sellercashback.ShipmentSellerCashbackModel
import com.tokopedia.purchase_platform.common.feature.sellercashback.ShipmentSellerCashbackViewHolder
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementViewHolder
import com.tokopedia.user.session.UserSessionInterface
import rx.subscriptions.CompositeSubscription
import timber.log.Timber

class CartAdapter(
    private val actionListener: ActionListener,
    private val cartItemActionListener: CartItemAdapter.ActionListener,
    private val sellerCashbackListener: SellerCashbackListener,
    private val userSession: UserSessionInterface,
    private val binderHelper: CartViewBinderHelper
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), CartItemViewHolder.ViewHolderListener {

    private val cartDataList = ArrayList<Any>()
    private var compositeSubscription = CompositeSubscription()

    var cartWishlistAdapter: CartWishlistAdapter? = null

    private var plusCoachMark: CoachMark2? = null
    private var mainCoachMark: CartMainCoachMarkUiModel = CartMainCoachMarkUiModel()

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

    fun updateListWithoutDiffUtil(newList: ArrayList<Any>) {
        cartDataList.clear()
        cartDataList.addAll(newList)
    }

    override fun getItemViewType(position: Int): Int {
        return when (cartDataList[position]) {
            is CartSelectedAmountHolderData -> CartSelectedAmountViewHolder.LAYOUT
            is CartGroupHolderData -> CartGroupViewHolder.LAYOUT
            is CartShopBottomHolderData -> CartShopBottomViewHolder.LAYOUT
            is CartItemHolderData -> CartItemViewHolder.TYPE_VIEW_ITEM_CART
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
            is DisabledCollapsedHolderData -> DisabledCollapsedViewHolder.LAYOUT
            is CartBuyAgainHolderData -> CartBuyAgainViewHolder.LAYOUT
            else -> super.getItemViewType(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        initializeCompositeSubscription()
        when (viewType) {
            CartSelectedAmountViewHolder.LAYOUT -> {
                val binding =
                    ItemSelectedAmountBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return CartSelectedAmountViewHolder(binding, actionListener)
            }

            CartGroupViewHolder.LAYOUT -> {
                val binding =
                    ItemGroupRevampBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
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
                return CartItemViewHolder(
                    binding,
                    cartItemActionListener,
                    mainCoachMark,
                    binderHelper
                )
            }

            CartShopBottomViewHolder.LAYOUT -> {
                val binding = ItemCartShopBottomRevampBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return CartShopBottomViewHolder(binding, actionListener)
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
                val binding = ItemCartDisabledHeaderRevampBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return DisabledItemHeaderViewHolder(binding, actionListener)
            }

            DisabledReasonViewHolder.LAYOUT -> {
                val binding = ItemCartDisabledReasonRevampBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return DisabledReasonViewHolder(binding, actionListener)
            }

            DisabledAccordionViewHolder.LAYOUT -> {
                val binding = ItemCartDisabledAccordionRevampBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return DisabledAccordionViewHolder(binding, actionListener)
            }

            DisabledCollapsedViewHolder.LAYOUT -> {
                val binding = ItemCartDisabledCollapsedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return DisabledCollapsedViewHolder(binding, actionListener)
            }

            CartBuyAgainViewHolder.LAYOUT -> {
                val binding = ItemCartBuyAgainBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return CartBuyAgainViewHolder(binding, actionListener)
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
        when (getItemViewType(position)) {
            CartSelectedAmountViewHolder.LAYOUT -> {
                val data = cartDataList[position] as CartSelectedAmountHolderData
                (holder as CartSelectedAmountViewHolder).bind(data)
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

            DisabledCollapsedViewHolder.LAYOUT -> {
                val data = cartDataList[position] as DisabledCollapsedHolderData
                (holder as DisabledCollapsedViewHolder).bind(data)
            }

            CartBuyAgainViewHolder.LAYOUT -> {
                val data = cartDataList[position] as CartBuyAgainHolderData
                (holder as CartBuyAgainViewHolder).bind(data)
            }
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        when (holder.itemViewType) {
            CartRecommendationViewHolder.LAYOUT -> {
                (holder as CartRecommendationViewHolder).clearImage()
            }
            CartRecentViewViewHolder.LAYOUT -> {
                (holder as? CartRecentViewViewHolder)?.recycle()
            }
        }
    }

    override fun getItemCount(): Int {
        return cartDataList.size
    }

    fun setCoachMark(coachMark: CoachMark2) {
        plusCoachMark = coachMark
    }

    fun setMainCoachMark(coachMark: CartMainCoachMarkUiModel) {
        mainCoachMark = coachMark
    }

    override fun onNeedToRefreshSingleProduct(childPosition: Int) {
        // work-around to prevent crash when focused quantity editor is going off screen
        try {
            notifyItemChanged(childPosition)
        } catch (ex: Exception) {
            Timber.e(ex)
        }
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
