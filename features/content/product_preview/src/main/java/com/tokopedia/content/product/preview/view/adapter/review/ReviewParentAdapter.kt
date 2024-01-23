package com.tokopedia.content.product.preview.view.adapter.review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.content.product.preview.databinding.ItemReviewParentContentBinding
import com.tokopedia.content.product.preview.databinding.ItemReviewParentLoadingBinding
import com.tokopedia.content.product.preview.view.uimodel.review.LikeUiState
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewContentUiModel
import com.tokopedia.content.product.preview.view.viewholder.review.ReviewParentContentViewHolder
import com.tokopedia.content.product.preview.view.viewholder.review.ReviewParentLoadingViewHolder

class ReviewParentAdapter(private val listener: ReviewParentContentViewHolder.Listener) :
    ListAdapter<ReviewContentUiModel, ViewHolder>(ReviewAdapterCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            TYPE_CONTENT -> {
                ReviewParentContentViewHolder.create(
                    ItemReviewParentContentBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    listener
                )
            }

            else -> {
                ReviewParentLoadingViewHolder(
                    ItemReviewParentLoadingBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder.itemViewType) {
            TYPE_CONTENT -> (holder as ReviewParentContentViewHolder).bind(item)
            else -> (holder as ReviewParentLoadingViewHolder).bind()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            payloads.forEach {
                when (val payload = it) {
                    is Payload.Like -> if (holder is ReviewParentContentViewHolder) {
                        holder.bindLike(
                            payload.state
                        )
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        // TODO: please adjust to other state as well
        return TYPE_CONTENT
    }

    companion object {
        private const val TYPE_CONTENT = 0
        private const val TYPE_LOADING = 1
    }

    sealed interface Payload {
        data class Like(val state: LikeUiState) : Payload
    }
}

internal class ReviewAdapterCallback : DiffUtil.ItemCallback<ReviewContentUiModel>() {
    override fun areItemsTheSame(oldItem: ReviewContentUiModel, newItem: ReviewContentUiModel): Boolean {
        return oldItem.reviewId == newItem.reviewId
    }

    override fun areContentsTheSame(oldItem: ReviewContentUiModel, newItem: ReviewContentUiModel): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: ReviewContentUiModel, newItem: ReviewContentUiModel): Any? {
        // TODO: changes in specified item please define
        return when {
            oldItem.likeState != newItem.likeState -> ReviewParentAdapter.Payload.Like(newItem.likeState)
            else -> super.getChangePayload(oldItem, newItem)
        }
    }
}
