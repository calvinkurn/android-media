package com.tokopedia.analytics.byteio

import android.app.Activity
import android.app.Application
import com.bytedance.applog.AppLog
import com.bytedance.applog.util.EventsSenderUtils
import com.tokopedia.analytics.byteio.AppLogParam.ACTIVITY_HASH_CODE
import com.tokopedia.analytics.byteio.AppLogParam.AUTHOR_ID
import com.tokopedia.analytics.byteio.AppLogParam.ENTER_FROM
import com.tokopedia.analytics.byteio.AppLogParam.ENTER_FROM_INFO
import com.tokopedia.analytics.byteio.AppLogParam.ENTRANCE_FORM
import com.tokopedia.analytics.byteio.AppLogParam.ENTRANCE_INFO
import com.tokopedia.analytics.byteio.AppLogParam.FIRST_SOURCE_PAGE
import com.tokopedia.analytics.byteio.AppLogParam.FIRST_TRACK_ID
import com.tokopedia.analytics.byteio.AppLogParam.IS_AD
import com.tokopedia.analytics.byteio.AppLogParam.IS_SHADOW
import com.tokopedia.analytics.byteio.AppLogParam.PAGE_NAME
import com.tokopedia.analytics.byteio.AppLogParam.PARENT_PRODUCT_ID
import com.tokopedia.analytics.byteio.AppLogParam.PARENT_REQUEST_ID
import com.tokopedia.analytics.byteio.AppLogParam.PARENT_TRACK_ID
import com.tokopedia.analytics.byteio.AppLogParam.PREVIOUS_PAGE
import com.tokopedia.analytics.byteio.AppLogParam.REQUEST_ID
import com.tokopedia.analytics.byteio.AppLogParam.SOURCE_CONTENT_ID
import com.tokopedia.analytics.byteio.AppLogParam.SOURCE_MODULE
import com.tokopedia.analytics.byteio.AppLogParam.SOURCE_PAGE_TYPE
import com.tokopedia.analytics.byteio.AppLogParam.SOURCE_PREVIOUS_PAGE
import com.tokopedia.analytics.byteio.AppLogParam.TRACK_ID
import com.tokopedia.analytics.byteio.Constants.EVENT_ORIGIN_FEATURE_KEY
import com.tokopedia.analytics.byteio.Constants.EVENT_ORIGIN_FEATURE_VALUE
import com.tokopedia.analytics.byteio.pdp.AtcBuyType
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.ENTER_METHOD
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.LIST_ITEM_ID
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.SEARCH_ENTRANCE
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.SEARCH_ID
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.SEARCH_RESULT_ID
import com.tokopedia.analyticsdebugger.cassava.Cassava
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.lang.ref.WeakReference

object AppLogAnalytics {

    @JvmField
    var currentActivityReference: WeakReference<Activity>? = null

    /**
     * Stores page data in a stack
     */
    private val _pageDataList = ArrayList<HashMap<String, Any>>()

    /**
     * Return immutable list
     */
    val pageDataList: List<Map<String, Any>>
        get() = _pageDataList.toList()

    @JvmField
    var currentActivityName: String = ""

    /**
     * key = activity name
     * value = page name.
     * We store activity name to prevent the newly created activity
     * to override value for current page name
     */
    @JvmField
    var pageNames = mutableListOf<Pair<String, Int?>>()

    @JvmField
    var activityCount: Int = 0

    // TODO check how to make this null again
    @JvmField
    var sourcePageType: SourcePageType? = null

    // TODO check how to make this null again
    @JvmField
    var globalTrackId: String? = null

    // TODO check how to make this null again
    @JvmField
    var globalRequestId: String? = null

    // TODO check how to make this null again
    @JvmField
    var entranceForm: EntranceForm? = null

    private val lock = Any()

    private var remoteConfig: RemoteConfig? = null

    internal fun addPageName(activity: Activity) {
        val actName = activity.javaClass.simpleName
        if (activity is IAppLogActivity) {
            synchronized(lock) {
                pageNames.add(actName to activity.hashCode())
            }
        } else {
            synchronized(lock) {
                pageNames.add(actName to null)
            }
        }
    }

    internal fun removePageName(activity: Activity) {
        synchronized(lock) {
            val pageNameToRemove =
                pageNames.indexOfLast { it.first == activity.javaClass.simpleName }
            if (pageNameToRemove != -1) {
                pageNames.removeAt(pageNameToRemove)
            }
        }
    }

    internal val Boolean.intValue
        get() = if (this) 1 else 0

    internal fun JSONObject.addPage() {
        put(PREVIOUS_PAGE, getLastDataBeforeCurrent(PAGE_NAME)?.toString().orEmpty())
        put(PAGE_NAME, getLastData(PAGE_NAME))
    }

    internal fun JSONObject.addEntranceForm() {
        put(ENTRANCE_FORM, getLastData(ENTRANCE_FORM))
    }

    private fun generateEntranceInfoJson(): JSONObject {
        return JSONObject().also {
            it.addEnterFromInfo()
            it.addEntranceForm()
            it.addSourcePageType()
            it.addTrackId()
            it.put(IS_AD, getLastData(IS_AD))
            it.addRequestId()
            it.addSourceModulePdp()
//            it.addEnterMethod()
            it.addEnterMethodPdp()
            it.addSourceContentId()
            it.put(SEARCH_ENTRANCE, getLastData(SEARCH_ENTRANCE))
            it.put(SEARCH_ID, getLastData(SEARCH_ID))
            it.put(SEARCH_RESULT_ID, getLastData(SEARCH_RESULT_ID))
            it.put(LIST_ITEM_ID, getLastData(LIST_ITEM_ID))
            it.put(FIRST_TRACK_ID, AppLogFirstTrackId.firstTrackId)
            it.put(FIRST_SOURCE_PAGE, AppLogFirstTrackId.firstSourcePage)
            it.put(PARENT_PRODUCT_ID, getPreviousDataFrom(PageName.PDP, PARENT_PRODUCT_ID))
            it.put(PARENT_TRACK_ID, getPreviousDataFrom(PageName.PDP, PARENT_TRACK_ID))
            it.put(PARENT_REQUEST_ID, getPreviousDataFrom(PageName.PDP, PARENT_REQUEST_ID))
        }
    }

    internal fun JSONObject.addEntranceInfo() {
        put(ENTRANCE_INFO, generateEntranceInfoJson().toString())
    }

    internal fun JSONObject.addAuthorId() {
        put(AUTHOR_ID, getLastData(AUTHOR_ID))
    }

    private fun generateEntranceInfoCartJson(): JSONObject {
        return JSONObject().also {
            it.put(ENTER_FROM_INFO, getEnterFromBeforeCart())
            it.put(ENTRANCE_FORM, PageName.CART)
            it.put(SOURCE_PAGE_TYPE, PageName.CART)
        }
    }

    internal fun JSONObject.addEntranceInfoCart() {
        val data = generateEntranceInfoCartJson().toString()
        put(ENTRANCE_INFO, data)
    }

    internal fun JSONObject.addEnterFrom() {
        put(ENTER_FROM, getEnterFrom())
    }

    fun getEnterFrom(): String? {
        return getLastData(ENTER_FROM)?.toString()
    }

    internal fun JSONObject.addEnterFromInfo() {
        // todo: explain
        put(ENTER_FROM_INFO, getLastDataBeforeCurrent(ENTER_FROM))
    }

    internal fun JSONObject.addPreviousEnterFrom() {
        put(ENTER_FROM, getLastDataBeforeCurrent(ENTER_FROM))
    }

    internal fun JSONObject.addSourcePreviousPage() {
        put(SOURCE_PREVIOUS_PAGE, getLastData(SOURCE_PREVIOUS_PAGE))
    }

    internal fun JSONObject.addSourcePageType() {
        put(SOURCE_PAGE_TYPE, getSourcePageType())
    }

    fun getSourcePageType(): String? {
        return getLastData(SOURCE_PAGE_TYPE)?.toString()
    }

    internal fun JSONObject.addSourceModulePdp() {
        val sourceModule = if (currentActivityName == "AtcVariantActivity") {
            getDataLast(SOURCE_MODULE, 2)
        } else {
            getDataLast(SOURCE_MODULE)
        }
        put(SOURCE_MODULE, sourceModule)
    }

    internal fun JSONObject.addEnterMethodPdp() {
        val sourceModule = if (currentActivityName == "AtcVariantActivity") {
            getDataLast(ENTER_METHOD, 2)
        } else {
            getDataLast(ENTER_METHOD)
        }
        put(ENTER_METHOD, sourceModule)
    }

    internal fun JSONObject.addRequestId() {
        put(REQUEST_ID, getLastData(REQUEST_ID))
    }

    internal fun JSONObject.addTrackId() {
        put(TRACK_ID, getLastData(TRACK_ID))
    }

    internal fun JSONObject.addEnterMethod() {
        val enterMethod = if (pageDataList.size > 1) {
            getLastDataBeforeCurrent(ENTER_METHOD)
        } else {
            getLastData(ENTER_METHOD)
        }
        put(ENTER_METHOD, enterMethod)
    }

    internal fun JSONObject.addSourceContentId() {
        put(SOURCE_CONTENT_ID, getLastData(SOURCE_CONTENT_ID))
    }

    fun lastTwoIsHavingHash(hash: Int): Boolean {
        return pageNames.previousHash() == hash || pageNames.previousHash(2) == hash
    }

    private fun List<Pair<String, Int?>>.previousHash(step: Int = 1): Int {
        return getOrNull(pageNames.lastIndex - step)?.second.orZero()
    }

    fun send(event: String, params: JSONObject) {
        if (remoteConfig?.getBoolean(RemoteConfigKey.ENABLE_BYTEIO_PLATFORM, true) == true) {
            params.put(EVENT_ORIGIN_FEATURE_KEY, EVENT_ORIGIN_FEATURE_VALUE)
            Cassava.save(params, event, "ByteIO")
            AppLog.onEventV3(event, params)
            Timber.d("($TAG) sending event ($event), value: ${params.toString(2)}")
        }
    }

    @JvmStatic
    fun init(application: Application) {
        initAppLog(application.applicationContext)
        remoteConfig = FirebaseRemoteConfigImpl(application.applicationContext)
        if (GlobalConfig.isAllowDebuggingTools()) {
            EventsSenderUtils.setEventsSenderEnable("573733", true, application)
            EventsSenderUtils.setEventVerifyHost("573733", "https://log.byteoversea.net")
        }
        Timber.d(
            """(%s) 
            |AppLog dId: ${AppLog.getDid()} 
            |userUniqueId: ${AppLog.getUserUniqueID()} 
            |userId: ${AppLog.getUserUniqueID()}
            """.trimMargin(),
            TAG
        )
    }

    /**
     * To add page data for every page
     */
    fun pushPageData() {
        val tempHashMap = HashMap<String, Any>()
        _pageDataList.add(tempHashMap)
    }

    fun removeLastPageData() {

    }

    /**
     * To remove last page data
     */
    fun removePageData(appLogInterface: AppLogInterface) {
        val pageDataIndexed = _pageDataList
            .withIndex()
            .find { it.value[ACTIVITY_HASH_CODE] == appLogInterface.hashCode() }
            ?: return

        val index = pageDataIndexed.index
        val pageData = pageDataIndexed.value

        if (pageData[IS_SHADOW] != true) {
            _pageDataList.removeAt(index)

            val shadowPageIndex = index - 1
            // Remove shadow stack
            removeShadowStack(shadowPageIndex)
        }
        Timber.d("Remove _pageDataList: ${_pageDataList.printForLog()}}")
    }

    // remove list of page data by hashcode
    fun removePageData(appLogInterface: AppLogInterface, listOfRemovedKey: List<String>) {
        val pageDataIndex = _pageDataList
            .withIndex()
            .find { it.value[ACTIVITY_HASH_CODE] == appLogInterface.hashCode() }
            ?: return

        listOfRemovedKey.forEach {
            _pageDataList[pageDataIndex.index].remove(it)
        }
    }

    private fun removeShadowStack(currentIndex: Int) {
        var tempCurrentIndex = currentIndex
        while (tempCurrentIndex >= 0 && _pageDataList.getOrNull(tempCurrentIndex)
            ?.get(IS_SHADOW) == true
        ) {
            _pageDataList.removeAt(tempCurrentIndex)
            tempCurrentIndex--
        }
    }

    private fun clearCurrentPageData() {
        _pageDataList.lastOrNull()?.clear()
    }

    /**
     * To update current page data
     */
    fun putPageData(key: String, value: Any) {
        _pageDataList.lastOrNull()?.put(key, value)
        Timber.d("Put _pageDataList: ${_pageDataList.printForLog()}}")
    }

    /**
     * To update the previous page data (current page index - 1)
     * For example: if the current page data is a bottom sheet,
     * then it will be useless to put any data there
     * since the bottom sheet will be dismissed when navigating to the next page.
     * Putting the data in the previous page data will fix the issue.
     */
    fun putPreviousPageData(key: String, value: Any) {
        val secondToLastData = _pageDataList.getOrNull(_pageDataList.lastIndex - 1)
        secondToLastData?.put(key, value)
    }

    fun removePageData(key: String) {
        _pageDataList.lastOrNull()?.remove(key)
    }

    fun putEnterMethod(enterMethod: EnterMethod) {
        putPageData(ENTER_METHOD, enterMethod.str)
    }

    fun getCurrentData(key: String): Any? {
        return _pageDataList.lastOrNull()?.get(key)
    }

    fun getLastData(key: String): Any? {
        _pageDataList.reversed().forEach { hashMap ->
            hashMap[key]?.let {
                return it
            }
        }
        return null
    }

    fun getDataLast(key: String, step: Int = 1): Any? {
        val idx = _pageDataList.lastIndex - step
        val map = _pageDataList.getOrNull(idx)
        return map?.get(key)
    }

    fun getLastDataBeforeCurrent(key: String): Any? {
        if (_pageDataList.isEmpty()) return null
        var idx = _pageDataList.lastIndex - 1
        while (idx >= 0) {
            val map = _pageDataList[idx]
            map[key]?.let {
                return it
            }
            idx--
        }
        return null
    }

    private fun getEnterFromBeforeCart(): Any? {
        if (_pageDataList.isEmpty()) return null
        var idx = _pageDataList.lastIndex
        var start = false
        while (idx >= 0) {
            val map = _pageDataList[idx]
            if (map.containsKey(ENTER_FROM) && start) {
                return map[ENTER_FROM]
            }
            if (map[PAGE_NAME] == PageName.CART) {
                start = true
            }
            idx--
        }
        return null
    }

    fun getLastDataBeforeHash(key: String, hash: Int): Any? {
        if (_pageDataList.isEmpty()) return null
        var idx = _pageDataList.lastIndex
        var startFind = false
        while (idx >= 0) {
            val map = _pageDataList[idx]
            map[key]?.let {
                if (startFind) return it
            }
            if (map.getOrDefault(ACTIVITY_HASH_CODE, null) == hash) {
                startFind = true
            }
            idx--
        }
        return null
    }

    fun getDataBeforeCurrent(key: String): Any? {
        if (_pageDataList.isEmpty()) return null
        val idx = _pageDataList.lastIndex - 1

        return if (idx >= 0) {
            _pageDataList[idx][key]
        } else {
            null
        }
    }

    fun clearAllPageData() {
        _pageDataList.clear()
    }

    fun pushPageData(appLogInterface: AppLogInterface) {
        pushPageData()
        putPageData(ACTIVITY_HASH_CODE, appLogInterface.hashCode())
        putAppLogInterfaceData(appLogInterface)
        Timber.d("Push _pageDataList: \n${_pageDataList.printForLog()}")
    }

    fun updateCurrentPageData(appLogInterface: AppLogInterface) {
        val hashCode = getCurrentData(ACTIVITY_HASH_CODE)
        clearCurrentPageData()
        hashCode?.let { putPageData(ACTIVITY_HASH_CODE, it) }
        putAppLogInterfaceData(appLogInterface)
    }

    private fun putAppLogInterfaceData(appLogInterface: AppLogInterface) {
        if (appLogInterface.getPageName().isNotBlank()) {
            putPageData(PAGE_NAME, appLogInterface.getPageName())
            if (appLogInterface.isEnterFromWhitelisted()) {
                putPageData(ENTER_FROM, appLogInterface.getEnterFrom())
            }
        }
        if (appLogInterface.isShadow()) {
            putPageData(IS_SHADOW, appLogInterface.isShadow())
        }
    }

    fun setGlobalParams(
        entranceForm: String? = null,
        enterMethod: String? = null,
        sourceModule: String? = null,
        isAd: Int? = null,
        trackId: String? = null,
        sourcePageType: String? = null,
        requestId: String? = null,
        parentProductId: String? = null,
        parentTrackId: String? = null,
        parentRequestId: String? = null
    ) {
        entranceForm?.let {
            putPageDataAndFirstTrackId(ENTRANCE_FORM, entranceForm)
        }
        enterMethod?.let {
            putPageDataAndFirstTrackId(ENTER_METHOD, enterMethod)
        }
        sourceModule?.let {
            putPageDataAndFirstTrackId(SOURCE_MODULE, sourceModule)
        }
        isAd?.let {
            putPageDataAndFirstTrackId(IS_AD, isAd)
        }
        trackId?.let {
            putPageDataAndFirstTrackId(TRACK_ID, trackId)
        }
        sourcePageType?.let {
            putPageDataAndFirstTrackId(SOURCE_PAGE_TYPE, sourcePageType)
        }
        requestId?.let {
            putPageDataAndFirstTrackId(REQUEST_ID, requestId)
        }
        parentProductId?.let {
            putPageDataAndFirstTrackId(PARENT_PRODUCT_ID, parentProductId)
        }
        parentTrackId?.let {
            putPageDataAndFirstTrackId(PARENT_TRACK_ID, parentTrackId)
        }
        parentRequestId?.let {
            putPageDataAndFirstTrackId(PARENT_REQUEST_ID, parentRequestId)
        }
    }

    private fun putPageDataAndFirstTrackId(key: String, value: Any) {
        _pageDataList.lastOrNull()?.put(key, value)
        AppLogFirstTrackId.putPageData(key, value)
        Timber.d("Put _pageDataList: ${_pageDataList.printForLog()}}")
    }

    fun removeGlobalParam() {
        listOf(
            ENTRANCE_FORM,
            ENTER_METHOD,
            SOURCE_MODULE,
            IS_AD,
            TRACK_ID,
            SOURCE_PAGE_TYPE,
            REQUEST_ID
        ).forEach {
            _pageDataList.lastOrNull()?.remove(it)
        }
    }

    private fun ArrayList<HashMap<String, Any>>.printForLog(): String {
        return takeLast(5).joinToString("\n")
    }

    fun getEntranceInfo(buyType: AtcBuyType): String {
        return JSONObject().also {
            it.put(ENTRANCE_INFO, generateEntranceInfoJson().toString())
            it.put("buy_type", buyType.code)
            it.put("os_name", "android")
        }.toString()
    }

    fun getEntranceInfoForCheckout(buyType: AtcBuyType, cartIds: List<String>): String {
        return JSONObject().also {
            it.put("funnel", buyType.funnel)
            it.put("buy_type", buyType.code.toString())
            it.put("os_name", "android")
            it.put(
                "cart_item",
                JSONArray().also { item ->
                    cartIds.forEach { id ->
                        item.put(
                            JSONObject().also { j ->
                                j.put("cart_id", id)
                                if (buyType == AtcBuyType.ATC) {
                                    j.put(ENTRANCE_INFO, generateEntranceInfoCartJson())
                                } else { // occ & ocs
                                    j.put(ENTRANCE_INFO, getEntranceInfoJsonForCheckoutInstant())
                                }
                            }
                        )
                    }
                }
            )
        }.toString()
    }

    /**
     * This method should be refactored to the normal getEntranceInfoJson, this is separated
     * to minimize changes and avoid regression during hotfix
     * */
    internal fun getEntranceInfoJsonForCheckoutInstant(): JSONObject {
        return JSONObject().also {
            it.addEnterFromInfo()
            it.addEntranceForm()
            it.addSourcePageType()
            it.addTrackId()
            it.put(IS_AD, getLastData(IS_AD))
            it.addRequestId()
            it.put(SOURCE_MODULE, getPreviousDataFrom(PageName.PDP, SOURCE_MODULE))
            it.put(ENTER_METHOD, getPreviousDataFrom(PageName.PDP, ENTER_METHOD))
            it.put(SEARCH_ENTRANCE, getLastData(SEARCH_ENTRANCE))
            it.put(SEARCH_ID, getLastData(SEARCH_ID))
            it.put(SEARCH_RESULT_ID, getLastData(SEARCH_RESULT_ID))
            it.put(LIST_ITEM_ID, getLastData(LIST_ITEM_ID))
            it.put(FIRST_TRACK_ID, AppLogFirstTrackId.firstTrackId)
            it.put(FIRST_SOURCE_PAGE, AppLogFirstTrackId.firstSourcePage)
            it.put(PARENT_PRODUCT_ID, getPreviousDataFrom(PageName.PDP, PARENT_PRODUCT_ID))
            it.put(PARENT_TRACK_ID, getPreviousDataFrom(PageName.PDP, PARENT_TRACK_ID))
            it.put(PARENT_REQUEST_ID, getPreviousDataFrom(PageName.PDP, PARENT_REQUEST_ID))
        }
    }

    /**
     * Starting from N-1, this method will start searching for a key after the current item is the anchor
     * */
    private fun getPreviousDataFrom(anchor: String, key: String): Any? {
        if (_pageDataList.isEmpty()) return null
        var idx = _pageDataList.lastIndex
        var start = false
        while (idx >= 0) {
            val map = _pageDataList[idx]
            if (map.containsKey(key) && start) {
                return map[key]
            }
            if (map[PAGE_NAME] == anchor) {
                start = true
            }
            idx--
        }
        return null
    }

    fun getSourcePreviousPage(): String? {
        getLastDataBeforeCurrent(PAGE_NAME)?.let {
            return it.toString()
        }
        return null
    }
}
