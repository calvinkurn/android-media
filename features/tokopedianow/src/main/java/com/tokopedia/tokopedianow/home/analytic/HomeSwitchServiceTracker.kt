package com.tokopedia.tokopedianow.home.analytic

data class HomeSwitchServiceTracker(val userId: String, val whIdOrigin: String, val whIdDestination: String, val is20mSwitcher: Boolean, val isImpressionTracker: Boolean)