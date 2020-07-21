package com.tokopedia.topupbills

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.topupbills.telco.prepaid.activity.TelcoPrepaidActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class TelcoPrepaidInstrumentTest {
    @get:Rule
    var mActivityRule: ActivityTestRule<TelcoPrepaidActivity> = ActivityTestRule(TelcoPrepaidActivity::class.java)

    @Test
    fun show_contents_pdp_telco_not_login() {
        onView(withId(R.id.page_container)).check(matches(isDisplayed()))
        Thread.sleep(1000)
        onView(withId(R.id.telco_input_number)).check(matches(isDisplayed()))
        onView(withId(R.id.view_pager)).check(matches(isDisplayed()))
    }

    @Test
    fun click_done_keyboard_fav_number() {
        Thread.sleep(1000)
        onView(withId(R.id.telco_input_number)).perform(click())
        onView(withId(R.id.edit_text_search)).perform(ViewActions.typeText(VALID_PHONE_NUMBER), ViewActions.pressImeActionButton())
        onView(withId(R.id.ac_input_number)).check(matches(ViewMatchers.withText(VALID_PHONE_NUMBER)))
    }

    companion object {
        private const val VALID_PHONE_NUMBER = "08123232323"
    }

}