package com.tokopedia.analytics.recommendation

import com.tokopedia.analytics.byteio.AppLogAnalytics
import io.mockk.verify
import org.json.JSONObject

internal fun verifyRecommendationEvent(expectedEvents: Set<String>) {
    verify(exactly = expectedEvents.size) {
        AppLogAnalytics.send(match { expectedEvents.contains(it) }, any())
    }
}

internal fun verifyRecommendationEvent(expectedEvents: String) {
    verify(exactly = 1) {
        AppLogAnalytics.send(expectedEvents, any())
    }
}


internal fun verifyRecommendationEventNot(expectedEvents: String) {
    verify(inverse = true) {
        AppLogAnalytics.send(expectedEvents, any())
    }
}
