package com.tokopedia.analytics.byteio

import android.app.Activity
import com.tokopedia.analytics.byteio.AppLogParam.PAGE_NAME
import com.tokopedia.analytics.byteio.AppLogParam.SOURCE_PAGE_TYPE
import com.tokopedia.analytics.byteio.AppLogParam.TRACK_ID

/**
 * This class will be reflection of AppLogAnalytics but the different is all of the activity
 * even not implemented byte io interface, will still put in this stack.
 *
 * If you want to include your page in the first track id flow, put your activity key or page name
 * in additionalPageName.
 */
object AppLogFirstTrackId {

    private var _firstTrackId = ""
    var firstTrackId: String
        get() = _firstTrackId
        set(value) {
            _firstTrackId = value
        }

    private var _firstSourcePage = ""
    var firstSourcePage: String
        get() = _firstSourcePage
        set(value) {
            _firstSourcePage = value
        }


    private val _pdpPageDataList = ArrayList<HashMap<String, Any>>()

    private val additionalPageName =
        listOf("shipmentactivity", "ordersummarypage", PageName.SKU, PageName.ORDER_SUBMIT)

    private fun appendPdpDataList(maps: HashMap<String, Any>) {
        _pdpPageDataList.add(maps)
    }

    fun appendDataFromMainList(activity: Activity, hashMap: HashMap<String, Any>?) {
        if (activity is AppLogInterface) {
            hashMap?.let {
                appendPdpDataList(it)
            }
        } else {
            appendPdpDataList(
                hashMapOf(
                    PAGE_NAME to activity::class.java.simpleName,
                    AppLogParam.ACTIVITY_HASH_CODE to activity.hashCode()
                )
            )
        }
    }

    fun updateFirstTrackId() {
        val currentPage = getCurrentData() ?: return
        updateGlobalData("", "")

        val currentPageName = currentPage[PAGE_NAME] as? String ?: ""
        val containAdditionalPageName = additionalPageName.isContainsOneOfString(
            currentPageName.lowercase()
        )
        if (currentPageName != PageName.PDP &&
            !containAdditionalPageName
        ) {
            return
        }

        for (i in _pdpPageDataList.size - 1 downTo 0) {
            val pageName = _pdpPageDataList[i][PAGE_NAME] as? String ?: ""
            if (pageName == PageName.PDP ||
                additionalPageName.isContainsOneOfString(pageName.lowercase())
            ) {
                val previousTrackId = getDataFromPreviousPage(TRACK_ID, i)
                val previousSourcePageType = getDataFromPreviousPage(SOURCE_PAGE_TYPE, i)

                if (additionalPageName.isContainsOneOfString(pageName)) {
                    updateGlobalData(previousTrackId, previousSourcePageType)
                    continue
                }

                if (previousTrackId.isEmpty() || previousSourcePageType.isEmpty()) {
                    break
                } else {
                    updateGlobalData(previousTrackId, previousSourcePageType)
                }
            } else {
                break
            }
        }
    }

    private fun updateGlobalData(trackId: String, firstSourcePage: String) {
        synchronized(this) {
            _firstTrackId = trackId
            _firstSourcePage = firstSourcePage
        }
    }

    private fun List<String>.isContainsOneOfString(pageName: String): Boolean {
        return any {
            pageName.lowercase().contains(it)
        }
    }

    fun putPageData(key: String, value: Any) {
        _pdpPageDataList.lastOrNull()?.put(key, value)
    }

    fun removeCurrentActivityByHashCode(activity: Activity) {
        val pageDataIndexed = _pdpPageDataList
            .withIndex()
            .find { it.value[AppLogParam.ACTIVITY_HASH_CODE] == activity.hashCode() }
            ?: return

        _pdpPageDataList.removeAt(pageDataIndexed.index)
    }

    fun removePageData(appLogInterface: AppLogInterface) {
        val pageDataIndexed = _pdpPageDataList
            .withIndex()
            .find { it.value[AppLogParam.ACTIVITY_HASH_CODE] == appLogInterface.hashCode() }
            ?: return

        val index = pageDataIndexed.index
        val pageData = pageDataIndexed.value

        if (pageData[AppLogParam.IS_SHADOW] != true) {
            _pdpPageDataList.removeAt(index)

            val shadowPageIndex = index - 1
            // Remove shadow stack
            removeShadowStack(shadowPageIndex)
        }
    }

    private fun getDataFromPreviousPage(key: String, index: Int): String {
        return if (index == 0) {
            _pdpPageDataList[index][key] as? String ?: ""
        } else {
            _pdpPageDataList[index - 1][key] as? String ?: ""
        }
    }

    private fun removeShadowStack(currentIndex: Int) {
        var tempCurrentIndex = currentIndex
        while (tempCurrentIndex >= 0 && _pdpPageDataList.getOrNull(tempCurrentIndex)
                ?.get(AppLogParam.IS_SHADOW) == true
        ) {
            _pdpPageDataList.removeAt(tempCurrentIndex)
            tempCurrentIndex--
        }
    }

    private fun getCurrentData(): Map<String, Any>? {
        return _pdpPageDataList.lastOrNull()
    }
}
