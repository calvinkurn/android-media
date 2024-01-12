package com.tokopedia.content.product.preview.view.viewholder.review

import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.content.common.util.buildSpannedString
import com.tokopedia.content.common.util.doOnLayout
import com.tokopedia.content.product.preview.R
import com.tokopedia.content.product.preview.databinding.ItemReviewParentContentBinding
import com.tokopedia.content.product.preview.view.uimodel.AuthorUiModel
import com.tokopedia.content.product.preview.view.uimodel.DescriptionUiModel
import com.tokopedia.content.product.preview.view.uimodel.LikeUiState
import com.tokopedia.content.product.preview.view.uimodel.MenuStatus
import com.tokopedia.content.product.preview.view.uimodel.ReviewUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ReviewParentContentViewHolder(
    private val binding: ItemReviewParentContentBinding,
    private val listener: Listener,
) : ViewHolder(binding.root) {

    private val clickableSpan: (String) -> ClickableSpan = { desc ->
        object : ClickableSpan() {
            override fun updateDrawState(tp: TextPaint) {
                tp.color =
                    MethodChecker.getColor(binding.root.context, unifyprinciplesR.color.Unify_GN500)
                tp.isUnderlineText = false
            }

            override fun onClick(widget: View) {
                binding.tvReviewDescription.maxLines = MAX_LINES_VALUE
                binding.tvReviewDescription.text = desc
            }
        }
    }

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
        setupReview(description.description)
    }

    private fun setupReview(description: String) = with(binding) {
        tvReviewDescription.invisible()
        tvReviewDescription.text = description
        tvReviewDescription.doOnLayout {
            val text = tvReviewDescription.layout
            if (text.lineCount <= MAX_LINES_THRESHOLD) return@doOnLayout

            val start = text.getLineStart(0)
            val end = text.getLineEnd(MAX_LINES_THRESHOLD - 1)

            tvReviewDescription.text = buildSpannedString {
                append(text.text.subSequence(start, end).dropLast(READ_MORE_COUNT))
                append(root.context.getString(R.string.product_prev_review_ellipsize))
                append(
                    root.context.getString(R.string.product_prev_review_expand),
                    clickableSpan(description),
                    Spanned.SPAN_EXCLUSIVE_INCLUSIVE
                )
            }
            tvReviewDescription.movementMethod = LinkMovementMethod.getInstance()
        }
        tvReviewDescription.show()
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
        fun onMenuClicked(menus: MenuStatus)
        fun onLike(status: LikeUiState)
    }

    companion object {
        fun create(binding: ItemReviewParentContentBinding, listener: Listener) =
            ReviewParentContentViewHolder(binding, listener)

        private const val MAX_LINES_VALUE = 25
        private const val MAX_LINES_THRESHOLD = 2
        private const val READ_MORE_COUNT = 16
    }
}
