package com.tokopedia.affiliate.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.ALMOST_OOS
import com.tokopedia.affiliate.AVAILABLE
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.EMPTY_STOCK
import com.tokopedia.affiliate.PRODUCT_INACTIVE
import com.tokopedia.affiliate.SHOP_CLOSED
import com.tokopedia.affiliate.SHOP_INACTIVE
import com.tokopedia.affiliate.model.response.AffiliatePerformanceListData
import com.tokopedia.affiliate.model.response.AffiliateSearchData
import com.tokopedia.affiliate.ui.custom.AffiliateStickyHeaderView
import com.tokopedia.affiliate.ui.custom.OnStickyHeaderListener
import com.tokopedia.affiliate.ui.viewholder.AffiliateDateFilterVH
import com.tokopedia.affiliate.ui.viewholder.AffiliatePerformaSharedProductCardsItemVH
import com.tokopedia.affiliate.ui.viewholder.AffiliatePerformanceChipRVVH
import com.tokopedia.affiliate.ui.viewholder.AffiliatePromotionCardItemVH
import com.tokopedia.affiliate.ui.viewholder.AffiliatePromotionShopItemVH
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateDataPlatformShimmerModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateDateFilterModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePerformaSharedProductCardsModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePerformanceChipRVModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePromotionCardModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePromotionShopModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateShimmerModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateStaggeredShimmerModel
import com.tokopedia.kotlin.extensions.view.orZero

class AffiliateAdapter(
    private val affiliateAdapterFactory: AffiliateAdapterFactory,
    private val source: String = "",
    private val userId: String = ""
) : BaseAdapter<AffiliateAdapterFactory>(affiliateAdapterFactory),
    AffiliateStickyHeaderView.OnStickyHeaderAdapter {
    companion object {
        private const val SHIMMER_ITEM_COUNT = 4

        private const val PRODUCT_TYPE = 0
        private const val PRODUCT_ACTIVE = 1
        const val SOURCE_HOME = "home"
        const val SOURCE_PROMOSIKAN = "promosikan"

    }

    private val itemImpressionSet = HashSet<Int>()
    private var onAffiliateStickyHeaderViewListener: OnStickyHeaderListener? = null

    fun addShimmer(isStaggered: Boolean = false) {
        for (i in 1..SHIMMER_ITEM_COUNT) {
            if (isStaggered) addElement(AffiliateStaggeredShimmerModel())
            else addElement(AffiliateShimmerModel())
        }
    }

    fun resetList() {
        this.visitables.clear()
        notifyDataSetChanged()
    }

    fun removeShimmer(listSize: Int) {
        if (itemCount >= (listSize + (SHIMMER_ITEM_COUNT - 1))) {
            for (i in (SHIMMER_ITEM_COUNT - 1) downTo 0) {
                this.visitables.removeAt(listSize + i)
            }
            notifyItemRangeRemoved(listSize, SHIMMER_ITEM_COUNT)
        }
    }

    fun addDataPlatformShimmer() {
        addElement(AffiliateDataPlatformShimmerModel())
        for (i in 1..SHIMMER_ITEM_COUNT) {
            addElement(AffiliateShimmerModel())
        }
    }

    override fun onViewAttachedToWindow(holder: AbstractViewHolder<*>) {
        when (source) {
            SOURCE_HOME -> handleHomeImpressions(holder)
            SOURCE_PROMOSIKAN -> handlePromoImpressions(holder)
        }

        super.onViewAttachedToWindow(holder)
    }

    private fun handlePromoImpressions(
        holder: AbstractViewHolder<*>
    ) {
        when (holder) {
            is AffiliatePromotionShopItemVH -> {
                if (!itemImpressionSet.add(holder.bindingAdapterPosition)) {
                    val item = list[holder.bindingAdapterPosition] as? AffiliatePromotionShopModel
                    item?.let { shopModel ->
                        sendPromoShopImpression(
                            shopModel.promotionItem,
                            holder.bindingAdapterPosition
                        )
                    }
                }
            }
            is AffiliatePromotionCardItemVH -> {
                if (!itemImpressionSet.add(holder.bindingAdapterPosition)) {
                    val item = list[holder.bindingAdapterPosition] as? AffiliatePromotionCardModel
                    item?.let { productModel ->
                        sendPromoProductImpression(
                            productModel.promotionItem,
                            holder.bindingAdapterPosition
                        )

                    }
                }
            }
        }
    }

    private fun handleHomeImpressions(
        holder: AbstractViewHolder<*>
    ) {
        if (holder is AffiliatePerformaSharedProductCardsItemVH) {
            if (!itemImpressionSet.add(holder.bindingAdapterPosition)) {
                val item =
                    list[holder.bindingAdapterPosition] as? AffiliatePerformaSharedProductCardsModel
                item?.let {
                    if (it.product.itemType == PRODUCT_TYPE) {
                        sendHomeProductImpression(it.product, holder.bindingAdapterPosition)
                    } else {
                        sendHomeShopImpression(it.product, holder.bindingAdapterPosition)
                    }
                }
            }
        }
    }

    private fun sendHomeProductImpression(
        item: AffiliatePerformanceListData.GetAffiliatePerformanceList.Data.Data.Item,
        position: Int
    ) {
        val status =
            if (item.status == PRODUCT_ACTIVE) AffiliateAnalytics.LabelKeys.ACTIVE
            else AffiliateAnalytics.LabelKeys.INACTIVE
        AffiliateAnalytics.trackEventImpression(
            AffiliateAnalytics.EventKeys.VIEW_ITEM_LIST,
            AffiliateAnalytics.ActionKeys.IMPRESSION_PRODUK_YANG_DIPROMOSIKAN,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE,
            userId,
            item.itemID,
            position,
            item.itemTitle,
            "${item.itemID} - ${
                item.metrics?.findLast { it?.metricType == "orderCommissionPerItem" }?.metricValue
            } - ${
                item.metrics?.findLast { it?.metricType == "totalClickPerItem" }?.metricValue
            } - ${
                item.metrics?.findLast { it?.metricType == "orderPerItem" }?.metricValue
            } - $status",
        )
    }

    private fun sendHomeShopImpression(
        item: AffiliatePerformanceListData.GetAffiliatePerformanceList.Data.Data.Item,
        position: Int
    ) {
        val status =
            if (item.status == PRODUCT_ACTIVE) AffiliateAnalytics.LabelKeys.ACTIVE
            else AffiliateAnalytics.LabelKeys.INACTIVE
        AffiliateAnalytics.trackEventImpression(
            AffiliateAnalytics.EventKeys.VIEW_ITEM_LIST,
            AffiliateAnalytics.ActionKeys.IMPRESSION_SHOP_LINK_DENGAN_PERFORMA,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE,
            userId,
            item.itemID,
            position,
            item.itemTitle,
            "${item.itemID} - ${
                item.metrics?.findLast { it?.metricType == "orderCommissionPerItem" }?.metricValue
            } - ${
                item.metrics?.findLast { it?.metricType == "totalClickPerItem" }?.metricValue
            } - ${
                item.metrics?.findLast { it?.metricType == "orderPerItem" }?.metricValue
            } - $status",
            AffiliateAnalytics.ItemKeys.AFFILAITE_HOME_SHOP_SELECT_CONTENT
        )
    }

    private fun sendPromoProductImpression(
        item: AffiliateSearchData.SearchAffiliate.Data.Card.Item,
        position: Int
    ) {
        val status = when (item.status?.messages?.first()?.messageType) {
            AVAILABLE -> AffiliateAnalytics.LabelKeys.AVAILABLE
            ALMOST_OOS -> AffiliateAnalytics.LabelKeys.ALMOST_OOS
            EMPTY_STOCK -> AffiliateAnalytics.LabelKeys.EMPTY_STOCK
            PRODUCT_INACTIVE -> AffiliateAnalytics.LabelKeys.PRODUCT_INACTIVE
            SHOP_INACTIVE -> AffiliateAnalytics.LabelKeys.SHOP_INACTIVE
            else -> ""
        }

        AffiliateAnalytics.trackEventImpression(
            AffiliateAnalytics.EventKeys.VIEW_ITEM_LIST,
            AffiliateAnalytics.ActionKeys.IMPRESSION_PRODUCT_SEARCH_RESULT_PAGE,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_PROMOSIKAN_PAGE,
            userId,
            item.itemId,
            position,
            item.title,
            "${item.itemId} - ${item.commission?.amount} - $status"
        )
    }

    private fun sendPromoShopImpression(
        item: AffiliateSearchData.SearchAffiliate.Data.Card.Item,
        position: Int
    ) {
        val status = when (item.status?.messages?.first()?.messageType) {
            AVAILABLE -> AffiliateAnalytics.LabelKeys.SHOP_ACTIVE
            SHOP_INACTIVE -> AffiliateAnalytics.LabelKeys.SHOP_INACTIVE
            SHOP_CLOSED -> AffiliateAnalytics.LabelKeys.SHOP_CLOSED
            else -> ""
        }
        AffiliateAnalytics.trackEventImpression(
            AffiliateAnalytics.EventKeys.VIEW_ITEM_LIST,
            AffiliateAnalytics.ActionKeys.IMPRESSION_SHOP_SEARCH_RESULT_PAGE,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_PROMOSIKAN_PAGE,
            userId,
            item.itemId,
            position,
            item.title,
            "${item.itemId} - ${item.commission?.amount} - $status"
        )
    }

    override fun stickyHeaderPositions(): IntArray = intArrayOf(
        visitables?.indexOfFirst { it is AffiliateDateFilterModel }.orZero(),
        visitables?.indexOfFirst { it is AffiliatePerformanceChipRVModel }.orZero()
    )


    override fun createStickyViewHolder(parent: ViewGroup?): List<AffiliateStickyHeaderView.StickyState> {
        return stickyHeaderPositions().map {

            val stickyViewType = getItemViewType(it)
            val view = onCreateViewItem(parent, stickyViewType)
            AffiliateStickyHeaderView.StickyState(
                it,
                affiliateAdapterFactory.createViewHolder(view, stickyViewType)
            )
        }
    }

    override fun bindSticky(viewHolder: RecyclerView.ViewHolder?) {
        when (viewHolder) {
            is AffiliateDateFilterVH -> {
                visitables.filterIsInstance(AffiliateDateFilterModel::class.java).firstOrNull()
                    ?.let {
                        viewHolder.bind(it)
                    }
            }
            is AffiliatePerformanceChipRVVH -> {
                visitables.filterIsInstance(AffiliatePerformanceChipRVModel::class.java)
                    .firstOrNull()?.let {
                    viewHolder.bind(it)
                }
            }
        }
    }

    override fun setListener(onAffiliateStickyHeaderViewListener: OnStickyHeaderListener?) {
        this.onAffiliateStickyHeaderViewListener = onAffiliateStickyHeaderViewListener
    }

}
