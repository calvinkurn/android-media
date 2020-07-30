package com.tokopedia.chat_common.domain.pojo.attachmentmenu

import com.tokopedia.chat_common.R

class ImageMenu : AttachmentMenu(
        R.drawable.ic_image_orange_chat_common, "Gambar", "image"
) {
    override fun onClick(listener: AttachmentMenuListener) {
        listener.onClickAttachImage(this)
    }
}