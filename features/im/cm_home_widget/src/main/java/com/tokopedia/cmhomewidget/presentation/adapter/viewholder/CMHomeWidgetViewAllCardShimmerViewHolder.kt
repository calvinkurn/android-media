package com.tokopedia.cmhomewidget.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.cmhomewidget.R
import com.tokopedia.cmhomewidget.databinding.LayoutCmHomeWidgetViewAllCardShimmerBinding
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetViewAllCardShimmerData

class CMHomeWidgetViewAllCardShimmerViewHolder(
    binding: LayoutCmHomeWidgetViewAllCardShimmerBinding
) : AbstractViewHolder<CMHomeWidgetViewAllCardShimmerData>(binding.root) {
    override fun bind(dataItem: CMHomeWidgetViewAllCardShimmerData?) {}

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_cm_home_widget_view_all_card_shimmer
    }
}