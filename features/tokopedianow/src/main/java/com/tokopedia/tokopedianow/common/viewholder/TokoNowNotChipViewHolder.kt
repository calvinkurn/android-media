package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowNotChipUiModel

class TokoNowNotChipViewHolder(
    itemView: View
) : AbstractViewHolder<TokoNowNotChipUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_not_chip
    }

    override fun bind(element: TokoNowNotChipUiModel?) {
    }
}