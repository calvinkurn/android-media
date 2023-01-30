package com.tokopedia.content.common.navigation.shorts

import com.tokopedia.applink.ApplinkConst

/**
 * Created By : Jonathan Darwin on November 07, 2022
 */
object PlayShorts {

    fun generateApplink(
        param: PlayShortsParam.() -> Unit = {}
    ): String {
        val baseApplink = ApplinkConst.PLAY_SHORTS
        val appLinkParam = PlayShortsParam().apply(param).buildParam()

        if(appLinkParam.isEmpty()) return baseApplink

        return "$baseApplink?$appLinkParam"
    }
}
