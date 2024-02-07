package com.tokopedia.content.product.preview.view.viewholder.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.content.product.preview.databinding.ItemImageProductPreviewBinding
import com.tokopedia.content.product.preview.view.components.ItemImageProductPreview
import com.tokopedia.content.product.preview.view.uimodel.product.ProductMediaUiModel

class ProductMediaImageViewHolder(
    private val binding: ItemImageProductPreviewBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(content: ProductMediaUiModel) {
        binding.cvProductPreviewMediaImage.apply {
            setContent {
                ItemImageProductPreview(imageUrl = content.url)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup) =
            ProductMediaImageViewHolder(
                binding = ItemImageProductPreviewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
    }
}
