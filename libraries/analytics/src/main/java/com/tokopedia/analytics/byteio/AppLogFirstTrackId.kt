package com.tokopedia.analytics.byteio

import android.app.Activity
import android.widget.Toast
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

    private val additionalPageName = listOf("shipmentactivity", "atcvariant", "ordersummarypage")

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
        synchronized(this) {
            firstTrackId = ""
            firstSourcePage = ""
        }

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

                /**
                 * Support page that doesn't have track id due to opening one of additionalPageName
                 * We need to continue to find first_track_id in checkout
                 *
                 *Example:
                 *  shopPage, pdp1(track_id), pdp2, checkout -> first_track_id should be pdp1
                 *  pdp2 doesn't have track id, so we continue to find the first_track_id
                 *  this case only happen when you want to get first_track_id
                 *  in the page that doesn't send track_id
                 */
                if (additionalPageName.isContainsOneOfString(pageName)) {
                    continue
                }

                if (previousTrackId.isEmpty() || previousSourcePageType.isEmpty()) {
                    break
                } else {
                    synchronized(this) {
                        _firstTrackId = previousTrackId
                        _firstSourcePage = previousSourcePageType
                    }
                }


                /**
                 * Need to stop the flow if previous page is cart
                 * especially for cart because the cart can go to pdp
                 * through recommendation with track_id
                 */
                val previousPageName = getDataFromPreviousPage(PAGE_NAME, i)
                if (additionalPageName.isContainsOneOfString(previousPageName)) {
                    break
                }
            } else {
                break
            }
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

    fun showToast(activity: Activity) {
        val currentPageName = _pdpPageDataList.lastOrNull()?.get(PAGE_NAME) as? String ?: ""
        if (currentPageName == PageName.PDP ||
            additionalPageName.isContainsOneOfString(currentPageName)
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
