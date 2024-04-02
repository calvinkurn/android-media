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

internal fun JSONObject.assertRecommendationParam(key: String, value: String) {
    assert(this[key] == value)
}

internal fun JSONObject.assertRecommendationParam(key: String, value: Int) {
    assert(this[key].toString() == value.toString())
}

internal fun JSONObject.assertCompareRecommendationParam(otherJSONObject: JSONObject, key: String) {
    assert(this[key] == otherJSONObject[key])
}
