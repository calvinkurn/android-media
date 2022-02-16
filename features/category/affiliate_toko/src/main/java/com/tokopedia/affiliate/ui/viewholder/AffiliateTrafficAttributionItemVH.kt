package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.interfaces.AffiliateDatePickerInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateDateRangePickerModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateTrafficAttributionModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.unifyprinciples.Typography

class AffiliateTrafficAttributionItemVH(itemView: View)
    : AbstractViewHolder<AffiliateTrafficAttributionModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_traffic_attribution_item
    }

    override fun bind(element: AffiliateTrafficAttributionModel?) {

    }
}
