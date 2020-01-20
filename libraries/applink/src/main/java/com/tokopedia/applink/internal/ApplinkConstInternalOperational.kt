package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalOperational {
    const val HOST_CONTACT_US = "contactus"
    const val HOST_TICKET = "customercare"
    const val INTERNAL_INBOX_LIST = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_TICKET/inbox-list"
    const val TICKET_DETAIL = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_TICKET/{id}/"
}