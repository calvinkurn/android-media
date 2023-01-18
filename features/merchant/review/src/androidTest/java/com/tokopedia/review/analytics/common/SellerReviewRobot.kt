package com.tokopedia.review.analytics.common

import android.app.Activity
import android.app.Instrumentation
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.review.common.Utils
import com.tokopedia.unifycomponents.BottomSheetUnify
import org.hamcrest.Matcher

class SellerReviewRobot {

    private fun isBottomSheetShowingAndSettled(bottomSheetUnify: BottomSheetUnify?): Boolean {
        val state = bottomSheetUnify?.bottomSheet?.state
        return state == BottomSheetBehavior.STATE_EXPANDED || state == BottomSheetBehavior.STATE_HALF_EXPANDED || state == BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun isViewVisible(matcher: Matcher<View>): Boolean {
        return try {
            Espresso.onView(matcher)
                .check(ViewAssertions.matches(ViewMatchers.isDisplayingAtLeast(90)))
            true
        } catch (t: Throwable) {
            false
        }
    }

    fun <T : Activity> performClose(activityTestRule: ActivityTestRule<T>) {
        activityTestRule.finishActivity()
    }

    fun performBack() {
        Espresso.pressBackUnconditionally()
    }

    fun clickAction(idView: Int) {
        waitUntilViewVisible(ViewMatchers.withId(idView))
        Espresso.onView(ViewMatchers.withId(idView)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())).perform(ViewActions.click())
    }

    fun clickAction(text: String) {
        waitUntilViewVisible(ViewMatchers.withText(text))
        Espresso.onView(ViewMatchers.withText(text)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())).perform(ViewActions.click())
    }

    fun scrollToRecyclerViewItem(recyclerViewId: Int, position: Int) {
        actionOnRecyclerViewItem(recyclerViewId, position, ViewActions.scrollTo())
    }

    fun clickRecyclerViewItem(recyclerViewId: Int, position: Int) {
        actionOnRecyclerViewItem(recyclerViewId, position, ViewActions.click())
    }

    fun actionOnRecyclerViewItem(recyclerViewId: Int, position: Int, action: ViewAction?) {
        waitUntilViewVisible(ViewMatchers.withId(recyclerViewId))
        Espresso.onView(ViewMatchers.withId(recyclerViewId)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position,
                action
            )
        )
    }

    fun waitUntilBottomSheetShowingAndSettled(bottomSheetFinder: () -> BottomSheetUnify?) {
        Utils.waitForCondition {
            try {
                isBottomSheetShowingAndSettled(bottomSheetFinder())
            } catch (_: Throwable) {
                false
            }
        }
    }

    fun waitUntilViewVisible(matcher: Matcher<View>) {
        Utils.waitForCondition {
            isViewVisible(matcher)
        }
    }

    fun blockAllIntent() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    infix fun assertTest(action: SellerReviewRobot.() -> Unit) = SellerReviewRobot().apply(action)

    fun validate(cassavaTestRule: CassavaTestRule, fileName: String) {
        ViewMatchers.assertThat(
            cassavaTestRule.validate(fileName),
            hasAllSuccess()
        )
    }

    fun validateWithTimeout(cassavaTestRule: CassavaTestRule, fileName: String, timeout: Long = 5000L) {
        val startTime = System.currentTimeMillis()
        while ((System.currentTimeMillis() - startTime) < timeout) {
            try {
                ViewMatchers.assertThat(
                    cassavaTestRule.validate(fileName),
                    hasAllSuccess()
                )
                return
            } catch (_: Throwable) {

            }
        }
        ViewMatchers.assertThat(
            cassavaTestRule.validate(fileName),
            hasAllSuccess()
        )
    }
}

fun actionTest(action: SellerReviewRobot.() -> Unit) = SellerReviewRobot().apply(action)
