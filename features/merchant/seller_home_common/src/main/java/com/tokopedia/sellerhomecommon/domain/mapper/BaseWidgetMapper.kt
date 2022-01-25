package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.sellerhomecommon.data.WidgetLastUpdatedSharedPrefInterface
import java.util.*

/**
 * Created by @ilhamsuaib on 25/01/22.
 */

abstract class BaseWidgetMapper(
    private val lastUpdatedSharedPref: WidgetLastUpdatedSharedPrefInterface
) {

    protected fun getLastUpdatedMillis(dataKey: String, isFromCache: Boolean): Long {
        val nowMillis = Date().time
        return if (isFromCache) {
            lastUpdatedSharedPref.getLastUpdateInfoInMillis(dataKey, nowMillis)
        } else {
            saveLastUpdated(dataKey, nowMillis)
            nowMillis
        }
    }

    private fun saveLastUpdated(dataKey: String, timeInMillis: Long) {
        lastUpdatedSharedPref.saveLastUpdateInfo(dataKey, timeInMillis)
    }
}