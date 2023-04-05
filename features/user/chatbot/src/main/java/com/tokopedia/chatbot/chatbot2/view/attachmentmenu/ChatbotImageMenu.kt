package com.tokopedia.chatbot.chatbot2.view.attachmentmenu

import com.tokopedia.chat_common.R
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.AttachmentMenu

class ChatbotImageMenu : AttachmentMenu(
    R.drawable.ic_image_orange_chatbot,
    "Gambar",
    "image"
) {
    override fun onClick(listener: AttachmentMenuListener) {
        listener.onClickAttachImage(this)
    }
}
