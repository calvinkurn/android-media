package com.tokopedia.applink.content

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalContent.COMMENT
import com.tokopedia.applink.internal.ApplinkConstInternalContent.COMMENT_EXTRA_PARAM
import com.tokopedia.applink.internal.ApplinkConstInternalContent.CONTENT_REPORT
import com.tokopedia.applink.internal.ApplinkConstInternalContent.INTERNAL_CONTENT_POST_DETAIL
import com.tokopedia.applink.internal.ApplinkConstInternalContent.INTERNAL_KOL_YOUTUBE
import com.tokopedia.applink.internal.ApplinkConstInternalContent.MEDIA_PREVIEW
import com.tokopedia.applink.internal.ApplinkConstInternalContent.VIDEO_DETAIL
import com.tokopedia.applink.startsWithPattern

object DeepLinkMapperKol {
    fun getKolDeepLink(deepLink: String): String {
        when {
            deepLink.startsWithPattern(ApplinkConst.KOL_COMMENT) -> {
                return deepLink.replace(ApplinkConst.KOL_COMMENT.substringBefore("{"), COMMENT.substringBefore("{")).plus(COMMENT_EXTRA_PARAM)
            }
            deepLink.startsWithPattern(ApplinkConst.CONTENT_DETAIL) -> {
                return deepLink.replace(ApplinkConst.CONTENT_DETAIL.substringBefore("{"), INTERNAL_CONTENT_POST_DETAIL)
            }
            deepLink.startsWithPattern(ApplinkConst.KOL_YOUTUBE) -> {
                return deepLink.replace(ApplinkConst.KOL_YOUTUBE.substringBefore("{"), INTERNAL_KOL_YOUTUBE.substringBefore("{"))
            }
            deepLink.startsWithPattern(ApplinkConst.KOL_CONTENT_REPORT) -> {
                return deepLink.replace(ApplinkConst.KOL_CONTENT_REPORT.substringBefore("{"), CONTENT_REPORT.substringBefore("{"))
            }
            deepLink.startsWithPattern(ApplinkConst.KOL_VIDEO_DETAIL) -> {
                return deepLink.replace(ApplinkConst.KOL_VIDEO_DETAIL.substringBefore("{"), VIDEO_DETAIL.substringBefore("{"))
            }
            deepLink.startsWithPattern(ApplinkConst.KOL_MEDIA_PREVIEW) -> {
                return deepLink.replace(ApplinkConst.KOL_MEDIA_PREVIEW.substringBefore("{"), MEDIA_PREVIEW.substringBefore("{"))
            }
        }
        return deepLink
    }
}