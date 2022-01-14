package com.tokopedia.review.analytics.common

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.review.common.Utils
import com.tokopedia.unifycomponents.BottomSheetUnify

class SellerReviewRobot {

    private fun isBottomSheetShowingAndSettled(bottomSheetUnify: BottomSheetUnify?): Boolean {
        val state = bottomSheetUnify?.bottomSheet?.state
        return state == BottomSheetBehavior.STATE_EXPANDED || state == BottomSheetBehavior.STATE_HALF_EXPANDED || state == BottomSheetBehavior.STATE_COLLAPSED
    }

    fun <T : Activity> performClose(activityTestRule: ActivityTestRule<T>) {
        activityTestRule.finishActivity()
    }

    fun clickAction(idView: Int) {
        Espresso.onView(ViewMatchers.withId(idView)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())).perform(ViewActions.click())
    }

    fun clickAction(text: String) {
        Espresso.onView(ViewMatchers.withText(text)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())).perform(ViewActions.click())
    }

    fun scrollToRecyclerViewItem(recyclerViewId: Int, position: Int) {
        actionOnRecyclerViewItem(recyclerViewId, position, ViewActions.scrollTo())
    }

    fun clickRecyclerViewItem(recyclerViewId: Int, position: Int) {
        actionOnRecyclerViewItem(recyclerViewId, position, ViewActions.click())
    }

    fun actionOnRecyclerViewItem(recyclerViewId: Int, position: Int, action: ViewAction?) {
        Espresso.onView(ViewMatchers.withId(recyclerViewId)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position,
                action
            )
        )
    }

    fun waitUntilBottomSheetShowingAndSettled(bottomSheetFinder: () -> BottomSheetUnify?) {
        Utils.waitForCondition {
            isBottomSheetShowingAndSettled(bottomSheetFinder())
        }
    }

    infix fun assertTest(action: SellerReviewRobot.() -> Unit) = SellerReviewRobot().apply(action)

    fun validate(cassavaTestRule: CassavaTestRule, fileName: String) {
        ViewMatchers.assertThat(
            cassavaTestRule.validate(fileName),
            hasAllSuccess()
        )
    }
}

fun actionTest(action: SellerReviewRobot.() -> Unit) = SellerReviewRobot().apply(action)