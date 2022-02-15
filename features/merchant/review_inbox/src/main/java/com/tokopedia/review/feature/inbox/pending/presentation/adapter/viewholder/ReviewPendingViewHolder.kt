package com.tokopedia.review.feature.inbox.pending.presentation.adapter.viewholder

import android.os.Handler
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.reputation.common.view.AnimatedRatingPickerReviewPendingView
import com.tokopedia.review.common.ReviewInboxConstants
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.util.ReviewPendingItemListener
import com.tokopedia.review.inbox.R
import com.tokopedia.review.inbox.databinding.ItemReviewPendingBinding
import com.tokopedia.unifycomponents.NotificationUnify

class ReviewPendingViewHolder(view: View, private val reviewPendingItemListener: ReviewPendingItemListener) : AbstractViewHolder<ReviewPendingUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_review_pending
        const val ANIMATION_DELAY = 200L
    }

    private val binding = ItemReviewPendingBinding.bind(view)

    override fun bind(element: ReviewPendingUiModel) {
        with(element.productrevWaitForFeedback) {
            with(product) {
                showProductImage(productImageUrl)
                showProductName(productName)
                showProductVariantName(productVariantName)
                setListener(reputationIDStr, productIDStr, inboxReviewIDStr, status.seen, status.isEligible)
                setupStars(reputationIDStr, productIDStr, inboxReviewIDStr, status.seen, status.isEligible)
            }
            showDate(timestamp.createTimeFormatted)
            showNew(status.seen)
            showIncentive(status.isEligible, status.incentiveLabel)
        }
    }

    private fun showProductImage(productImageUrl: String) {
        if (productImageUrl.isEmpty()) {
            showBrokenProductImage()
            return
        }
        binding.reviewPendingProductImage.apply {
            loadImage(productImageUrl)
            show()
        }
    }

    private fun showBrokenProductImage() {
        binding.reviewPendingProductImage.loadImage(R.drawable.image_not_loaded)
    }

    private fun showProductName(productName: String) {
        binding.reviewPendingProductName.setTextAndCheckShow(productName)
    }

    private fun showProductVariantName(productVariantName: String) {
        if (productVariantName.isEmpty()) {
            binding.reviewPendingProductVariant.hide()
            return
        }
        binding.reviewPendingProductVariant.apply {
            text = (getString(R.string.review_pending_variant, productVariantName))
            show()
        }
    }

    private fun setListener(reputationId: String, productId: String, inboxReviewId: String, seen: Boolean, isEligible: Boolean) {
        binding.root.setOnClickListener {
            reviewPendingItemListener.trackCardClicked(reputationId, productId, isEligible)
            binding.reviewPendingStars.renderInitialReviewWithData(ReviewInboxConstants.RATING_5)
            invokeListener(reputationId, productId, ReviewInboxConstants.RATING_5, inboxReviewId, seen)
        }
    }

    private fun setupStars(reputationId: String, productId: String, inboxReviewId: String, seen: Boolean, isEligible: Boolean) {
        binding.reviewPendingStars.apply {
            resetStars()
            setListener(object : AnimatedRatingPickerReviewPendingView.AnimatedReputationListener {
                override fun onClick(position: Int) {
                    reviewPendingItemListener.trackStarsClicked(reputationId, productId, position, isEligible)
                    invokeListener(reputationId, productId, position, inboxReviewId, seen)
                }
            })
            show()
        }
    }

    private fun invokeListener(reputationId: String, productId: String, position: Int, inboxReviewId: String, seen: Boolean) {
        Handler().postDelayed({ binding.root.context?.let { reviewPendingItemListener.onStarsClicked(reputationId, productId, position, inboxReviewId, seen) } }, ANIMATION_DELAY)
    }

    private fun showDate(date: String) {
        binding.reviewPendingDate.setTextAndCheckShow(date)
    }

    private fun showNew(seen: Boolean) {
        binding.reviewPendingNewIcon.apply {
            setNotification("", NotificationUnify.NONE_TYPE, NotificationUnify.COLOR_PRIMARY)
            showWithCondition(!seen)
        }
    }

    private fun showIncentive(isEligible: Boolean, incentiveLabel: String) {
        if (isEligible) {
            binding.reviewPendingOvoIncentiveLabel.apply {
                setLabel(incentiveLabel)
                show()
            }
            return
        }
        binding.reviewPendingOvoIncentiveLabel.hide()
    }
}