package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.LastFilterDataView
import com.tokopedia.search.result.presentation.view.listener.LastFilterListener

class LastFilterViewHolder(
    itemView: View,
    val lastFilterListener: LastFilterListener,
): AbstractViewHolder<LastFilterDataView>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_product_last_filter_view_holder
    }

    override fun bind(element: LastFilterDataView) {

    }
}