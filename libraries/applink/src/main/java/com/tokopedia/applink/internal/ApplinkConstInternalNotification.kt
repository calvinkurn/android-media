package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalNotification {

    const val HOST_MARKETPLACE = "marketplace"

    const val PATH_NOTIFICATION = "notification"

    const val PATH_NOTIFICATION_BUYER = "notif-center"

    const val INTERNAL_MARKETPLACE = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_MARKETPLACE"

    const val NOTIFICATION = "$INTERNAL_MARKETPLACE/$PATH_NOTIFICATION"

    const val NOTIFICATION_BUYER = "$INTERNAL_MARKETPLACE/$PATH_NOTIFICATION_BUYER"

}