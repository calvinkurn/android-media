package com.tokopedia.applink.content

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.applink.startsWithPattern

/**
 * Created by jegul on 2019-11-04
 */
object DeeplinkMapperContent {

    fun getRegisteredNavigationContent(deeplink: String): String {
        return if (deeplink.startsWithPattern(ApplinkConst.PROFILE)) getRegisteredNavigation(deeplink)
        else deeplink
    }

    fun getRegisteredNavigationPlay(deeplink: String): String {
        return if (deeplink.startsWithPattern(ApplinkConst.PLAY_DETAIL)) getRegisteredNavigation(deeplink)
        else deeplink
    }

    fun getRegisteredNavigationInterestPick(deeplink: String): String {
        return if (deeplink.startsWithPattern(ApplinkConst.INTEREST_PICK)) getRegisteredNavigation(deeplink)
        else deeplink
    }

    /**
     * tokopedia://people/{user_id}
     * tokopedia://people/{user_id}?after_post=true
     * tokopedia://people/{user_id}?after_edit=true
     * tokopedia://people/{user_id}?success_post=true
     */
    private fun getRegisteredNavigation(deeplink: String): String {
        return deeplink.replace(DeeplinkConstant.SCHEME_TOKOPEDIA, DeeplinkConstant.SCHEME_INTERNAL)
    }

    fun isPostDetailDeepLink(uri: Uri): Boolean {
        val lastPathSegment = uri.lastPathSegment?.toIntOrNull()
        return uri.host == ApplinkConstInternalContent.HOST_CONTENT && uri.pathSegments.size == 1 && lastPathSegment != null
    }

    fun getKolDeepLink(deepLink: String): String {
        return if (deepLink.startsWithPattern(ApplinkConst.KOL_COMMENT)) {
            getRegisteredNavigation(deepLink).plus(ApplinkConstInternalContent.COMMENT_EXTRA_PARAM)
        } else if (deepLink.startsWithPattern(ApplinkConst.CONTENT_DETAIL)) {
            deepLink.replace(ApplinkConst.CONTENT_DETAIL.substringBefore("{"), ApplinkConstInternalContent.INTERNAL_CONTENT_POST_DETAIL)
        } else {
            return getRegisteredNavigation(deepLink)
        }
    }
}