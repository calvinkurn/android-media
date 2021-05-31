package com.tokopedia.seller.search.feature.initialsearch.view.viewholder.highlight

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemHighlightInitialSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.HistorySearchListener
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.item_chips_highlight_search.view.*

class ItemHighLightInitialChipsAdapter(private val highLightListener: HistorySearchListener) :
        RecyclerView.Adapter<ItemHighLightInitialChipsAdapter.ChipsListViewHolder>() {

    private var highLightChipsUiModel = mutableListOf<ItemHighlightInitialSearchUiModel>()

    fun setChipsHighlight(newHighLightChipsUiModelInitial: List<ItemHighlightInitialSearchUiModel>) {
        val callBack = HighlightSearchDiffUtil(highLightChipsUiModel, newHighLightChipsUiModelInitial)
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
        fun bind(data: ItemHighlightInitialSearchUiModel) {
            with(itemView) {
                chipsHighlightItem.apply {
                    centerText = true
                    chipText = data.title
                    chipSize = ChipsUnify.SIZE_MEDIUM
                    chipType = ChipsUnify.TYPE_NORMAL
                    setOnClickListener {
                        highLightListener.onHighlightItemClicked(data, adapterPosition)
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        background = ContextCompat.getDrawable(context, R.drawable.chips_ripple)
                    }
                }
            }
        }
    }
}