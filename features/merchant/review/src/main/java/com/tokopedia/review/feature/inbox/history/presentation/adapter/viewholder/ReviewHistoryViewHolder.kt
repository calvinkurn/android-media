package com.tokopedia.review.feature.inbox.history.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.review.R
import com.tokopedia.review.common.util.getReviewStar
import com.tokopedia.review.feature.inbox.history.presentation.adapter.ReviewHistoryAttachedProductAdapter
import com.tokopedia.review.feature.inbox.history.presentation.adapter.uimodel.ReviewHistoryUiModel
import kotlinx.android.synthetic.main.item_review_history.view.*

class ReviewHistoryViewHolder(view: View) : AbstractViewHolder<ReviewHistoryUiModel>(view) {

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
                showAttachedImages(attachments)
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

    private fun showAttachedImages(attachedImages: List<String>) {
        if(attachedImages.isEmpty()) {
            return
        }
        val attachedImageAdapter = ReviewHistoryAttachedProductAdapter()
        attachedImageAdapter.setData(attachedImages)
        itemView.reviewHistoryAttachedImages.apply {
            adapter = attachedImageAdapter
            layoutManager = LinearLayoutManager(context)
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
        itemView.reviewHistoryDate.setTextAndCheckShow(date)
    }

    private fun showOtherReview(hasResponse: Boolean) {
        itemView.reviewHistoryReply.showWithCondition(hasResponse)
    }
}