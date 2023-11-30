package com.tokopedia.content.product.preview.view.adapter.product

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ProductContentAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    
    private val _productContentList = mutableListOf<String>()
    private val productContentList: List<String>
        get() = _productContentList
    
    fun insertData(data: List<String>) {
        _productContentList.clear()
        _productContentList.addAll(data)
        notifyItemRangeInserted(0, productContentList.size)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int = productContentList.size

    override fun getItemViewType(position: Int): Int {
        return TYPE_IMAGE
    }
    
    companion object {
        private const val TYPE_IMAGE = 0 
        private const val TYPE_VIDEO = 1
    }

}
