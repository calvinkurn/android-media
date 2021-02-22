package com.tokopedia.tradein

import com.tokopedia.keys.Keys
import com.tokopedia.url.TokopediaUrl.Companion.getInstance

object TradeinConstants {
    @JvmField
    var LAKU6_BASEURL = getInstance().LAKU6
    @JvmField
    var LAKU6_BASEURL_STAGING = "http://wst.laku6.com"
    const val APPID = "1:109002668043:android:f4cc247c743f7921"
    const val ACTION_GO_TO_SHIPMENT = "ACTION_GO_TO_SHIPMENT"
    const val CAMPAIGN_ID_PROD = "tokopediaTradeInProduction"
    const val CAMPAIGN_ID_STAGING = "tokopediaSandbox"
    const val LOGIN_REQUIRED = 1
    const val LOGEED_IN = 2



    object UseCase {
        const val KEY_SPID = "spids"
        const val KEY_TRADEIN = "tradeIn"
        const val KEY_WEIGHT = "weight"
        const val KEY_DESTINATION = "destination"
        const val KEY_ORIGIN = "origin"
        const val KEY_SHOP_IDS = "shopIds"
        const val KEY_FIELDS = "fields"

        const val SHIPMENT = "shipment"
        const val SP_ID = "48"
        const val TRADE_IN = 2
    }

    object Deeplink  {
        const val TRADEIN_DISCOVERY_INFO_URL = "tokopedia://discovery/tukar-tambah-edukasi"
    }
}