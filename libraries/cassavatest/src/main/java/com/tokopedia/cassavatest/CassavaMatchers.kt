package com.tokopedia.cassavatest

import com.tokopedia.analyticsdebugger.cassava.validator.core.Status
import com.tokopedia.analyticsdebugger.cassava.validator.core.Validator
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