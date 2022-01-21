package com.tokopedia.tokopedianow.searchcategory.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProgressBarDataView

class ProgressBarViewHolder(
    itemView: View
): AbstractViewHolder<ProgressBarDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_progress_bar
    }

    override fun bind(element: ProgressBarDataView) {
    }
}