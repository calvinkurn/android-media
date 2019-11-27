package com.tokopedia.globalnavwidget

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

internal class GlobalNavWidgetPillAdapter(
        private val itemListener: GlobalNavWidgetListener
): RecyclerView.Adapter<GlobalNavWidgetPillViewHolder>() {

    private val itemList = mutableListOf<GlobalNavWidgetModel.Item>()

    fun setItemList(itemList: List<GlobalNavWidgetModel.Item>) {
        this.itemList.clear()
        this.itemList.addAll(itemList)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GlobalNavWidgetPillViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(GlobalNavWidgetPillViewHolder.LAYOUT, parent, false)

        return GlobalNavWidgetPillViewHolder(view, itemListener)
    }

    override fun onBindViewHolder(holder: GlobalNavWidgetPillViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}