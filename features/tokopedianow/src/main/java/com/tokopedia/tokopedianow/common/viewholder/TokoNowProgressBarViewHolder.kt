package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowProgressBarUiModel

class TokoNowProgressBarViewHolder(
    itemView: View
): AbstractViewHolder<TokoNowProgressBarUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_loading_more
    }

    override fun bind(element: TokoNowProgressBarUiModel?) { /* nothing to do */}
}
