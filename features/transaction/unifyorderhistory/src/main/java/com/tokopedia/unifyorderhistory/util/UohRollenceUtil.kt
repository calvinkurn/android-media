package com.tokopedia.unifyorderhistory.util

import com.tokopedia.remoteconfig.RemoteConfigInstance

object UohRollenceUtil {

    fun isEnableAutoRedirectionToCartOnRepurchase(): Boolean {
        return try {
            val abTestPlatform = RemoteConfigInstance.getInstance().abTestPlatform
            val abTestUohCartRedirectRepurchase = abTestPlatform.getString("change me please", "")
            abTestUohCartRedirectRepurchase == ""
        } catch (throwable: Throwable) {
            false
        }
    }

}
