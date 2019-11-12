package com.tokopedia.applink.content

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.startsWithPattern

/**
 * Created by jegul on 2019-11-04
 */
object DeeplinkMapperContent {

    fun getRegisteredNavigationContent(deeplink: String): String {
        return if (deeplink.startsWithPattern(ApplinkConst.PROFILE)) getRegisteredNavigationProfile(deeplink)
        else deeplink
    }

    /**
     * tokopedia://people/{user_id}
     * tokopedia://people/{user_id}?after_post=true
     * tokopedia://people/{user_id}?after_edit=true
     * tokopedia://people/{user_id}?success_post=true
     */
    private fun getRegisteredNavigationProfile(deeplink: String): String {
        return deeplink.replace(DeeplinkConstant.SCHEME_TOKOPEDIA, DeeplinkConstant.SCHEME_INTERNAL)
    }

}