package com.tokopedia.search.result.product.separator

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R

class VerticalSeparatorViewHolder(
    itemView: View
): AbstractViewHolder<VerticalSeparatorDataView>(itemView) {

    override fun bind(element: VerticalSeparatorDataView?) { }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.search_separator_view_holder
    }
}
