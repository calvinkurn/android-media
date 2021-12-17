package com.tokopedia.tokopedianow.categoryfilter.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.categoryfilter.presentation.adapter.CategoryFilterChipAdapter.*
import com.tokopedia.tokopedianow.categoryfilter.presentation.adapter.differ.CategoryFilterChipDiffer
import com.tokopedia.tokopedianow.categoryfilter.presentation.uimodel.CategoryFilterChip
import com.tokopedia.tokopedianow.categoryfilter.presentation.viewholder.CategoryFilterChipViewHolder
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter

class CategoryFilterChipAdapter(
    typeFactory: CategoryFilterChipAdapterTypeFactory
) : BaseTokopediaNowListAdapter<CategoryFilterChip, CategoryFilterChipAdapterTypeFactory>(
    typeFactory, CategoryFilterChipDiffer()
) {

    class CategoryFilterChipAdapterTypeFactory(
        private val onCLickFilterChip: ((CategoryFilterChip) -> Unit)?
    ) : BaseAdapterTypeFactory(), CategoryFilterChipTypeFactory {

        override fun type(uiModel: CategoryFilterChip): Int = CategoryFilterChipViewHolder.LAYOUT

        override fun createViewHolder(
            parent: View,
            type: Int
        ): AbstractViewHolder<out Visitable<*>> {
            return when (type) {
                CategoryFilterChipViewHolder.LAYOUT -> CategoryFilterChipViewHolder(parent, onCLickFilterChip)
                else -> super.createViewHolder(parent, type)
            }
        }
    }

    interface CategoryFilterChipTypeFactory {
        fun type(uiModel: CategoryFilterChip): Int
    }
}