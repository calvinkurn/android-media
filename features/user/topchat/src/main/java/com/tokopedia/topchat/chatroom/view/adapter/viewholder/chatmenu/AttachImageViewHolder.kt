package com.tokopedia.topchat.chatroom.view.adapter.viewholder.chatmenu

import android.view.View

class AttachImageViewHolder(listener: ChatMenuListener, itemView: View?) : BaseChatMenuViewHolder(listener, itemView) {

    override fun onItemClick() {
        listener.onClickImagePicker()
    }

}