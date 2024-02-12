package com.tokopedia.content.product.preview.view.viewholder.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.content.product.preview.databinding.ItemImageProductPreviewBinding
import com.tokopedia.content.product.preview.view.components.ItemImageProductPreview
import com.tokopedia.content.product.preview.view.uimodel.product.ProductContentUiModel

class ProductContentImageViewHolder(
    private val binding: ItemImageProductPreviewBinding
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.cvProductPreviewContentImage.setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnDetachedFromWindowOrReleasedFromPool
        )
    }

    fun bind(content: ProductContentUiModel) {
        binding.cvProductPreviewContentImage.apply {
            setContent {
                ItemImageProductPreview(imageUrl = content.url)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup) =
            ProductContentImageViewHolder(
                binding = ItemImageProductPreviewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
    }
}
