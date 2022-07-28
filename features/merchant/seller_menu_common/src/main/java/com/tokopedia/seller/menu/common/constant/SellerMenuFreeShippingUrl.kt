package com.tokopedia.seller.menu.common.constant

import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl

object SellerMenuFreeShippingUrl {

    val URL_FREE_SHIPPING_INTERIM_PAGE = if (TokopediaUrl.getInstance().TYPE == Env.STAGING) "https://staging.tokopedia.com/bebas-ongkir" else "https://m.tokopedia.com/bebas-ongkir"
    const val URL_PLUS_PAGE = "http://seller.tokopedia.com/plus"
}