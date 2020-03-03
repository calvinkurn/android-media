package com.tokopedia.reviewseller.feature.reviewlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewlist.view.model.SummaryReviewModel
import com.tokopedia.unifyprinciples.Typography

class ReviewSummaryViewHolder(itemView: View): AbstractViewHolder<SummaryReviewModel>(itemView) {

    private val tgCountRating: Typography = itemView.findViewById(R.id.tgCountRating)
    private val tgCountReview: Typography = itemView.findViewById(R.id.tgCountReview)
    private val tgFiveReview: Typography = itemView.findViewById(R.id.tgFiveReview)
    private val tgPeriodReview: Typography = itemView.findViewById(R.id.tgPeriodReview)

    override fun bind(element: SummaryReviewModel?) {

        tgCountRating.text = element?.rating
        tgCountReview.text = element?.review
        tgFiveReview.text = "/ 5.0"
        tgPeriodReview.text = element?.period
    }

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.summary_review_product
    }
}