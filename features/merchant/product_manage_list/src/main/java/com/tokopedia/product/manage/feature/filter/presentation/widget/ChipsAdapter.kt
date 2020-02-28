package com.tokopedia.product.manage.feature.filter.presentation.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.manage.oldlist.R

class ChipsAdapter(private val listener: ChipClickListener) : RecyclerView.Adapter<ChipsAdapter.ItemViewHolder>() {
    private var namesData: List<String> = listOf()
    private var selectionData: List<Boolean> = listOf()

    companion object {
        const val MAXIMUM_CHIPS = 5
    }

    fun setData(namesData: List<String>, selectionData: List<Boolean>) {
        if(namesData.size > MAXIMUM_CHIPS) {
            this.namesData = namesData.subList(0, MAXIMUM_CHIPS)
            this.selectionData = selectionData.subList(0, MAXIMUM_CHIPS)
        } else {
            this.namesData = namesData
            this.selectionData = selectionData
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chips, parent, false)
        return ItemViewHolder(itemView, listener)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(namesData[position], selectionData[position])
    }

    override fun getItemCount(): Int {
        return namesData.size
    }

    inner class ItemViewHolder(itemView: View,
                               private val clickListener: ChipClickListener) : RecyclerView.ViewHolder(itemView) {
        private var chips: ChipWidget =  itemView.findViewById(R.id.chips_item)

        fun bind(name: String, isSelected: Boolean) {
            chips.bind(name, clickListener, isSelected)
        }
    }
}