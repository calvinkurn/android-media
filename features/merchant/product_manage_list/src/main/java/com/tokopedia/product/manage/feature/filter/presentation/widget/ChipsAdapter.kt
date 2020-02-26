package com.tokopedia.product.manage.feature.filter.presentation.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.design.item.DeletableItemView
import com.tokopedia.product.manage.oldlist.R

class ChipsAdapter(private val listener: ItemClickListener) : RecyclerView.Adapter<ChipsAdapter.ItemViewHolder>() {
    private var data: List<String> = listOf()

    companion object {
        const val MAXIMUM_CHIPS = 5
    }

    fun setData(data: List<String>) {
        if(data.size > MAXIMUM_CHIPS) {
            this.data = data.subList(0,MAXIMUM_CHIPS-1)
        } else {
            this.data = data
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chips, parent, false)
        return ItemViewHolder(itemView, listener)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ItemViewHolder(itemView: View, private val clickListener: ItemClickListener) : RecyclerView.ViewHolder(itemView) {
        private var chips: ChipWidget =  itemView.findViewById(R.id.chips_item)

        fun bind(name: String) {
            chips.bind(name)
        }
    }
}

interface ItemClickListener {
    fun onItemClicked()
}