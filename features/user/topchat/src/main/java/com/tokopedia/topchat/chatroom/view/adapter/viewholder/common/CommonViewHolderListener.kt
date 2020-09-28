package com.tokopedia.topchat.chatroom.view.adapter.viewholder.common

import com.tokopedia.topchat.common.analytics.TopChatAnalytics

interface CommonViewHolderListener {
    fun isSeller(): Boolean
    fun getAnalytic(): TopChatAnalytics
}