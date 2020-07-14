package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalNotification {

    @JvmField
    val HOST_MARKETPLACE = "marketplace"

    @JvmField
    val PATH_NOTIFICATION = "notification"

    @JvmField
    val PATH_NOTIFICATION_BUYER = "notif-center"

    @JvmField
    val INTERNAL_MARKETPLACE = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_MARKETPLACE"

    @JvmField
    val NOTIFICATION = "$INTERNAL_MARKETPLACE/$PATH_NOTIFICATION"

    @JvmField
    val NOTIFICATION_BUYER = "$INTERNAL_MARKETPLACE/$PATH_NOTIFICATION_BUYER"

}