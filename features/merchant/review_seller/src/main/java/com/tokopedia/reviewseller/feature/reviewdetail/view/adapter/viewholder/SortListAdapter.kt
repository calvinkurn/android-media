package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.TopicSortFilterListener
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.SortItemUiModel
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.item_chips.view.*

class SortListAdapter(private val topicSortFilterListener: TopicSortFilterListener): RecyclerView.Adapter<SortListAdapter.SortListViewHolder>() {

    private var sortFilterListUiModel: List<SortItemUiModel>? = null

    fun setSortFilter(sortFilterListUiModel: List<SortItemUiModel>) {
        this.sortFilterListUiModel = sortFilterListUiModel
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

    class SortListViewHolder(itemView: View, private val topicSortFilterListener: TopicSortFilterListener): RecyclerView.ViewHolder(itemView) {
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
                        topicSortFilterListener.onSortClicked(data, adapterPosition)
                    }
                }
            }
        }
    }
}