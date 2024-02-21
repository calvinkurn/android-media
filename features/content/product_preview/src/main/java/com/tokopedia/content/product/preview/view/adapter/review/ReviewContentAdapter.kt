package com.tokopedia.content.product.preview.view.adapter.review

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.content.product.preview.view.listener.ReviewInteractionListener
import com.tokopedia.content.product.preview.view.listener.ReviewMediaListener
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewContentUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewLikeUiState
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewMediaUiModel
import com.tokopedia.content.product.preview.view.viewholder.review.ReviewContentViewHolder

class ReviewContentAdapter(
    private val reviewInteractionListener: ReviewInteractionListener,
    private val reviewMediaListener: ReviewMediaListener
) : ListAdapter<ReviewContentUiModel, ViewHolder>(ReviewAdapterCallback()) {

    private val mediaViewPool: RecycledViewPool = RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            TYPE_CONTENT -> {
                ReviewContentViewHolder.create(
                    parent = parent,
                    reviewInteractionListener = reviewInteractionListener,
                    reviewMediaListener = reviewMediaListener,
                    mediaViewPool = mediaViewPool
                )
            }

            else -> super.createViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder.itemViewType) {
            TYPE_CONTENT -> (holder as ReviewContentViewHolder).bind(item)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            payloads.forEach {
                when (val payload = it) {
                    is Payload.Like -> {
                        (holder as ReviewContentViewHolder).bindLike(payload.state)
                    }

                    is Payload.WatchMode -> {
                        (holder as ReviewContentViewHolder).bindWatchMode(payload.isWatchMode)
                    }

                    is Payload.MediaDataChanged -> {
                        (holder as ReviewContentViewHolder).bindMediaDataChanged(payload.mediaData)
                    }

                    is Payload.ScrollingChanged -> {
                        (holder as ReviewContentViewHolder).bindScrolling(payload.isScrolling)
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
        (holder as ReviewContentViewHolder).onRecycled()
    }

    companion object {
        private const val TYPE_CONTENT = 0
    }

    sealed interface Payload {
        data class Like(val state: ReviewLikeUiState) : Payload
        data class WatchMode(val isWatchMode: Boolean) : Payload
        data class MediaDataChanged(val mediaData: List<ReviewMediaUiModel>) : Payload
        data class ScrollingChanged(val isScrolling: Boolean) : Payload
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
                oldItem.medias != newItem.medias -> Payload.MediaDataChanged(newItem.medias)
                oldItem.isScrolling != newItem.isScrolling -> Payload.ScrollingChanged(newItem.isScrolling)
                else -> super.getChangePayload(oldItem, newItem)
            }
        }
    }
}
