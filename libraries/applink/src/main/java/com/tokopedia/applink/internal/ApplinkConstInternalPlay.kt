package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalPlay {

    @JvmField
    val HOST_MARKETPLACE = "groupchat"

    @JvmField
    //tokopedia-android-internal://groupchat
    val GROUPCHAT_LIST = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_MARKETPLACE"

    @JvmField
    //tokopedia-android-internal://groupchat/{channel_id}/
    val GROUPCHAT_DETAIL = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_MARKETPLACE/{channel_id}/"

}