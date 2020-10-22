package com.tokopedia.search.result.presentation.view.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.search.result.presentation.model.InspirationCardOptionViewModel
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.InspirationCardOptionChipViewHolder
import com.tokopedia.search.result.presentation.view.listener.InspirationCardListener

class InspirationCardOptionAdapter(
        private val inspirationCardListener: InspirationCardListener
) : RecyclerView.Adapter<InspirationCardOptionChipViewHolder>() {

    private val itemList = mutableListOf<InspirationCardOptionViewModel>()

    fun setItemList(itemList: List<InspirationCardOptionViewModel>) {
        this.itemList.clear()
        this.itemList.addAll(itemList)

        notifyItemRangeInserted(0, itemList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InspirationCardOptionChipViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(InspirationCardOptionChipViewHolder.LAYOUT, parent, false)

        return InspirationCardOptionChipViewHolder(view, inspirationCardListener)
    }

    override fun onBindViewHolder(holder: InspirationCardOptionChipViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}