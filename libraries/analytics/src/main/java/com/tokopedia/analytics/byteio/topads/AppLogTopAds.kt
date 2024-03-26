package com.tokopedia.analytics.byteio.topads

import com.tokopedia.analytics.byteio.AppLogAnalytics
import org.json.JSONObject

/**
 * @see https://bytedance.sg.larkoffice.com/docx/IJW6dwOlPoEMEnxySgvlRBlCgNh
 */
object AppLogTopAds {

    /**
     * @param rit String
     * @param productId ID of Product
     * @param cardType AdsLogConst.AdCardStyle
     */
    fun sendEventShowMallAd(
        rit: String,
        productId: String,
        cardType: String,
        sessionInfo: String
    ) {
        AppLogAnalytics.send(
            AdsLogConst.Event.SHOW,
            JSONObject().apply {
                put(AdsLogConst.TAG, AdsLogConst.Tag.TOKO_MALL_AD)
                put(AdsLogConst.RIT, rit)
                put(AdsLogConst.Param.SYSTEM_START_TIMESTAMP, System.currentTimeMillis())
                put(AdsLogConst.Param.PRODUCT_ID, productId)
                put(AdsLogConst.Param.MALL_CARD_TYPE, cardType)
                // todo need to confirm
                put(AdsLogConst.Param.SESSION_INFO, sessionInfo)
            }
        )
    }

    /**
     * @param rit String
     * @param productId ID of Product
     * @param enterFrom AdsLogConst.EnterFrom
     * @param channel AdsLogConst.Channel
     * @param cardType AdsLogConst.AdCardStyle
     */
    fun sendEventShowMallAdResult(
        rit: String,
        productId: String,
        enterFrom: String,
        channel: String,
        cardType: String
    ) {
        AppLogAnalytics.send(
            AdsLogConst.Event.SHOW,
            JSONObject().apply {
                put(AdsLogConst.TAG, AdsLogConst.Tag.TOKO_RESULT_MALL_AD)
                put(AdsLogConst.RIT, rit)
                put(AdsLogConst.Param.SYSTEM_START_TIMESTAMP, System.currentTimeMillis())
                put(AdsLogConst.Param.PRODUCT_ID, productId)
                put(AdsLogConst.Param.MALL_CARD_TYPE, cardType)
                put(AdsLogConst.Param.ENTER_FROM, enterFrom)
                put(AdsLogConst.Param.CHANNEL, channel)
            }
        )
    }

    /**
     * @param rit String
     * @param productId ID of Product
     * @param cardType AdsLogConst.AdCardStyle
     */
    fun sendEventShowOverMallAd(
        rit: String,
        productId: String,
        cardType: String,
        sessionInfo: String,
        sizePercent: String
    ) {
        AppLogAnalytics.send(
            AdsLogConst.Event.SHOW_OVER,
            JSONObject().apply {
                put(AdsLogConst.TAG, AdsLogConst.Tag.TOKO_MALL_AD)
                put(AdsLogConst.RIT, rit)
                put(AdsLogConst.Param.SYSTEM_START_TIMESTAMP, System.currentTimeMillis())
                put(AdsLogConst.Param.PRODUCT_ID, productId)
                put(AdsLogConst.Param.MALL_CARD_TYPE, cardType)
                // TODO: Need to confirm, whether the sessionInfo contains the structured JSON string.
                put(AdsLogConst.Param.SESSION_INFO, sessionInfo)
                put(AdsLogConst.Param.SIZE_PERCENT, sizePercent)
            }
        )
    }

    /**
     * @param rit String
     * @param productId ID of Product
     * @param enterFrom AdsLogConst.EnterFrom
     * @param channel AdsLogConst.Channel
     * @param cardType AdsLogConst.AdCardStyle
     */
    fun sendEventShowOverMallAdResult(
        rit: String,
        productId: String,
        enterFrom: String,
        channel: String,
        cardType: String,
        sizePercent: String
    ) {
        AppLogAnalytics.send(
            AdsLogConst.Event.SHOW_OVER,
            JSONObject().apply {
                put(AdsLogConst.TAG, AdsLogConst.Tag.TOKO_RESULT_MALL_AD)
                put(AdsLogConst.RIT, rit)
                put(AdsLogConst.Param.SYSTEM_START_TIMESTAMP, System.currentTimeMillis())
                put(AdsLogConst.Param.PRODUCT_ID, productId)
                put(AdsLogConst.Param.MALL_CARD_TYPE, cardType)
                put(AdsLogConst.Param.ENTER_FROM, enterFrom)
                put(AdsLogConst.Param.CHANNEL, channel)
                put(AdsLogConst.Param.SIZE_PERCENT, sizePercent)
            }
        )
    }

    /**
     * @param rit
     * @param productId ID of Product
     * @param refer AdsLogConst.Refer
     * @param cardType AdsLogConst.AdCardStyle
     */
    fun sendEventRealtimeClickMallAd(
        rit: String,
        productId: String,
        refer: String,
        cardType: String,
        sessionInfo: String,
        timeIntervalClick: String
    ) {
        AppLogAnalytics.send(
            AdsLogConst.Event.REALTIME_CLICK,
            JSONObject().apply {
                put(AdsLogConst.RIT, rit)
                put(AdsLogConst.TAG, AdsLogConst.Tag.TOKO_MALL_AD)
                put(AdsLogConst.REFER, refer)
                put(AdsLogConst.Param.SYSTEM_START_TIMESTAMP, System.currentTimeMillis())
                put(AdsLogConst.Param.PRODUCT_ID, productId)
                put(AdsLogConst.Param.MALL_CARD_TYPE, cardType)
                put(AdsLogConst.Param.SESSION_INFO, sessionInfo)
                put(AdsLogConst.Param.TIME_INTERVAL_BETWEEN_CURRENT_N_CLICK, timeIntervalClick)
            }
        )
    }

    /**
     * @param rit
     * @param productId ID of Product
     * @param refer AdsLogConst.Refer
     * @param cardType AdsLogConst.AdCardStyle
     * @param channel AdsLogConst.Channel
     * @param enterFrom AdsLogConst.EnterFrom
     */
    fun sendEventRealtimeClickMallAdResult(
        rit: String,
        productId: String,
        refer: String,
        cardType: String,
        channel: String,
        enterFrom: String
    ) {
        AppLogAnalytics.send(
            AdsLogConst.Event.REALTIME_CLICK,
            JSONObject().apply {
                put(AdsLogConst.RIT, rit)
                put(AdsLogConst.TAG, AdsLogConst.Tag.TOKO_RESULT_MALL_AD)
                put(AdsLogConst.REFER, refer)
                put(AdsLogConst.Param.SYSTEM_START_TIMESTAMP, System.currentTimeMillis())
                put(AdsLogConst.Param.PRODUCT_ID, productId)
                put(AdsLogConst.Param.MALL_CARD_TYPE, cardType)
                put(AdsLogConst.Param.CHANNEL, channel)
                put(AdsLogConst.Param.ENTER_FROM, enterFrom)
            }
        )
    }
}
