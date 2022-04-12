package com.tokopedia.play.broadcaster.helper

import com.tokopedia.analyticsdebugger.cassava.core.Status
import com.tokopedia.analyticsdebugger.cassava.core.Validator
import com.tokopedia.analyticsdebugger.cassava.core.containsPairOf
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

fun containsEventAction(value: String) = contains("eventAction" to value)

val analyticUserSession: UserSessionInterface
    get() = mockk<UserSessionInterface>(relaxed = true).apply {
        every { shopId } returns "12345"
        every { userId } returns "12345"
    }