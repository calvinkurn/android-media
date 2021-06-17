package com.tokopedia.pms.analytics

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.pms.R
import com.tokopedia.pms.paymentlist.presentation.viewholder.CommonPaymentTransferViewHolder
import com.tokopedia.test.application.espresso_component.CommonActions
import org.hamcrest.Matchers.allOf
import org.junit.Rule

class PmsListPageRobot {
    @get:Rule
    var cassavaTestRule = CassavaTestRule(false)

    fun clickHtpTest() {
        val viewInteraction = onView(allOf(withId(R.id.recycler_view))).check(matches(isDisplayed()))
        viewInteraction.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                CommonActions.clickChildViewWithId(R.id.goToHowToPay)
            )
        )
    }

    infix fun assertTest(action: PmsListPageRobot.() -> Unit) = PmsListPageRobot().apply(action)

    fun validate(fileName: String) {
        assertThat(cassavaTestRule.validate(fileName), hasAllSuccess())
    }
}

fun actionTest(action: PmsListPageRobot.() -> Unit) = PmsListPageRobot().apply(action)
