package com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.inbox.R

class LoadingInboxReputationViewholder constructor(itemView: View?) :
    AbstractViewHolder<LoadingModel?>(itemView) {
    public override fun bind(element: LoadingModel) {
        itemView.setLayoutParams(
            AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }

    companion object {
        @LayoutRes
        val LAYOUT: Int = R.layout.item_shimmering_inbox_reputation
    }
}