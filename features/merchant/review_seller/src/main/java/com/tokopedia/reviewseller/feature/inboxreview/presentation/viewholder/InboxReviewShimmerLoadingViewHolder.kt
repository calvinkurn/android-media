package com.tokopedia.reviewseller.feature.inboxreview.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.reviewseller.R

class InboxReviewShimmerLoadingViewHolder(view: View): AbstractViewHolder<LoadingModel>(view) {

    companion object {
        val LAYOUT = R.layout.shimmer_inbox_review
    }

    override fun bind(element: LoadingModel?) {}

}