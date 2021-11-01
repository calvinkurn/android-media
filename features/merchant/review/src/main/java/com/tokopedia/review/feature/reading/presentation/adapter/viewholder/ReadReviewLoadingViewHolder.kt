package com.tokopedia.review.feature.reading.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.R

class ReadReviewLoadingViewHolder(view: View) : AbstractViewHolder<LoadingMoreModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_read_review_loading_more
    }

    override fun bind(element: LoadingMoreModel?) {
        // No Op
    }
}