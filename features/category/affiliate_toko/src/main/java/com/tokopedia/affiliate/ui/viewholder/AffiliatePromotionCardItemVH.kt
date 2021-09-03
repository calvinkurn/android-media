package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate_toko.R
import com.tokopedia.affiliate.ui.custom.AffiliatePromotionProductCard
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePromotionCardVHViewModel
import com.tokopedia.productcard.ProductCardListView

class AffiliatePromotionCardItemVH(itemView: View)
    : AbstractViewHolder<AffiliatePromotionCardVHViewModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_promotion_product_card_item_layout

    }

    override fun bind(element: AffiliatePromotionCardVHViewModel?) {
        element?.promotionItem?.let {
            itemView.findViewById<ProductCardListView>(R.id.affiliate_product_card).setProductModel(
                    AffiliatePromotionProductCard.toAffiliateProductModel(it))
        }
    }
}
