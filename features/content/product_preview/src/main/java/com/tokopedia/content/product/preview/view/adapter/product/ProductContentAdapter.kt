package com.tokopedia.content.product.preview.view.adapter.product

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.content.product.preview.utils.KEY_REF_SELECTED_PRODUCT_CONTENT
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            when (holder) {
                is ProductContentVideoViewHolder -> holder.bindWithPayloads(payloads)
            }
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
            return when {
                oldItem.type == MediaType.Image && newItem.type == MediaType.Image -> oldItem.contentId == newItem.contentId
                oldItem.type == MediaType.Video && newItem.type == MediaType.Video -> oldItem.contentId == newItem.contentId
                else -> oldItem.contentId == newItem.contentId
            }
        }

        override fun areContentsTheSame(oldItem: ContentUiModel, newItem: ContentUiModel): Boolean {
            return when {
                oldItem.type == MediaType.Image && newItem.type == MediaType.Image -> oldItem == newItem
                oldItem.type == MediaType.Video && newItem.type == MediaType.Video -> oldItem == newItem
                else -> oldItem == newItem
            }
        }

        override fun getChangePayload(oldItem: ContentUiModel, newItem: ContentUiModel): Any {
            val payloads = mutableListOf<Int>()
            if (oldItem.selected != newItem.selected) payloads.add(KEY_REF_SELECTED_PRODUCT_CONTENT)
            payloads.add(0)
            return payloads
        }
    }
}
