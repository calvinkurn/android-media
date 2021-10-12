package com.tokopedia.tokopedianow.categoryfilter.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.categoryfilter.presentation.uimodel.CategoryFilterChip
import com.tokopedia.tokopedianow.databinding.ItemCategoryFilterChipBinding
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.view.binding.viewBinding

class CategoryFilterChipViewHolder(
    itemView: View,
    private val onCLickFilterChip: ((CategoryFilterChip) -> Unit)?
) : AbstractViewHolder<CategoryFilterChip>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_category_filter_chip
    }

    private var binding: ItemCategoryFilterChipBinding? by viewBinding()

    override fun bind(data: CategoryFilterChip) {
        setupFilterChip(data)
        setOnClickListener(data)
    }

    fun resetChip() {
        binding?.chipFilter?.chipType = ChipsUnify.TYPE_NORMAL
    }

    private fun setupFilterChip(data: CategoryFilterChip) {
        binding?.chipFilter?.run {
            chipText = data.title
            chipType = data.chipType
        }
    }

    private fun setOnClickListener(data: CategoryFilterChip) {
        binding?.chipFilter?.apply {
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