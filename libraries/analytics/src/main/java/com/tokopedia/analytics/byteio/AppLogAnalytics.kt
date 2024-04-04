package com.tokopedia.analytics.byteio

import android.app.Activity
import android.app.Application
import android.graphics.pdf.PdfDocument.Page
import com.bytedance.applog.AppLog
import com.bytedance.applog.util.EventsSenderUtils
import com.tokopedia.analytics.byteio.AppLogParam.ACTIVITY_HASH_CODE
import com.tokopedia.analytics.byteio.AppLogParam.ENTER_FROM
import com.tokopedia.analytics.byteio.AppLogParam.ENTER_FROM_INFO
import com.tokopedia.analytics.byteio.AppLogParam.ENTRANCE_FORM
import com.tokopedia.analytics.byteio.AppLogParam.ENTRANCE_INFO
import com.tokopedia.analytics.byteio.AppLogParam.IS_AD
import com.tokopedia.analytics.byteio.AppLogParam.IS_SHADOW
import com.tokopedia.analytics.byteio.AppLogParam.PAGE_NAME
import com.tokopedia.analytics.byteio.AppLogParam.PREVIOUS_PAGE
import com.tokopedia.analytics.byteio.AppLogParam.REQUEST_ID
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
            it.put(SEARCH_ENTRANCE, getLastData(SEARCH_ENTRANCE))
            it.put(SEARCH_ID, getLastData(SEARCH_ID))
            it.put(SEARCH_RESULT_ID, getLastData(SEARCH_RESULT_ID))
            it.put(LIST_ITEM_ID, getLastData(LIST_ITEM_ID))
        }
    }

    internal fun JSONObject.addEntranceInfo() {
        put(ENTRANCE_INFO, generateEntranceInfoJson().toString())
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
        put(ENTER_FROM, getLastData(ENTER_FROM))
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
        put(SOURCE_PAGE_TYPE, getLastData(SOURCE_PAGE_TYPE))
    }

    internal fun JSONObject.addSourceModulePdp() {
        val sourceModule = if (currentActivityName == "AtcVariantActivity") {
            getLastDataExactStep(SOURCE_MODULE, 2)
        } else {
            getLastDataExactStep(SOURCE_MODULE)
        }
        put(SOURCE_MODULE, sourceModule)
    }

    internal fun JSONObject.addEnterMethodPdp() {
        val sourceModule = if (currentActivityName == "AtcVariantActivity") {
            getLastDataExactStep(ENTER_METHOD, 2)
        } else {
            getLastDataExactStep(ENTER_METHOD)
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
        val enterMethod = if(pageDataList.size > 1)
            getLastDataBeforeCurrent(ENTER_METHOD)
        else getLastData(ENTER_METHOD)
        put(ENTER_METHOD, enterMethod)
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
            Timber.d("(%s) sending event ($event), value: ${params.toString(2)}", TAG)
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
        Timber.d("Push _pageDataList: ${_pageDataList.printForLog()}")
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

    fun getLastDataExactStep(key: String, step: Int = 1): Any? {
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
                putPageData(ENTER_FROM, appLogInterface.getPageName())
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
        requestId: String? = null
    ) {
        entranceForm?.let {
            putPageData(ENTRANCE_FORM, entranceForm)
        }
        enterMethod?.let {
            putPageData(ENTER_METHOD, enterMethod)
        }
        sourceModule?.let {
            putPageData(SOURCE_MODULE, sourceModule)
        }
        isAd?.let {
            putPageData(IS_AD, isAd)
        }
        trackId?.let {
            putPageData(TRACK_ID, trackId)
        }
        sourcePageType?.let {
            putPageData(SOURCE_PAGE_TYPE, sourcePageType)
        }
        requestId?.let {
            putPageData(REQUEST_ID, requestId)
        }
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
            it.put("funnel", if (buyType == AtcBuyType.ATC) "regular" else "occ")
            it.put("buy_type", buyType.code.toString())
            it.put("os_name", "android")
            it.put("cart_item", JSONArray().also { item ->
                cartIds.forEach { id ->
                    item.put(JSONObject().also { j ->
                        j.put("cart_id", id)
                        if (buyType == AtcBuyType.ATC) {
                            j.put(ENTRANCE_INFO, generateEntranceInfoCartJson())
                        } else {
                            j.put(ENTRANCE_INFO, generateEntranceInfoJson())
                        }

                    })
                }
            })
        }.toString()
    }

    fun getSourcePreviousPage(): String? {
        getLastDataBeforeCurrent(PAGE_NAME)?.let {
            return it.toString()
        }
        return null
    }
}
