package com.tokopedia.review.feature.inbox.history.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

class ReviewHistoryLoadingViewHolder(view: View) : AbstractViewHolder<LoadingMoreModel>(view) {

    companion object {
        val LAYOUT = 0
    }

    override fun bind(element: LoadingMoreModel) {
        // No op
    }
}