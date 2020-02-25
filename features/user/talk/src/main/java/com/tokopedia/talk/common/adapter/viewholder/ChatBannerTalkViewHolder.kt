package com.tokopedia.talk.common.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.R
import com.tokopedia.talk.producttalk.view.data.ChatBannerUiModel

class ChatBannerTalkViewHolder(itemView: View?) : AbstractViewHolder<ChatBannerUiModel>(itemView) {
    override fun bind(element: ChatBannerUiModel?) {

    }

    companion object {
        val LAYOUT = R.layout.item_talk_chat_banner
    }
}