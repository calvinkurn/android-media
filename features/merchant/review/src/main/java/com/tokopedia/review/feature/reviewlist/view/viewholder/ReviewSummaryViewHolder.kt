package com.tokopedia.review.feature.reviewlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.R
import com.tokopedia.review.common.util.roundDecimal
import com.tokopedia.review.feature.reviewlist.view.adapter.ReviewSellerAdapter
import com.tokopedia.review.feature.reviewlist.view.model.ProductRatingOverallUiModel
import com.tokopedia.unifyprinciples.Typography

class ReviewSummaryViewHolder(val view: View,
                              private val listener: ReviewSummaryViewListener) : AbstractViewHolder<ProductRatingOverallUiModel>(view) {

    private val tgCountRating: Typography = view.findViewById(R.id.tgCountRating)
    private val tgCountReview: Typography = view.findViewById(R.id.tgCountReview)
    private val tgFiveReview: Typography = view.findViewById(R.id.tgFiveReview)
    private val tgPeriodReview: Typography = view.findViewById(R.id.tgPeriodReview)

    override fun bind(element: ProductRatingOverallUiModel?) {

        tgCountRating.text = element?.rating?.roundDecimal()
        tgCountReview.text = element?.reviewCount?.toString()
        tgFiveReview.text = getString(R.string.rating_overall_product)
        updatePeriod(element?.period ?: "")

        listener.onAddedCoachMarkOverallRating(view)
    }

    override fun bind(element: ProductRatingOverallUiModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty()) {
            return
        }

        when (payloads[0] as Int) {
            ReviewSellerAdapter.PAYLOAD_SUMMARY_PERIOD -> updatePeriod(element.period ?: "")
        }

    }

    private fun updatePeriod(periodString: String) {
        tgPeriodReview.text = view.context.getString(R.string.date_summary_calculation_builder, periodString)
    }

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.summary_review_product
    }

    interface ReviewSummaryViewListener {
        fun onAddedCoachMarkOverallRating(view: View)
    }
}