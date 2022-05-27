package com.tokopedia.sellerhomecommon.data

/**
 * Created by @ilhamsuaib on 21/01/22.
 */

interface WidgetLastUpdatedSharedPrefInterface {

    companion object {
        const val DEFAULT_LAST_UPDATED = 0L
    }

    fun saveLastUpdateInfo(dataKey: String, timeInMillis: Long)

    fun getLastUpdateInfoInMillis(dataKey: String, default: Long): Long
}