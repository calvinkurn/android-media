package com.tokopedia.content.product.preview.view.adapter.product

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ProductIndicatorAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val _productIndicatorList = mutableListOf<String>()
    private val productIndicatorList: List<String>
        get() = _productIndicatorList

    fun insertData(data: List<String>) {
        _productIndicatorList.clear()
        _productIndicatorList.addAll(data)
        notifyItemRangeInserted(0, productIndicatorList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int = productIndicatorList.size

}
