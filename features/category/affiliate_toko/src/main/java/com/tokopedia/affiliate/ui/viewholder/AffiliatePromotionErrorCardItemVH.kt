package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate_toko.R
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePromotionErrorCardVHViewModel

class AffiliatePromotionErrorCardItemVH(itemView: View)
    : AbstractViewHolder<AffiliatePromotionErrorCardVHViewModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_promotion_error_item_layout
    }

    override fun bind(element: AffiliatePromotionErrorCardVHViewModel?) {

    }
}
