package com.tokopedia.tokopedianow.categoryfilter.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.categoryfilter.presentation.uimodel.CategoryFilterChip
import com.tokopedia.unifycomponents.ChipsUnify

class CategoryFilterChipViewHolder(
    itemView: View,
    private val onCLickFilterChip: ((CategoryFilterChip) -> Unit)?
) : AbstractViewHolder<CategoryFilterChip>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_category_filter_chip
    }

    private var chipFilter: ChipsUnify? = null

    init {
        chipFilter = itemView.findViewById(R.id.chip_filter)
    }

    override fun bind(data: CategoryFilterChip) {
        setChipText(data)
        setOnClickListener(data)
    }

    private fun setChipText(data: CategoryFilterChip) {
        chipFilter?.run {
            chipFilter?.chipText = data.title
            chipFilter?.chipType = data.chipType
            chipFilter?.requestLayout()
        }
    }

    private fun setOnClickListener(data: CategoryFilterChip) {
        chipFilter?.apply {
            setOnClickListener {
                chipType = if (chipType == ChipsUnify.TYPE_SELECTED) {
                    ChipsUnify.TYPE_NORMAL
                } else {
                    ChipsUnify.TYPE_SELECTED
                }
                onCLickFilterChip?.invoke(data)
            }
        }
    }
}