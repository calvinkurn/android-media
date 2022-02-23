package com.tokopedia.applink.uprofile

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalFeed.INTERNAL_FEED_DETAILS

object DeepLinkMapperUserProfile {
    fun getRegisteredUserProfile(deepLink: String): String {
        if (deepLink.startsWith(ApplinkConst.USER_PROFILE_LANDING)) {
            return deepLink.replace(ApplinkConst.USER_PROFILE_LANDING, INTERNAL_FEED_DETAILS)
        }
        return deepLink
    }

    fun getRegisteredUserProfileFollowers(deepLink: String): String {
        if (deepLink.startsWith(ApplinkConst.USER_PROFILE_FOLLOWERS)) {
            return deepLink.replace(ApplinkConst.USER_PROFILE_FOLLOWERS, INTERNAL_FEED_DETAILS)
        }
        return deepLink
    }
}