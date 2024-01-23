package com.tokopedia.content.product.preview.view.viewholder.review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.content.product.preview.databinding.ItemReviewContentImageBinding
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewMediaUiModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageWithoutPlaceholder

class ReviewMediaImageViewHolder(
    private val binding: ItemReviewContentImageBinding
) : ViewHolder(binding.root) {

    fun bind(content: ReviewMediaUiModel) = with(binding) {
        ivReviewContentImage.loadImageWithoutPlaceholder(content.url) {
            listener(
                onSuccess = { _, _ ->
                    hideLoading()
                },
                onError = { hideLoading() }
            )
        }
    }

    private fun hideLoading() {
        binding.apply {
            ivReviewContentImage.show()
            loaderImage.hide()
        }
    }

    companion object {
        fun create(parent: ViewGroup) = ReviewMediaImageViewHolder(
            ItemReviewContentImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }
    
}
