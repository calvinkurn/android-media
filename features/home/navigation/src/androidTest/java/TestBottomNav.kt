package com.tokopedia.navigation

import android.view.View
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.navigation.presentation.customview.LottieBottomNavbar
import com.tokopedia.navigation.test.R
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test

class TestBottomNav {
    @get:Rule
    var activityRule: ActivityTestRule<TestBottomNavActivity> = ActivityTestRule(TestBottomNavActivity::class.java)

    val context = InstrumentationRegistry.getInstrumentation().context

    val POSITION_HOME = 0
    val POSITION_FEED = 1
    val POSITION_OS = 2
    val POSITION_CART = 3
    val POSITION_ACCOUNT = 4

    @Test
    fun testWhenPageInflatedWithoutNotification_bottomNavHome_showAllFiveComponent() {
        onView(allOf(withTagValue(Matchers.`is`(getLottieAnimationViewId(POSITION_HOME))))).check(matches(isDisplayed()))
        onView(allOf(withTagValue(Matchers.`is`(getLottieAnimationViewId(POSITION_FEED))))).check(matches(isDisplayed()))
        onView(allOf(withTagValue(Matchers.`is`(getLottieAnimationViewId(POSITION_OS))))).check(matches(isDisplayed()))
        onView(allOf(withTagValue(Matchers.`is`(getLottieAnimationViewId(POSITION_CART))))).check(matches(isDisplayed()))
        onView(allOf(withTagValue(Matchers.`is`(getLottieAnimationViewId(POSITION_ACCOUNT))))).check(matches(isDisplayed()))

        onView(allOf(withTagValue(Matchers.`is`(getTitleTextViewId(POSITION_HOME))))).check(matches(isDisplayed()))
        onView(allOf(withTagValue(Matchers.`is`(getTitleTextViewId(POSITION_FEED))))).check(matches(isDisplayed()))
        onView(allOf(withTagValue(Matchers.`is`(getTitleTextViewId(POSITION_OS))))).check(matches(isDisplayed()))
        onView(allOf(withTagValue(Matchers.`is`(getTitleTextViewId(POSITION_CART))))).check(matches(isDisplayed()))
        onView(allOf(withTagValue(Matchers.`is`(getTitleTextViewId(POSITION_ACCOUNT))))).check(matches(isDisplayed()))

        onView(allOf(withId(R.id.notification_badge),
                withTagValue(Matchers.`is`(getBadgeTextViewId(POSITION_HOME))))).check(matches(not(isDisplayed())))
        onView(allOf(withId(R.id.notification_badge),
                withTagValue(Matchers.`is`(getBadgeTextViewId(POSITION_FEED))))).check(matches(not(isDisplayed())))
        onView(allOf(withId(R.id.notification_badge),
                withTagValue(Matchers.`is`(getBadgeTextViewId(POSITION_OS))))).check(matches(not(isDisplayed())))
        onView(allOf(withId(R.id.notification_badge),
                withTagValue(Matchers.`is`(getBadgeTextViewId(POSITION_CART))))).check(matches(not(isDisplayed())))
        onView(allOf(withId(R.id.notification_badge),
                withTagValue(Matchers.`is`(getBadgeTextViewId(POSITION_ACCOUNT))))).check(matches(not(isDisplayed())))
    }

    @Test
    fun testWhenItemClicked_bottomNavItem_shouldFinishAnyAnimationAfter2sec() {
        onView(allOf(withTagValue(Matchers.`is`(getLottieAnimationViewId(POSITION_HOME))))).perform(click())
        onView(allOf(withTagValue(Matchers.`is`(getLottieAnimationViewId(POSITION_FEED))))).perform(click())
        onView(allOf(withTagValue(Matchers.`is`(getLottieAnimationViewId(POSITION_OS))))).perform(click())
        onView(allOf(withTagValue(Matchers.`is`(getLottieAnimationViewId(POSITION_CART))))).perform(click())
        onView(allOf(withTagValue(Matchers.`is`(getLottieAnimationViewId(POSITION_ACCOUNT))))).perform(click())

        Thread.sleep(2000)

        val lottieBottomNavbar: LottieBottomNavbar =
                activityRule.activity.findViewById(R.id.bottom_navbar)
        val linearLayout = lottieBottomNavbar.getChildAt(0)
        val lottieAnimation1 = linearLayout.findViewWithTag<LottieAnimationView>(getLottieAnimationViewId(POSITION_HOME))
        assertThat(lottieAnimation1.progress, Matchers.greaterThan(0.9f))

        val lottieAnimation2 = linearLayout.findViewWithTag<LottieAnimationView>(getLottieAnimationViewId(POSITION_FEED))
        assertThat(lottieAnimation2.progress, Matchers.greaterThan(0.9f))

        val lottieAnimation3 = linearLayout.findViewWithTag<LottieAnimationView>(getLottieAnimationViewId(POSITION_OS))
        assertThat(lottieAnimation3.progress, Matchers.greaterThan(0.9f))

        val lottieAnimation4 = linearLayout.findViewWithTag<LottieAnimationView>(getLottieAnimationViewId(POSITION_CART))
        assertThat(lottieAnimation4.progress, Matchers.greaterThan(0.9f))

        val lottieAnimation5 = linearLayout.findViewWithTag<LottieAnimationView>(getLottieAnimationViewId(POSITION_ACCOUNT))
        assertThat(lottieAnimation5.progress, Matchers.greaterThan(0.9f))
    }

    @Test
    fun testWhenSelectAnItem_theLastSelectedWillFadeOut_andTheCurrentSelectedWillFadeIn() {
        onView(allOf(withTagValue(Matchers.`is`(getLottieAnimationViewId(POSITION_OS))))).perform(click())
        onView(allOf(withTagValue(Matchers.`is`(getLottieAnimationViewId(POSITION_CART))))).perform(click())

        Thread.sleep(3000)

        val lottieBottomNavbar: LottieBottomNavbar =
                activityRule.activity.findViewById(R.id.bottom_navbar)

        val linearLayout = lottieBottomNavbar.getChildAt(0)
        val lottieAnimation3 = linearLayout.findViewWithTag<LottieAnimationView>(getLottieAnimationViewId(POSITION_OS))
        assertThat(lottieAnimation3.speed, Matchers.greaterThan(0f))

        val lottieAnimation4 = linearLayout.findViewWithTag<LottieAnimationView>(getLottieAnimationViewId(POSITION_CART))
        assertThat(lottieAnimation4.speed, Matchers.greaterThan(0f))
    }

    @Test
    fun testWhenBadgeUpdatedWith0_bottomNavItem_shouldShowBadgeTextViewWithoutNumber() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val lottieBottomNavbar: LottieBottomNavbar =
                    activityRule.activity.findViewById(R.id.bottom_navbar)
            lottieBottomNavbar.setBadge(0, POSITION_HOME)
        }
        onView(withTagValue(Matchers.`is`(getLottieAnimationViewId(POSITION_HOME)))).check(matches(isDisplayed()))
        onView(withTagValue(Matchers.`is`(getBadgeTextViewId(POSITION_HOME)))).check(matches(isDisplayed()))
        onView(withTagValue(Matchers.`is`(getBadgeTextViewId(POSITION_HOME)))).check(matches(withText("")))

        val lottieBottomNavbar: LottieBottomNavbar =
                activityRule.activity.findViewById(R.id.bottom_navbar)
        val textViewBadgeNumber: TextView = lottieBottomNavbar.getChildAt(POSITION_HOME).findViewWithTag(getBadgeTextViewId(POSITION_HOME))
    }

    @Test
    fun testWhenBadgeUpdatedWithAbove0_bottomNavItem_shouldShowBadgeTextViewWithNumberInPosition() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val lottieBottomNavbar: LottieBottomNavbar =
                    activityRule.activity.findViewById(R.id.bottom_navbar)
            lottieBottomNavbar.setBadge(88, POSITION_HOME)
        }
        onView(withTagValue(Matchers.`is`(getLottieAnimationViewId(POSITION_HOME)))).check(matches(isDisplayed()))
        onView(withTagValue(Matchers.`is`(getBadgeTextViewId(POSITION_HOME)))).check(matches(isDisplayed()))
        onView(withTagValue(Matchers.`is`(getBadgeTextViewId(POSITION_HOME)))).check(matches(withText("88")))
    }

    @Test
    fun testWhenBadgeRemoved_bottomNavItem_shouldNotShowAnyBadge() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val lottieBottomNavbar: LottieBottomNavbar =
                    activityRule.activity.findViewById(R.id.bottom_navbar)
            lottieBottomNavbar.setBadge(88, POSITION_HOME)

            //remove
            lottieBottomNavbar.setBadge(visibility = View.INVISIBLE, iconPosition = POSITION_HOME)
        }
        onView(withTagValue(Matchers.`is`(getLottieAnimationViewId(POSITION_HOME)))).check(matches(isDisplayed()))
        onView(withTagValue(Matchers.`is`(getBadgeTextViewId(POSITION_HOME)))).check(matches(not(isDisplayed())))
    }

    private fun getLottieAnimationViewId(id: Int) = context.getString(R.string.tag_lottie_animation_view)+id
    private fun getBadgeTextViewId(id: Int) = context.getString(R.string.tag_badge_textview)+id
    private fun getTitleTextViewId(id: Int) = context.getString(R.string.tag_title_textview)+id

    private fun waitForData() {
        Thread.sleep(1000000)
    }

}
