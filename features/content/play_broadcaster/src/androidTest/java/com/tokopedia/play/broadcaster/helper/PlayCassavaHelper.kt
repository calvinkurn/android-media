package com.tokopedia.play.broadcaster.helper

import com.tokopedia.analyticsdebugger.cassava.core.Status
import com.tokopedia.analyticsdebugger.cassava.core.Validator
import com.tokopedia.analyticsdebugger.cassava.core.containsPairOf
import com.tokopedia.analyticsdebugger.cassava.core.regexEquals
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

/**
 * Created by kenny.hadisaputra on 04/03/22
 */
fun contains(pair: Pair<String, String>): Matcher<List<Validator>> {
    return object : TypeSafeMatcher<List<Validator>>(ArrayList::class.java) {
        override fun describeTo(description: Description?) {
            description?.appendText("last hit contains pair of $pair")
        }

        override fun matchesSafely(item: List<Validator>?): Boolean {
            if (item == null) return false
            return item.any {
                it.data.containsPairOf(pair) && it.status == Status.SUCCESS
            }
        }
    }
}

fun containsScreenName(pair: Pair<String, String>): Matcher<List<Validator>> {
    return object : TypeSafeMatcher<List<Validator>>(ArrayList::class.java) {
        override fun describeTo(description: Description?) {
            description?.appendText("last hit contains pair of $pair")
        }

        override fun matchesSafely(item: List<Validator>?): Boolean {
            if (item == null) return false
            return item.any {
                /**
                 * can't use [containsPairOf] from [Comparator.kt]
                 * because there's an issue in [regexEquals] function (the regex & value is swapped)
                 * Hence, the test with regex symbols will always failing.
                 */
                it.status == Status.SUCCESS && it.data.any { map ->
                    map.key == pair.first && validateRegex(map.value.toString(), pair.second)
                }
            }
        }

        private fun validateRegex(regex: String, value: String): Boolean {
            val regex = Regex(regex)
            return regex.matchEntire(value) != null
        }
    }
}

fun containsEventAction(value: String) = contains("eventAction" to value)

fun containsScreenName(value: String) = containsScreenName("screenName" to value)

val analyticUserSession: UserSessionInterface
    get() = mockk<UserSessionInterface>(relaxed = true).apply {
        every { shopId } returns "12345"
        every { userId } returns "12345"
    }
