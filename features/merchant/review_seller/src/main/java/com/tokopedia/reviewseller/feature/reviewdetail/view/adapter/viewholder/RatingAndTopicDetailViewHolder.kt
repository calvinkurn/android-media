package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.RatingBarUiModel
import kotlinx.android.synthetic.main.item_rating_bar_review_detail.view.*

class RatingAndTopicDetailViewHolder(val view: View) : AbstractViewHolder<RatingBarUiModel>(view) {

    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_rating_bar_review_detail
    }

    override fun bind(element: RatingBarUiModel) {
        view.rating_star_label.text = element.ratingLabel.toString()
        view.rating_total_review.text = String.format("(${element.ratingCount})")
        view.progress_bar_rating.apply {
            setValue(50, true)
        }
    }
}