package com.tokopedia.moneyin

import com.tokopedia.url.TokopediaUrl.Companion.getInstance

object MoneyinConstants {
    @JvmField
    var LAKU6_BASEURL = getInstance().LAKU6
    @JvmField
    var LAKU6_BASEURL_STAGING = "http://wst.laku6.com"
    const val APPID = "1:109002668043:android:f4cc247c743f7921"
    const val CAMPAIGN_ID_PROD = "tokopediaTradeInProduction"
    const val CAMPAIGN_ID_STAGING = "tokopediaSandbox"
    const val LOGIN_REQUIRED = 1
    const val LOGEED_IN = 2
    const val MONEYIN = 2

}