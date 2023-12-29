package com.tokopedia.search.result.mps.emptystate

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchMpsEmptyStateFilterBinding
import com.tokopedia.utils.view.binding.viewBinding

class MPSEmptyStateFilterViewHolder(
    itemView: View,
    private val emptyStateListener: EmptyStateListener,
): AbstractViewHolder<MPSEmptyStateFilterDataView>(itemView) {

    private var binding: SearchMpsEmptyStateFilterBinding? by viewBinding()

    init {
        binding?.let {
            it.mpsEmptyStateFilterResetFilter.setOnClickListener {
                emptyStateListener.onEmptyButtonFilterClicked()
            }
        }
    }

    override fun bind(element: MPSEmptyStateFilterDataView) {

    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.search_mps_empty_state_filter
    }
}
