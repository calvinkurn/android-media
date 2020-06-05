package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.SortItemUiModel
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.item_chips.view.*

class SortListAdapter(private val topicSortFilterListener: TopicSortFilterListener.Sort): RecyclerView.Adapter<SortListAdapter.SortListViewHolder>() {

    var sortFilterListUiModel: List<SortItemUiModel>? = null

    fun setSortFilter(sortFilterListUiModel: List<SortItemUiModel>) {
        this.sortFilterListUiModel = sortFilterListUiModel
    }

    fun updatedSortFilter(position: Int) {
        val itemSelected = sortFilterListUiModel?.getOrNull(position)

        sortFilterListUiModel?.filter {
            it.isSelected
        }?.filterNot { it == itemSelected }?.onEach { it.isSelected = false }

        itemSelected?.isSelected = true
        notifyDataSetChanged()
    }

    fun resetSortFilter() {
        sortFilterListUiModel?.mapIndexed { index, sortItemUiModel ->
            sortItemUiModel.isSelected = index == 0
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chips, parent, false)
        return SortListViewHolder(view, topicSortFilterListener)
    }

    override fun getItemCount(): Int {
        return sortFilterListUiModel?.size ?: 0
    }

    override fun onBindViewHolder(holder: SortListViewHolder, position: Int) {
        sortFilterListUiModel?.get(position)?.let { holder.bind(it) }
    }

    class SortListViewHolder(itemView: View, private val topicSortFilterListener: TopicSortFilterListener.Sort): RecyclerView.ViewHolder(itemView) {
        fun bind(data: SortItemUiModel) {
            with(itemView) {
                chipsItem.apply {
                    centerText = true
                    chipText = data.title
                    chipSize = ChipsUnify.SIZE_MEDIUM
                    chipType = if(data.isSelected) {
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