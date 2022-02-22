package com.tokopedia.navigation.util

import org.json.JSONObject
import io.embrace.android.embracesdk.Embrace

class MainParentServerLogger {
    companion object {
        private const val MP_EMBRACE_BREADCRUMB_FORMAT = "%s, %s, %s"
        private const val MP_CURRENT_VISIBLE_FRAGMENT = "CurrentVisibleFragment"
        private const val MP_PAGE = "MainParent"

        fun sendEmbraceBreadCrumb(
            currentSelectedTabName: String
        ) {
            val embraceJsonData = JSONObject()
            embraceJsonData.put(
                MP_CURRENT_VISIBLE_FRAGMENT,
                currentSelectedTabName
            )

            Embrace.getInstance().logBreadcrumb(
                String.format(
                    MP_EMBRACE_BREADCRUMB_FORMAT,
                    MP_PAGE,
                    embraceJsonData
                )
            )
        }
    }
}