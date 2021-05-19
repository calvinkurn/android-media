package com.tokopedia.inbox.view.activity.base

import android.content.Intent
import android.net.Uri
import com.tokopedia.applink.ApplinkConst

open class InboxChatTest : InboxTest() {
    override fun createActivityIntent(): Intent {
        val inboxChat = Uri.parse(ApplinkConst.INBOX).buildUpon().apply {
            appendQueryParameter(
                    ApplinkConst.Inbox.PARAM_PAGE,
                    ApplinkConst.Inbox.VALUE_PAGE_CHAT
            )
        }.build()
        return Intent().apply {
            data = inboxChat
        }
    }
}