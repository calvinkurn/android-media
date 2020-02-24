package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.RatingBarModel
import kotlinx.android.synthetic.main.partial_rating_bar_review_detail.view.*

class RatingBarViewHolder (val view: View?): AbstractViewHolder<RatingBarModel>(view) {
    companion object {
        @JvmStatic
        val LAYOUT = R.layout.partial_rating_bar_review_detail
    }

    override fun bind(element: RatingBarModel?) {
        itemView.review_rating_bar_rv.adapter = element?.ratingBarAdapter
    }
}