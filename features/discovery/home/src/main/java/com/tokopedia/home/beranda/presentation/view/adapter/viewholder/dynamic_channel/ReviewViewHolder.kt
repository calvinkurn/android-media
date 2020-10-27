package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.listener.HomeReviewListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ReviewDataModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.reputation.common.view.AnimatedReputationView
import kotlinx.android.synthetic.main.home_item_review.view.*

class ReviewViewHolder(
        itemView: View,
        private val reviewListener: HomeReviewListener,
        private val categoryListener: HomeCategoryListener
) : AbstractViewHolder<ReviewDataModel>(itemView) {

    var isPressed = false

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_item_review
        private const val FPM_REVIEW = "home_review"
        private const val cardBg = "https://ecs7.tokopedia.net/android/others/review_home_bg.png"
    }
    private var performanceMonitoring: PerformanceMonitoring? = null
    private val performanceTraceName = "mp_home_review_widget_load_time"

    init {
        performanceMonitoring = PerformanceMonitoring()
    }

    override fun bind(element: ReviewDataModel) {
        performanceMonitoring?.startTrace(performanceTraceName)
        itemView.review_card_bg?.loadImage(cardBg)
        element.suggestedProductReview.let { suggestedProductReview ->
            if (suggestedProductReview.suggestedProductReview.linkURL.isEmpty()) {
                itemView.loading_review.visibility = View.VISIBLE
            } else {
                isPressed = false
                itemView.loading_review.visibility = View.GONE
                itemView.review_title.text = String.format("%s %s",
                        suggestedProductReview.suggestedProductReview.title,
                        suggestedProductReview.suggestedProductReview.description
                )
                itemView.img_review?.loadImageRounded(suggestedProductReview.suggestedProductReview.imageUrl, 8, FPM_REVIEW)

                itemView.addOnImpressionListener(element, object : ViewHintListener {
                    override fun onViewHint() {
                        HomePageTracking.homeReviewImpression(
                                categoryListener.getTrackingQueueObj(),
                                suggestedProductReview.suggestedProductReview,
                                adapterPosition,
                                suggestedProductReview.suggestedProductReview.orderId,
                                suggestedProductReview.suggestedProductReview.productId,
                                element.channel.id,
                                suggestedProductReview.suggestedProductReview.description
                        )
                        categoryListener.sendIrisTrackerHashMap(HomePageTracking.getHomeReviewImpressionIris(element.suggestedProductReview.suggestedProductReview,
                                adapterPosition,
                                suggestedProductReview.suggestedProductReview.orderId,
                                suggestedProductReview.suggestedProductReview.productId,
                                element.channel.id, suggestedProductReview.suggestedProductReview.description))
                    }
                })

                itemView.review_card_content_container.setOnClickListener {
                    if (!isPressed) {
                        HomePageTracking.homeReviewOnBlankSpaceClickTracker(
                                suggestedProductReview.suggestedProductReview.orderId,
                                suggestedProductReview.suggestedProductReview.productId,
                                element.channel.id,
                                suggestedProductReview.suggestedProductReview.description
                        )
                        reviewListener.onReviewClick(
                                adapterPosition,
                                5,
                                0,
                                suggestedProductReview.suggestedProductReview.linkURL
                        )
                        isPressed = true
                    }
                }

                itemView.animated_review.resetStars()
                itemView.animated_review.setListener(object : AnimatedReputationView.AnimatedReputationListener {
                    override fun onClick(position: Int) {
                        if (!isPressed) {
                            HomePageTracking.homeReviewOnRatingChangedTracker(
                                    suggestedProductReview.suggestedProductReview.orderId,
                                    suggestedProductReview.suggestedProductReview.productId,
                                    position,
                                    suggestedProductReview.suggestedProductReview.description,
                                    element.channel.id
                            )
                            reviewListener.onReviewClick(
                                    adapterPosition,
                                    position,
                                    500,
                                    suggestedProductReview.suggestedProductReview.linkURL
                            )
                            isPressed = true
                        }
                    }
                })

                itemView.ic_close_review.setOnClickListener {
                    HomePageTracking.homeReviewOnCloseTracker(
                            suggestedProductReview.suggestedProductReview.orderId,
                            suggestedProductReview.suggestedProductReview.productId,
                            suggestedProductReview.suggestedProductReview.description,
                            element.channel.id
                    )
                    reviewListener.onCloseClick()
                }
            }
        }
        performanceMonitoring?.stopTrace()
        performanceMonitoring = null
    }
}