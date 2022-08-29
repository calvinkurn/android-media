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
import com.tokopedia.filter.databinding.SortFilterBottomSheetChipsLayoutBinding
import com.tokopedia.filter.databinding.SortFilterBottomSheetSortViewHolderBinding
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.view.binding.viewBinding

internal class SortViewHolder(
        itemView: View,
        private val sortViewListener: SortViewListener
): AbstractViewHolder<SortViewModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.sort_filter_bottom_sheet_sort_view_holder
    }
    private var binding: SortFilterBottomSheetSortViewHolderBinding? by viewBinding()

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
        binding?.sortItemRecyclerView?.let {
            it.layoutManager = layoutManager
            it.isNestedScrollingEnabled = false
            it.addItemDecorationIfNotExists(spacingItemDecoration)
        }
    }

    override fun bind(element: SortViewModel) {
        binding?.sortItemRecyclerView?.swapAdapter(SortItemAdapter(element.sortItemViewModelList, sortViewListener), false)
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
        private var binding: SortFilterBottomSheetChipsLayoutBinding? by viewBinding()

        fun bind(sortItemViewModel: SortItemViewModel) {
            val binding = binding?: return
            binding.sortFilterChipsUnify.chipText = sortItemViewModel.sort.name
            binding.sortFilterChipsUnify.chipType = ChipsUnify.TYPE_NORMAL
            binding.sortFilterChipsUnify.chipSize = ChipsUnify.SIZE_MEDIUM
            binding.sortFilterChipsUnify.chipType =
                    if (sortItemViewModel.isSelected) ChipsUnify.TYPE_SELECTED
                    else ChipsUnify.TYPE_NORMAL
            binding.sortFilterChipsUnify.setOnClickListener {
                sortViewListener.onSortItemClick(sortItemViewModel)
            }
        }
    }
}