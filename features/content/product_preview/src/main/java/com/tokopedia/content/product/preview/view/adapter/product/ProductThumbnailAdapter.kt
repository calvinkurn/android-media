package com.tokopedia.content.product.preview.view.adapter.product

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.content.product.preview.view.listener.ProductThumbnailListener
import com.tokopedia.content.product.preview.view.uimodel.product.ProductMediaUiModel
import com.tokopedia.content.product.preview.view.viewholder.product.ProductThumbnailViewHolder

class ProductThumbnailAdapter(
    private val productThumbnailListener: ProductThumbnailListener
) : ListAdapter<ProductMediaUiModel, ViewHolder>(ProductThumbnailDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ProductThumbnailViewHolder.create(parent, productThumbnailListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ProductThumbnailViewHolder).bind(getItem(position))
    }

    internal class ProductThumbnailDiffUtil : DiffUtil.ItemCallback<ProductMediaUiModel>() {
        override fun areItemsTheSame(
            oldItem: ProductMediaUiModel,
            newItem: ProductMediaUiModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ProductMediaUiModel,
            newItem: ProductMediaUiModel
        ): Boolean {
            return oldItem.selected == newItem.selected
        }
    }
}
