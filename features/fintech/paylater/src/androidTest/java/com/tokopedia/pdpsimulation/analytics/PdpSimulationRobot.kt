package com.tokopedia.pdpsimulation.analytics

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.pdpsimulation.test.R
import com.tokopedia.test.application.espresso_component.CommonMatcher
import org.hamcrest.MatcherAssert
import org.hamcrest.core.AllOf

class PdpSimulationRobot {


    fun clickTenure() {
        onView(withId(R.id.rvPayLaterSimulation)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                clickRecyclerViewChild(R.id.clSimulationCard)
            )
        )
    }


    fun clickInstallmentBottomSheet() {
        onView(
            CommonMatcher.firstView(
                AllOf.allOf(
                    withId(R.id.partnerTenureInfo),
                    isDisplayed()
                )
            )
        ).perform(
            click()
        )
    }

    private fun clickRecyclerViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null
        override fun getDescription() = "Click on a child view with specified id."
        override fun perform(uiController: UiController, view: View) =
            click().perform(uiController, view.findViewById(viewId))
    }


    infix fun assertTest(action: PdpSimulationRobot.() -> Unit) = PdpSimulationRobot().apply(action)

    fun hasPassedAnalytics(rule: CassavaTestRule, path: String) {
        MatcherAssert.assertThat(rule.validate(path), hasAllSuccess())
    }
}

fun actionTest(action: PdpSimulationRobot.() -> Unit) = PdpSimulationRobot().apply(action)