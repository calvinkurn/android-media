package com.tokopedia.product.manage.feature.filter.presentation.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterDataViewModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterViewModel

class ChipsAdapter(private val listener: ChipClickListener, private val canSelectMany: Boolean, private val title: String = "") : RecyclerView.Adapter<ChipsAdapter.ItemViewHolder>() {
    private var data: MutableList<FilterDataViewModel> = mutableListOf()

    companion object {
        const val MAXIMUM_CHIPS = 5
    }

    fun setData(element: FilterViewModel) {
        this.data.clear()
        var numSelected = 0
        element.data.forEach {
            if(it.select) numSelected++
        }
        var dataToDisplay = element.data
        if(numSelected < MAXIMUM_CHIPS) {
            if(element.data.size > MAXIMUM_CHIPS) {
                dataToDisplay = element.data.subList(0, MAXIMUM_CHIPS)
            }
        } else {
            dataToDisplay = element.data.subList(0, numSelected)
        }
        data = dataToDisplay
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(com.tokopedia.product.manage.R.layout.item_chips, parent, false)
        return ItemViewHolder(itemView, listener)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ItemViewHolder(itemView: View,
                               private val clickListener: ChipClickListener) : RecyclerView.ViewHolder(itemView) {
        private var chips: ChipWidget =  itemView.findViewById(com.tokopedia.product.manage.R.id.chips_item)

        fun bind(element: FilterDataViewModel) {
            chips.bind(element, clickListener, canSelectMany, title)
        }
    }
}