package com.tokopedia.globalnavwidget

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

internal class GlobalNavWidgetCardAdapter(
        private val itemListener: GlobalNavWidgetListener
): RecyclerView.Adapter<GlobalNavWidgetCardViewHolder>() {

    private val itemList = mutableListOf<GlobalNavWidgetModel.Item>()

    fun setItemList(itemList: List<GlobalNavWidgetModel.Item>) {
        this.itemList.clear()
        this.itemList.addAll(itemList)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GlobalNavWidgetCardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(GlobalNavWidgetCardViewHolder.LAYOUT, parent, false)

        return GlobalNavWidgetCardViewHolder(view, itemListener)
    }

    override fun onBindViewHolder(holder: GlobalNavWidgetCardViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}