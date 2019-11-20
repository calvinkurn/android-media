package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.listener.HomeReviewListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ReviewViewModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.reputation.common.view.AnimatedReputationView
import kotlinx.android.synthetic.main.home_item_review.view.*

class ReviewViewHolder(
        itemView: View,
        private val reviewListener: HomeReviewListener,
        private val categoryListener: HomeCategoryListener
) : AbstractViewHolder<ReviewViewModel>(itemView) {

    var isPressed = false

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_item_review

        private const val cardBg = "https://ecs7.tokopedia.net/android/others/review_home_bg.png"
    }

    override fun bind(element: ReviewViewModel) {
        if (element.suggestedProductReview.suggestedProductReview.title.isEmpty()) {
            itemView.loading_review.visibility = View.VISIBLE
        } else {
            isPressed = false
            itemView.loading_review.visibility = View.GONE
            itemView.review_title.text = String.format("%s %s",
                    element.suggestedProductReview.suggestedProductReview.title,
                    element.suggestedProductReview.suggestedProductReview.description
            )
            ImageHandler.loadImage(
                    itemView.context,
                    itemView.img_review,
                    element.suggestedProductReview.suggestedProductReview.imageUrl,
                    R.drawable.ic_loading_image
            )
        }

        itemView.addOnImpressionListener(element, object : ViewHintListener {
            override fun onViewHint() {
                HomePageTracking.homeReviewImpression(
                        categoryListener.trackingQueue,
                        element.suggestedProductReview.suggestedProductReview,
                        adapterPosition,
                        element.suggestedProductReview.suggestedProductReview.orderId,
                        element.suggestedProductReview.suggestedProductReview.productId
                )
            }
        })

        ImageHandler.LoadImage(itemView.review_card_bg, cardBg)
        itemView.review_card_content_container.setOnClickListener{
            if (!isPressed) {
                HomePageTracking.homeReviewOnBlankSpaceClickTracker(
                        element.suggestedProductReview.suggestedProductReview.orderId,
                        element.suggestedProductReview.suggestedProductReview.productId
                )
                reviewListener.onReviewClick(
                        adapterPosition,
                        5,
                        0,
                        element.suggestedProductReview.suggestedProductReview.linkURL
                )
                isPressed = true
            }
        }

        itemView.animated_review.resetStars()
        itemView.animated_review.setListener(object : AnimatedReputationView.AnimatedReputationListener {
            override fun onClick(position: Int) {
                if (!isPressed) {
                    HomePageTracking.homeReviewOnRatingChangedTracker(
                            element.suggestedProductReview.suggestedProductReview.orderId,
                            element.suggestedProductReview.suggestedProductReview.productId,
                            position + 1
                    )
                    reviewListener.onReviewClick(
                            adapterPosition,
                            position,
                            500,
                            element.suggestedProductReview.suggestedProductReview.linkURL
                    )
                    isPressed = true
                }
            }
        })

        itemView.ic_close_review.setOnClickListener{
            HomePageTracking.homeReviewOnCloseTracker(
                    element.suggestedProductReview.suggestedProductReview.orderId,
                    element.suggestedProductReview.suggestedProductReview.productId
            )
            reviewListener.onCloseClick()
        }
    }
}