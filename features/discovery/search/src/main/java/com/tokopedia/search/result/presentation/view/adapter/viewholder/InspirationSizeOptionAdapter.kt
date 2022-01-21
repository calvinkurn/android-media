package com.tokopedia.search.result.presentation.view.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.search.result.presentation.model.InspirationSizeDataView
import com.tokopedia.search.result.presentation.model.InspirationSizeOptionDataView
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.InspirationSizeOptionViewHolder
import com.tokopedia.search.result.presentation.view.listener.InspirationSizeOptionListener

class InspirationSizeOptionAdapter(
        private val inspirationSizeOptionListener: InspirationSizeOptionListener,
): RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    private val itemList = mutableListOf<InspirationSizeOptionDataView>()
    private var inspirationSizedataView: InspirationSizeDataView? = null

    fun setItemList(itemList: List<InspirationSizeOptionDataView>) {
        this.itemList.clear()
        this.itemList.addAll(itemList)

        notifyItemRangeInserted(0, itemList.size)
    }

    fun setInspirationSizeDataView(dataview: InspirationSizeDataView) {
        inspirationSizedataView = dataview
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(InspirationSizeOptionViewHolder.LAYOUT, parent, false)

        return InspirationSizeOptionViewHolder(view, inspirationSizeOptionListener, this)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as InspirationSizeOptionViewHolder).bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}