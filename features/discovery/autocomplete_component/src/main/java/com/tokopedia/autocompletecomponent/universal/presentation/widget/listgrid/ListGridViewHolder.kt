package com.tokopedia.autocompletecomponent.universal.presentation.widget.listgrid

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.UniversalSearchDoubleLineItemLayoutBinding
import com.tokopedia.autocompletecomponent.databinding.UniversalSearchListGridItemLayoutBinding
import com.tokopedia.autocompletecomponent.universal.presentation.widget.doubleline.DoubleLineDataView
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.utils.view.binding.viewBinding

class ListGridViewHolder(itemView: View): AbstractViewHolder<ListGridDataView>(itemView) {
    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.universal_search_list_grid_item_layout
    }

    private var binding: UniversalSearchListGridItemLayoutBinding? by viewBinding()

    override fun bind(data: ListGridDataView) {
        bindTitle(data)
    }

    private fun bindTitle(data: ListGridDataView) {
        binding?.universalSearchListGridTitle?.shouldShowWithAction(data.title.isNotEmpty()) {
            binding?.universalSearchListGridTitle?.text = data.title
        }
    }
}