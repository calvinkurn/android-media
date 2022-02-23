package com.tokopedia.navigation.util

import com.tokopedia.analytics.performance.util.EmbraceMonitoring
import org.json.JSONObject
import java.lang.Exception

class MainParentServerLogger {
    companion object {
        private const val MP_EMBRACE_BREADCRUMB_FORMAT = "%s, %s"
        private const val MP_CURRENT_VISIBLE_FRAGMENT = "CurrentVisibleFragment"
        private const val MP_PAGE = "MainParent"

        fun sendEmbraceBreadCrumb(
            currentSelectedTabName: String
        ) {
            try {
                val embraceJsonData = JSONObject()
                embraceJsonData.put(
                    MP_CURRENT_VISIBLE_FRAGMENT,
                    currentSelectedTabName
                )

                EmbraceMonitoring.logBreadcrumb(
                    String.format(
                        MP_EMBRACE_BREADCRUMB_FORMAT,
                        MP_PAGE,
                        embraceJsonData
                    )
                )
            } catch (e: Exception) {

            }
        }
    }
}