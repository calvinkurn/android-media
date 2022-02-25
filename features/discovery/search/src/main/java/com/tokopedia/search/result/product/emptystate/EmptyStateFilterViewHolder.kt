package com.tokopedia.search.result.product.emptystate

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchEmptyStateFilterProductBinding
import com.tokopedia.utils.view.binding.viewBinding

class EmptyStateFilterViewHolder(
    view: View,
    private val emptyStateListener: EmptyStateListener,
): AbstractViewHolder<EmptyStateFilterDataView>(view) {

    private var binding: SearchEmptyStateFilterProductBinding? by viewBinding()

    init {
        binding?.btnResetFilters?.setOnClickListener {
            emptyStateListener.resetFilters()
        }
    }

    override fun bind(element: EmptyStateFilterDataView) { }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.search_empty_state_filter_product
    }
}