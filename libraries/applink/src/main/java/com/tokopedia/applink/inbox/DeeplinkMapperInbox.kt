package com.tokopedia.applink.inbox

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.config.GlobalConfig

object DeeplinkMapperInbox {

    fun getRegisteredNavigationNotifcenter(): String {
        return if (!GlobalConfig.isSellerApp()) {
            Uri.parse(ApplinkConstInternalMarketplace.INBOX).buildUpon().apply {
                appendQueryParameter(
                    ApplinkConst.Inbox.PARAM_PAGE,
                    ApplinkConst.Inbox.VALUE_PAGE_NOTIFICATION
                )
                appendQueryParameter(
                    ApplinkConst.Inbox.PARAM_SHOW_BOTTOM_NAV,
                    false.toString()
                )
            }.toString()
        } else {
            ApplinkConst.SELLER_INFO
        }
    }
}