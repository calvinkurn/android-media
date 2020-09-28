package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.SeparatorViewModel
import kotlinx.android.synthetic.main.search_result_product_separator.view.*

class SeparatorViewHolder(itemView: View) : AbstractViewHolder<SeparatorViewModel>(itemView) {

    override fun bind(element: SeparatorViewModel) {
        itemView.searchProductSeparator?.visibility = View.VISIBLE
    }

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_product_separator
    }
}