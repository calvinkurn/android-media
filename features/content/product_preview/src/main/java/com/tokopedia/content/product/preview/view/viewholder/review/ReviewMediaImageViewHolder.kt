package com.tokopedia.content.product.preview.view.viewholder.review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.content.product.preview.databinding.ItemImageProductPreviewBinding
import com.tokopedia.content.product.preview.view.components.ItemImageProductPreview
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewMediaUiModel

class ReviewMediaImageViewHolder(
    private val binding: ItemImageProductPreviewBinding
) : ViewHolder(binding.root) {

    fun bind(content: ReviewMediaUiModel) {
        binding.cvProductPreviewContentImage.apply {
            setContent {
                ItemImageProductPreview(imageUrl = content.url)
            }
        }
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
