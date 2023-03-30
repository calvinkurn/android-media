package com.tokopedia.search.result.mps.emptystate

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchMpsEmptyStateKeywordBinding
import com.tokopedia.utils.view.binding.viewBinding

class MPSEmptyStateKeywordViewHolder(
    itemView: View,
): AbstractViewHolder<MPSEmptyStateKeywordDataView>(itemView) {

    private var binding: SearchMpsEmptyStateKeywordBinding? by viewBinding()

    override fun bind(element: MPSEmptyStateKeywordDataView) {

    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.search_mps_empty_state_keyword
    }
}
