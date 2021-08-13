package com.tokopedia.customer_mid_app.activity

import com.tokopedia.tkpd.ConsumerSplashScreen
import com.tokopedia.analytics.performance.util.SplashScreenPerformanceTracker

/**
 * Created by Devara Fikry Akmal 07/01/2021
 *
 * Why I created this:
 *
 * Using consumer splash screen will starting home intent automatically on finishSplashScreen()
 * This simply to prevent that case
 */

class TestConsumerSplashScreen: ConsumerSplashScreen() {
    override fun finishSplashScreen() {
        SplashScreenPerformanceTracker.stopMonitoring()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {

    }
}