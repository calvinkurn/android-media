package com.tokopedia.topchat.chatroom.view.adapter

import com.tokopedia.chat_common.BaseChatAdapter
import com.tokopedia.chat_common.data.ImageUploadViewModel

/**
 * @author : Steven 02/01/19
 */
class TopChatRoomAdapter (adapterTypeFactory: TopChatTypeFactoryImpl)
    : BaseChatAdapter(adapterTypeFactory) {

    fun showRetryFor(model: ImageUploadViewModel, b: Boolean) {
        val position = visitables.indexOf(model)
        if(position < 0) return
        if (visitables[position] is ImageUploadViewModel) {
            (visitables[position] as ImageUploadViewModel).isRetry = true
            notifyItemChanged(position)
        }
    }
}