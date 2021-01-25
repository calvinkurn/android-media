package com.tokopedia.review.feature.reviewdetail.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.review.R
import com.tokopedia.review.feature.reviewdetail.view.model.SortItemUiModel
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.item_chips.view.*

class SortListAdapter(private val topicSortFilterListener: TopicSortFilterListener.Sort) : RecyclerView.Adapter<SortListAdapter.SortListViewHolder>() {

    var sortFilterListUiModel = mutableListOf<SortItemUiModel>()

    fun setSortFilter(sortFilterListUiModel: List<SortItemUiModel>) {
        this.sortFilterListUiModel = sortFilterListUiModel.toMutableList()
        val callback = SortListDiffUtil(this.sortFilterListUiModel, sortFilterListUiModel)
        val diffResult = DiffUtil.calculateDiff(callback)
        this.sortFilterListUiModel.clear()
        this.sortFilterListUiModel.addAll(sortFilterListUiModel)
        diffResult.dispatchUpdatesTo(this)
    }

    fun updatedSortFilter(position: Int) {
        sortFilterListUiModel.mapIndexed { index, sortItemUiModel ->
            if(sortItemUiModel.isSelected) {
                sortItemUiModel.isSelected = false
                notifyItemChanged(index)
            }
        }

        val itemSelected = sortFilterListUiModel.getOrNull(position)
        itemSelected?.isSelected = true
        notifyItemChanged(position)
    }

    fun resetSortFilter() {
        val defaultSortIndex = 0
        sortFilterListUiModel.mapIndexed { index, sortItemUiModel ->
            if (index == defaultSortIndex) {
                sortFilterListUiModel.getOrNull(index)?.isSelected = true
                notifyItemChanged(index)
            } else if (sortItemUiModel.isSelected) {
                sortItemUiModel.isSelected = false
                notifyItemChanged(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chips, parent, false)
        return SortListViewHolder(view, topicSortFilterListener)
    }

    override fun getItemCount(): Int {
        return sortFilterListUiModel.size
    }

    override fun onBindViewHolder(holder: SortListViewHolder, position: Int) {
        val data = sortFilterListUiModel[position]
        holder.bind(data)
    }

    class SortListViewHolder(itemView: View, private val topicSortFilterListener: TopicSortFilterListener.Sort) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: SortItemUiModel) {
            with(itemView) {
                chipsItem.apply {
                    centerText = true
                    chipText = data.title
                    chipSize = ChipsUnify.SIZE_MEDIUM
                    chipType = if (data.isSelected) {
                        ChipsUnify.TYPE_SELECTED
                    } else {
                        ChipsUnify.TYPE_NORMAL
                    }
                    setOnClickListener {
                        topicSortFilterListener.onSortClicked(chipType.orEmpty(), adapterPosition)
                    }
                }
            }
        }
    }
}