package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalOperational {
    const val HOST_CONTACT_US = "contactus"
    const val INTERNAL_INBOX_LIST = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_CONTACT_US/inbox-list"
}