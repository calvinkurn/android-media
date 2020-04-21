package com.tokopedia.product.manage.feature.list.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.view.model.FilterTabViewModel
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.item_product_manage_filter.view.*

class FilterTabViewHolder(
    itemView: View,
    private val listener: ProductFilterListener
) : AbstractViewHolder<FilterTabViewModel>(itemView) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.item_product_manage_filter
    }

    private val context by lazy { itemView.context }
    private val chipFilter by lazy { itemView.chipFilter }
    private val chipText by lazy { itemView.chipFilter.chip_text }

    override fun bind(filter: FilterTabViewModel) {
        setFilterTitle(filter)
        setOnClickListener(filter)
    }

    private fun setFilterTitle(filter: FilterTabViewModel) {
        chipText.text = context.getString(filter.titleId, filter.count)
    }

    private fun setOnClickListener(filter: FilterTabViewModel) {
        itemView.setOnClickListener {
            onClickProductFilter(filter)
            toggleSelectFilter()
        }
    }

    private fun onClickProductFilter(filter: FilterTabViewModel) {
        listener.onClickProductFilter(filter, this, chipText.text.toString())
    }

    private fun toggleSelectFilter() {
        val chipType = if(isFilterSelected()) {
            ChipsUnify.TYPE_NORMAL
        } else {
            ChipsUnify.TYPE_SELECTED
        }
        chipFilter.chipType = chipType
    }

    private fun isFilterSelected(): Boolean {
        return chipFilter.chipType == ChipsUnify.TYPE_SELECTED
    }

    fun resetFilter() {
        chipFilter.chipType = ChipsUnify.TYPE_NORMAL
    }

    interface ProductFilterListener {
        fun onClickMoreFilter(filter: FilterTabViewModel, tabName: String)
        fun onClickProductFilter(filter: FilterTabViewModel, viewHolder: FilterTabViewHolder, tabName: String)
    }
}