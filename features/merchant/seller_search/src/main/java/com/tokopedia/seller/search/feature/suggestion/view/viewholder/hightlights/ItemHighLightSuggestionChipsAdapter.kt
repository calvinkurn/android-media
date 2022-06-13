package com.tokopedia.seller.search.feature.suggestion.view.viewholder.hightlights

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.util.setRippleEffect
import com.tokopedia.seller.search.databinding.ItemChipsHighlightSearchBinding
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.HighlightSuggestionSearchListener
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.hightlights.ItemHighlightSuggestionSearchUiModel
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.view.binding.viewBinding

class ItemHighLightSuggestionChipsAdapter(private val highLightListener: HighlightSuggestionSearchListener) :
        RecyclerView.Adapter<ItemHighLightSuggestionChipsAdapter.ChipsListViewHolder>() {

    private var highLightChipsUiModel = mutableListOf<ItemHighlightSuggestionSearchUiModel>()

    fun setChipsHighlight(newHighLightChipsUiModelInitial: List<ItemHighlightSuggestionSearchUiModel>) {
        val callBack = HighlightSuggestionSearchDiffUtil(highLightChipsUiModel, newHighLightChipsUiModelInitial)
        val diffResult = DiffUtil.calculateDiff(callBack)
        highLightChipsUiModel.clear()
        highLightChipsUiModel.addAll(newHighLightChipsUiModelInitial)
        diffResult.dispatchUpdatesTo(this)
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

        private val binding: ItemChipsHighlightSearchBinding? by viewBinding()

        fun bind(data: ItemHighlightSuggestionSearchUiModel) {
            binding?.run {
                chipsHighlightItem.run {
                    centerText = true
                    chipText = data.title
                    chipSize = ChipsUnify.SIZE_MEDIUM
                    chipType = ChipsUnify.TYPE_NORMAL
                    setOnClickListener {
                        highLightListener.onHighlightItemClicked(data, adapterPosition)
                    }
                    setRippleEffect()
                }
            }
        }
    }
}