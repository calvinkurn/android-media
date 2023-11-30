package com.tokopedia.content.product.preview.view.adapter.review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.content.product.preview.databinding.ItemReviewParentContentBinding
import com.tokopedia.content.product.preview.databinding.ItemReviewParentLoadingBinding
import com.tokopedia.content.product.preview.view.viewholder.review.ReviewParentContentViewHolder
import com.tokopedia.content.product.preview.view.viewholder.review.ReviewParentLoadingViewHolder

class ReviewParentAdapter : Adapter<ViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            TYPE_CONTENT -> {
                ReviewParentContentViewHolder(
                    ItemReviewParentContentBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent, false
                    )
                )
            }

            else -> {
                ReviewParentLoadingViewHolder(
                    ItemReviewParentLoadingBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent, false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_CONTENT -> (holder as ReviewParentContentViewHolder).bind()
            else -> (holder as ReviewParentLoadingViewHolder).bind()
        }
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
