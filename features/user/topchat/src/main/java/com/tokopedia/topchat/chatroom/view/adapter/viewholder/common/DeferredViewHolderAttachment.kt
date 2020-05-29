package com.tokopedia.topchat.chatroom.view.adapter.viewholder.common

import androidx.collection.ArrayMap
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.Attachment

interface DeferredViewHolderAttachment {
    fun getLoadedChatAttachments(): ArrayMap<String, Attachment>
}