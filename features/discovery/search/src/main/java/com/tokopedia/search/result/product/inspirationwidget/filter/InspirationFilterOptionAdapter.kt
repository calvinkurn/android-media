package com.tokopedia.search.result.product.inspirationwidget.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class InspirationFilterOptionAdapter(
    private val inspirationFilterListener: InspirationFilterListener,
): RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    private val itemList = mutableListOf<InspirationFilterOptionDataView>()

    fun setItemList(itemList: List<InspirationFilterOptionDataView>) {
        this.itemList.clear()
        this.itemList.addAll(itemList)

        notifyItemRangeInserted(0, itemList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(InspirationFilterOptionViewHolder.LAYOUT, parent, false)

        return InspirationFilterOptionViewHolder(view, inspirationFilterListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as InspirationFilterOptionViewHolder).bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
