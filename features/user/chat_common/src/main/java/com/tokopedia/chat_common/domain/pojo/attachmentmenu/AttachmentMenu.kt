package com.tokopedia.chat_common.domain.pojo.attachmentmenu

import androidx.annotation.DrawableRes

abstract class AttachmentMenu (
        @DrawableRes
        val icon: Int,
        val title: String,
        val label: String
) {

    interface AttachmentMenuListener {
        fun onClickAttachProduct(menu: AttachmentMenu)
        fun onClickAttachImage(menu: AttachmentMenu)
        fun createAttachmentMenus(): List<AttachmentMenu>
    }

    abstract fun onClick(listener: AttachmentMenuListener)
}