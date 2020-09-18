package com.tokopedia.user_identification_common

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.url.TokopediaUrl.Companion.getInstance

/**
 * @author by nisie on 16/11/18.
 */
object KycCommonUrl {
    const val TERMS_CONDITION = "https://m.tokopedia.com/myshop/verify/terms"
    private var BASE_URL = getInstance().MOBILEWEB
    private val URL_TERMS_AND_CONDITION = BASE_URL + "terms/merchantkyc"
    @JvmField
    val APPLINK_TERMS_AND_CONDITION = String.format("%s?url=%s", ApplinkConst.WEBVIEW, URL_TERMS_AND_CONDITION)
}