package com.tokopedia.search.result.presentation.view.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.search.result.presentation.model.SizeDataView
import com.tokopedia.search.result.presentation.model.SizeOptionDataView
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.InspirationSizeOptionViewHolder
import com.tokopedia.search.result.presentation.view.listener.InspirationSizeOptionListener

class InspirationSizeOptionAdapter(
        private val inspirationSizeOptionListener: InspirationSizeOptionListener,
): RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    private val itemList = mutableListOf<SizeOptionDataView>()
    private var inspirationSizedataView: SizeDataView? = null

    fun setItemList(itemList: List<SizeOptionDataView>) {
        this.itemList.clear()
        this.itemList.addAll(itemList)

        notifyItemRangeInserted(0, itemList.size)
    }

    fun setInspirationSizeDataView(dataview: SizeDataView) {
        inspirationSizedataView = dataview
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(InspirationSizeOptionViewHolder.LAYOUT, parent, false)

        return InspirationSizeOptionViewHolder(view, inspirationSizeOptionListener, this)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val isActive = inspirationSizedataView?.activeOptions?.contains(itemList[position].filters.value) ?: false
        (holder as InspirationSizeOptionViewHolder).bind(itemList[position], isActive)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}