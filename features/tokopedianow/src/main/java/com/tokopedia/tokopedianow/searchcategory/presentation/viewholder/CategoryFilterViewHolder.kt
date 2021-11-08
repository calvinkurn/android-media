package com.tokopedia.tokopedianow.searchcategory.presentation.viewholder

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowSearchCategoryCategoryFilterBinding
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowSearchCategoryCategoryFilterChipsBinding
import com.tokopedia.tokopedianow.searchcategory.presentation.customview.FilterChip
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.CategoryFilterListener
import com.tokopedia.tokopedianow.searchcategory.presentation.model.CategoryFilterDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.CategoryFilterItemDataView
import com.tokopedia.utils.view.binding.viewBinding

class CategoryFilterViewHolder(
        itemView: View,
        private val categoryFilterListener: CategoryFilterListener
): AbstractViewHolder<CategoryFilterDataView>(itemView) {

    companion object {
        const val NO_ITEM_DECORATION_COUNT = 0

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_search_category_category_filter
    }

    private var binding: ItemTokopedianowSearchCategoryCategoryFilterBinding? by viewBinding()

    override fun bind(element: CategoryFilterDataView) {
        val context = itemView.context ?: return
        val filterRecyclerView = binding?.tokoNowSearchCategoryCategoryFilterRecyclerView ?: return

        filterRecyclerView.layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
        filterRecyclerView.adapter = Adapter(element.categoryFilterItemList, categoryFilterListener)
        filterRecyclerView.scrollToSelected(element)

        if (filterRecyclerView.itemDecorationCount == NO_ITEM_DECORATION_COUNT) {
            val unifySpace16 = com.tokopedia.unifyprinciples.R.dimen.unify_space_16
            val spacing = context.resources.getDimensionPixelSize(unifySpace16)
            filterRecyclerView.addItemDecoration(ItemDecoration(spacing))
        }
    }

    private fun RecyclerView.scrollToSelected(element: CategoryFilterDataView) {
        val categoryFilterItemList = element.categoryFilterItemList
        val selectedFilterIndex = categoryFilterItemList.indexOfFirst { it.isSelected }

        if (selectedFilterIndex in categoryFilterItemList.indices)
            scrollToPosition(selectedFilterIndex)
    }

    private class Adapter(
            private val list: List<CategoryFilterItemDataView>,
            private val categoryFilterListener: CategoryFilterListener,
    ): RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                    ViewHolder.LAYOUT,
                    parent,
                    false,
            )

            return ViewHolder(view, categoryFilterListener)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if (position !in list.indices) return

            holder.bind(list[position])
        }

        override fun getItemCount() = list.size
    }

    private class ViewHolder(
            itemView: View,
            private val categoryFilterListener: CategoryFilterListener,
    ): RecyclerView.ViewHolder(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_tokopedianow_search_category_category_filter_chips
        }

        private var binding: ItemTokopedianowSearchCategoryCategoryFilterChipsBinding? by viewBinding()

        fun bind(categoryFilterItemDataView: CategoryFilterItemDataView) {
            val option = categoryFilterItemDataView.option

            binding?.tokoNowSearchCategoryCategoryFilterChip?.apply {
                setContent(
                    FilterChip.Model(
                        imageUrl = option.iconUrl,
                        title = option.name,
                        state = categoryFilterItemDataView.isSelected
                    )
                )

                listener = object: FilterChip.Listener {
                    override fun onClick(state: Boolean) {
                        categoryFilterListener.onCategoryFilterChipClick(option, state)
                    }
                }
            }
        }
    }

    private class ItemDecoration(private val spacing: Int): RecyclerView.ItemDecoration() {

        companion object {
            const val FIRST_CHILD_ADAPTER_POSITION = 0
        }

        override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State,
        ) {
            val adapter = parent.adapter ?: return

            when (parent.getChildAdapterPosition(view)) {
                FIRST_CHILD_ADAPTER_POSITION -> {
                    outRect.left = spacing
                    outRect.right = quarterSpacingOffset()
                }
                getLastItemIndexInAdapter(adapter) -> {
                    outRect.left = quarterSpacingOffset()
                    outRect.right = spacing
                }
                else -> {
                    outRect.left = quarterSpacingOffset()
                    outRect.right = quarterSpacingOffset()
                }
            }
        }

        @Suppress("MagicNumber")
        private fun getLastItemIndexInAdapter(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) = (adapter.itemCount - 1)

        @Suppress("MagicNumber")
        private fun quarterSpacingOffset() = spacing / 4
    }
}