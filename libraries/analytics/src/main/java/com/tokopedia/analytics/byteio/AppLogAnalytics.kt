package com.tokopedia.analytics.byteio

import android.app.Activity
import android.app.Application
import com.bytedance.applog.AppLog
import com.bytedance.applog.util.EventsSenderUtils
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
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.ENTER_METHOD
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.LIST_ITEM_ID
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.SEARCH_ENTRANCE
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.SEARCH_ID
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.SEARCH_RESULT_ID
import com.tokopedia.analyticsdebugger.cassava.Cassava
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
    var pageNames = mutableListOf<Pair<String, String?>>()

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

    internal fun addPageName(activity: Activity) {
        val actName = activity.javaClass.simpleName
        if (activity is IAppLogActivity) {
            synchronized(lock) {
                pageNames.add(actName to activity.getPageName())
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
                pageNames.findLast { it.first == activity.javaClass.simpleName }
            if (pageNameToRemove != null) {
                pageNames.remove(pageNameToRemove)
            }
        }
    }

    internal val Boolean.intValue
        get() = if (this) 1 else 0

    internal fun JSONObject.addPage() {
        put(PREVIOUS_PAGE, getLastDataBeforeCurrent(PAGE_NAME))
        put(PAGE_NAME, getLastData(PAGE_NAME))
    }

    internal fun JSONObject.addEntranceForm() {
        put(ENTRANCE_FORM, getLastData(ENTRANCE_FORM))
    }

    internal fun JSONObject.addEntranceInfo() {
        val data = JSONObject().also {
            it.put(ENTER_FROM_INFO, getLastData(ENTER_FROM))
            it.addEntranceForm()
            it.addSourcePageType()
            it.addTrackId()
            it.put(IS_AD, getLastData(IS_AD))
            it.addRequestId()
            it.addSourceModule()
            it.addEnterMethod()
            it.put(SEARCH_ENTRANCE, getLastData(SEARCH_ENTRANCE))
            it.put(SEARCH_ID, getLastData(SEARCH_ID))
            it.put(SEARCH_RESULT_ID, getLastData(SEARCH_RESULT_ID))
            it.put(LIST_ITEM_ID, getLastData(LIST_ITEM_ID))
        }
        put(ENTRANCE_INFO, data)
    }

    internal fun JSONObject.addEnterFrom() {
        put(ENTER_FROM, getLastData(ENTER_FROM))
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

    internal fun JSONObject.addSourceModule() {
        put(SOURCE_MODULE, getLastData(SOURCE_MODULE))
    }

    internal fun JSONObject.addRequestId() {
        put(REQUEST_ID, getLastData(REQUEST_ID))
    }

    internal fun JSONObject.addTrackId() {
        put(TRACK_ID, getLastData(TRACK_ID))
    }

    internal fun JSONObject.addEnterMethod() {
        put(ENTER_METHOD, getLastDataBeforeCurrent(ENTER_METHOD))
    }

    private fun currentPageName(): String {
        return synchronized(lock) {
            pageNames.findLast { it.first == currentActivityName }?.second ?: ""
        }
    }

    internal fun previousPageName(skip: Int = 1): String {
        return synchronized(lock) {
            (pageNames.getOrNull(pageNames.indexOf(pageNames.findLast { it.first == currentActivityName }) - skip)?.second)
                ?: ""
        }
    }

    fun send(event: String, params: JSONObject) {
        params.put(EVENT_ORIGIN_FEATURE_KEY, EVENT_ORIGIN_FEATURE_VALUE)
        Cassava.save(params, event, "ByteIO")
        AppLog.onEventV3(event, params)
        Timber.d("(%s) sending event ($event), value: ${params.toString(2)}", TAG)
    }

    @JvmStatic
    fun init(application: Application) {
        initAppLog(application.applicationContext)
        EventsSenderUtils.setEventsSenderEnable("573733", true, application)
        EventsSenderUtils.setEventVerifyHost("573733", "https://log.byteoversea.net")
        Timber.d(
            """(%s) 
            |AppLog dId: ${AppLog.getDid()} 
            |userUniqueId: ${AppLog.getUserUniqueID()} 
            |userId: ${AppLog.getUserUniqueID()}""".trimMargin(), TAG
        )
    }

    /**
     * To add page data for every page
     */
    fun pushPageData() {
        val tempHashMap = HashMap<String, Any>()
        _pageDataList.add(tempHashMap)
        Timber.d("Push _pageDataList: ${_pageDataList}}")
    }

    /**
     * To remove last page data
     */
    fun popPageData() {
        if (getCurrentData(IS_SHADOW) == false) {
            _pageDataList.removeLastOrNull()
            // Remove shadow stack
            if (getCurrentData(IS_SHADOW) == true) {
                _pageDataList.removeLastOrNull()
            }
        }
        Timber.d("Pop _pageDataList: ${_pageDataList}}")
    }

    private fun clearCurrentPageData() {
        _pageDataList.lastOrNull()?.clear()
    }

    /**
     * To update current page data
     */
    fun putPageData(key: String, value: Any) {
        _pageDataList.lastOrNull()?.put(key, value)
        Timber.d("Put _pageDataList: ${_pageDataList}}")
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

    fun getLastDataBeforeCurrent(key: String): Any? {
        if (_pageDataList.isEmpty()) return null
        var idx = _pageDataList.size - 2
        while (idx > 0) {
            val map = _pageDataList[idx]
            map[key]?.let {
                return it
            }
            idx--
        }
        return null
    }

    fun clearAllPageData() {
        _pageDataList.clear()
    }

    fun pushPageData(appLogInterface: AppLogInterface) {
        pushPageData()
        putAppLogInterfaceData(appLogInterface)
    }

    fun updateCurrentPageData(appLogInterface: AppLogInterface) {
        clearCurrentPageData()
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
        sourcePageType: SourcePageType? = null,
        requestId: String? = null,
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
            putPageData(SOURCE_PAGE_TYPE, sourcePageType.str)
        }
        requestId?.let {
            putPageData(REQUEST_ID, requestId)
        }
    }
}
