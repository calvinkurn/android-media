package com.tokopedia.chat_common.domain.pojo.attachmentmenu

import com.tokopedia.chat_common.R

class VideoMenu : AttachmentMenu(
    R.drawable.ic_video_upload,
    "Video",
    "video"
) {
    override fun onClick(listener: AttachmentMenuListener) {
        listener.onClickAttachVideo(this)
    }
}
