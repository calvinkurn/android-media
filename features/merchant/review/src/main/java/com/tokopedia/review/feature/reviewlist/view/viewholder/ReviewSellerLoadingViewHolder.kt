package com.tokopedia.review.feature.reviewlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.R

class ReviewSellerLoadingViewHolder(view: View): AbstractViewHolder<LoadingModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.layout_rating_product_shimmer
    }

    override fun bind(element: LoadingModel) {}
}