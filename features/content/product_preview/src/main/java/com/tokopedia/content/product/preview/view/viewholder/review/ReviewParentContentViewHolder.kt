package com.tokopedia.content.product.preview.view.viewholder.review

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.content.common.report_content.model.ContentMenuItem
import com.tokopedia.content.product.preview.R
import com.tokopedia.content.product.preview.databinding.ItemReviewParentContentBinding
import com.tokopedia.content.product.preview.view.uimodel.AuthorUiModel
import com.tokopedia.content.product.preview.view.uimodel.DescriptionUiModel
import com.tokopedia.content.product.preview.view.uimodel.LikeUiState
import com.tokopedia.content.product.preview.view.uimodel.ReviewUiModel
import com.tokopedia.iconunify.IconUnify
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

        binding.ivReviewMenu.setOnClickListener {
            listener.onMenuClicked(item.menus)
        }
    }

    private fun bindAuthor(author: AuthorUiModel) = with(binding.layoutAuthorReview) {
        tvAuthorName.text = author.name
        ivAuthor.loadImageCircle(url = author.avatarUrl)
        lblAuthorStats.setLabel(author.type)
        lblAuthorStats.showWithCondition(author.type.isNotBlank())

        lblAuthorStats.setOnClickListener {
            listener.onReviewCredibilityClicked(author)
        }
        tvAuthorName.setOnClickListener {
            listener.onReviewCredibilityClicked(author)
        }
    }

    private fun bindDescription(description: DescriptionUiModel) = with(binding) {
        val divider = root.context.getString(R.string.circle_dot_divider)
        tvReviewDetails.text = buildString {
            append(description.stars)
            append(" $divider ")
            append(description.productType)
            append(" $divider ")
            append(description.timestamp)
        }
        tvReviewDescription.text = description.description
    }

    fun bindLike(state: LikeUiState) = with(binding.layoutLikeReview) {
        val icon = when (state.state) {
            LikeUiState.LikeStatus.Reset -> IconUnify.THUMB
            else -> IconUnify.THUMB_FILLED
        }
        ivReviewLike.setImage(newIconId = icon)
        tvLikeCount.text = state.count.toString()
        root.setOnClickListener {
            listener.onLike(state)
        }
    }

    interface Listener {
        fun onReviewCredibilityClicked(author: AuthorUiModel)
        fun onMenuClicked(menus: List<ContentMenuItem>)
        fun onLike(status: LikeUiState)
    }

    companion object {
        fun create(binding: ItemReviewParentContentBinding, listener: Listener) =
            ReviewParentContentViewHolder(binding, listener)
    }
}
