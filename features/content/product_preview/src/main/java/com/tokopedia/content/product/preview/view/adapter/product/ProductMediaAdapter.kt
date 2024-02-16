package com.tokopedia.content.product.preview.view.adapter.product

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.content.product.preview.view.listener.MediaImageListener
import com.tokopedia.content.product.preview.view.listener.ProductPreviewVideoListener
import com.tokopedia.content.product.preview.view.uimodel.MediaType
import com.tokopedia.content.product.preview.view.uimodel.product.ProductMediaUiModel
import com.tokopedia.content.product.preview.view.viewholder.product.ProductMediaImageViewHolder
import com.tokopedia.content.product.preview.view.viewholder.product.ProductMediaVideoViewHolder

class ProductMediaAdapter(
    private val productPreviewVideoListener: ProductPreviewVideoListener,
    private val mediaImageLister: MediaImageListener
) : ListAdapter<ProductMediaUiModel, ViewHolder>(ProductMediaDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            TYPE_IMAGE -> ProductMediaImageViewHolder.create(parent, mediaImageLister)
            TYPE_VIDEO -> ProductMediaVideoViewHolder.create(parent, productPreviewVideoListener)
            else -> super.createViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_IMAGE -> (holder as ProductMediaImageViewHolder).bind(getItem(position))
            TYPE_VIDEO -> (holder as ProductMediaVideoViewHolder).bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).type) {
            MediaType.Image -> TYPE_IMAGE
            MediaType.Video -> TYPE_VIDEO
            else -> error("Item ${getItem(position).type} is not supported")
        }
    }

    internal class ProductMediaDiffUtil : DiffUtil.ItemCallback<ProductMediaUiModel>() {
        override fun areItemsTheSame(
            oldItem: ProductMediaUiModel,
            newItem: ProductMediaUiModel
        ): Boolean {
            return oldItem.contentId == newItem.contentId
        }

        override fun areContentsTheSame(
            oldItem: ProductMediaUiModel,
            newItem: ProductMediaUiModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    companion object {
        private const val TYPE_IMAGE = 0
        private const val TYPE_VIDEO = 1
    }
}
