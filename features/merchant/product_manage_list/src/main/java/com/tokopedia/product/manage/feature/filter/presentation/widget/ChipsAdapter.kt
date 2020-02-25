package com.tokopedia.product.manage.feature.filter.presentation.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.item.DeletableItemView
import com.tokopedia.product.manage.oldlist.R

class ChipsAdapter(private val listener: ItemClickListener) : RecyclerView.Adapter<ChipsAdapter.ItemViewHolder>() {
    private var data: List<String> = listOf()

    fun setData(data: List<String>) {
        this.data = data
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
        private var textView: DeletableItemView = itemView.findViewById(R.id.chips_item)

        fun bind(name: String) {
            textView.setItemName(name)
            textView.setOnTextClickListener {
                clickListener.onItemClicked()
            }
        }
    }
}

interface ItemClickListener {
    fun onItemClicked()
}