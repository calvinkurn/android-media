package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.uimodel.ReviewUiModel

class ReviewViewHolder(
        itemView: View?
) : AbstractViewHolder<ReviewUiModel>(itemView) {

    override fun bind(element: ReviewUiModel) {

    }

    companion object {
        val LAYOUT = R.layout.item_topchat_review_reminder
    }
}