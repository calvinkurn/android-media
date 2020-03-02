package com.tokopedia.product.manage.feature.filter.presentation.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterDataViewModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterViewModel

class ChipsAdapter(private val listener: ChipClickListener) : RecyclerView.Adapter<ChipsAdapter.ItemViewHolder>() {
    private var namesData: MutableList<String> = mutableListOf()
    private var selectionData: MutableList<Boolean> = mutableListOf()

    companion object {
        const val MAXIMUM_CHIPS = 5
    }

    fun setData(element: FilterViewModel) {
        this.namesData.clear()
        this.selectionData.clear()
        val dataToDisplay: List<FilterDataViewModel> = if(element.data.size > MAXIMUM_CHIPS) {
            element.data.subList(0, MAXIMUM_CHIPS)
        } else {
            element.data
        }
        for(data in dataToDisplay) {
            this.namesData.add(data.name)
            this.selectionData.add(data.select)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(com.tokopedia.product.manage.R.layout.item_chips, parent, false)
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
        private var chips: ChipWidget =  itemView.findViewById(com.tokopedia.product.manage.R.id.chips_item)

        fun bind(name: String, isSelected: Boolean) {
            chips.bind(name, clickListener, isSelected)
        }
    }
}