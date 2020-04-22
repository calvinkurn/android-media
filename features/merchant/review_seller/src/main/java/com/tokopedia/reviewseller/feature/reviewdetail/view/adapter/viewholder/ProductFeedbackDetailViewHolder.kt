package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.FeedbackUiModel

class ProductFeedbackDetailViewHolder(view: View) : AbstractViewHolder<FeedbackUiModel>(view) {

    companion object {
        @JvmStatic
        val LAYOUT = R.layout.product_feedback_detail
    }

    override fun bind(element: FeedbackUiModel?) {

    }
}