package com.tokopedia.cmhomewidget.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.cmhomewidget.R
import com.tokopedia.cmhomewidget.databinding.LayoutCmHomeWidgetPaymentCardShimmerBinding
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetPaymentCardShimmerData


class CMHomeWidgetPaymentCardShimmerViewHolder(
    binding: LayoutCmHomeWidgetPaymentCardShimmerBinding
) : AbstractViewHolder<CMHomeWidgetPaymentCardShimmerData>(binding.root) {
    override fun bind(dataItem: CMHomeWidgetPaymentCardShimmerData?) {}

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_cm_home_widget_payment_card_shimmer
    }
}