package com.tokopedia.product.manage.feature.list.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.view.model.FilterViewModel
import com.tokopedia.product.manage.feature.list.view.model.FilterViewModel.*
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.item_product_manage_filter.view.*

class FilterViewHolder(
    itemView: View,
    private val listener: ProductFilterListener
) : AbstractViewHolder<FilterViewModel>(itemView) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.item_product_manage_filter
    }

    private val context by lazy { itemView.context }
    private val chipFilter by lazy { itemView.chipFilter }
    private val chipText by lazy { itemView.chipFilter.chip_text }

    override fun bind(filter: FilterViewModel) {
        setFilterTitle(filter)
        setFilterImage(filter)
        setOnClickListener(filter)
    }

    private fun setFilterTitle(filter: FilterViewModel) {
        chipText.text = context.getString(filter.titleId, filter.count)
    }

    private fun setFilterImage(filter: FilterViewModel) {
        filter.icon?.let {
            chipFilter.chipImageResource = ContextCompat.getDrawable(context, it)
        }
    }

    private fun setOnClickListener(filter: FilterViewModel) {
        itemView.setOnClickListener {
            onClickProductFilter(filter)
            toggleSelectFilter(filter)
        }
    }

    private fun onClickProductFilter(filter: FilterViewModel) {
        listener.onClickProductFilter(filter, this)
    }

    private fun toggleSelectFilter(filter: FilterViewModel) {
        if(filter !is MoreFilter) {
            val chipType = if(isFilterSelected()) {
                ChipsUnify.TYPE_NORMAL
            } else {
                ChipsUnify.TYPE_SELECTED
            }
            chipFilter.chipType = chipType
        }
    }

    private fun isFilterSelected(): Boolean {
        return chipFilter.chipType == ChipsUnify.TYPE_SELECTED
    }

    fun resetFilter() {
        chipFilter.chipType = ChipsUnify.TYPE_NORMAL
    }

    interface ProductFilterListener {
        fun onClickProductFilter(filter: FilterViewModel, viewHolder: FilterViewHolder)
    }
}