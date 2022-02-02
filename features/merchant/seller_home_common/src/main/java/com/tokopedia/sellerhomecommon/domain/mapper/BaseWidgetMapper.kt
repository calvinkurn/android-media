package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.sellerhomecommon.data.WidgetLastUpdatedSharedPrefInterface
import com.tokopedia.sellerhomecommon.presentation.model.LastUpdatedUiModel
import java.util.*

/**
 * Created by @ilhamsuaib on 25/01/22.
 */

abstract class BaseWidgetMapper(
    private val lastUpdatedSharedPref: WidgetLastUpdatedSharedPrefInterface
) {

    protected fun getLastUpdatedMillis(dataKey: String, isFromCache: Boolean): LastUpdatedUiModel {
        val nowMillis = Date().time
        val lastUpdated = if (isFromCache) {
            lastUpdatedSharedPref.getLastUpdateInfoInMillis(dataKey, nowMillis)
        } else {
            saveLastUpdated(dataKey, nowMillis)
            nowMillis
        }
        return LastUpdatedUiModel(
            lastUpdatedInMillis = lastUpdated,
            isTheLatest = !isFromCache,
            shouldShow = isFromCache
        )
    }

    private fun saveLastUpdated(dataKey: String, timeInMillis: Long) {
        lastUpdatedSharedPref.saveLastUpdateInfo(dataKey, timeInMillis)
    }
}