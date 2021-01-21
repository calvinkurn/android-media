package com.tokopedia.seller.search.feature.initialsearch.view.viewholder.highlight

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemHighlightSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.HistorySearchListener
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.item_chips_highlight_search.view.*

class ItemHighLightChipsAdapter(private val highLightListener: HistorySearchListener) :
        RecyclerView.Adapter<ItemHighLightChipsAdapter.ChipsListViewHolder>() {

    private var highLightChipsUiModel = mutableListOf<ItemHighlightSearchUiModel>()

    fun setChipsHighlight(newHighLightChipsUiModel: List<ItemHighlightSearchUiModel>) {
        if (newHighLightChipsUiModel.size >= MAX_CHIPS_FILTER) {
            val maxChipsHighlight = newHighLightChipsUiModel.take(MAX_CHIPS_FILTER)
            val callBack = HighlightSearchDiffUtil(highLightChipsUiModel, maxChipsHighlight)
            val diffResult = DiffUtil.calculateDiff(callBack)
            highLightChipsUiModel.clear()
            highLightChipsUiModel.addAll(maxChipsHighlight)
            diffResult.dispatchUpdatesTo(this)
        } else {
            val callBack = HighlightSearchDiffUtil(highLightChipsUiModel, newHighLightChipsUiModel)
            val diffResult = DiffUtil.calculateDiff(callBack)
            highLightChipsUiModel.clear()
            highLightChipsUiModel.addAll(newHighLightChipsUiModel)
            diffResult.dispatchUpdatesTo(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipsListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chips_highlight_search, parent, false)
        return ChipsListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return highLightChipsUiModel.size
    }

    override fun onBindViewHolder(holder: ChipsListViewHolder, position: Int) {
        val data = highLightChipsUiModel[position]
        holder.bind(data)
    }

    inner class ChipsListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: ItemHighlightSearchUiModel) {
            with(itemView) {
                chipsHighlightItem.apply {
                    centerText = true
                    chipText = data.title
                    chipSize = ChipsUnify.SIZE_MEDIUM
                    chipType = ChipsUnify.TYPE_NORMAL
                    setOnClickListener {
                        highLightListener.onHighlightItemClicked(data, adapterPosition)
                    }
                }
            }
        }
    }

    companion object {
        const val MAX_CHIPS_FILTER = 5
    }
}