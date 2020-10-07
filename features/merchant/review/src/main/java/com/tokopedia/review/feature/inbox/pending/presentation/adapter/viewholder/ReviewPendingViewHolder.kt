package com.tokopedia.review.feature.inbox.pending.presentation.adapter.viewholder

import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.reputation.common.view.AnimatedRatingPickerReviewPendingView
import com.tokopedia.review.R
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.util.ReviewPendingItemListener
import kotlinx.android.synthetic.main.item_review_pending.view.*

class ReviewPendingViewHolder(view: View, private val reviewPendingItemListener: ReviewPendingItemListener) : AbstractViewHolder<ReviewPendingUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_review_pending
        const val UNLOCK_UNIFY_LABEL = true
    }

    override fun bind(element: ReviewPendingUiModel) {
        with(element.productrevWaitForFeedback) {
            with(product) {
                showProductImage(productImageUrl)
                showProductName(productName)
                showProductVariantName(productVariantName)
                setListener(reputationId, productId, inboxReviewId, status.seen)
                setupStars(reputationId, productId, inboxReviewId, status.seen)
            }
            showDate(timestamp.createTimeFormatted)
            showNew(status.seen)
            showOvoIncentive(status.isEligible)
        }
    }

    private fun showProductImage(productImageUrl: String) {
        if(productImageUrl.isEmpty()) {
            showBrokenProductImage()
            return
        }
        itemView.reviewPendingProductImage.apply {
            loadImage(productImageUrl)
            show()
        }
    }

    private fun showBrokenProductImage() {
        itemView.reviewPendingProductImage.loadImageDrawable(R.drawable.image_not_loaded)
    }

    private fun showProductName(productName: String) {
        itemView.reviewPendingProductName.setTextAndCheckShow(productName)
    }

    private fun showProductVariantName(productVariantName: String) {
        if(productVariantName.isEmpty()) {
            itemView.reviewPendingProductVariant.hide()
            return
        }
        itemView.reviewPendingProductVariant.apply {
            text = (getString(R.string.review_pending_variant, productVariantName))
            show()
        }
    }

    private fun setListener(reputationId: Int, productId: Int, inboxReviewId: Int, seen: Boolean) {
        itemView.setOnClickListener {
            reviewPendingItemListener.trackCardClicked(reputationId, productId)
            itemView.reviewPendingStars.renderInitialReviewWithData(5)
            Handler().postDelayed({reviewPendingItemListener.onStarsClicked(reputationId, productId, 5, inboxReviewId, seen)}, 200)
        }
    }

    private fun setupStars(reputationId: Int, productId: Int, inboxReviewId: Int, seen: Boolean) {
        itemView.reviewPendingStars.apply {
            resetStars()
            setListener(object : AnimatedRatingPickerReviewPendingView.AnimatedReputationListener {
                override fun onClick(position: Int) {
                    reviewPendingItemListener.trackStarsClicked(reputationId, productId, position)
                    Handler().postDelayed({reviewPendingItemListener.onStarsClicked(reputationId, productId, position, inboxReviewId, seen)}, 200)
                }
            })
            show()
        }
    }

    private fun showDate(date: String) {
        itemView.reviewPendingDate.setTextAndCheckShow(date)
    }

    private fun showNew(seen: Boolean) {
        itemView.reviewPendingNewIcon.showWithCondition(!seen)
    }

    private fun showOvoIncentive(isEligible: Boolean) {
        if(isEligible) {
            itemView.reviewPendingOvoIncentiveLabel.apply {
                setLabelImage(R.drawable.ic_ovo_incentive_label)
                show()
            }
            return
        }
        itemView.reviewPendingOvoIncentiveLabel.hide()
    }
}