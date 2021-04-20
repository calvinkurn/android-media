package com.tokopedia.review.feature.reviewdetail.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.R

class ShimmerReviewDetailViewHolder(view: View): AbstractViewHolder<LoadingModel>(view) {

    companion object {
        val LAYOUT = R.layout.rating_product_detail_shimmer
    }

    override fun bind(element: LoadingModel) {}
}