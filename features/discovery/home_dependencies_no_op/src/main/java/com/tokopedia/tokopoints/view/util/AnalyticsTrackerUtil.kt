package com.tokopedia.tokopoints.view.util

import android.content.Context

class AnalyticsTrackerUtil {

    interface EventKeys {
        companion object {
            @JvmField
            val EVENT_TOKOPOINT = "eventTokopoint"

            @JvmField
            val TOKOPOINTS_LABEL = "tokopoints"
        }
    }

    interface CategoryKeys {
        companion object {
            @JvmField
            val HOMEPAGE = "homepage-tokopoints"
        }
    }

    interface ActionKeys {
        companion object {
            @JvmField
            val CLICK_POINT = "click point & tier status"
        }
    }

    companion object {
        @JvmStatic
        fun sendEvent(context: Context, event: String, category: String,
                      action: String, label: String) {
        }
    }
}