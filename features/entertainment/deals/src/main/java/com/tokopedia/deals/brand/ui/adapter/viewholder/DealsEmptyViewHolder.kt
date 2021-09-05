package com.tokopedia.deals.brand.ui.adapter.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.deals.R
import com.tokopedia.deals.brand.model.DealsEmptyDataView
import com.tokopedia.deals.common.listener.EmptyStateListener
import com.tokopedia.deals.databinding.ItemDealsCategoryEmptyBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class DealsEmptyViewHolder(
        private val emptyStateListener: EmptyStateListener,
        itemView: View
): BaseViewHolder(itemView) {

    fun bind(empty: DealsEmptyDataView) {
        val binding = ItemDealsCategoryEmptyBinding.bind(itemView)
        with(binding){
            txtCategoryEmptyTitle?.text = empty.title
            txtCategoryEmptyDescription?.text = empty.desc
            if (empty.isFilter) btnCategoryEmptyResetFilter.show()
            else btnCategoryEmptyResetFilter.hide()

            btnCategoryEmptyResetFilter.setOnClickListener {
                emptyStateListener.resetFilter()
            }

        }
    }

    companion object {
        val LAYOUT = R.layout.item_deals_category_empty
    }
}