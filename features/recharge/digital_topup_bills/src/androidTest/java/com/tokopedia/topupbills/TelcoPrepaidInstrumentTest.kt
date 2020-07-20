package com.tokopedia.topupbills

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.topupbills.telco.view.activity.TelcoPrepaidActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class TelcoPrepaidInstrumentTest {
    @get:Rule
    var mActivityRule: ActivityTestRule<TelcoPrepaidActivity> = ActivityTestRule(TelcoPrepaidActivity::class.java)

    @Test
    fun testHomeLayout() {
        onView(withId(R.id.page_container)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.loading_telco_shimmering)).check(ViewAssertions.matches(isDisplayed()))
        Thread.sleep(1000)
        onView(withId(R.id.telco_input_number)).check(ViewAssertions.matches(isDisplayed()))
    }
}