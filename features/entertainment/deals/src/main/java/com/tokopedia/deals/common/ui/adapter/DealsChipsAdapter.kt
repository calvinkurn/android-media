package com.tokopedia.deals.common.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.deals.common.listener.DealsChipListener
import com.tokopedia.deals.common.ui.adapter.viewholder.ChipViewHolder
import com.tokopedia.deals.common.ui.dataview.ChipDataView

open class DealsChipsAdapter(private val chipListener: DealsChipListener) :
        RecyclerView.Adapter<ChipViewHolder>() {

    var chips: MutableList<ChipDataView> = mutableListOf()
        set(value) {
            val diff = DiffUtil.calculateDiff(ChipDiffCallback(field, value))
            field = value
            diff.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(ChipViewHolder.LAYOUT, parent, false)
        return ChipViewHolder(itemView, chipListener)
    }

    override fun getItemCount(): Int = chips.size

    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) {
        holder.bindData(chips[position])
    }

    private class ChipDiffCallback(
            private val oldProductCards: List<ChipDataView>,
            private val newProductCards: List<ChipDataView>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldProductCards.size

        override fun getNewListSize(): Int = newProductCards.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldProductCards[oldItemPosition].id == newProductCards[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldProductCards[oldItemPosition] == newProductCards[newItemPosition]
        }
    }
}