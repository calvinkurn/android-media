package com.tokopedia.review.feature.reviewdetail.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.review.R
import com.tokopedia.review.feature.reviewdetail.view.model.SortFilterItemWrapper
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.item_chips.view.*

class TopicListAdapter(private val topicSortFilterListener: TopicSortFilterListener.Topic) : RecyclerView.Adapter<TopicListAdapter.TopicListViewHolder>() {

    var sortFilterList = mutableListOf<SortFilterItemWrapper>()

    fun setTopicFilter(sortFilterList: List<SortFilterItemWrapper>) {
        this.sortFilterList = sortFilterList.toMutableList()
        val callback = TopicListDiffUtil(this.sortFilterList, sortFilterList)
        val diffResult = DiffUtil.calculateDiff(callback)
        this.sortFilterList.clear()
        this.sortFilterList.addAll(sortFilterList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateTopicFilter(updatedState: Boolean, position: Int) {
        val isSelected = sortFilterList.getOrNull(position)
        sortFilterList.map { sortItemUiModel ->
            if(isSelected == sortItemUiModel) {
                sortItemUiModel.isSelected = !updatedState
                notifyItemChanged(position)
            }
        }
    }

    fun resetSortFilter() {
        sortFilterList.mapIndexed { index, sortItemUiModel ->
            if(sortItemUiModel.isSelected) {
                sortItemUiModel.isSelected = false
                notifyItemChanged(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chips, parent, false)
        return TopicListViewHolder(view, topicSortFilterListener)
    }

    override fun getItemCount(): Int {
        return sortFilterList.size
    }

    override fun onBindViewHolder(holder: TopicListViewHolder, position: Int) {
        sortFilterList.getOrNull(position)?.let { holder.bind(it) }
    }

    class TopicListViewHolder(itemView: View, private val topicSortFilterListener: TopicSortFilterListener.Topic) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: SortFilterItemWrapper) {
            with(itemView) {
                chipsItem.apply {
                    centerText = true
                    chipText = "${data.titleUnformated} (${data.count})"
                    chipSize = ChipsUnify.SIZE_MEDIUM
                    chipType = if (data.isSelected) {
                        ChipsUnify.TYPE_SELECTED
                    } else {
                        ChipsUnify.TYPE_NORMAL
                    }
                    setOnClickListener {
                        topicSortFilterListener.onTopicClicked(chipType.orEmpty(), adapterPosition)
                    }
                }
            }
        }
    }
}