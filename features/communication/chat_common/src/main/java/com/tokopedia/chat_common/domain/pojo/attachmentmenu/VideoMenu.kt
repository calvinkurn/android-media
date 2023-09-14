package com.tokopedia.chat_common.domain.pojo.attachmentmenu

import com.tokopedia.iconunify.IconUnify

class VideoMenu : AttachmentMenu(
    icon = IconUnify.VIDEO,
    title = "Video",
    label = "video"
) {
    override fun onClick(listener: AttachmentMenuListener) {
        listener.onClickAttachVideo(this)
    }
}
