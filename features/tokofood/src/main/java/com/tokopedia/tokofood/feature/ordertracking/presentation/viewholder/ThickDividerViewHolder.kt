package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ThickDividerUiModel

class ThickDividerViewHolder(view: View): AbstractViewHolder<ThickDividerUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_order_tracking_thick_divider_full
    }

    override fun bind(element: ThickDividerUiModel?) {}
}