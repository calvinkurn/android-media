package com.tokopedia.review.feature.inbox.history.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.common.util.ReviewAttachedImagesClickListener
import com.tokopedia.review.common.util.getReviewStar
import com.tokopedia.review.feature.inbox.history.presentation.adapter.uimodel.ReviewHistoryUiModel
import com.tokopedia.review.feature.inbox.history.presentation.util.ReviewHistoryItemListener
import com.tokopedia.review.inbox.R
import com.tokopedia.review.inbox.databinding.ItemReviewHistoryBinding
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.adapter.typefactory.ReviewMediaThumbnailTypeFactory
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailVisitable

class ReviewHistoryViewHolder(
    view: View,
    private val reviewAttachedImagesClickListener: ReviewAttachedImagesClickListener,
    private val reviewHistoryItemListener: ReviewHistoryItemListener
) : AbstractViewHolder<ReviewHistoryUiModel>(view) {

    companion object {
        val LAYOUT = com.tokopedia.review.inbox.R.layout.item_review_history
    }

    private val binding = ItemReviewHistoryBinding.bind(view)
    private var element: ReviewHistoryUiModel? = null

    init {
        binding.reviewDetailAttachedMedia.setListener(ReviewMediaThumbnailListener())
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
                setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_32))
            }
            return
        }
        binding.reviewHistoryDescription.apply {
            text = reviewDescription.removeNewLine()
            setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
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

    private inner class ReviewMediaThumbnailListener: ReviewMediaThumbnailTypeFactory.Listener {
        override fun onMediaItemClicked(item: ReviewMediaThumbnailVisitable, position: Int) {
            element?.let {
                reviewHistoryItemListener.trackAttachedImageClicked(
                    it.productrevFeedbackHistory.product.productId,
                    it.productrevFeedbackHistory.review.feedbackId
                )
                reviewAttachedImagesClickListener.onAttachedMediaClicked(
                    it.productrevFeedbackHistory.product.productId,
                    it.productrevFeedbackHistory.review.feedbackId,
                    position,
                    it.productrevFeedbackHistory.review.imageAttachments.map { it.fullSize },
                    it.productrevFeedbackHistory.review.videoAttachments.mapNotNull { it.url }
                )
            }
        }

        override fun onRemoveMediaItemClicked(
            item: ReviewMediaThumbnailVisitable,
            position: Int
        ) {
            // noop
        }
    }
}