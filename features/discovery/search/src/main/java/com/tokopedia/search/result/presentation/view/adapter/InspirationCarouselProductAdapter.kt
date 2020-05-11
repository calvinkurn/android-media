package com.tokopedia.search.result.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.search.result.presentation.model.InspirationCarouselViewModel
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.InspirationCarouselProductViewHolder
import com.tokopedia.search.result.presentation.view.listener.InspirationCarouselListener

class InspirationCarouselProductAdapter(
        private val inspirationCarouselListener: InspirationCarouselListener
) : RecyclerView.Adapter<InspirationCarouselProductViewHolder>() {

    private val itemList = mutableListOf<InspirationCarouselViewModel.Option>()

    fun setItemList(itemList: List<InspirationCarouselViewModel.Option>) {
        this.itemList.clear()
        this.itemList.addAll(itemList)

        notifyItemRangeInserted(0, itemList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InspirationCarouselProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(InspirationCarouselProductViewHolder.LAYOUT, parent, false)

        return InspirationCarouselProductViewHolder(view, inspirationCarouselListener)
    }

    override fun onBindViewHolder(holder: InspirationCarouselProductViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}