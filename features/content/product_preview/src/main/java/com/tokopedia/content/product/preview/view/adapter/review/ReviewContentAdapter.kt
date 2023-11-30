package com.tokopedia.content.product.preview.view.adapter.review

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ReviewContentAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val _reviewContentList = mutableListOf<String>()
    private val reviewContentList: List<String>
        get() = _reviewContentList

    fun insertData(data: List<String>) {
        _reviewContentList.clear()
        _reviewContentList.addAll(data)
        notifyItemRangeInserted(0, reviewContentList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int = reviewContentList.size

    override fun getItemViewType(position: Int): Int {
        return TYPE_IMAGE
    }

    companion object {
        private const val TYPE_IMAGE = 0
        private const val TYPE_VIDEO = 1
    }

}

