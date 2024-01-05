package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateCommisionDividerItemModel
import com.tokopedia.affiliate_toko.R

class AffiliateCommisionDivderItemVH(itemView: View) :
    AbstractViewHolder<AffiliateCommisionDividerItemModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_commission_divider_item
    }

    override fun bind(element: AffiliateCommisionDividerItemModel?) = Unit
}
