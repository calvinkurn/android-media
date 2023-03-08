package com.tokopedia.search.result.mps.emptystate

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R

class MPSEmptyStateFilterViewHolder(
    itemView: View,
): AbstractViewHolder<MPSEmptyStateFilterDataView>(itemView) {

    override fun bind(element: MPSEmptyStateFilterDataView) {

    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.search_mps_empty_state_filter
    }
}
