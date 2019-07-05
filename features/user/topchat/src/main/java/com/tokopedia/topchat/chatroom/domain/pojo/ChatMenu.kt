package com.tokopedia.topchat.chatroom.domain.pojo

import android.support.annotation.DrawableRes

data class ChatMenu(
        @DrawableRes
        val icon: Int,
        val title: String
)