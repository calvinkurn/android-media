package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductSeparatorBinding
import com.tokopedia.search.result.presentation.model.SeparatorDataView
import com.tokopedia.utils.view.binding.viewBinding

class SeparatorViewHolder(itemView: View) : AbstractViewHolder<SeparatorDataView>(itemView) {

    private var binding : SearchResultProductSeparatorBinding? by viewBinding()

    override fun bind(element: SeparatorDataView) {
        binding?.searchProductSeparator?.visibility = View.VISIBLE
    }

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_product_separator
    }
}