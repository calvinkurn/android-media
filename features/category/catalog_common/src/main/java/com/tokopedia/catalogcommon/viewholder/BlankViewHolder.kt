package com.tokopedia.catalogcommon.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.uimodel.BlankUiModel


class BlankViewHolder(itemView: View) : AbstractViewHolder<BlankUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_item_blank
    }

    override fun bind(element: BlankUiModel) {
        // no-op
    }
}
