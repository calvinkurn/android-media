package com.tokopedia.content.product.preview.view.adapter.review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.content.product.preview.databinding.ItemReviewContentImageBinding
import com.tokopedia.content.product.preview.databinding.ItemReviewContentLoadingBinding
import com.tokopedia.content.product.preview.databinding.ItemReviewContentVideoBinding
import com.tokopedia.content.product.preview.view.viewholder.review.ReviewContentImageViewHolder
import com.tokopedia.content.product.preview.view.viewholder.review.ReviewContentLoadingViewHolder
import com.tokopedia.content.product.preview.view.viewholder.review.ReviewContentVideoViewHolder

class ReviewContentAdapter : Adapter<ViewHolder>() {

    private val _reviewContentList = mutableListOf<String>()
    private val reviewContentList: List<String>
        get() = _reviewContentList

    fun insertData(data: List<String>) {
        _reviewContentList.clear()
        _reviewContentList.addAll(data)
        notifyItemRangeInserted(0, reviewContentList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            TYPE_IMAGE -> {
                ReviewContentImageViewHolder(
                    ItemReviewContentImageBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent, false
                    )
                )
            }

            TYPE_VIDEO -> {
                ReviewContentVideoViewHolder(
                    ItemReviewContentVideoBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent, false
                    )
                )
            }

            else -> {
                ReviewContentLoadingViewHolder(
                    ItemReviewContentLoadingBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent, false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_IMAGE -> (holder as ReviewContentImageViewHolder).bind()
            TYPE_VIDEO -> (holder as ReviewContentVideoViewHolder).bind()
            else -> (holder as ReviewContentLoadingViewHolder).bind()
        }
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

