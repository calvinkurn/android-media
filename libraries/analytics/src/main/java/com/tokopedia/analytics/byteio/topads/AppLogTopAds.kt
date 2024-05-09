package com.tokopedia.analytics.byteio.topads

import android.content.Context
import android.os.SystemClock
import com.bytedance.common.utility.NetworkUtils
import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.analytics.byteio.AppLogParam.PAGE_NAME
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.search.AppLogSearch
import com.tokopedia.analytics.byteio.topads.models.AdsLogRealtimeClickModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowOverModel
import com.tokopedia.config.GlobalConfig
import org.json.JSONObject

/**
 * @see https://bytedance.sg.larkoffice.com/docx/IJW6dwOlPoEMEnxySgvlRBlCgNh
 */
object AppLogTopAds {

    /**
     * @param context Context
     * @param currentPageName String
     * @param adsLogShowOverModel AdsLogShowOverModel
     */
    fun sendEventShowOver(
        context: Context,
        adsLogShowOverModel: AdsLogShowOverModel
    ) {
        AppLogAnalytics.send(
            AdsLogConst.Event.SHOW_OVER,
            JSONObject().apply {
                put(
                    AdsLogConst.Param.AD_EXTRA_DATA,
                    JSONObject().apply {
                        if(isSearchPage(getPageName())) {
                            putChannelName(getChannel())
                            putEnterFrom(getEnterFrom())
                        }
                        put(AdsLogConst.Param.MALL_CARD_TYPE, AdsLogConst.AdCardStyle.PRODUCT_CARD)
                        put(AdsLogConst.Param.PRODUCT_ID, adsLogShowOverModel.adExtraData.productId)
                        put(AdsLogConst.Param.SIZE_PERCENT, adsLogShowOverModel.adExtraData.sizePercent)
                        putProductName(adsLogShowOverModel.adExtraData.productName)
                    }
                )

                put(AdsLogConst.Param.CATEGORY, AdsLogConst.EVENT_V3)
                put(AdsLogConst.Param.IS_AD_EVENT, "1")
                putNetworkType(context)
                put(AdsLogConst.Param.VALUE, adsLogShowOverModel.adsValue)
                put(
                    AdsLogConst.Param.LOG_EXTRA,
                    adsLogShowOverModel.logExtra
                )
                put(AdsLogConst.Param.GROUP_ID, "0")

                put(AdsLogConst.Param.SYSTEM_START_TIMESTAMP, getSystemBootTime())

                putTag(getPageName())
            }
        )
    }

    /**
     * @param context Context
     * @param adsLogShowModel AdsLogShowModel
     */
    fun sendEventShow(
        context: Context,
        adsLogShowModel: AdsLogShowModel
    ) {
        val pageName = getPageName()
        AppLogAnalytics.send(
            AdsLogConst.Event.SHOW,
            JSONObject().apply {
                put(
                    AdsLogConst.Param.AD_EXTRA_DATA,
                    JSONObject().apply {
                        if(isSearchPage(pageName)) {
                            putChannelName(getChannel())
                            putEnterFrom(getEnterFrom())
                        }
                        put(AdsLogConst.Param.MALL_CARD_TYPE, AdsLogConst.AdCardStyle.PRODUCT_CARD)
                        put(AdsLogConst.Param.PRODUCT_ID, adsLogShowModel.adExtraData.productId)
                        putProductName(adsLogShowModel.adExtraData.productName)
                    }
                )

                put(AdsLogConst.Param.CATEGORY, AdsLogConst.EVENT_V3)
                put(AdsLogConst.Param.IS_AD_EVENT, "1")
                putNetworkType(context)
                put(AdsLogConst.Param.VALUE, adsLogShowModel.adsValue)
                put(
                    AdsLogConst.Param.LOG_EXTRA,
                    adsLogShowModel.logExtra
                )
                put(AdsLogConst.Param.GROUP_ID, "0")
                put(AdsLogConst.Param.SYSTEM_START_TIMESTAMP, getSystemBootTime())

                putTag(pageName)
            }
        )
    }

    /**
     * @param context Context
     * @param adsLogRealtimeClickModel AdsLogRealtimeClickModel
     */
    fun sendEventRealtimeClick(
        context: Context,
        adsLogRealtimeClickModel: AdsLogRealtimeClickModel
    ) {
        val pageName = getPageName()
        AppLogAnalytics.send(
            AdsLogConst.Event.REALTIME_CLICK,
            JSONObject().apply {
                put(
                    AdsLogConst.Param.AD_EXTRA_DATA,
                    JSONObject().apply {
                        if(isSearchPage(pageName)) {
                            putChannelName(getChannel())
                            putEnterFrom(getEnterFrom())
                        }
                        put(AdsLogConst.Param.MALL_CARD_TYPE, adsLogRealtimeClickModel.adExtraData.mallCardType)
                        put(AdsLogConst.Param.PRODUCT_ID, adsLogRealtimeClickModel.adExtraData.productId)
                        putProductName(adsLogRealtimeClickModel.adExtraData.productName)
                    }
                )

                put(AdsLogConst.Param.CATEGORY, AdsLogConst.EVENT_V3)
                put(AdsLogConst.Param.IS_AD_EVENT, "1")
                putNetworkType(context)
                put(AdsLogConst.Param.VALUE, adsLogRealtimeClickModel.adsValue)
                put(
                    AdsLogConst.Param.LOG_EXTRA,
                    adsLogRealtimeClickModel.logExtra
                )
                put(AdsLogConst.Param.GROUP_ID, "0")
                put(AdsLogConst.REFER, adsLogRealtimeClickModel.refer)
                put(AdsLogConst.Param.SYSTEM_START_TIMESTAMP, getSystemBootTime())

                putTag(pageName)
            }
        )
    }

    private fun getPageName() = AppLogAnalytics.currentPageName

    private fun getSystemBootTime(): String = (System.currentTimeMillis() - SystemClock.elapsedRealtime()).toString()

    private fun getEnterFrom(): String {
        val prevPageName = AppLogAnalytics.getLastAdsDataBeforeCurrent(PAGE_NAME)?.toString().orEmpty()
        return if (prevPageName == PageName.HOME) AdsLogConst.EnterFrom.MALL else AdsLogConst.EnterFrom.OTHER
    }

    private fun getChannel(): String {
        val prevPageName = AppLogAnalytics.getLastAdsDataBeforeCurrent(PAGE_NAME)?.toString().orEmpty()
        return when(prevPageName) {
            AppLogSearch.ParamValue.GOODS_SEARCH, PageName.SEARCH_RESULT,
            PageName.HOME, PageName.OFFICIAL_STORE ->
                AdsLogConst.Channel.PRODUCT_SEARCH
            PageName.PDP -> AdsLogConst.Channel.PDP_SEARCH
            PageName.SHOP -> AdsLogConst.Channel.STORE_SEARCH
            PageName.FIND_PAGE -> AdsLogConst.Channel.FIND_SEARCH
            PageName.DISCOVERY -> AdsLogConst.Channel.DISCOVERY_SEARCH
            else -> ""
        }
    }

    private fun JSONObject.putEnterFrom(enterFrom: String) {
        put(AdsLogConst.Param.ENTER_FROM, enterFrom)
    }

    private fun JSONObject.putChannelName(channelName: String) {
        if (channelName.isNotBlank()) {
            put(AdsLogConst.Param.CHANNEL, channelName)
        }
    }

    private fun JSONObject.putProductName(productName: String) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            put(AdsLogConst.Param.PRODUCT_NAME, productName)
        }
    }

    private fun JSONObject.putTag(currentPageName: Any?) {
        val tagName = if (isSearchPage(currentPageName)) AdsLogConst.Tag.TOKO_RESULT_MALL_AD else AdsLogConst.Tag.TOKO_MALL_AD
        put(AdsLogConst.TAG, tagName)
    }

    private fun isSearchPage(currentPageName: Any?): Boolean {
        return currentPageName in listOf(PageName.SEARCH_RESULT, AppLogSearch.ParamValue.GOODS_SEARCH)
    }

    private fun JSONObject.putNetworkType(context: Context) {
        val networkType = NetworkUtils.getNetworkType(context)
        put(AdsLogConst.Param.NT, networkType.value)
    }
}
