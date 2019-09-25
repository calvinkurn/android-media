package com.tokopedia.globalnavwidget

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

internal class GlobalNavWidgetAdapter(
        private val itemListener: GlobalNavWidgetListener
): RecyclerView.Adapter<GlobalNavWidgetViewHolder>() {

    private val itemList = mutableListOf<GlobalNavWidgetModel.Item>()

    fun setItemList(itemList: List<GlobalNavWidgetModel.Item>) {
        this.itemList.clear()
        this.itemList.addAll(itemList)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GlobalNavWidgetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(GlobalNavWidgetViewHolder.LAYOUT, parent, false)

        return GlobalNavWidgetViewHolder(view, itemListener)
    }

    override fun onBindViewHolder(holder: GlobalNavWidgetViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}