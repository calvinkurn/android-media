package com.tokopedia.content.product.preview.view.adapter.review

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.content.product.preview.view.listener.ReviewInteractionListener
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewContentUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewLikeUiState
import com.tokopedia.content.product.preview.view.viewholder.review.ReviewParentContentViewHolder

class ReviewParentAdapter(
    private val reviewInteractionListener: ReviewInteractionListener
) : ListAdapter<ReviewContentUiModel, ViewHolder>(ReviewAdapterCallback()) {

    private val mediasViewPool: RecycledViewPool = RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            TYPE_CONTENT -> {
                ReviewParentContentViewHolder.create(
                    parent = parent,
                    reviewInteractionListener = reviewInteractionListener,
                    mediasViewPool = mediasViewPool
                )
            }
            else -> super.createViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder.itemViewType) {
            TYPE_CONTENT -> (holder as ReviewParentContentViewHolder).bind(item)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            payloads.forEach {
                when (val payload = it) {
                    is Payload.Like -> {
                        (holder as ReviewParentContentViewHolder).bindLike(payload.state)
                    }
                    is Payload.WatchMode -> {
                        (holder as ReviewParentContentViewHolder).bindWatchMode(payload.isWatchMode)
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return TYPE_CONTENT
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        (holder as ReviewParentContentViewHolder).onRecycled()
    }

    companion object {
        private const val TYPE_CONTENT = 0
    }

    sealed interface Payload {
        data class Like(val state: ReviewLikeUiState) : Payload
        data class WatchMode(val isWatchMode: Boolean) : Payload
    }

    internal class ReviewAdapterCallback : DiffUtil.ItemCallback<ReviewContentUiModel>() {
        override fun areItemsTheSame(
            oldItem: ReviewContentUiModel,
            newItem: ReviewContentUiModel
        ): Boolean {
            return oldItem.reviewId == newItem.reviewId
        }

        override fun areContentsTheSame(
            oldItem: ReviewContentUiModel,
            newItem: ReviewContentUiModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(
            oldItem: ReviewContentUiModel,
            newItem: ReviewContentUiModel
        ): Any? {
            return when {
                oldItem.likeState != newItem.likeState -> Payload.Like(newItem.likeState)
                oldItem.isWatchMode != newItem.isWatchMode -> Payload.WatchMode(newItem.isWatchMode)
                else -> super.getChangePayload(oldItem, newItem)
            }
        }
    }
}
