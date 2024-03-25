package com.tokopedia.home_account.account_settings

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.url.TokopediaUrl.Companion.getInstance

/**
 * @author okasurya on 9/14/18.
 */
object AccountHomeUrl {
    private val BASE_MOBILE_URL = getInstance().MOBILEWEB
    private val URL_TOKOPEDIA_CORNER = BASE_MOBILE_URL + "tokopedia-corner"
    val APPLINK_TOKOPEDIA_CORNER =
        String.format("%s?url=%s", ApplinkConst.WEBVIEW, URL_TOKOPEDIA_CORNER)
}
