package com.tokopedia.content.product.preview.view.viewholder.review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.content.product.preview.databinding.ItemReviewContentImageBinding
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewMediaUiModel
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageWithoutPlaceholder

class ReviewMediaImageViewHolder(
    private val binding: ItemReviewContentImageBinding
) : ViewHolder(binding.root) {

    fun bind(content: ReviewMediaUiModel) {
        showLoading()
        renderImage(content.url)
    }

    private fun renderImage(imageUrl: String) = with(binding.ivReviewContentImage) {
        loadImageWithoutPlaceholder(imageUrl) {
            listener(
                onSuccess = { _, _ -> hideLoading() },
                onError = { showErrorImage() }
            )
        }
    }

    private fun showLoading() {
        binding.apply {
            ivReviewContentImage.invisible()
            loaderImage.show()
        }
    }

    private fun hideLoading() {
        binding.apply {
            ivReviewContentImage.show()
            loaderImage.invisible()
        }
    }

    private fun showErrorImage() {
        binding.apply {
            ivReviewContentImage.apply {
                setImageResource(0)
                show()
            }
            loaderImage.invisible()
        }
    }

    companion object {
        fun create(parent: ViewGroup) = ReviewMediaImageViewHolder(
            ItemReviewContentImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}
