package com.tokopedia.content.product.preview.view.adapter.product

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.content.product.preview.view.listener.ProductThumbnailListener
import com.tokopedia.content.product.preview.view.uimodel.product.ProductMediaUiModel
import com.tokopedia.content.product.preview.view.viewholder.product.ProductThumbnailViewHolder

class ProductThumbnailAdapter(
    private val productThumbnailListener: ProductThumbnailListener
) : ListAdapter<ProductMediaUiModel, ProductThumbnailViewHolder>(ProductThumbnailDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductThumbnailViewHolder {
        return ProductThumbnailViewHolder.create(parent, productThumbnailListener)
    }

    override fun onBindViewHolder(holder: ProductThumbnailViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(holder: ProductThumbnailViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            payloads.forEach { payloadData ->
                val data = payloadData as? List<*> ?: return@forEach
                data.forEach {
                    when (val payload = it) {
                        is Payload.SelectedChanged -> holder.bindSelected(payload.isSelected)
                    }
                }
            }
        }
    }

    sealed interface Payload {
        data class SelectedChanged(val isSelected: Boolean): Payload
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
            return oldItem.contentId == newItem.contentId
        }

        override fun getChangePayload(
            oldItem: ProductMediaUiModel,
            newItem: ProductMediaUiModel
        ): Any {
            val payloads = mutableListOf<Payload>()
            if (oldItem.selected != newItem.selected) payloads.add(Payload.SelectedChanged(newItem.selected))
            return payloads
        }
    }
}
