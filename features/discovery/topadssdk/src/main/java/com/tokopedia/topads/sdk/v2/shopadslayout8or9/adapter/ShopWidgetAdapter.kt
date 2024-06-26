package com.tokopedia.topads.sdk.v2.shopadslayout8or9.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.sdk.v2.shopadslayout8or9.factory.ShopWidgetFactory
import com.tokopedia.topads.sdk.v2.shopadslayout8or9.model.ShopWidgetItem
import com.tokopedia.topads.sdk.v2.shopadslayout8or9.viewholder.AbstractViewHolder

class ShopWidgetAdapter(private val shopWidgetFactory: ShopWidgetFactory) :
    RecyclerView.Adapter<AbstractViewHolder<ShopWidgetItem>>() {

    private val list:MutableList<ShopWidgetItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<ShopWidgetItem> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return shopWidgetFactory.createViewHolder(view, viewType) as AbstractViewHolder<ShopWidgetItem>
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<ShopWidgetItem>, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].type(shopWidgetFactory)
    }

    fun submitList(list: List<ShopWidgetItem>){
        val itemCount = itemCount
        this.list.clear()
        notifyItemRangeRemoved(0, itemCount)

        this.list.addAll(list)
        notifyItemRangeInserted(0, this.list.size)
    }
}
