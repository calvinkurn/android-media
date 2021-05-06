package com.tokopedia.cassavatest

import com.tokopedia.analyticsdebugger.cassava.validator.core.*
import com.tokopedia.analyticsdebugger.database.GtmLogDB
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun hasAllSuccess(): Matcher<List<Validator>> {
    return object : TypeSafeMatcher<List<Validator>>(ArrayList::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("All analytic hits are successful")
        }

        override fun describeMismatchSafely(item: List<Validator>?, mismatchDescription: Description?) {
            val indexWithFailure = item?.mapIndexed { index, validator ->
                if (validator.status != Status.SUCCESS) index else -1
            }?.filter { it >= 0 }?.joinToString()
            mismatchDescription
                    ?.appendText(" has mismatch status on query number ")
                    ?.appendValue(indexWithFailure)
        }

        override fun matchesSafely(result: List<Validator>): Boolean {
            return result.all { it.status == Status.SUCCESS }
        }
    }
}

/*
* Match if a tracker has given key and value pair on any of listed gtm object
* Can also work on nested tracker
* */
fun containsPairOf(pair: Pair<String, String>): Matcher<List<GtmLogDB>> {
    return object : TypeSafeMatcher<List<GtmLogDB>>(ArrayList::class.java) {
        override fun describeTo(description: Description?) {
            description?.appendText("last hit contains pair of $pair")
        }

        override fun matchesSafely(item: List<GtmLogDB>?): Boolean {
            if (item == null) return false
            return item.any {
                it.data.toJsonMap().containsPairOf(pair)
            }
        }
    }
}

fun containsMapOf(map: Map<String, Any>, mode: String = CassavaTestRule.MODE_EXACT): Matcher<List<GtmLogDB>> {
    return object : TypeSafeMatcher<List<GtmLogDB>>(ArrayList::class.java) {
        override fun describeTo(description: Description?) {
            description?.appendText("last hit contains map of $map")
        }

        override fun matchesSafely(item: List<GtmLogDB>?): Boolean {
            if (item == null) return false
            val isExact = mode != CassavaTestRule.MODE_SUBSET
            return item.containsMapOf(map, isExact)
        }
    }
}