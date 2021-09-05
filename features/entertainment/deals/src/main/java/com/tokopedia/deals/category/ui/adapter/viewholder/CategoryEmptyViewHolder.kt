package com.tokopedia.deals.category.ui.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.deals.R
import com.tokopedia.deals.category.listener.DealsCategoryEmptyListener
import com.tokopedia.deals.category.ui.dataview.CategoryEmptyDataView
import com.tokopedia.deals.databinding.ItemDealsCategoryEmptyBinding
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.kotlin.extensions.view.visible

class CategoryEmptyViewHolder(
    itemView: View,
    private val categoryEmptyListener: DealsCategoryEmptyListener
) : RecyclerView.ViewHolder(itemView) {

    fun bindData(categoryEmpty: CategoryEmptyDataView) {
        val binding = ItemDealsCategoryEmptyBinding.bind(itemView)
        binding.run {
            if (categoryEmpty.hasResetFilterButton) {
                btnCategoryEmptyResetFilter.visible()
                btnCategoryEmptyResetFilter.setOnClickListener { categoryEmptyListener.onFilterReset() }
            } else {
                btnCategoryEmptyResetFilter.gone()
            }
            imgCategoryEmpty.loadImageDrawable(R.drawable.img_category_empty)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_deals_category_empty
    }
}