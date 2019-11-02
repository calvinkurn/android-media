package com.tokopedia.core.analytics.screen

import androidx.fragment.app.FragmentActivity

class IndexScreenTracking {

    companion object {
        @JvmStatic
        fun sendScreen(activity: FragmentActivity, openScreenAnalytics: IOpenScreenAnalytics) {

        }
    }
}

interface IOpenScreenAnalytics {
    val screenName: String
}