package com.tokopedia.buyerorder

import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.buyerorder.unifiedhistory.list.view.activity.UohListActivity
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert


/**
 * Created by fwidjaja on 08/11/20.
 */
class UohRobot {
    fun launchFrom(rule: ActivityTestRule<UohListActivity>) {
        rule.launchActivity(null)
    }

    fun login(rule: ActivityTestRule<UohListActivity>) {
        InstrumentationAuthHelper.loginToAnUser(rule.activity.application)
    }

    fun atPosition(position: Int, @NonNull itemMatcher: Matcher<View?>): Matcher<View?>? {
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder: RecyclerView.ViewHolder = view.findViewHolderForAdapterPosition(position)
                        ?: // has no item on such position
                        return false
                return itemMatcher.matches(viewHolder.itemView)
            }
        }
    }
}

fun runBot(func: UohRobot.() -> Unit) = UohRobot().apply(func)

fun submit(func: ResultRobot.() -> Unit): ResultRobot {
    return ResultRobot().apply(func)
}

class ResultRobot {
    fun hasPassedAnalytics(repository: GtmLogDBSource, queryString: String) {
        MatcherAssert.assertThat(getAnalyticsWithQuery(repository, queryString), hasAllSuccess())
    }
}