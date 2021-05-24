package com.tokopedia.topchat.chatroom.view.adapter.viewholder.srw

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.srw.SrwBubbleUiModel

class SrwBubbleViewHolder(
        itemView: View?
) : AbstractViewHolder<SrwBubbleUiModel>(itemView) {

    override fun bind(element: SrwBubbleUiModel) {

    }

    companion object {
        val LAYOUT = R.layout.item_chat_srw_bubble
    }
}