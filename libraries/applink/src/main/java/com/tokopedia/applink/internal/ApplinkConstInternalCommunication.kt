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
    const val ID = "id"
    const val SOURCE = "source"
    const val SELECTED_ID = "selected_id"

    // Sharing Experience
    const val PRODUCT_LIST_DATA = "product_list_data"
    const val SHARE_TRACKER = "tracker" // TODO: change to actual tracker needed
    const val SHARE_DEFAULT_URL = "default_url"
    const val SHARE_DEFAULT_IMAGE_URL = "default_image_url"

    // TokoChat BottomSheet Type
    const val GUIDE_CHAT = "guide-chat"

    /**
     * Internal applink communication list
     */

    /**
     * TokoChat
     */
    // TokoChatActivity
    const val TOKO_CHAT = "$INTERNAL_COMMUNICATION/tokochat"

    // TokoChatListActivity
    const val TOKOCHAT_LIST = "$INTERNAL_COMMUNICATION/tokochat/list"

    // TokoChatBottomSheetActivity
    const val TOKOCHAT_BOTTOMSHEET = "$INTERNAL_COMMUNICATION/tokochat/bottomsheet/{type}"

    /**
     * Inbox
     */
    // UniversalInboxActivity
    const val UNIVERSAL_INBOX = "$INTERNAL_COMMUNICATION/universal-inbox"

    /**
     * Share
     */
    // PostPurchaseSharingActivity
    const val POST_PURCHASE_SHARING = "$INTERNAL_COMMUNICATION/sharing/post-purchase"

    // ShareExLoadingActivity
    const val SHARE_EXPERIENCE = "$INTERNAL_COMMUNICATION/share-experience"
}
