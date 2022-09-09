package com.tokopedia.sellerhomecommon.data

/**
 * Created by @ilhamsuaib on 21/01/22.
 */

interface WidgetLastUpdatedSharedPrefInterface {

    fun saveLastUpdateInfo(dataKey: String, timeInMillis: Long)

    fun getLastUpdateInfoInMillis(dataKey: String, default: Long): Long
}