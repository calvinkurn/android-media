package com.tokopedia.chat_common.domain.pojo.attachmentmenu

import com.tokopedia.iconunify.IconUnify

open class ImageMenu : AttachmentMenu(
    icon = IconUnify.IMAGE,
    title = "Gambar",
    label = "image"
) {
    override fun onClick(listener: AttachmentMenuListener) {
        listener.onClickAttachImage(this)
    }
}
