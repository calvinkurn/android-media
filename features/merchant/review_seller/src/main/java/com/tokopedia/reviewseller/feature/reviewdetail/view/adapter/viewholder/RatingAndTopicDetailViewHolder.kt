package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.RatingBarUiModel
import kotlinx.android.synthetic.main.item_rating_bar_review_detail.view.*

class RatingAndTopicDetailViewHolder(val view: View) : AbstractViewHolder<RatingBarUiModel>(view) {

    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_rating_bar_review_detail
        private const val DEFAULT_RATING_BAR_VALUE = 0
    }

    override fun bind(element: RatingBarUiModel) {
        view.rating_star_value.text = element.ratingCount.toString()
        view.rating_total_review.text = String.format("(${element.ratingLabel})")
        view.progress_bar_rating.setValue(element.ratingCount.orZero(), true)
    }
}