package com.tokopedia.homenav.mainnav.view.adapter.viewholder.review

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderReviewBinding
import com.tokopedia.homenav.mainnav.view.analytics.TrackingTransactionSection
import com.tokopedia.homenav.mainnav.view.datamodel.review.ReviewModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.track.TrackApp
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class ReviewItemViewHolder(itemView: View, val mainNavListener: MainNavListener) :
    AbstractViewHolder<ReviewModel>(itemView) {
    private var binding: HolderReviewBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_review
        private const val UTM_SOURCE_GLOBAL_NAV = "global_nav"
        private const val SOURCE = "source"
        private const val RATING = "rating"
        private const val RATE_STARS_1 = "1"
        private const val RATE_STARS_2 = "2"
        private const val RATE_STARS_3 = "3"
        private const val RATE_STARS_4 = "4"
        private const val RATE_STARS_5 = "5"
    }

    private fun setLayoutFullWidth(element: ReviewModel) {
        val layoutParams = binding?.orderReviewCard?.layoutParams
        if (element.navReviewModel.fullWidth) {
            layoutParams?.width = ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            layoutParams?.width =
                itemView.resources.getDimension(com.tokopedia.homenav.R.dimen.nav_card_me_page_size).toInt()
        }
        binding?.orderReviewCard?.layoutParams = layoutParams
    }

    override fun bind(element: ReviewModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun clickReviewCard(element: ReviewModel, rateStars: String) {
        val tracking = TrackingTransactionSection.getClickReviewStars(adapterPosition, mainNavListener.getUserId(), element.navReviewModel, rateStars)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(tracking.first, tracking.second)
        mainNavListener.showReviewProduct(generateUriShowBottomSheetReview(element, rateStars))
    }

    private fun generateUriShowBottomSheetReview(element: ReviewModel, rateStars: String) : String {
        return Uri.parse(
            UriUtil.buildUri(
                ApplinkConstInternalMarketplace.CREATE_REVIEW,
                element.navReviewModel.reputationId,
                element.navReviewModel.productId
            )
        )
            .buildUpon()
            .appendQueryParameter(RATING, rateStars)
            .appendQueryParameter(
                SOURCE,
                UTM_SOURCE_GLOBAL_NAV
            )
            .build()
            .toString()
    }

    override fun bind(element: ReviewModel) {
        setLayoutFullWidth(element)
        binding?.orderReviewProductName?.text = element.navReviewModel.productName

        if (element.navReviewModel.imageUrl.isNotEmpty()) {
            binding?.orderReviewImage?.setImageUrl(element.navReviewModel.imageUrl)
        }

        binding?.orderReviewContainer?.setOnClickListener {
            val tracking = TrackingTransactionSection.getClickReviewCard(adapterPosition, mainNavListener.getUserId(), element.navReviewModel)
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(tracking.first, tracking.second)
            mainNavListener.showReviewProduct(generateUriShowBottomSheetReview(element, RATE_STARS_5))
        }
        binding?.layoutReviewStars?.star1?.setOnClickListener {
            clickReviewCard(element, RATE_STARS_1)
        }
        binding?.layoutReviewStars?.star2?.setOnClickListener {
            clickReviewCard(element, RATE_STARS_2)
        }
        binding?.layoutReviewStars?.star3?.setOnClickListener {
            clickReviewCard(element, RATE_STARS_3)
        }
        binding?.layoutReviewStars?.star4?.setOnClickListener {
            clickReviewCard(element, RATE_STARS_4)
        }
        binding?.layoutReviewStars?.star5?.setOnClickListener {
            clickReviewCard(element, RATE_STARS_5)
        }
        itemView.addOnImpressionListener(element) {
            mainNavListener.putEEToTrackingQueue(
                TrackingTransactionSection.getImpressionOnReviewProduct(
                    userId = mainNavListener.getUserId(),
                    element = element.navReviewModel,
                    position = adapterPosition
                )
            )
        }
    }
}
