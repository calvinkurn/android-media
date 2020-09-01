package com.tokopedia.search.robot

import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface

internal class TopAdsTrackingResultRobot(private val topAdsCount: Int) {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    fun allSuccess() {
        val topAdsAssertion = TopAdsAssertion(context, TopAdsVerificatorInterface { topAdsCount })
        topAdsAssertion.assert()
        topAdsAssertion.after()
    }
}