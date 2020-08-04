package com.tokopedia.deals.brand.ui.adapter.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.deals.R
import com.tokopedia.deals.brand.model.DealsEmptyDataView
import com.tokopedia.deals.common.listener.EmptyStateListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.item_deals_category_empty.view.*

class DealsEmptyViewHolder(
        private val emptyStateListener: EmptyStateListener,
        itemView: View
): BaseViewHolder(itemView) {

    fun bind(empty: DealsEmptyDataView) {
        with(itemView) {
            txt_category_empty_title?.text = empty.title
            txt_category_empty_description?.text = empty.desc
            if (empty.isFilter) btn_category_empty_reset_filter.show()
            else btn_category_empty_reset_filter.hide()

            btn_category_empty_reset_filter.setOnClickListener {
                emptyStateListener.resetFilter()
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_deals_category_empty
    }
}