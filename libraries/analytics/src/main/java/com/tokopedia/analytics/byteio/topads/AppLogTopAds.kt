package com.tokopedia.analytics.byteio.topads

import android.app.Activity
import android.content.Context
import android.os.SystemClock
import com.bytedance.common.utility.NetworkUtils
import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogParam.PAGE_NAME
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.search.AppLogSearch
import com.tokopedia.analytics.byteio.topads.models.AdsLogRealtimeClickModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowOverModel
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.orZero
import org.json.JSONObject

/**
 * @see https://bytedance.sg.larkoffice.com/docx/IJW6dwOlPoEMEnxySgvlRBlCgNh
 */
object AppLogTopAds {

    private const val EXTERNAL_SEARCH = "external search"

    //key = PAGE_NAME, Pair(activityName, activityHashCode, fragmentName)
    private val _adsPageDataList = ArrayList<HashMap<String, Triple<String, Int, Any>>>()

    var isSearchPageNonEmptyState = true

    @JvmField
    var currentActivityName: String = ""

    @JvmField
    var currentPageName: String = ""

    /**
     * To update current page data
     */
    @JvmStatic
    fun putAdsPageData(activity: Activity, key: String, value: Any) {
        val adsMap = hashMapOf(key to Triple(activity.javaClass.simpleName, activity.hashCode(), value))
        _adsPageDataList.add(adsMap)
    }

    @JvmStatic
    fun updateAdsFragmentPageData(activity: Activity?, key: String, value: Any) {
        if (activity == null) return

        val currentActivityIdx = getCurrentAdsActivityIdx(activity)
        if (currentActivityIdx >= 0) {
            val adsPageLastData = _adsPageDataList.getOrNull(currentActivityIdx)
            adsPageLastData?.let { updatedData ->
                val currentActivity = updatedData[key]?.first.orEmpty()
                val currentActHashCode = updatedData[key]?.second.orZero()
                updatedData[key] = Triple(currentActivity, currentActHashCode, value)
                _adsPageDataList[currentActivityIdx] = updatedData
            }
        }
    }

    fun removeLastAdsPageData(activity: Activity) {
        val currentIndex = _adsPageDataList.indexOfFirst { map ->
            map.values.any { it.second == activity.hashCode() && it.third != PageName.FIND_PAGE }
        }
        if (currentIndex >= 0) _adsPageDataList.removeAt(currentIndex)
    }

    private fun getCurrentAdsActivityIdx(activity: Activity): Int {
        val currentActivityIdx = _adsPageDataList.indexOfFirst { map ->
            map.values.any { it.second == activity.hashCode() }
        }
        return currentActivityIdx
    }

    private fun getLastAdsDataBeforeCurrent(key: String): Any? {
        if (_adsPageDataList.isEmpty()) return null
        var idx = _adsPageDataList.lastIndex - 1
        while (idx >= 0) {
            val map = _adsPageDataList[idx]
            map[key]?.let {
                return it.third
            }
            idx--
        }
        return null
    }

    private fun getTwoLastAdsDataBeforeCurrent(key: String): Any? {
        if (_adsPageDataList.isEmpty()) return null
        val adsPageDataNonEmpty = _adsPageDataList.filter { map -> map.values.any { (it.third as? String)?.isNotBlank() == true } }
        var idx = adsPageDataNonEmpty.lastIndex - 2
        while (idx >= 0) {
            val map = adsPageDataNonEmpty[idx]
            map[key]?.let {
                return it.third
            }
            idx--
        }
        return null
    }

    private fun getLastAdsPageNameBeforeCurrent(key: String): Any? {
        if (_adsPageDataList.isEmpty()) return null
        val adsPageDataNonEmpty = _adsPageDataList.filter { map -> map.values.any { (it.third as? String)?.isNotBlank() == true } }
        var idx = adsPageDataNonEmpty.lastIndex - 1
        while (idx >= 0) {
            val map = adsPageDataNonEmpty[idx]
            map[key]?.let {
                return it.third
            }
            idx--
        }
        return null
    }


    /**
     * @param context Context
     * @param currentPageName String
     * @param adsLogShowOverModel AdsLogShowOverModel
     */
    fun sendEventShowOver(
        context: Context,
        adsLogShowOverModel: AdsLogShowOverModel
    ) {
        val tagValue = getTagValue(currentPageName)
        AppLogAnalytics.send(
            AdsLogConst.Event.SHOW_OVER,
            JSONObject().apply {
                put(
                    AdsLogConst.Param.AD_EXTRA_DATA,
                    JSONObject().apply {
                        if(tagValue == AdsLogConst.Tag.TOKO_RESULT_MALL_AD) {
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

                putTag(tagValue)
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
        val tagValue = getTagValue(currentPageName)
        AppLogAnalytics.send(
            AdsLogConst.Event.SHOW,
            JSONObject().apply {
                put(
                    AdsLogConst.Param.AD_EXTRA_DATA,
                    JSONObject().apply {
                        if(tagValue == AdsLogConst.Tag.TOKO_RESULT_MALL_AD) {
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

                putTag(tagValue)
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
        val tagValue = getTagValue(currentPageName)

        AppLogAnalytics.send(
            AdsLogConst.Event.REALTIME_CLICK,
            JSONObject().apply {
                put(
                    AdsLogConst.Param.AD_EXTRA_DATA,
                    JSONObject().apply {
                        if(tagValue == AdsLogConst.Tag.TOKO_RESULT_MALL_AD) {
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

                putTag(tagValue)
            }
        )
    }

    fun getChannelNameParam(): String {
        val prevPageName = getLastAdsPageNameBeforeCurrent(PAGE_NAME)?.toString().orEmpty()
        val mapChannelName = mapPrevPageNameToChannelName(prevPageName)
        //Find -> SRP = External Search
        //Find -> SRP -> SRP = Find Search
        //Find -> SRP -> SRP -> SRP = Product Search

        val prevTwoLastPageName = getTwoLastAdsDataBeforeCurrent(PAGE_NAME)?.toString().orEmpty()

        if (prevTwoLastPageName == PageName.FIND_PAGE) return AdsLogConst.Channel.FIND_SEARCH

        return if (mapChannelName == AdsLogConst.Channel.FIND_SEARCH) EXTERNAL_SEARCH else mapChannelName
    }

    private fun getSystemBootTime(): String = (System.currentTimeMillis() - SystemClock.elapsedRealtime()).toString()

    private fun getEnterFrom(): String {
        val prevPageName = getLastAdsDataBeforeCurrent(PAGE_NAME)?.toString().orEmpty()
        return if (prevPageName == PageName.HOME) AdsLogConst.EnterFrom.MALL else AdsLogConst.EnterFrom.OTHER
    }

    private fun getChannel(): String {
        //Find -> SRP = Find Search
        //Find -> SRP -> SRP -> SRP = Product Search
        val prevLastPageName = getLastAdsDataBeforeCurrent(PAGE_NAME)?.toString().orEmpty()

        //Find -> SRP -> SRP = Find Search
        val prevTwoLastPageName = getTwoLastAdsDataBeforeCurrent(PAGE_NAME)?.toString().orEmpty()

        if (prevTwoLastPageName == PageName.FIND_PAGE) return AdsLogConst.Channel.FIND_SEARCH

        return mapPrevPageNameToChannelName(prevLastPageName)
    }

    private fun mapPrevPageNameToChannelName(prevPageName: String): String {
        return when(prevPageName) {
            AppLogSearch.ParamValue.GOODS_SEARCH, PageName.SEARCH_RESULT,
            PageName.HOME, PageName.OFFICIAL_STORE -> AdsLogConst.Channel.PRODUCT_SEARCH
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

    private fun JSONObject.putTag(tagValue: String) {
        put(AdsLogConst.TAG, tagValue)
    }

    private fun getTagValue(currentPageName: Any?): String {
        return if (isSearchPage(currentPageName)) AdsLogConst.Tag.TOKO_RESULT_MALL_AD else AdsLogConst.Tag.TOKO_MALL_AD
    }

    private fun isSearchPage(currentPageName: Any?): Boolean {
        val isPrevPageNotFindPage = getLastAdsDataBeforeCurrent(PAGE_NAME)?.toString().orEmpty() != PageName.FIND_PAGE
        return currentPageName in listOf(PageName.SEARCH_RESULT, AppLogSearch.ParamValue.GOODS_SEARCH)
            && isSearchPageNonEmptyState && isPrevPageNotFindPage
    }

    private fun JSONObject.putNetworkType(context: Context) {
        val networkType = NetworkUtils.getNetworkType(context)
        put(AdsLogConst.Param.NT, networkType.value)
    }
}
