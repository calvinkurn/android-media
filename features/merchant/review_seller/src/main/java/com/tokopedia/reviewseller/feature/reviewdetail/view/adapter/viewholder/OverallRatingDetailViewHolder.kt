package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.OverallRatingDetailUiModel
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifyprinciples.Typography

class OverallRatingDetailViewHolder(val view: View,
                                    private val listener: OverallRatingDetailListener): AbstractViewHolder<OverallRatingDetailUiModel>(view) {

    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_overall_review_detail
    }

    private val ratingStar: Typography = view.findViewById(R.id.rating_star_overall)
    private val totalReview: Typography = view.findViewById(R.id.total_review)
    private val reviewPeriod: ChipsUnify = view.findViewById(R.id.review_period_filter_button_detail)

    override fun bind(element: OverallRatingDetailUiModel?) {
        ratingStar.text = element?.ratingAvg.toString()
        totalReview.text = element?.ratingCount.toString()

        reviewPeriod.apply {
            setOnClickListener {

            }
            setChevronClickListener {

            }
        }

        listener.onSetTitleToolbar(element?.productName.toString())

    }

    interface OverallRatingDetailListener {
        fun onSetTitleToolbar(title: String)
    }
}