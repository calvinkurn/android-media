package com.tokopedia.tokomart.searchcategory.presentation.viewholder

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.searchcategory.presentation.customview.FilterChip
import com.tokopedia.tokomart.searchcategory.presentation.listener.CategoryFilterListener
import com.tokopedia.tokomart.searchcategory.presentation.model.CategoryFilterDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.CategoryFilterItemDataView

class CategoryFilterViewHolder(
        itemView: View,
        private val categoryFilterListener: CategoryFilterListener
): AbstractViewHolder<CategoryFilterDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokomart_search_category_category_filter
    }

    private val filterRecyclerView: RecyclerView? = itemView.findViewById(
            R.id.tokomartSearchCategoryCategoryFilterRecyclerView
    )

    override fun bind(element: CategoryFilterDataView) {
        val context = itemView.context ?: return
        val filterRecyclerView = filterRecyclerView ?: return

        filterRecyclerView.layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
        filterRecyclerView.adapter = Adapter(element.categoryFilterItemList, categoryFilterListener)

        if (filterRecyclerView.itemDecorationCount == 0) {
            val unifySpace16 = com.tokopedia.unifyprinciples.R.dimen.unify_space_16
            val spacing = context.resources.getDimensionPixelSize(unifySpace16)
            filterRecyclerView.addItemDecoration(ItemDecoration(spacing))
        }
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
            val LAYOUT = R.layout.item_tokomart_search_category_category_filter_chips
        }

        private val filterChip: FilterChip? = itemView.findViewById(R.id.tokomartSearchCategoryCategoryFilterChip)

        fun bind(categoryFilterItemDataView: CategoryFilterItemDataView) {
            val option = categoryFilterItemDataView.option

            filterChip?.setContent(
                    FilterChip.Model(
                            imageUrl = option.iconUrl,
                            title = option.name,
                            state = categoryFilterItemDataView.isSelected
                    )
            )

            filterChip?.listener = object: FilterChip.Listener {
                override fun onClick(state: Boolean) {
                    categoryFilterListener.onCategoryFilterChipClick(option, state)
                }
            }
        }
    }

    private class ItemDecoration(private val spacing: Int): RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val adapter = parent.adapter ?: return

            when (parent.getChildAdapterPosition(view)) {
                0 -> {
                    outRect.left = spacing
                    outRect.right = spacing / 4
                }
                (adapter.itemCount - 1) -> {
                    outRect.left = spacing / 4
                    outRect.right = spacing
                }
                else -> {
                    outRect.left = spacing / 4
                    outRect.right = spacing / 4
                }
            }
        }
    }
}