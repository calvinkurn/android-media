package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateBottomDividerItemModel
import com.tokopedia.affiliate_toko.R

class AffiliateBottomSheetDivderItemVH(itemView: View)
    : AbstractViewHolder<AffiliateBottomDividerItemModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_bottom_sheet_divider_item
    }

    override fun bind(element: AffiliateBottomDividerItemModel?) {

    }
}
