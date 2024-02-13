package com.tokopedia.content.product.preview.view.adapter.review

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.content.product.preview.view.listener.ProductPreviewVideoListener
import com.tokopedia.content.product.preview.view.uimodel.MediaType
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewMediaUiModel
import com.tokopedia.content.product.preview.view.viewholder.review.ReviewMediaImageViewHolder
import com.tokopedia.content.product.preview.view.viewholder.review.ReviewMediaVideoViewHolder

class ReviewMediaAdapter(
    private val productPreviewVideoListener: ProductPreviewVideoListener
) : ListAdapter<ReviewMediaUiModel, ViewHolder>(ReviewMediaAdapterCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            TYPE_IMAGE -> ReviewMediaImageViewHolder.create(parent)
            TYPE_VIDEO -> ReviewMediaVideoViewHolder.create(parent, productPreviewVideoListener)
            else -> super.createViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_IMAGE -> (holder as ReviewMediaImageViewHolder).bind(getItem(position))
            TYPE_VIDEO -> (holder as ReviewMediaVideoViewHolder).bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).type) {
            MediaType.Image -> TYPE_IMAGE
            MediaType.Video -> TYPE_VIDEO
            else -> error("Item ${getItem(position).type} is not supported")
        }
    }

    internal class ReviewMediaAdapterCallback : DiffUtil.ItemCallback<ReviewMediaUiModel>() {
        override fun areItemsTheSame(
            oldItem: ReviewMediaUiModel,
            newItem: ReviewMediaUiModel
        ): Boolean {
            return oldItem.mediaId == newItem.mediaId
        }

        override fun areContentsTheSame(
            oldItem: ReviewMediaUiModel,
            newItem: ReviewMediaUiModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    companion object {
        private const val TYPE_IMAGE = 0
        private const val TYPE_VIDEO = 1
    }
}
