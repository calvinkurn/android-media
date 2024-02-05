package com.tokopedia.content.product.preview.view.adapter.product

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.content.product.preview.view.listener.ProductPreviewVideoListener
import com.tokopedia.content.product.preview.view.uimodel.MediaType
import com.tokopedia.content.product.preview.view.uimodel.product.ProductContentUiModel
import com.tokopedia.content.product.preview.view.viewholder.product.ProductContentImageViewHolder
import com.tokopedia.content.product.preview.view.viewholder.product.ProductContentVideoViewHolder

class ProductContentAdapter(
    private val productPreviewVideoListener: ProductPreviewVideoListener
) : ListAdapter<ProductContentUiModel, ViewHolder>(ProductContentDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            TYPE_IMAGE -> ProductContentImageViewHolder.create(parent)
            TYPE_VIDEO -> ProductContentVideoViewHolder.create(parent, productPreviewVideoListener)
            else -> super.createViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_IMAGE -> (holder as ProductContentImageViewHolder).bind(getItem(position))
            TYPE_VIDEO -> (holder as ProductContentVideoViewHolder).bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).type) {
            MediaType.Image -> TYPE_IMAGE
            MediaType.Video -> TYPE_VIDEO
            else -> error("Item ${getItem(position).type} is not supported")
        }
    }

    internal class ProductContentDiffUtil : DiffUtil.ItemCallback<ProductContentUiModel>() {
        override fun areItemsTheSame(
            oldItem: ProductContentUiModel,
            newItem: ProductContentUiModel
        ): Boolean {
            return oldItem.contentId == newItem.contentId
        }

        override fun areContentsTheSame(
            oldItem: ProductContentUiModel,
            newItem: ProductContentUiModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    companion object {
        private const val TYPE_IMAGE = 0
        private const val TYPE_VIDEO = 1
    }
}
