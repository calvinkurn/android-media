package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.sellerhomecommon.data.WidgetLastUpdatedSharedPrefInterface
import com.tokopedia.sellerhomecommon.presentation.model.LastUpdatedUiModel
import java.util.*

/**
 * Created by @ilhamsuaib on 25/01/22.
 */

abstract class BaseWidgetMapper(
    private val lastUpdatedSharedPref: WidgetLastUpdatedSharedPrefInterface,
    private val isEnabled: Boolean
) {

    protected fun getLastUpdatedMillis(dataKey: String, isFromCache: Boolean): LastUpdatedUiModel {
        return if (isEnabled) {
            val nowMillis = Date().time
            val lastUpdated = if (isFromCache) {
                lastUpdatedSharedPref.getLastUpdateInfoInMillis(dataKey, nowMillis)
            } else {
                saveLastUpdated(dataKey, nowMillis)
                nowMillis
            }

            LastUpdatedUiModel(
                lastUpdatedInMillis = lastUpdated,
                needToUpdated = isFromCache,
                isEnabled = isEnabled
            )
        } else {
            LastUpdatedUiModel(isEnabled = isEnabled)
        }
    }

    private fun saveLastUpdated(dataKey: String, timeInMillis: Long) {
        if (isEnabled) {
            lastUpdatedSharedPref.saveLastUpdateInfo(dataKey, timeInMillis)
        }
    }
}