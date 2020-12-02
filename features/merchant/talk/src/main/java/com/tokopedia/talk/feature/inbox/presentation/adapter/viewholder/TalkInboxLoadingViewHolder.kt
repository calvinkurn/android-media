package com.tokopedia.talk.feature.inbox.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.R

class TalkInboxLoadingViewHolder(view: View) : AbstractViewHolder<LoadingMoreModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_talk_inbox_shimmer
    }

    override fun bind(element: LoadingMoreModel?) {
        // No Op
    }
}