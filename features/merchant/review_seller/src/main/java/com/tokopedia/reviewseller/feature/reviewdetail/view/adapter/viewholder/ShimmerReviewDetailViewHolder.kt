package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.reviewseller.R

class ShimmerReviewDetailViewHolder(view: View): AbstractViewHolder<LoadingModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.rating_product_detail_shimmer
    }

    override fun bind(element: LoadingModel) {}
}