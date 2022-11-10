package com.tokopedia.applink.internal

import android.net.Uri
import com.tokopedia.applink.DLP
import com.tokopedia.applink.DeeplinkMapper
import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalOperational {
    const val HOST_CONTACT_US = "contactus"
    const val HOST_TICKET = "customercare"
    const val HOST_RESOLUTION = "resolution"
    const val INTERNAL_INBOX_LIST = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_TICKET/inbox-list"
    const val TICKET_DETAIL = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_TICKET/{id}/"
    const val SUCCESS_RESO = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_RESOLUTION/success-create"
    const val DETAIL_COMPLAIN = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_RESOLUTION/detail-complain"

    fun buildApplinkResolution(uri: Uri): String {
        val url = uri.getQueryParameter("URL").orEmpty()
        if (url.isEmpty()) {
            return Uri.parse(SUCCESS_RESO).toString()
        }
        return Uri.parse(SUCCESS_RESO).buildUpon().appendQueryParameter("URL", url).build().toString()
    }
}