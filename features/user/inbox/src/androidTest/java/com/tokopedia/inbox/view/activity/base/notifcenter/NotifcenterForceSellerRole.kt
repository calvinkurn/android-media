package com.tokopedia.inbox.view.activity.base.notifcenter

import android.net.Uri
import com.tokopedia.applink.ApplinkConst

abstract class NotifcenterForceSellerRole: InboxNotifcenterTest() {

    override fun onBuildUri(uriBuilder: Uri.Builder) {
        super.onBuildUri(uriBuilder)
        uriBuilder.appendQueryParameter(
            ApplinkConst.Inbox.PARAM_ROLE,
            ApplinkConst.Inbox.VALUE_ROLE_SELLER
        )
    }

}