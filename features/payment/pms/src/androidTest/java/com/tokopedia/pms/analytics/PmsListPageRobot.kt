package com.tokopedia.pms.analytics

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.pms.R
import com.tokopedia.test.application.espresso_component.CommonActions
import org.hamcrest.Matchers.allOf

class PmsListPageRobot {

    fun clickHtpTest(position: Int) {
        val viewInteraction =
            onView(allOf(withId(R.id.recycler_view))).check(matches(isDisplayed()))
        viewInteraction.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position,
                CommonActions.clickChildViewWithId(R.id.goToHowToPay)
            )
        )
    }

    fun testChevronClick(position: Int) {
        val viewInteraction =
            onView(allOf(withId(R.id.recycler_view))).check(matches(isDisplayed()))
        viewInteraction.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position,
                CommonActions.clickChildViewWithId(R.id.cardMenu)
            )
        )
    }

    fun testCardClick(position: Int) {
        val viewInteraction =
            onView(allOf(withId(R.id.recycler_view))).check(matches(isDisplayed()))
        viewInteraction.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position,
                CommonActions.clickChildViewWithId(R.id.waitingPaymentCard)
            )
        )
    }

    fun clickItemOnActionBottomSheet(position: Int) {
        val viewInteraction =
            onView(allOf(withId(R.id.baseRecyclerView))).check(matches(isDisplayed()))
        viewInteraction.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position,
                CommonActions.clickChildViewWithId(R.id.itemActionCL)
            )
        )
    }

    fun clickItemOnDetailBottomSheet(position: Int, resId: Int) {
        val viewInteraction =
            onView(allOf(withId(R.id.baseRecyclerView))).check(matches(isDisplayed()))
        viewInteraction.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position,
                CommonActions.clickChildViewWithId(resId)
            )
        )
    }

    fun clickDialogButton(close: Boolean) {
        val resId =
            if (close) com.tokopedia.dialog.R.id.dialog_btn_secondary
            else com.tokopedia.dialog.R.id.dialog_btn_primary
        val viewInteraction = onView(allOf(withId(resId)))
            .check(matches(isDisplayed()))
        viewInteraction.perform(click())
    }

    infix fun assertTest(action: PmsListPageRobot.() -> Unit) = PmsListPageRobot().apply(action)

    fun validate(cassavaTestRule: CassavaTestRule, queryId: String) {
        assertThat(cassavaTestRule.validate(queryId), hasAllSuccess())
    }

    fun actionClickView(resId: Int) {
        val viewInteraction =
            onView(allOf(withId(resId))).check(matches(isDisplayed()))
        viewInteraction.perform(click())
    }

    fun actionClickString(buttonText: String) {
        val viewInteraction =
            onView(allOf(withText(buttonText))).check(matches(isDisplayed()))
        viewInteraction.perform(click())
    }
}

fun actionTest(action: PmsListPageRobot.() -> Unit) = PmsListPageRobot().apply(action)
