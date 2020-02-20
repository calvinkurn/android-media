package com.tokopedia.reviewseller.feature.reviewlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.SummaryReviewModel
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_rating_produk.view.tgCountRating
import kotlinx.android.synthetic.main.item_rating_produk.view.tgCountReview
import kotlinx.android.synthetic.main.summary_review_product.view.*

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