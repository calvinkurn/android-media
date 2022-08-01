package com.tokopedia.affiliate.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.PAGE_TYPE_PDP
import com.tokopedia.affiliate.PAGE_TYPE_SHOP
import com.tokopedia.affiliate.interfaces.AffiliateHomeImpressionListener
import com.tokopedia.affiliate.interfaces.AffiliateItemImpressionListener
import com.tokopedia.affiliate.interfaces.AffiliatePromoImpressionListener
import com.tokopedia.affiliate.ui.viewholder.AffiliatePerformaSharedProductCardsItemVH
import com.tokopedia.affiliate.ui.viewholder.AffiliatePromotionCardItemVH
import com.tokopedia.affiliate.ui.viewholder.AffiliatePromotionShopItemVH
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateDataPlatformShimmerModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePerformaSharedProductCardsModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePromotionCardModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePromotionShopModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateShimmerModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateStaggeredShimmerModel

class AffiliateAdapter(
    affiliateAdapterFactory: AffiliateAdapterFactory,
    private val itemImpressionListener: AffiliateItemImpressionListener? = null
) : BaseAdapter<AffiliateAdapterFactory>(affiliateAdapterFactory) {
    companion object{
        private const val SHIMMER_ITEM_COUNT = 4

    }
    private val itemImpressionSet = HashSet<Int>()

    fun addShimmer(isStaggered : Boolean = false){
        for (i in 1..SHIMMER_ITEM_COUNT) {
            if(isStaggered) addElement(AffiliateStaggeredShimmerModel())
            else addElement(AffiliateShimmerModel())
        }
    }
    fun resetList(){
        this.visitables.clear()
        notifyDataSetChanged()
    }

    fun removeShimmer(listSize: Int) {
        if(itemCount >= (listSize + (SHIMMER_ITEM_COUNT - 1))) {
            for(i in (SHIMMER_ITEM_COUNT - 1) downTo 0){
                this.visitables.removeAt(listSize + i)
            }
            notifyItemRangeRemoved(listSize,SHIMMER_ITEM_COUNT)
        }
    }

    fun addDataPlatformShimmer() {
        addElement(AffiliateDataPlatformShimmerModel())
        for (i in 1..SHIMMER_ITEM_COUNT) {
             addElement(AffiliateShimmerModel())
        }
    }
    override fun onViewAttachedToWindow(holder: AbstractViewHolder<*>) {

        val position = holder.adapterPosition
        if (itemImpressionListener is AffiliatePromoImpressionListener) {
            handlePromoImpressions(holder, position, itemImpressionListener)
        } else if (itemImpressionListener is AffiliateHomeImpressionListener) {
            handleHomeImpressions(holder, position, itemImpressionListener)
        }


        super.onViewAttachedToWindow(holder)
    }

    private fun handlePromoImpressions(
        holder: AbstractViewHolder<*>,
        position: Int,
        itemImpressionListener: AffiliatePromoImpressionListener
    ) {
        when (holder) {
            is AffiliatePromotionShopItemVH -> {
                if (!itemImpressionSet.add(position)) {
                    val item = list[position] as? AffiliatePromotionShopModel
                    item?.let { shopModel ->
                        itemImpressionListener.onItemImpression(
                            shopModel.promotionItem,
                            holder.adapterPosition,
                            PAGE_TYPE_SHOP
                        )
                    }
                }
            }
            is AffiliatePromotionCardItemVH -> {
                if (!itemImpressionSet.add(position)) {
                    val item = list[position] as? AffiliatePromotionCardModel
                    item?.let { productModel ->
                        itemImpressionListener.onItemImpression(
                            productModel.promotionItem,
                            holder.adapterPosition,
                            PAGE_TYPE_PDP
                        )
                    }
                }
            }
        }
    }

    private fun handleHomeImpressions(
        holder: AbstractViewHolder<*>,
        position: Int,
        itemImpressionListener: AffiliateHomeImpressionListener
    ) {
        if (holder is AffiliatePerformaSharedProductCardsItemVH) {
            if (!itemImpressionSet.add(position)) {
                val item = list[position] as? AffiliatePerformaSharedProductCardsModel
                item?.let {
                    itemImpressionListener.onItemImpression(
                        it.product,
                        holder.adapterPosition,
                        PAGE_TYPE_SHOP
                    )
                }
            }
        }
    }
}
