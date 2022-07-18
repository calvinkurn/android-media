package com.tokopedia.topads.sdk.shopwidgetthreeproducts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.sdk.shopwidgetthreeproducts.factory.ShopWidgetFactory
import com.tokopedia.topads.sdk.shopwidgetthreeproducts.model.ShopWidgetItem
import com.tokopedia.topads.sdk.shopwidgetthreeproducts.viewholder.AbstractViewHolder

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
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}