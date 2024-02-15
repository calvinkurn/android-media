package com.tokopedia.tokopedianow.home.robot

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.test.application.environment.ActivityScenarioTestRule
import com.tokopedia.tokopedianow.home.presentation.activity.TokoNowHomeActivity
import com.tokopedia.tokopedianow.test.robot.BaseTestRobot
import org.hamcrest.Matchers.allOf
import com.tokopedia.unifycomponents.R as unifycomponentsR

class HomePageRobot(
    private val context: Context,
    private val activityScenarioRule: ActivityScenarioTestRule<TokoNowHomeActivity>,
): BaseTestRobot() {

    fun openHomePage() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalTokopediaNow.HOME)
        activityScenarioRule.launchActivity(intent)
    }

    fun clickCarouselChipTab(tab: String) {
        onView(allOf(withId(unifycomponentsR.id.chip_text), withText(tab))).perform(click())
    }

    fun onResumeActivity() {
        activityScenarioRule.scenario?.moveToState(Lifecycle.State.STARTED)
        activityScenarioRule.scenario?.moveToState(Lifecycle.State.RESUMED)
    }
}
