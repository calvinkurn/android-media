package com.tokopedia.deals.category.ui.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.deals.R
import com.tokopedia.deals.category.listener.DealsCategoryEmptyListener
import com.tokopedia.deals.category.ui.dataview.CategoryEmptyDataView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.kotlin.extensions.view.visible
import kotlinx.android.synthetic.main.item_deals_category_empty.view.*

class CategoryEmptyViewHolder(
    itemView: View,
    private val categoryEmptyListener: DealsCategoryEmptyListener
) : RecyclerView.ViewHolder(itemView) {

    fun bindData(categoryEmpty: CategoryEmptyDataView) {
        itemView.run {
            if (categoryEmpty.hasResetFilterButton) {
                btn_category_empty_reset_filter.visible()
                btn_category_empty_reset_filter.setOnClickListener { categoryEmptyListener.onFilterReset() }
            } else {
                btn_category_empty_reset_filter.gone()
            }
            img_category_empty.loadImageDrawable(R.drawable.img_category_empty)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_deals_category_empty
    }
}