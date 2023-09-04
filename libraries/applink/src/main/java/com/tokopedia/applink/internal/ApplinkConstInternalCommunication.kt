package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia-android-internal://communication".
 */
object ApplinkConstInternalCommunication {

    const val HOST_COMMUNICATION = "communication"
    const val INTERNAL_COMMUNICATION = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_COMMUNICATION"

    /**
     * Parameters key
     */

    // General
    const val SOURCE = "source"

    // Sharing Experience
    const val PRODUCT_IDS = "product_ids"


    /**
     * Internal applink communication list
     */

    // TokoChatActivity
    const val TOKO_CHAT = "$INTERNAL_COMMUNICATION/tokochat"

    // TokoChat List
    const val TOKOCHAT_LIST = "$INTERNAL_COMMUNICATION/tokochat/list"

    // UniversalInboxActivity
    const val UNIVERSAL_INBOX = "$INTERNAL_COMMUNICATION/universal-inbox"

    // PostPurchaseSharingActivity
    const val POST_PURCHASE_SHARING = "$INTERNAL_COMMUNICATION/sharing/post-purchase"
}
