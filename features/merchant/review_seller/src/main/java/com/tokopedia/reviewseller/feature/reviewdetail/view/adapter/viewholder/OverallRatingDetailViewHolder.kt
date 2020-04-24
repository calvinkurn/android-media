package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.OverallRatingDetailUiModel
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifyprinciples.Typography


class OverallRatingDetailViewHolder(val view: View): AbstractViewHolder<OverallRatingDetailUiModel>(view) {

    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_overall_review_detail
    }

    private val ratingStar: Typography = view.findViewById(R.id.rating_star_overall)
    private val totalReview: Typography = view.findViewById(R.id.total_review)
    private val reviewPeriod: ChipsUnify = view.findViewById(R.id.review_period_filter_button_detail)

    override fun bind(element: OverallRatingDetailUiModel?) {

        ratingStar.text = element?.ratingAvg.toString()

        val strReview = getString(R.string.review_text)
        val ratingCount = element?.reviewCount.toString()
        val strReviewSpan = SpannableString("$ratingCount $strReview")
        strReviewSpan.setSpan(StyleSpan(Typeface.BOLD), 0, ratingCount.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        totalReview.text = strReviewSpan

        reviewPeriod.apply {
            chip_text.text = getString(R.string.default_filter_detail)
            setOnClickListener {

            }
            setChevronClickListener {

            }
        }
    }

}