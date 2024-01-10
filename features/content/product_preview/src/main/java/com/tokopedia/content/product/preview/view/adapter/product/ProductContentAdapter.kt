package com.tokopedia.content.product.preview.view.adapter.product

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.content.product.preview.utils.TYPE_IMAGE
import com.tokopedia.content.product.preview.utils.TYPE_UNKNOWN
import com.tokopedia.content.product.preview.utils.TYPE_VIDEO
import com.tokopedia.content.product.preview.view.listener.ProductPreviewListener
import com.tokopedia.content.product.preview.view.uimodel.ContentUiModel
import com.tokopedia.content.product.preview.view.uimodel.MediaType
import com.tokopedia.content.product.preview.view.viewholder.product.ProductContentImageViewHolder
import com.tokopedia.content.product.preview.view.viewholder.product.ProductContentLoadingViewHolder
import com.tokopedia.content.product.preview.view.viewholder.product.ProductContentVideoViewHolder

class ProductContentAdapter(
    private val listener: ProductPreviewListener
) : ListAdapter<ContentUiModel, ViewHolder>(ProductContentDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            TYPE_IMAGE -> ProductContentImageViewHolder.create(parent, listener)
            TYPE_VIDEO -> ProductContentVideoViewHolder.create(parent, listener)
            else -> ProductContentLoadingViewHolder.create(parent)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_IMAGE -> (holder as ProductContentImageViewHolder).bind(getItem(position))
            TYPE_VIDEO -> (holder as ProductContentVideoViewHolder).bind(getItem(position))
            else -> (holder as ProductContentLoadingViewHolder).bind()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).type) {
            MediaType.Image -> TYPE_IMAGE
            MediaType.Video -> TYPE_VIDEO
            MediaType.Unknown -> TYPE_UNKNOWN
        }
    }

    internal class ProductContentDiffUtil : DiffUtil.ItemCallback<ContentUiModel>() {
        override fun areItemsTheSame(oldItem: ContentUiModel, newItem: ContentUiModel): Boolean {
            return oldItem.url == newItem.url // if we have id in the next, it will be better to use id instead
        }

        override fun areContentsTheSame(oldItem: ContentUiModel, newItem: ContentUiModel): Boolean {
            return oldItem == newItem
        }
    }
}
