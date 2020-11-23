package com.tokopedia.sellerorder.filter.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterChipsUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.item_chips_som_filter.view.*

class SomFilterItemChipsAdapter(private val somFilterListener: SomFilterListener) : RecyclerView.Adapter<SomFilterItemChipsAdapter.ChipsListViewHolder>() {

    private var somFilterChipsUiModel = mutableListOf<SomFilterChipsUiModel>()
    private var nameFilter = ""

    fun setChipsFilter(newSomFilterChipsUiModel: List<SomFilterChipsUiModel>, nameFilter: String) {
        this.nameFilter = nameFilter
        if (newSomFilterChipsUiModel.size >= MAX_CHIPS_FILTER) {
            val maxChipsFilter = newSomFilterChipsUiModel.take(MAX_CHIPS_FILTER)
            val callBack = SomSubFilterDiffUtil(somFilterChipsUiModel, maxChipsFilter)
            val diffResult = DiffUtil.calculateDiff(callBack)
            somFilterChipsUiModel.clear()
            somFilterChipsUiModel.addAll(maxChipsFilter)
            diffResult.dispatchUpdatesTo(this)
        } else {
            val callBack = SomSubFilterDiffUtil(somFilterChipsUiModel, newSomFilterChipsUiModel)
            val diffResult = DiffUtil.calculateDiff(callBack)
            somFilterChipsUiModel.clear()
            somFilterChipsUiModel.addAll(newSomFilterChipsUiModel)
            diffResult.dispatchUpdatesTo(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipsListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chips_som_filter, parent, false)
        return ChipsListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return somFilterChipsUiModel.size
    }

    override fun onBindViewHolder(holder: ChipsListViewHolder, position: Int) {
        val data = somFilterChipsUiModel[position]
        holder.bind(data)
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
                                nameFilter,
                                adapterPosition,
                                chipType.orEmpty(),
                                data.name)
                    }
                }
            }
        }
    }

    companion object {
        const val MAX_CHIPS_FILTER = 5
    }
}