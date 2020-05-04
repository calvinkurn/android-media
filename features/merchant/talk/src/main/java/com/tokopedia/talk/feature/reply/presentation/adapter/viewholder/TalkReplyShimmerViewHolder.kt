package com.tokopedia.talk.feature.reply.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyShimmerModel
import com.tokopedia.talk_old.R

class TalkReplyShimmerViewHolder(view: View) : AbstractViewHolder<LoadingMoreModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_talk_reply_shimmer
    }

    override fun bind(element: LoadingMoreModel) {
        // No Op
    }
}