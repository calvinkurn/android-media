package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateCommissionItemModel
import com.tokopedia.affiliate_toko.R

class AffiliateCommissionDetailsItemVH(itemView: View)
    : AbstractViewHolder<AffiliateCommissionItemModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_commison_detail_item
    }

    override fun bind(element: AffiliateCommissionItemModel?) {

    }
}
