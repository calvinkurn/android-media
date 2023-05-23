package com.tokopedia.review.feature.inbox.history.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.common.util.getReviewStar
import com.tokopedia.review.feature.inbox.history.presentation.adapter.uimodel.ReviewHistoryUiModel
import com.tokopedia.review.inbox.R
import com.tokopedia.review.inbox.databinding.ItemReviewHistoryBinding
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.adapter.typefactory.ReviewMediaThumbnailTypeFactory
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailUiModel

class ReviewHistoryViewHolder(
    view: View,
    reviewMediaThumbnailRecycledViewPool: RecyclerView.RecycledViewPool,
    reviewMediaThumbnailListener: ReviewMediaThumbnailTypeFactory.Listener
) : AbstractViewHolder<ReviewHistoryUiModel>(view) {

    companion object {
        val LAYOUT = com.tokopedia.review.inbox.R.layout.item_review_history
    }

    private val binding = ItemReviewHistoryBinding.bind(view)
    private var element: ReviewHistoryUiModel? = null

    init {
        binding.reviewDetailAttachedMedia.setListener(reviewMediaThumbnailListener)
        binding.reviewDetailAttachedMedia.setRecycledViewPool(reviewMediaThumbnailRecycledViewPool)
    }

    override fun bind(element: ReviewHistoryUiModel) {
        this.element = element
        with(element.productrevFeedbackHistory) {
            with(product) {
                showProductName(productName)
                showProductVariantName(productVariantName)
            }
            with(review) {
                showDescription(reviewText)
                showAttachedImages(element.attachedMediaThumbnail)
                setupStarRatings(rating)
                showBadRatingReason(badRatingReason)
            }
            showDate(timestamp.createTimeFormatted)
            showOtherReview(status.hasResponse)
        }
    }

    private fun showProductName(productName: String) {
        binding.reviewHistoryProductName.setTextAndCheckShow(productName)
    }

    private fun showProductVariantName(productVariantName: String) {
        if(productVariantName.isEmpty()) {
            binding.reviewHistoryProductVariant.hide()
            return
        }
        binding.reviewHistoryProductVariant.apply {
            text = (getString(R.string.review_pending_variant, productVariantName))
            show()
        }
    }

    private fun showAttachedImages(attachedMediaThumbnail: ReviewMediaThumbnailUiModel) {
        if(attachedMediaThumbnail.mediaThumbnails.isEmpty()) {
            binding.reviewDetailAttachedMedia.hide()
            return
        }
        binding.reviewDetailAttachedMedia.apply {
            setData(attachedMediaThumbnail)
            show()
        }
    }

    private fun setupStarRatings(rating: Int) {
        binding.reviewHistoryStars.apply {
            setImageResource(getReviewStar(rating))
            show()
        }
    }

    private fun showDescription(reviewDescription: String) {
        if(reviewDescription.isBlank()) {
            binding.reviewHistoryDescription.apply {
                text = getString(R.string.no_reviews_yet)
                setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_32))
            }
            return
        }
        binding.reviewHistoryDescription.apply {
            text = reviewDescription.removeNewLine()
            setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_96))
        }
    }

    private fun showDate(date: String) {
        if(date.isNotEmpty()) {
            binding.reviewHistoryDate.apply {
                text = binding.root.context.resources.getString(R.string.review_date, date)
                show()
            }
        } else {
            binding.reviewHistoryDate.hide()
        }
    }

    private fun showOtherReview(hasResponse: Boolean) {
        if(hasResponse) {
            binding.reviewHistoryReply.show()
            return
        }
        binding.reviewHistoryReply.hide()
    }

    private fun String.removeNewLine(): String {
        return this.replace("\n", "")
    }

    private fun showBadRatingReason(badRatingReason: String) {
        binding.reviewHistoryBadRatingReason.showBadRatingReason(badRatingReason)
    }
}