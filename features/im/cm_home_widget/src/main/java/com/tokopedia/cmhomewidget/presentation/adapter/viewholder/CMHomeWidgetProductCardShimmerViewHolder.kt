package com.tokopedia.cmhomewidget.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.cmhomewidget.R
import com.tokopedia.cmhomewidget.databinding.LayoutCmHomeWidgetProductCardShimmerBinding
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetProductCardShimmerData

class CMHomeWidgetProductCardShimmerViewHolder(
    binding: LayoutCmHomeWidgetProductCardShimmerBinding
) : AbstractViewHolder<CMHomeWidgetProductCardShimmerData>(binding.root) {
    override fun bind(dataItem: CMHomeWidgetProductCardShimmerData?) {}

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_cm_home_widget_product_card_shimmer
    }
}