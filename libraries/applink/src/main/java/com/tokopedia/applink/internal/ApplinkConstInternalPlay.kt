package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalPlay {

    @JvmField
    val HOST_GROUPCHAT = "groupchat"

    @JvmField
    //tokopedia-android-internal://groupchat
    val GROUPCHAT_LIST = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_GROUPCHAT"

    @JvmField
    //tokopedia-android-internal://groupchat/{channel_id}/
    val GROUPCHAT_DETAIL = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_GROUPCHAT/{channel_id}/"

}