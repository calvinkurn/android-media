package com.tokopedia.buyerorder

import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ScrollToAction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.buyerorder.unifiedhistory.list.view.activity.UohListActivity
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers.anyOf
import org.hamcrest.TypeSafeMatcher


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
}

fun runBot(func: UohRobot.() -> Unit) = UohRobot().apply(func)

fun submit(func: ResultRobot.() -> Unit): ResultRobot {
    return ResultRobot().apply(func)
}

fun nthChildOf(parentMatcher: Matcher<View?>, childPosition: Int): Matcher<View?>? {
    return object : TypeSafeMatcher<View?>() {
        override fun describeTo(description: Description) {
            description.appendText("position $childPosition of parent ")
            parentMatcher.describeTo(description)
        }

        override fun matchesSafely(view: View?): Boolean {
            if (view?.parent !is ViewGroup) return false
            val parent = view.parent as ViewGroup
            return (parentMatcher.matches(parent)
                    && parent.childCount > childPosition && parent.getChildAt(childPosition) == view)
        }
    }
}

var customScrollTo: ViewAction = object : ViewAction {
    override fun getConstraints(): Matcher<View> {
        return allOf(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE), isDescendantOfA(anyOf(
                isAssignableFrom(ScrollView::class.java),
                isAssignableFrom(HorizontalScrollView::class.java),
                isAssignableFrom(NestedScrollView::class.java)))
        )
    }

    override fun getDescription(): String? {
        return null
    }

    override fun perform(uiController: UiController?, view: View?) {
        ScrollToAction().perform(uiController, view)
    }
}

class ResultRobot {
    fun hasPassedAnalytics(repository: GtmLogDBSource, queryString: String) {
        MatcherAssert.assertThat(getAnalyticsWithQuery(repository, queryString), hasAllSuccess())
    }
}