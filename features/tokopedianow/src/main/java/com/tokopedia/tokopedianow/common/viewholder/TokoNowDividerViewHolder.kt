package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowDividerUiModel

class TokoNowDividerViewHolder(
    itemView: View,
) : AbstractViewHolder<TokoNowDividerUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_divider
    }

    override fun bind(element: TokoNowDividerUiModel?) { /* nothing to do */ }
}
