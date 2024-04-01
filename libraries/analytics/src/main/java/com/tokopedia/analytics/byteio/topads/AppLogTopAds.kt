package com.tokopedia.analytics.byteio.topads

import android.content.Context
import com.bytedance.common.utility.NetworkUtils
import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.FragmentName
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.topads.models.AdsLogRealtimeClickModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowOverModel
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
        currentPageName: String,
        adsLogShowOverModel: AdsLogShowOverModel
    ) {
        AppLogAnalytics.send(
            AdsLogConst.Event.SHOW,
            JSONObject().apply {
                put(AdsLogConst.PARAMS, {
                    put(
                        AdsLogConst.Param.AD_EXTRA_DATA,
                        JSONObject().apply {
                            putChannelName(currentPageName)
                            putEnterFrom()
                            put(AdsLogConst.Param.MALL_CARD_TYPE, AdsLogConst.AdCardStyle.PRODUCT_CARD)
                            put(AdsLogConst.Param.PRODUCT_ID, adsLogShowOverModel.adExtraData.productId)
                            // todo need to confirm
                            put(AdsLogConst.Param.SIZE_PERCENT, adsLogShowOverModel.adExtraData.sizePercent)
                            put(AdsLogConst.RIT, adsLogShowOverModel.rit)
                        }
                    )

                    put(AdsLogConst.Param.CATEGORY, AdsLogConst.EVENT_V3)
                    put(AdsLogConst.Param.IS_AD_EVENT, "1")
                    putNetworkType(context)
                    put(AdsLogConst.Param.VALUE, adsLogShowOverModel.adsValue)
                    put(AdsLogConst.Param.LOG_EXTRA, adsLogShowOverModel.logExtra)
                    put(AdsLogConst.Param.GROUP_ID, "0")

                    // todo need to confirm
                    put(AdsLogConst.Param.SIZE_PERCENT, adsLogShowOverModel.adExtraData.sizePercent)

                    put(AdsLogConst.Param.SYSTEM_START_TIMESTAMP, adsLogShowOverModel.systemTimeStartClick)

                    putTag(currentPageName)
                    put(AdsLogConst.RIT, adsLogShowOverModel.rit)
                })
            }
        )
    }

    /**
     * @param context Context
     * @param currentPageName String
     * @param adsLogShowModel AdsLogShowModel
     */
    fun sendEventShow(
        context: Context,
        currentPageName: String,
        adsLogShowModel: AdsLogShowModel
    ) {
        AppLogAnalytics.send(
            AdsLogConst.Event.SHOW_OVER,
            JSONObject().apply {
                put(AdsLogConst.PARAMS, {
                    put(
                        AdsLogConst.Param.AD_EXTRA_DATA,
                        JSONObject().apply {
                            putChannelName(currentPageName)
                            putEnterFrom()
                            put(AdsLogConst.Param.MALL_CARD_TYPE, AdsLogConst.AdCardStyle.PRODUCT_CARD)
                            put(AdsLogConst.Param.PRODUCT_ID, adsLogShowModel.adExtraData.productId)
                        }
                    )

                    put(AdsLogConst.Param.CATEGORY, AdsLogConst.EVENT_V3)
                    put(AdsLogConst.Param.IS_AD_EVENT, "1")
                    putNetworkType(context)
                    put(AdsLogConst.Param.VALUE, adsLogShowModel.adsValue)
                    put(AdsLogConst.Param.LOG_EXTRA, adsLogShowModel.logExtra)
                    put(AdsLogConst.Param.GROUP_ID, "0")
                    put(AdsLogConst.Param.SYSTEM_START_TIMESTAMP, adsLogShowModel.systemTimeStartClick)

                    putTag(currentPageName)
                    put(AdsLogConst.RIT, adsLogShowModel.rit)
                })
            }
        )
    }

    /**
     * @param context Context
     * @param currentPageName String
     * @param adsLogRealtimeClickModel AdsLogRealtimeClickModel
     */
    fun sendEventRealtimeClick(
        context: Context,
        currentPageName: String,
        adsLogRealtimeClickModel: AdsLogRealtimeClickModel
    ) {
        AppLogAnalytics.send(
            AdsLogConst.Event.REALTIME_CLICK,
            JSONObject().apply {
                put(AdsLogConst.PARAMS, {
                    put(
                        AdsLogConst.Param.AD_EXTRA_DATA,
                        JSONObject().apply {
                            putChannelName(currentPageName)
                            putEnterFrom()
                            put(AdsLogConst.Param.MALL_CARD_TYPE, AdsLogConst.AdCardStyle.PRODUCT_CARD)
                            put(AdsLogConst.Param.PRODUCT_ID, adsLogRealtimeClickModel.adExtraData.productId)
                        }
                    )

                    put(AdsLogConst.Param.CATEGORY, AdsLogConst.EVENT_V3)
                    put(AdsLogConst.Param.IS_AD_EVENT, "1")
                    putNetworkType(context)
                    put(AdsLogConst.Param.VALUE, adsLogRealtimeClickModel.adsValue)
                    put(AdsLogConst.Param.LOG_EXTRA, adsLogRealtimeClickModel.logExtra)
                    put(AdsLogConst.Param.GROUP_ID, "0")
                    put(AdsLogConst.REFER, adsLogRealtimeClickModel.refer)
                    put(AdsLogConst.Param.SYSTEM_START_TIMESTAMP, adsLogRealtimeClickModel.systemTimeStartClick)

                    putTag(currentPageName)
                    put(AdsLogConst.RIT, adsLogRealtimeClickModel.rit)
                })
            }
        )
    }

    private fun JSONObject.putEnterFrom() {
        val twoLastFragmentName = AppLogAnalytics.getTwoLastPage()
        val enterFrom = if (twoLastFragmentName == FragmentName.HOME_FRAGMENT) AdsLogConst.EnterFrom.MALL else AdsLogConst.EnterFrom.OTHER
        put(AdsLogConst.Param.ENTER_FROM, enterFrom)
    }

    // todo need to confirm for this value
    private fun JSONObject.putChannelName(pageName: String) {
        val pageNameSearch = "$pageName search"
        put(AdsLogConst.Param.CHANNEL, pageNameSearch)
    }

    private fun JSONObject.putTag(currentPageName: String) {
        val tagName = if (currentPageName == PageName.SEARCH_RESULT) AdsLogConst.Tag.TOKO_RESULT_MALL_AD else AdsLogConst.Tag.TOKO_MALL_AD
        put(AdsLogConst.TAG, tagName)
    }

    private fun JSONObject.putNetworkType(context: Context) {
        val networkType = NetworkUtils.getNetworkType(context)
        put(AdsLogConst.Param.NT, networkType)
    }
}
