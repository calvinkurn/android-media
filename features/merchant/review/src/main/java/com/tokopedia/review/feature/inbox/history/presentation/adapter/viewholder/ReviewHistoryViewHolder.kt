package com.tokopedia.review.feature.inbox.history.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.review.R
import com.tokopedia.review.common.data.ProductrevReviewAttachment
import com.tokopedia.review.common.util.getReviewStar
import com.tokopedia.review.common.presentation.util.ReviewAttachedImagesClickListener
import com.tokopedia.review.feature.inbox.history.presentation.adapter.uimodel.ReviewHistoryUiModel
import com.tokopedia.review.feature.inbox.history.presentation.util.ReviewHistoryItemListener
import kotlinx.android.synthetic.main.item_review_history.view.*

class ReviewHistoryViewHolder(view: View,
                              private val reviewAttachedImagesClickListener: ReviewAttachedImagesClickListener,
                              private val reviewHistoryItemListener: ReviewHistoryItemListener
) : AbstractViewHolder<ReviewHistoryUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_review_history
    }

    override fun bind(element: ReviewHistoryUiModel) {
        with(element.productrevFeedbackHistory) {
            with(product) {
                showProductName(productName)
                showProductVariantName(productVariantName)
            }
            with(review) {
                showDescription(reviewText)
                showAttachedImages(attachments, product.productName, product.productId, feedbackId)
                setupStarRatings(rating)
            }
            showDate(timestamp.createTimeFormatted)
            showOtherReview(status.hasResponse)
        }
    }

    private fun showProductName(productName: String) {
        itemView.reviewHistoryProductName.setTextAndCheckShow(productName)
    }

    private fun showProductVariantName(productVariantName: String) {
        if(productVariantName.isEmpty()) {
            itemView.reviewHistoryProductVariant.hide()
            return
        }
        itemView.reviewHistoryProductVariant.apply {
            text = (getString(R.string.review_pending_variant, productVariantName))
            show()
        }
    }

    private fun showAttachedImages(attachedImages: List<ProductrevReviewAttachment>, productName: String, productId: Int, feedbackId: Int) {
        if(attachedImages.isEmpty()) {
            return
        }
        itemView.reviewHistoryAttachedImages.apply {
            setOnClickListener {
                reviewHistoryItemListener.trackAttachedImageClicked(productId, feedbackId)
            }
            setImages(attachedImages, productName, reviewAttachedImagesClickListener)
            show()
        }
    }

    private fun setupStarRatings(rating: Int) {
        itemView.reviewHistoryStars.apply {
            setImageResource(getReviewStar(rating))
            show()
        }
    }

    private fun showDescription(reviewDescription: String) {
        itemView.reviewHistoryDescription.setTextAndCheckShow(reviewDescription)
    }

    private fun showDate(date: String) {
        if(date.isNotEmpty()) {
            itemView.reviewHistoryDate.text = itemView.context.resources.getString(R.string.review_date, date)
        } else {
            itemView.reviewHistoryDate.hide()
        }
    }

    private fun showOtherReview(hasResponse: Boolean) {
        itemView.reviewHistoryReply.showWithCondition(hasResponse)
    }
}