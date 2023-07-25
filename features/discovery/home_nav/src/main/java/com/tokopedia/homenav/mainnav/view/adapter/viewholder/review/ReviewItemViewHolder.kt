package com.tokopedia.homenav.mainnav.view.adapter.viewholder.review

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.homenav.MePage
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderReviewBinding
import com.tokopedia.homenav.mainnav.view.datamodel.review.ReviewModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */

@MePage(MePage.Widget.REVIEW)
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

    private fun clickReviewStars(element: ReviewModel, rateStars: String) {
        mainNavListener.onReviewCardClicked(
            element.navReviewModel,
            element.position,
            true,
            rateStars,
            generateUriShowBottomSheetReview(element, rateStars)
        )
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
            mainNavListener.onReviewCardClicked(
                element.navReviewModel,
                element.position,
                false,
                RATE_STARS_5,
                generateUriShowBottomSheetReview(element, RATE_STARS_5)
            )
        }
        binding?.layoutReviewStars?.star1?.setOnClickListener {
            clickReviewStars(element, RATE_STARS_1)
        }
        binding?.layoutReviewStars?.star2?.setOnClickListener {
            clickReviewStars(element, RATE_STARS_2)
        }
        binding?.layoutReviewStars?.star3?.setOnClickListener {
            clickReviewStars(element, RATE_STARS_3)
        }
        binding?.layoutReviewStars?.star4?.setOnClickListener {
            clickReviewStars(element, RATE_STARS_4)
        }
        binding?.layoutReviewStars?.star5?.setOnClickListener {
            clickReviewStars(element, RATE_STARS_5)
        }
        itemView.addOnImpressionListener(element) {
            mainNavListener.onReviewCardImpressed(element.navReviewModel, element.position)
        }
    }
}
