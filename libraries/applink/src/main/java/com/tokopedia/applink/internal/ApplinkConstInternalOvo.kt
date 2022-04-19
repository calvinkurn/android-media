package com.tokopedia.applink.internal

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia-android-internal://ovo".
 */
object ApplinkConstInternalOvo {

    const val STATUS = "status"
    const val MESSAGE = "message"

    private const val HOST_OVO = "ovo"
    private const val INTERNAL_OVO = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_OVO}"
    const val OVO_UPGRADE = "$INTERNAL_OVO/upgrade"
    const val OVO_UPGRADE_STATUS = "$INTERNAL_OVO/upgradestatus"

}
