package com.tokopedia.topchat.chatlist.data.util

import androidx.annotation.StringRes

interface TopChatListResourceProvider {
    fun getStringResource(@StringRes res: Int): String
}
