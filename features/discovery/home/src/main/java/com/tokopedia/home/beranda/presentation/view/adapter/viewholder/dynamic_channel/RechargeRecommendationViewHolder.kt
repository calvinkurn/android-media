package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.RechargeRecommendationViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.recharge_recommendation.RechargeRecommendationAdapter
import kotlinx.android.synthetic.main.home_recharge_recommendation_list.view.*

class RechargeRecommendationViewHolder(
        itemView: View,
        private val listener: RechargeRecommendationListener
) : AbstractViewHolder<RechargeRecommendationViewModel>(itemView) {

    var isPressed = false

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_recharge_recommendation_list

        private const val cardBg = "https://ecs7.tokopedia.net/android/others/review_home_bg.png"
    }

    override fun bind(element: RechargeRecommendationViewModel) {
        itemView.recharge_recommendation_list.adapter =
                RechargeRecommendationAdapter(element.rechargeRecommendation, listener)

//        ImageHandler.LoadImage(itemView.review_card_bg, cardBg)
//
//        if (element.suggestedProductReview.suggestedProductReview.linkURL.isEmpty()) {
//            itemView.loading_review.visibility = View.VISIBLE
//        } else {
//            isPressed = false
//            itemView.loading_review.visibility = View.GONE
//            itemView.review_title.text = String.format("%s %s",
//                    element.suggestedProductReview.suggestedProductReview.title,
//                    element.suggestedProductReview.suggestedProductReview.description
//            )
//
//            ImageHandler.loadRoundedImage(
//                    itemView.img_review,
//                    element.suggestedProductReview.suggestedProductReview.imageUrl,
//                    8.dpToPx(itemView.resources.displayMetrics).toFloat(),
//                    R.drawable.ic_loading_image,
//                    -1
//            )
//
//            itemView.addOnImpressionListener(element, object : ViewHintListener {
//                override fun onViewHint() {
//                    HomePageTracking.homeReviewImpression(
//                            categoryListener.trackingQueue,
//                            element.suggestedProductReview.suggestedProductReview,
//                            adapterPosition,
//                            element.suggestedProductReview.suggestedProductReview.orderId,
//                            element.suggestedProductReview.suggestedProductReview.productId
//                    )
//                }
//            })
//
//            itemView.review_card_content_container.setOnClickListener {
//                if (!isPressed) {
//                    HomePageTracking.homeReviewOnBlankSpaceClickTracker(
//                            element.suggestedProductReview.suggestedProductReview.orderId,
//                            element.suggestedProductReview.suggestedProductReview.productId
//                    )
//                    reviewListener.onReviewClick(
//                            adapterPosition,
//                            5,
//                            0,
//                            element.suggestedProductReview.suggestedProductReview.linkURL
//                    )
//                    isPressed = true
//                }
//            }
//
//            itemView.animated_review.resetStars()
//            itemView.animated_review.setListener(object : AnimatedReputationView.AnimatedReputationListener {
//                override fun onClick(position: Int) {
//                    if (!isPressed) {
//                        HomePageTracking.homeReviewOnRatingChangedTracker(
//                                element.suggestedProductReview.suggestedProductReview.orderId,
//                                element.suggestedProductReview.suggestedProductReview.productId,
//                                position + 1
//                        )
//                        reviewListener.onReviewClick(
//                                adapterPosition,
//                                position,
//                                500,
//                                element.suggestedProductReview.suggestedProductReview.linkURL
//                        )
//                        isPressed = true
//                    }
//                }
//            })
//
//            itemView.ic_close_review.setOnClickListener {
//                HomePageTracking.homeReviewOnCloseTracker(
//                        element.suggestedProductReview.suggestedProductReview.orderId,
//                        element.suggestedProductReview.suggestedProductReview.productId
//                )
//                reviewListener.onCloseClick()
//            }
        }

    interface RechargeRecommendationListener {
        fun onContentClickListener(applink: String)
        fun onDeclineClickListener(requestParams: Map<String, String>)
        fun removeRechargeRecommendation()
    }
}