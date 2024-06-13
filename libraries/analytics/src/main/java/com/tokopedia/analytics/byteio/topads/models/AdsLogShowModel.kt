package com.tokopedia.analytics.byteio.topads.models

import android.content.Context
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.analytics.byteio.topads.AppLogTopAds
import com.tokopedia.analytics.byteio.topads.getSystemBootTime
import com.tokopedia.analytics.byteio.topads.putChannelName
import com.tokopedia.analytics.byteio.topads.putEnterFrom
import com.tokopedia.analytics.byteio.topads.putNetworkType
import com.tokopedia.analytics.byteio.topads.putProductName
import com.tokopedia.analytics.byteio.topads.putTag
import org.json.JSONObject

data class AdsLogShowModel(
    val adsValue: Long,
    val logExtra: String,
    val adExtraData: AdExtraData
) {
    data class AdExtraData(
        val mallCardType: String = AdsLogConst.AdCardStyle.PRODUCT_CARD,
        val productId: String,
        val productName: String,
    )

    fun toJSONObject(context: Context) = JSONObject().apply {

        val tagValue = AppLogTopAds.getTagValue(AppLogTopAds.currentPageName)

        put(
            AdsLogConst.Param.AD_EXTRA_DATA,
            JSONObject().apply {
                if(tagValue == AdsLogConst.Tag.TOKO_RESULT_MALL_AD) {
                    putChannelName(AppLogTopAds.getChannel())
                    putEnterFrom(AppLogTopAds.getEnterFrom())
                }
                put(AdsLogConst.Param.MALL_CARD_TYPE, AdsLogConst.AdCardStyle.PRODUCT_CARD)
                put(AdsLogConst.Param.PRODUCT_ID, adExtraData.productId)
                putProductName(adExtraData.productName)
            }
        )

        put(AdsLogConst.Param.CATEGORY, AdsLogConst.EVENT_V3)
        put(AdsLogConst.Param.IS_AD_EVENT, "1")
        putNetworkType(context)
        put(AdsLogConst.Param.VALUE, adsValue)
        put(AdsLogConst.Param.LOG_EXTRA, logExtra)
        put(AdsLogConst.Param.GROUP_ID, "0")
        put(AdsLogConst.Param.SYSTEM_START_TIMESTAMP, getSystemBootTime())

        putTag(tagValue)
    }
}
