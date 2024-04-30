package com.tokopedia.analytics.byteio

import android.app.Activity
import android.widget.Toast
import com.tokopedia.analytics.byteio.AppLogParam.IS_ADDITIONAL
import com.tokopedia.analytics.byteio.AppLogParam.PAGE_NAME
import com.tokopedia.analytics.byteio.AppLogParam.SOURCE_PAGE_TYPE
import com.tokopedia.analytics.byteio.AppLogParam.TRACK_ID

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

    private val additionalPageName = listOf(PageName.CART)

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
                    PAGE_NAME to "x",
                    AppLogParam.ACTIVITY_HASH_CODE to activity.hashCode()
                )
            )
        }
    }

    fun updateFirstTrackId() {
        val currentPage = getCurrentData() ?: return
        synchronized(this) {
            firstTrackId = ""
            firstSourcePage = ""
        }

        val containAdditionalPageName = additionalPageName.contains(currentPage[PAGE_NAME])

        if (currentPage[PAGE_NAME] != PageName.PDP &&
            !containAdditionalPageName
        ) {
            return
        }

        for (i in _pdpPageDataList.size - 1 downTo 0) {
            if (_pdpPageDataList[i][PAGE_NAME] == PageName.PDP ||
                additionalPageName.contains(_pdpPageDataList[i][PAGE_NAME])
            ) {
                val trackId = getDataFromPreviousPage(TRACK_ID, i)
                val sourcePageType = getDataFromPreviousPage(SOURCE_PAGE_TYPE, i)
                val isCheckout = getDataFromPreviousPage(IS_ADDITIONAL, i) == "checkout"

                if (isCheckout) {
                    continue
                }

                if (trackId.isEmpty() || sourcePageType.isEmpty()) {
                    break
                } else {
                    synchronized(this) {
                        _firstTrackId = trackId
                        _firstSourcePage = sourcePageType
                    }
                }
            } else {
                break
            }
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

    fun showToast(activity: Activity) {
        if (_pdpPageDataList.lastOrNull()?.get(PAGE_NAME) == PageName.PDP ||
            additionalPageName.contains(_pdpPageDataList.lastOrNull()?.get(PAGE_NAME) ?: "")
        ) {
            Toast.makeText(
                activity.applicationContext,
                "First Track Id = $firstTrackId\n" +
                    "First Page Source = $firstSourcePage",
                Toast.LENGTH_SHORT
            ).show()
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
