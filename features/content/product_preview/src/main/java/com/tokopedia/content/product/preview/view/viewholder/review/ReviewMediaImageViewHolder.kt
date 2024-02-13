package com.tokopedia.content.product.preview.view.viewholder.review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.content.product.preview.databinding.ItemImageProductPreviewBinding
import com.tokopedia.content.product.preview.view.components.items.ItemImageProductPreview
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewMediaUiModel

class ReviewMediaImageViewHolder(
    private val binding: ItemImageProductPreviewBinding
) : ViewHolder(binding.root) {

    init {
        binding.cvProductPreviewMediaImage.setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnDetachedFromWindowOrReleasedFromPool
        )
    }

    fun bind(content: ReviewMediaUiModel) {
        binding.cvProductPreviewMediaImage.apply {
            setContent {
                ItemImageProductPreview(imageUrl = content.url)
            }
        }
    }

    fun bindSelected(isSelected: Boolean) {
        // do nothing yet
    }

    companion object {
        fun create(parent: ViewGroup) = ReviewMediaImageViewHolder(
            ItemImageProductPreviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}
