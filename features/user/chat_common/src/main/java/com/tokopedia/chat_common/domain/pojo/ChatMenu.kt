package com.tokopedia.chat_common.domain.pojo

import androidx.annotation.DrawableRes

data class ChatMenu(
        @DrawableRes
        val icon: Int,
        val title: String,
        val label: String
)