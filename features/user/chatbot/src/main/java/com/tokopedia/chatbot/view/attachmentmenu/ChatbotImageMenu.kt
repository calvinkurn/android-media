package com.tokopedia.chatbot.view.attachmentmenu

import com.tokopedia.chat_common.domain.pojo.attachmentmenu.AttachmentMenu

class ChatbotImageMenu : AttachmentMenu(
        com.tokopedia.chatbot.R.drawable.ic_image_orange, "Gambar", "image"
) {
    override fun onClick(listener: AttachmentMenuListener) {
        listener.onClickAttachImage(this)
    }
}