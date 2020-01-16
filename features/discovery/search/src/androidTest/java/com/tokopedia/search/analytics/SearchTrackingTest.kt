package com.tokopedia.search.analytics

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics
import org.junit.Before
import org.junit.Test

class SearchTrackingTest {

    companion object {
        const val TAG = "SearchTrackingTest"
    }

    private class GTMDummyAnalytics(context: Context): ContextAnalytics(context) {
        override fun sendGeneralEvent(value: MutableMap<String, Any>?) {
            var valueString = ""
            value?.forEach {
                valueString += it.key + ": " + it.value + "\n"
            }

            Log.i(TAG, "sendGeneralEvent Map<String, Any>:\n $valueString")
        }

        override fun sendGeneralEvent(event: String?, category: String?, action: String?, label: String?) {
        }

        override fun sendEvent(eventName: String?, eventValue: MutableMap<String, Any>?) {
        }

        override fun sendEnhanceEcommerceEvent(value: MutableMap<String, Any>?) {
        }

        override fun sendScreenAuthenticated(screenName: String?) {
        }

        override fun sendScreenAuthenticated(screenName: String?, customDimension: MutableMap<String, String>?) {
        }

        override fun sendScreenAuthenticated(screenName: String?, shopID: String?, shopType: String?, pageType: String?, productId: String?) {
        }
    }

    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        TrackApp.initTrackApp(appContext.applicationContext as Application)
        TrackApp.getInstance().registerImplementation(TrackApp.GTM, GTMDummyAnalytics::class.java)
        TrackApp.getInstance().initializeAllApis()
    }

    @Test
    fun test_TrackGTMEventSearchAttempt() {
        SearchTracking.trackGTMEventSearchAttempt(null, false, null)

        SearchTracking.trackGTMEventSearchAttempt(
                "samsung",
                true,
                mutableMapOf<String, String>().also {
                    it["65"] = "Handphone & Tablet"
                    it["65"] = "Handphone & Tablet"
                    it["65"] = "Handphone & Tablet"
                    it["66"] = "Test Category"
                }
        )

        SearchTracking.trackGTMEventSearchAttempt("samsung", false, mutableMapOf<String, String>())
    }
}