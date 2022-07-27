package com.tokopedia.tokopedianow.home.analytic

data class HomeSwitchServiceTracker(val userId: String, val whIdOrigin: String, val whIdDestination: String, val isNow15: Boolean, val isImpressionTracker: Boolean)