package com.tokopedia.applink.inbox

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

object DeeplinkMapperInbox {

    fun getRegisteredNavigationNotifcenter(): String {
        var applink = ApplinkConstInternalMarketplace.NOTIFICATION_CENTER
        if (useNewNotifcenterOnInbox() && !GlobalConfig.isSellerApp()) {
            applink = Uri.parse(ApplinkConstInternalMarketplace.INBOX).buildUpon().apply {
                appendQueryParameter(
                    ApplinkConst.Inbox.PARAM_PAGE,
                    ApplinkConst.Inbox.VALUE_PAGE_NOTIFICATION
                )
                appendQueryParameter(
                    ApplinkConst.Inbox.PARAM_SHOW_BOTTOM_NAV,
                    false.toString()
                )
            }.toString()
        }
        return applink
    }

    private fun useNewNotifcenterOnInbox(): Boolean {
        return RemoteConfigInstance.getInstance().abTestPlatform.getString(
            RollenceKey.KEY_NEW_NOTFICENTER, RollenceKey.VARIANT_OLD_NOTFICENTER
        ) == RollenceKey.VARIANT_NEW_NOTFICENTER
    }
}