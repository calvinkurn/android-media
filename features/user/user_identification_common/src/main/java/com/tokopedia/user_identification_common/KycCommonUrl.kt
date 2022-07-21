package com.tokopedia.user_identification_common

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.url.TokopediaUrl.Companion.getInstance

/**
 * @author by nisie on 16/11/18.
 */
object KycCommonUrl {
    private val URL_TERMS_AND_CONDITION = "https://www.tokopedia.com/help/article/syarat-dan-ketentuan-verifikasi-pengguna"
    @JvmField
    val APPLINK_TERMS_AND_CONDITION = String.format("%s?url=%s", ApplinkConst.WEBVIEW, URL_TERMS_AND_CONDITION)
}