package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.interfaces.AffiliatePerformanceChipClick
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePerformanceChipModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.unifycomponents.ChipsUnify

class AffiliatePerformanceChipVH(
    itemView: View,
    private val affiliatePerformanceChipClick: AffiliatePerformanceChipClick?
) : AbstractViewHolder<AffiliatePerformanceChipModel>(itemView) {



    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_item_performance_chip

    }

    override fun bind(element: AffiliatePerformanceChipModel?) {
        itemView.findViewById<ChipsUnify>(R.id.chip_performance_item).let {
            it.chipText = element?.chipType?.name
            it.chipType = if (element?.chipType?.isSelected == true) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
            it.setOnClickListener {
                affiliatePerformanceChipClick?.onChipClick(element?.chipType)
            }
        }
    }


}