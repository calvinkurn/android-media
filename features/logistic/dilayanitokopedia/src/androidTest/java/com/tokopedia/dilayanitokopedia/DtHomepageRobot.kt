package com.tokopedia.dilayanitokopedia

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import com.tokopedia.dilayanitokopedia.ui.home.DtHomeActivity
import com.tokopedia.test.application.matcher.RecyclerViewMatcher
import java.util.regex.Pattern.matches

fun dtHomepage(func: DtHomepageRobot.() -> Unit) = DtHomepageRobot().apply(func)

class DtHomepageRobot {

    fun launch(mActivityTestRule: ActivityTestRule<DtHomeActivity>) {
        mActivityTestRule.launchActivity(Intent())
        waitForData(5000)
    }

    fun impressFirstWidget() {
        Espresso.onView(RecyclerViewMatcher(R.id.rv_home).atPositionOnView(1, R.id.rv_product))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    fun clickFirstItemInFirstWidget() {
        Espresso.onView(RecyclerViewMatcher(R.id.rv_home).atPositionOnView(1, R.id.rv_product))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(ViewActions.click())

        waitForData(3000)
    }

    private fun waitForData(millis: Long = 500L) {
        Thread.sleep(millis)
    }

    infix fun validateAnalytics(func: ResultRobot.() -> Unit): ResultRobot {
        return ResultRobot.apply(func)
    }
}
