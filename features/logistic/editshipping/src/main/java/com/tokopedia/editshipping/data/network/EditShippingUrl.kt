package com.tokopedia.editshipping.data.network

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.url.TokopediaUrl.Companion.getInstance

object EditShippingUrl {
    val BASE_MOBILE_URL = getInstance().MOBILEWEB

    val URL_BEBAS_ONGKIR = BASE_MOBILE_URL + "bebas-ongkir"

    @JvmStatic
    val APPLINK_BEBAS_ONGKIR = String.format("%s?titlebar=false&url=%s", ApplinkConst.WEBVIEW, URL_BEBAS_ONGKIR)
}
