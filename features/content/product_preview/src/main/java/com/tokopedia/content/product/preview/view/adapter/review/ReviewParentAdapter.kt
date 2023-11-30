package com.tokopedia.content.product.preview.view.adapter.review

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ReviewParentAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val _reviewParentList = mutableListOf<String>()
    private val reviewParentList: List<String>
        get() = _reviewParentList

    fun insertData(data: List<String>) {
        _reviewParentList.clear()
        _reviewParentList.addAll(data)
        notifyItemRangeInserted(0, reviewParentList.size)
    }

    fun updateData(data: List<String>) {
        val startPosition = reviewParentList.size
        _reviewParentList.addAll(data)
        notifyItemRangeInserted(startPosition, reviewParentList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int = reviewParentList.size

    override fun getItemViewType(position: Int): Int {
        return TYPE_CONTENT
    }

    companion object {
        private const val TYPE_CONTENT = 0
        private const val TYPE_LOADING = 1
    }

}
