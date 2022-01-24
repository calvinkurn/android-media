package com.tokopedia.inbox.view.activity.base.chat

import android.net.Uri
import com.tokopedia.applink.ApplinkConst

open class InboxChatBuyerTest : InboxChatTest() {

    override fun onBuildUri(uriBuilder: Uri.Builder) {
        super.onBuildUri(uriBuilder)
        uriBuilder.appendQueryParameter(
                ApplinkConst.Inbox.PARAM_ROLE,
                ApplinkConst.Inbox.VALUE_ROLE_BUYER
        )
    }

}