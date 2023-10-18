package com.tokopedia.contactus

import android.content.Intent
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import com.tokopedia.contactus.inboxtickets.view.inbox.InboxContactUsActivity
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.InboxDetailActivity
import com.tokopedia.contactus.utils.InstrumentedTestUtil.scrollRecyclerViewToPosition
import com.tokopedia.test.application.espresso_component.CommonMatcher
import com.tokopedia.test.application.matcher.RecyclerViewMatcher

class InboxRobotPattern {

    fun launchInboxPage(activityRule: ActivityTestRule<InboxContactUsActivity>, intent: Intent) {
        activityRule.launchActivity(intent)
    }

    fun launchDetailInboxPage(activityRule: ActivityTestRule<InboxDetailActivity>, intent: Intent) {
        activityRule.launchActivity(intent)
    }

    fun waitForData(longToWaint: Long = 1000L) {
        Thread.sleep(longToWaint)
    }

    fun Int.isVisible() {
        Espresso.onView(ViewMatchers.withId(this))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    fun Int.isGone() {
        Espresso.onView(ViewMatchers.withId(this))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }

    fun click(resId: Int) {
        Espresso.onView(
            CommonMatcher
                .firstView(ViewMatchers.withId(resId))
        )
            .perform(ViewActions.click())
    }

    fun RecyclerView?.clickOnPosition(position: Int) {
        Espresso.onView(ViewMatchers.withId(this?.id ?: throw NoSuchElementException()))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    position, ViewActions.click()
                )
            )
    }

    fun Int.isVisibleInPosition(position: Int, onRecyclerView: RecyclerView?) {
        if (onRecyclerView == null) {
            throw NoSuchElementException()
        }
        Espresso.onView(
            RecyclerViewMatcher(onRecyclerView.id)
                .atPositionOnView(position, this)
        ).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    fun Int.isTextShowedInPosition(position: Int, withText: String, onRecyclerView: RecyclerView?) {
        if (onRecyclerView == null) {
            throw NoSuchElementException()
        }
        Espresso.onView(RecyclerViewMatcher(onRecyclerView.id).atPositionOnView(position, this))
            .check(
                ViewAssertions.matches(
                    ViewMatchers.withText(withText)
                )
            )
    }

    fun findRecyclerViewWithId(
        resId: Int,
        onActivityRule: ActivityTestRule<InboxDetailActivity>
    ): RecyclerView {
        return onActivityRule.activity.findViewById(resId)
    }

    fun RecyclerView?.scrollToPosition(
        position: Int,
        onActivityRule: ActivityTestRule<InboxDetailActivity>
    ) {
        if (this == null) {
            throw NoSuchElementException()
        }
        onActivityRule.scrollRecyclerViewToPosition(this, position)
    }

    fun RecyclerView?.isHaveView(viewToCheck: Int, onPosition: Int) {
        if (this == null) {
            throw NoSuchElementException()
        }
        Espresso.onView(
            RecyclerViewMatcher(this.id)
                .atPositionOnView(onPosition, viewToCheck)
        ).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    fun RecyclerView?.isNotHaveView(viewToCheck: Int, onPosition: Int) {
        if (this == null) {
            throw NoSuchElementException()
        }
        Espresso.onView(
            RecyclerViewMatcher(this.id)
                .atPositionOnView(onPosition, viewToCheck)
        )
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }

    fun RecyclerView?.getSize(): Int {
        if (this == null) {
            throw NoSuchElementException()
        }
        return this.size
    }

    fun String.isVisible() {
        Espresso.onView(ViewMatchers.withText(this))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    infix fun verify(func: InboxRobotPattern.() -> Unit) = InboxRobotPattern().apply(func)

    infix fun doing(func: InboxRobotPattern.() -> Unit) = InboxRobotPattern().apply(func)

    infix fun launchPage(func: InboxRobotPattern.() -> Unit) = InboxRobotPattern().apply(func)

}

fun initRulePage(func: InboxRobotPattern.() -> Unit) = InboxRobotPattern().apply(func)
