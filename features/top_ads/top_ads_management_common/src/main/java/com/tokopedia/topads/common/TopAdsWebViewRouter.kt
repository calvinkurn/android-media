package com.tokopedia.topads.common

import android.app.Activity
import android.os.Bundle

interface TopAdsWebViewRouter {
    fun isSupportedDelegateDeepLink(appLinks: String): Boolean
    fun actionNavigateByApplinksUrl(activity: Activity, applinks: String, bundle: Bundle)
}