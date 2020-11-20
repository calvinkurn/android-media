package com.tokopedia.buyerorder

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Checks
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.buyerorder.unifiedhistory.list.view.activity.UohListActivity
import com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.viewholder.UohOrderListViewHolder
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.unifycomponents.UnifyButton
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert
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

fun clickOnViewChild(viewId: Int) = object : ViewAction {
    override fun getConstraints() = null

    override fun getDescription() = "Click on a child view with specified id."

    override fun perform(uiController: UiController, view: View) = click().perform(uiController, view.findViewById<View>(viewId))
}