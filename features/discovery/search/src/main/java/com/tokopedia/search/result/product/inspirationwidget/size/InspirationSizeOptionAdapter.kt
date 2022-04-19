package com.tokopedia.search.result.product.inspirationwidget.size

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class InspirationSizeOptionAdapter(
    private val inspirationSizeListener: InspirationSizeListener,
): RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    private val itemList = mutableListOf<InspirationSizeOptionDataView>()
    private var inspirationSizeDataView: InspirationSizeDataView? = null

    fun setItemList(itemList: List<InspirationSizeOptionDataView>) {
        this.itemList.clear()
        this.itemList.addAll(itemList)

        notifyItemRangeInserted(0, itemList.size)
    }

    fun setInspirationSizeDataView(dataView: InspirationSizeDataView) {
        inspirationSizeDataView = dataView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(InspirationSizeOptionViewHolder.LAYOUT, parent, false)

        return InspirationSizeOptionViewHolder(view, inspirationSizeListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as InspirationSizeOptionViewHolder).bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}