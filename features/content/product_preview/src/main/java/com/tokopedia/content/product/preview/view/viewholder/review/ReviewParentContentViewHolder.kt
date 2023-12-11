package com.tokopedia.content.product.preview.view.viewholder.review

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.content.product.preview.databinding.ItemReviewParentContentBinding
import com.tokopedia.content.product.preview.view.uimodel.AuthorUiModel
import com.tokopedia.content.product.preview.view.uimodel.DescriptionUiModel
import com.tokopedia.content.product.preview.view.uimodel.LikeUiState
import com.tokopedia.content.product.preview.view.uimodel.ReviewUiModel
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImageCircle

class ReviewParentContentViewHolder(
    private val binding: ItemReviewParentContentBinding
) : ViewHolder(binding.root) {
    fun bind(item: ReviewUiModel) {
        bindAuthor(item.author)
        bindDescription(item.description)
        bindLike(item.likeState)
    }

    private fun bindAuthor(author: AuthorUiModel) = with(binding.layoutAuthorReview) {
        tvAuthorName.text = author.name
        ivAuthor.loadImageCircle(url = author.avatarUrl)
        lblAuthorStats.setLabel(author.type)
        lblAuthorStats.showWithCondition(author.type.isNotBlank())
    }

    private fun bindDescription(description: DescriptionUiModel) = with(binding) {
        tvReviewDetails.text = buildString {
            append(description.stars)
            append(" • ")
            append(description.productType)
            append(" • ")
            append(description.timestamp)
        }
        tvReviewDescription.text = description.description
    }

    private fun bindLike(state: LikeUiState) = with(binding.layoutLikeReview) {
        tvLikeCount.text = state.count.toString()
    }

    companion object {
        fun create(binding: ItemReviewParentContentBinding) =
            ReviewParentContentViewHolder(binding)
    }
}
