package com.tokopedia.shop.score.penalty.presentation.adapter.filter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.penalty.presentation.adapter.FilterPenaltyBottomSheetListener
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterUiModel
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.item_chips_penalty_filter.view.*

class ItemChipsFilterPenaltyAdapter(private val filterPenaltyBottomSheetListener: FilterPenaltyBottomSheetListener,
                                    private val nameFilter: String
): RecyclerView.Adapter<ItemChipsFilterPenaltyAdapter.ItemChipsFilterPenaltyViewHolder>() {

    private var itemChipsFilterPenaltyList = mutableListOf<PenaltyFilterUiModel.ChipsFilterPenaltyUiModel>()

    fun setItemChipsFilterPenaltyList(data: List<PenaltyFilterUiModel.ChipsFilterPenaltyUiModel>){
        if (data.isNullOrEmpty()) return
        itemChipsFilterPenaltyList.clear()
        itemChipsFilterPenaltyList.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemChipsFilterPenaltyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chips_penalty_filter, parent, false)
        return ItemChipsFilterPenaltyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemChipsFilterPenaltyViewHolder, position: Int) {
       holder.bind(itemChipsFilterPenaltyList.getOrNull(position) ?: PenaltyFilterUiModel.ChipsFilterPenaltyUiModel())
    }

    override fun getItemCount(): Int = itemChipsFilterPenaltyList.size

    inner class ItemChipsFilterPenaltyViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun bind(data: PenaltyFilterUiModel.ChipsFilterPenaltyUiModel) {
            with(itemView) {
                chipsItemPenalty?.apply {
                    centerText = true
                    chipText = data.title
                    chipSize = ChipsUnify.SIZE_MEDIUM
                    chipType = if (data.isSelected) {
                        ChipsUnify.TYPE_SELECTED
                    } else {
                        ChipsUnify.TYPE_NORMAL
                    }
                    setOnClickListener {
                        filterPenaltyBottomSheetListener.onChipsFilterItemClick(nameFilter, chipType.orEmpty(),
                                chipsItemPenalty.chipText.orEmpty(),
                                adapterPosition)
                    }
                }
            }
        }
    }
}