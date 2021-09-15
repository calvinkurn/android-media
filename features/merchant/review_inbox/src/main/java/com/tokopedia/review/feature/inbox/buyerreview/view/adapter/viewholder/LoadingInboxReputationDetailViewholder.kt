package com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.inbox.R

class LoadingInboxReputationDetailViewholder constructor(itemView: View?) :
    AbstractViewHolder<LoadingModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT: Int = R.layout.item_shimmering_inbox_detail_reputation
    }

    override fun bind(element: LoadingModel) {
        itemView.layoutParams = AbsListView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}