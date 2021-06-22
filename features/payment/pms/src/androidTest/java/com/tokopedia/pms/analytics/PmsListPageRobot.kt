package com.tokopedia.pms.analytics

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.pms.R
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.espresso_component.CommonMatcher.firstView
import kotlinx.android.synthetic.main.fragment_change_bank_account.*
import org.hamcrest.Matchers.allOf
import org.junit.Rule

class PmsListPageRobot {
    @get:Rule
    var cassavaTestRule = CassavaTestRule(false)

    fun clickHtpTest(position: Int) {
       /* onView(allOf(withId(com.tokopedia.unifycomponents.R.id.bottom_sheet_close)))
            .check(matches(isDisplayed())).perform(click())*/
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

    fun clickWaitingCardTest(position: Int) {
        val viewInteraction =
            onView(allOf(withId(R.id.recycler_view))).check(matches(isDisplayed()))
        viewInteraction.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position,
                CommonActions.clickChildViewWithId(R.id.waitingPaymentCard)
            )
        )
    }

    infix fun assertTest(action: PmsListPageRobot.() -> Unit) = PmsListPageRobot().apply(action)

    fun validate(queryId: String) {
        assertThat(cassavaTestRule.validate(queryId), hasAllSuccess())
    }

    fun actionClickView(resId: Int) {
        val viewInteraction =
            onView(allOf(withId(resId))).check(matches(isDisplayed()))
        viewInteraction.perform(click())
    }

    fun actionClickViewFromText(text: Int) {
        val viewInteraction =
            onView(allOf(withText(text))).check(matches(isDisplayed()))
        viewInteraction.perform(click())
    }
}

fun actionTest(action: PmsListPageRobot.() -> Unit) = PmsListPageRobot().apply(action)
