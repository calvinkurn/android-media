package com.tokopedia.chat_common.domain.pojo.attachmentmenu

import com.tokopedia.chat_common.R

class ChatbotImageMenu : AttachmentMenu(
       R.drawable.ic_image_orange, "Gambar", "image"
) {
    override fun onClick(listener: AttachmentMenuListener) {
        listener.onClickAttachImage(this)
    }
}
