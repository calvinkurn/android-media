package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalNotification {

    @JvmField
    val HOST_MARKETPLACE = "marketplace"

    @JvmField
    val PATH_NOTIFICATION = "notification"

    @JvmField
    val INTERNAL_MARKETPLACE = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_MARKETPLACE"

    @JvmField
    val NOTIFICATION = "$INTERNAL_MARKETPLACE/$PATH_NOTIFICATION"

}