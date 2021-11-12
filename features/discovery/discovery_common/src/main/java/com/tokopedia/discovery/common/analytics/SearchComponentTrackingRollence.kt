package com.tokopedia.discovery.common.analytics

import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics

object SearchComponentTrackingRollence {

    fun impress(
        searchComponentTracking: SearchComponentTracking,
        experimentName: String,
        fallback: () -> Unit = { },
    ) {
        try {
            val remoteConfig = RemoteConfigInstance.getInstance().abTestPlatform
            val analytics = TrackApp.getInstance().gtm

            impress(
                remoteConfig,
                analytics,
                searchComponentTracking,
                experimentName
            )
        } catch (throwable: Throwable) {
            fallback()
        }
    }

    fun click(
        searchComponentTracking: SearchComponentTracking,
        experimentName: String,
        fallback: () -> Unit = { },
    ) {
        try {
            val remoteConfig = RemoteConfigInstance.getInstance().abTestPlatform
            val analytics = TrackApp.getInstance().gtm

            click(
                remoteConfig,
                analytics,
                searchComponentTracking,
                experimentName
            )
        } catch (throwable: Throwable) {
            fallback()
        }
    }

    fun clickOtherAction(
        searchComponentTracking: SearchComponentTracking,
        experimentName: String,
        fallback: () -> Unit = { },
    ) {
        try {
            val remoteConfig = RemoteConfigInstance.getInstance().abTestPlatform
            val analytics = TrackApp.getInstance().gtm

            clickOtherAction(
                remoteConfig,
                analytics,
                searchComponentTracking,
                experimentName
            )
        } catch (throwable: Throwable) {
            fallback()
        }
    }

    private fun RemoteConfig.isEnabled(experimentName: String): Boolean {
        val remoteConfigValue = getString(experimentName)
        return remoteConfigValue == experimentName
    }

    internal fun impress(
        remoteConfig: RemoteConfig,
        analytics: Analytics,
        searchComponentTracking: SearchComponentTracking,
        experimentName: String,
        fallback: () -> Unit = { },
    ) {
        if (remoteConfig.isEnabled(experimentName))
            searchComponentTracking.impress(analytics)
        else
            fallback()
    }

    internal fun click(
        remoteConfig: RemoteConfig,
        analytics: Analytics,
        searchComponentTracking: SearchComponentTracking,
        experimentName: String,
        fallback: () -> Unit = { },
    ) {
        if (remoteConfig.isEnabled(experimentName))
            searchComponentTracking.click(analytics)
        else
            fallback()
    }

    internal fun clickOtherAction(
        remoteConfig: RemoteConfig,
        analytics: Analytics,
        searchComponentTracking: SearchComponentTracking,
        experimentName: String,
        fallback: () -> Unit = { },
    ) {
        if (remoteConfig.isEnabled(experimentName))
            searchComponentTracking.clickOtherAction(analytics)
        else
            fallback()
    }
}