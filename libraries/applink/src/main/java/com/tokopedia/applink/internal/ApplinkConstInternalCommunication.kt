package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia-android-internal://communication".
 */
object ApplinkConstInternalCommunication {

    const val HOST_COMMUNICATION = "communication"
    const val INTERNAL_COMMUNICATION = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_COMMUNICATION}"

    //TokoChatActivity
    const val TOKO_CHAT = "${INTERNAL_COMMUNICATION}/tokochat"

    //UniversalInboxActivity
    const val UNIVERSAL_INBOX = "${INTERNAL_COMMUNICATION}/universal-inbox"
}
