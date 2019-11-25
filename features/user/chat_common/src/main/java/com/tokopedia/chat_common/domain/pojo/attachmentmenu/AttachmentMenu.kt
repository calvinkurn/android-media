package com.tokopedia.chat_common.domain.pojo.attachmentmenu

import android.content.Context
import androidx.annotation.DrawableRes

abstract class AttachmentMenu (
        @DrawableRes
        val icon: Int,
        val title: String,
        val label: String
) {
    abstract fun onClick(context: Context)
}