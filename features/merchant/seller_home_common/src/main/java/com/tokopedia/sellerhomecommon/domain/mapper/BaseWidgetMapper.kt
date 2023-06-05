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

    companion object {
        private const val PREFERENCE_KEY_FORMAT = "last_updated_%s"
    }

    protected fun getLastUpdatedMillis(dataKey: String, isFromCache: Boolean): LastUpdatedUiModel {
        return if (isEnabled) {
            val nowMillis = Date().time
            val preferenceKey = String.format(PREFERENCE_KEY_FORMAT, dataKey)
            val lastUpdated = if (isFromCache) {
                lastUpdatedSharedPref.getLastUpdateInfoInMillis(preferenceKey, nowMillis)
            } else {
                saveLastUpdated(preferenceKey, nowMillis)
                nowMillis
            }

            LastUpdatedUiModel(
                lastUpdatedInMillis = lastUpdated,
                needToUpdated = isFromCache,
                isEnabled = true
            )
        } else {
            LastUpdatedUiModel(isEnabled = false)
        }
    }

    private fun saveLastUpdated(dataKey: String, timeInMillis: Long) {
        if (isEnabled) {
            lastUpdatedSharedPref.saveLastUpdateInfo(dataKey, timeInMillis)
        }
    }
}