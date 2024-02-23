package com.tokopedia.content.product.preview.view.adapter.review

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.tokopedia.content.product.preview.view.adapter.review.ReviewContentAdapter.Payload.Like
import com.tokopedia.content.product.preview.view.adapter.review.ReviewContentAdapter.Payload.MediaDataChanged
import com.tokopedia.content.product.preview.view.adapter.review.ReviewContentAdapter.Payload.ScrollingChanged
import com.tokopedia.content.product.preview.view.adapter.review.ReviewContentAdapter.Payload.WatchMode
import com.tokopedia.content.product.preview.view.listener.ReviewInteractionListener
import com.tokopedia.content.product.preview.view.listener.ReviewMediaListener
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewContentUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewLikeUiState
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewMediaUiModel
import com.tokopedia.content.product.preview.view.viewholder.review.ReviewContentViewHolder

class ReviewContentAdapter(
    private val reviewInteractionListener: ReviewInteractionListener,
    private val reviewMediaListener: ReviewMediaListener
) : ListAdapter<ReviewContentUiModel, ReviewContentViewHolder>(ReviewAdapterCallback()) {

    private val mediaViewPool: RecycledViewPool = RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewContentViewHolder {
        return ReviewContentViewHolder.create(
            parent = parent,
            reviewInteractionListener = reviewInteractionListener,
            reviewMediaListener = reviewMediaListener,
            mediaViewPool = mediaViewPool
        )
    }

    override fun onBindViewHolder(holder: ReviewContentViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onBindViewHolder(holder: ReviewContentViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            payloads.forEach {
                when (val payload = it) {
                    is Like -> holder.bindLike(payload.state)
                    is WatchMode -> holder.bindWatchMode(payload.isWatchMode)
                    is MediaDataChanged -> holder.bindMediaDataChanged(payload.mediaData)
                    is ScrollingChanged -> holder.bindScrolling(payload.isScrolling)
                }
            }
        }
    }

    override fun onViewRecycled(holder: ReviewContentViewHolder) {
        holder.onRecycled()
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
                oldItem.likeState != newItem.likeState -> Like(newItem.likeState)
                oldItem.isWatchMode != newItem.isWatchMode -> WatchMode(newItem.isWatchMode)
                oldItem.medias != newItem.medias -> MediaDataChanged(newItem.medias)
                oldItem.isScrolling != newItem.isScrolling -> ScrollingChanged(newItem.isScrolling)
                else -> super.getChangePayload(oldItem, newItem)
            }
        }
    }
}
