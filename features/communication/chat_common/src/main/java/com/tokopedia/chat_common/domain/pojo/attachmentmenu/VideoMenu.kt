package com.tokopedia.chat_common.domain.pojo.attachmentmenu

import com.tokopedia.chat_common.R

class VideoMenu : AttachmentMenu(
    R.drawable.iconunify_video, "Video", "video"
) {
    override fun onClick(listener: AttachmentMenuListener) {
        listener.onClickAttachVideo(this)
    }
}