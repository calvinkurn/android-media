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
    private val binding: ItemReviewParentContentBinding,
    private val listener: Listener,
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

        lblAuthorStats.rootView.setOnClickListener {
            listener.onReviewCredibilityClicked(author)
        }
        tvAuthorName.setOnClickListener {
            listener.onReviewerClicked(author)
        }
        ivAuthor.setOnClickListener {
            listener.onReviewerClicked(author)
        }
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

    interface Listener {
        fun onReviewCredibilityClicked(author: AuthorUiModel)
        fun onReviewerClicked(author: AuthorUiModel)
    }

    companion object {
        fun create(binding: ItemReviewParentContentBinding, listener: Listener) =
            ReviewParentContentViewHolder(binding, listener)
    }
}
