package com.tokopedia.filter.bottomsheet.sort

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.filter.R
import com.tokopedia.filter.common.helper.ChipSpacingItemDecoration
import com.tokopedia.filter.common.helper.addItemDecorationIfNotExists
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.sort_filter_bottom_sheet_chips_layout.view.*
import kotlinx.android.synthetic.main.sort_filter_bottom_sheet_sort_view_holder.view.*

internal class SortViewHolder(
        itemView: View,
        private val sortViewListener: SortViewListener
): AbstractViewHolder<SortViewModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.sort_filter_bottom_sheet_sort_view_holder
    }

    private val layoutManager = ChipsLayoutManager
            .newBuilder(itemView.context)
            .setOrientation(ChipsLayoutManager.HORIZONTAL)
            .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
            .build()

    private val spacingItemDecoration = ChipSpacingItemDecoration(
            itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8),
            itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)
    )

    init {
        itemView.sortItemRecyclerView?.let {
            it.layoutManager = layoutManager
            it.isNestedScrollingEnabled = false
            it.addItemDecorationIfNotExists(spacingItemDecoration)
        }
    }

    override fun bind(element: SortViewModel) {
        itemView.sortItemRecyclerView?.swapAdapter(SortItemAdapter(element.sortItemViewModelList, sortViewListener), false)
    }

    private class SortItemAdapter(
            val sortItemList: List<SortItemViewModel>,
            val sortViewListener: SortViewListener
    ): RecyclerView.Adapter<SortItemViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortItemViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.sort_filter_bottom_sheet_chips_layout, parent, false)
            return SortItemViewHolder(view, sortViewListener)
        }

        override fun getItemCount() = sortItemList.size

        override fun onBindViewHolder(holder: SortItemViewHolder, position: Int) {
            holder.bind(sortItemList[position])
        }
    }

    private class SortItemViewHolder(
            itemView: View,
            private val sortViewListener: SortViewListener
    ): RecyclerView.ViewHolder(itemView) {

        fun bind(sortItemViewModel: SortItemViewModel) {
            itemView.sortFilterChipsUnify?.chipText = sortItemViewModel.sort.name
            itemView.sortFilterChipsUnify?.chipType = ChipsUnify.TYPE_NORMAL
            itemView.sortFilterChipsUnify?.chipSize = ChipsUnify.SIZE_MEDIUM
            itemView.sortFilterChipsUnify?.chipType =
                    if (sortItemViewModel.isSelected) ChipsUnify.TYPE_SELECTED
                    else ChipsUnify.TYPE_NORMAL
            itemView.sortFilterChipsUnify?.setOnClickListener {
                sortViewListener.onSortItemClick(sortItemViewModel)
            }
        }
    }
}