package com.tokopedia.analyticsdebugger.cassava.core

import org.junit.Test

val GENERAL_MAP = mapOf(
        "eventAction" to "match",
        "foo" to emptyList<String>(),
        "event" to true
)

val NON_STRING_VAL = mapOf<String, Any>(
        "foo" to true
)

val EVENT_ONLY_MAP = mapOf(
        "foo" to "foo",
        "event" to "match"
)

class ValidatorKtTest {

    @Test
    fun `given valid query returns eventAction first`() {
        val actual = getAnalyticsName(GENERAL_MAP)
        assert(actual == "match")
    }

    @Test
    fun `given query with non string val returns empty string`() {
        val actual = getAnalyticsName(NON_STRING_VAL)
        assert(actual.isEmpty())
    }

    @Test
    fun `given query with event only gets event name`() {
        val actual = getAnalyticsName(EVENT_ONLY_MAP)
        assert(actual == "match")
    }
}