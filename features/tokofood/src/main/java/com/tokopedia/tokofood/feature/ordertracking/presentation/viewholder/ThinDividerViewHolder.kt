package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ThinDividerUiModel

class ThinDividerViewHolder(view: View): AbstractViewHolder<ThinDividerUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_order_tracking_thin_divider_full
    }

    override fun bind(element: ThinDividerUiModel?) {}

}