package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.topchat.R

class TopchatEmptyViewHolder(itemView: View?) : BaseChatViewHolder<EmptyModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_topchat_empty
    }
}