package com.tokopedia.reviewseller.feature.reviewlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductRatingOverallUiModel
import com.tokopedia.unifyprinciples.Typography

class ReviewSummaryViewHolder(itemView: View): AbstractViewHolder<ProductRatingOverallUiModel>(itemView) {

    private val tgCountRating: Typography = itemView.findViewById(R.id.tgCountRating)
    private val tgCountReview: Typography = itemView.findViewById(R.id.tgCountReview)
    private val tgFiveReview: Typography = itemView.findViewById(R.id.tgFiveReview)
    private val tgPeriodReview: Typography = itemView.findViewById(R.id.tgPeriodReview)

    override fun bind(element: ProductRatingOverallUiModel?) {

        tgCountRating.text = element?.rating?.toString()
        tgCountReview.text = element?.reviewCount?.toString()
        tgFiveReview.text = getString(R.string.rating_overall_product)
        tgPeriodReview.text = "1 Des 2019 - Hari Ini"
    }

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.summary_review_product
    }
}