package com.tokopedia.applink.content

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.applink.startsWithPattern

object DeepLinkMapperCreatePost {
    fun getContentCreatePostDeepLink(deepLink: String): String {
        when {
            deepLink.startsWith(ApplinkConst.CONTENT_CREATE_POST) -> {
                return deepLink.replace(ApplinkConst.CONTENT_CREATE_POST, ApplinkConstInternalContent.INTERNAL_CONTENT_CREATE_POST)
            }
            deepLink.startsWithPattern(ApplinkConst.CONTENT_DRAFT_POST) -> {
                return deepLink.replace(ApplinkConst.CONTENT_DRAFT_POST.substringBefore("{"), ApplinkConstInternalContent.INTERNAL_CONTENT_DRAFT_POST)
            }
            deepLink.startsWith(ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST) -> {
                return deepLink.replace(ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST, ApplinkConstInternalContent.INTERNAL_AFFILIATE_CREATE_POST)
            }
            deepLink.startsWith(ApplinkConst.AFFILIATE_DRAFT_POST) -> {
                return deepLink.replace(ApplinkConst.AFFILIATE_DRAFT_POST.substringBefore("{"), ApplinkConstInternalContent.INTERNAL_AFFILIATE_DRAFT_POST)
            }
            deepLink.startsWith(ApplinkConst.AFFILIATE_EDIT) -> {
                return deepLink.replace(ApplinkConst.AFFILIATE_EDIT, ApplinkConstInternalContent.INTERNAL_AFFILIATE_EDIT)
            }
            deepLink.startsWith(ApplinkConst.SHOP_POST_EDIT) -> {
                return deepLink.replace(ApplinkConst.SHOP_POST_EDIT, ApplinkConstInternalContent.INTERNAL_SHOP_POST_EDIT)
            }
        }

        return deepLink
    }
}