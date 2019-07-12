package com.tokopedia.chat_common.view.adapter.viewholder.chatmenu

import android.view.View

class ProductLinkViewHolder(listener: ChatMenuListener, itemView: View) : BaseChatMenuViewHolder(listener, itemView) {

    override fun onItemClick() {
        listener.onClickAttachProduct()
    }

}