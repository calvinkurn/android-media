package com.tokopedia.sellerorder.filter.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterChipsUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.item_chips.view.*

class SomFilterItemChipsAdapter(private val somFilterListener: SomFilterListener) : RecyclerView.Adapter<SomFilterItemChipsAdapter.ChipsListViewHolder>() {

    private var somFilterData: SomFilterUiModel? = null
    private var somFilterChipsUiModel: List<SomFilterChipsUiModel>? = null

    fun setChipsFilter(somFilterUiModel: SomFilterUiModel) {
        if (somFilterUiModel.somFilterData.size >= MAX_CHIPS_FILTER) {
            this.somFilterData = somFilterUiModel
            somFilterChipsUiModel = somFilterUiModel.somFilterData.take(MAX_CHIPS_FILTER)
        } else {
            this.somFilterData = somFilterUiModel
            somFilterChipsUiModel = somFilterUiModel.somFilterData
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipsListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chips, parent, false)
        return ChipsListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return somFilterChipsUiModel?.size ?: 0
    }

    override fun onBindViewHolder(holder: ChipsListViewHolder, position: Int) {
        somFilterChipsUiModel?.get(position)?.let { holder.bind(it) }
    }

    inner class ChipsListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: SomFilterChipsUiModel) {
            with(itemView) {
                chipsItem.apply {
                    centerText = true
                    chipText = data.name
                    chipSize = ChipsUnify.SIZE_MEDIUM
                    chipType = if (data.isSelected) {
                        ChipsUnify.TYPE_SELECTED
                    } else {
                        ChipsUnify.TYPE_NORMAL
                    }
                    setOnClickListener {
                        somFilterListener.onFilterChipsClicked(data,
                                somFilterData?.nameFilter.orEmpty(),
                                adapterPosition,
                                chipType.orEmpty(),
                                data.name)
                    }
                }
            }
        }
    }

    companion object {
        const val MAX_CHIPS_FILTER = 6
    }
}