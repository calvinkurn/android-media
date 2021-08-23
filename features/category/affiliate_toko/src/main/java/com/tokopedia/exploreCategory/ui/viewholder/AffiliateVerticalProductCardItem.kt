package com.tokopedia.exploreCategory.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate_toko.R
import com.tokopedia.exploreCategory.ui.viewholder.viewmodel.AffiliateProductCardVHViewModel

class AffiliateVerticalProductCardItem(itemView: View)
    : AbstractViewHolder<AffiliateProductCardVHViewModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_vertical_product_card_item_layout
    }

    override fun bind(element: AffiliateProductCardVHViewModel?) {

    }
}