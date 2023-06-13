package com.tokopedia.tkpd.feed_component.helper

import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.analyticsdebugger.cassava.core.Status
import com.tokopedia.analyticsdebugger.cassava.core.Validator
import com.tokopedia.analyticsdebugger.cassava.core.containsPairOf
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

/**
 * Created By : Jonathan Darwin on September 23, 2022
 */
class FeedCassavaHelper(
    private val analyticFile: String,
    private val cassavaTestRule: CassavaTestRule
) {

    fun assertCassavaByEventAction(eventAction: String) {
        ViewMatchers.assertThat(
            cassavaTestRule.validate(analyticFile),
            containsEventAction(eventAction)
        )
    }

    private fun containsEventAction(value: String) = contains("eventAction" to value)

    private fun contains(pair: Pair<String, String>): Matcher<List<Validator>> {
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
}
