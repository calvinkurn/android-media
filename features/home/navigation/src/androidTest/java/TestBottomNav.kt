package com.tokopedia.navigation

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.navigation.presentation.customview.LottieBottomNavbar
import com.tokopedia.navigation.test.R
import com.tokopedia.test.application.espresso_component.CommonMatcher.withTagStringValue
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

    @Test
    fun testWhenPageInflatedWithoutNotification_bottomNavHome_showAllFiveComponent() {
        onView(withTagStringValue(getLottieAnimationViewId(POSITION_HOME))).check(matches(isDisplayed()))
        onView(withTagStringValue(getLottieAnimationViewId(POSITION_FEED))).check(matches(isDisplayed()))
        onView(withTagStringValue(getLottieAnimationViewId(POSITION_OS))).check(matches(isDisplayed()))

        onView(withTagStringValue(getTitleTextViewId(POSITION_HOME))).check(matches(isDisplayed()))
        onView(withTagStringValue(getTitleTextViewId(POSITION_FEED))).check(matches(isDisplayed()))
        onView(withTagStringValue(getTitleTextViewId(POSITION_OS))).check(matches(isDisplayed()))

        onView(allOf(withId(R.id.notification_badge),
                withTagStringValue(getBadgeTextViewId(POSITION_HOME)))).check(matches(not(isDisplayed())))
        onView(allOf(withId(R.id.notification_badge),
                withTagStringValue(getBadgeTextViewId(POSITION_FEED)))).check(matches(not(isDisplayed())))
        onView(allOf(withId(R.id.notification_badge),
                withTagStringValue(getBadgeTextViewId(POSITION_OS)))).check(matches(not(isDisplayed())))
    }

    @Test
    fun testWhenSelectAnItem_theLastSelectedWillFadeOut_andTheCurrentSelectedWillFadeIn() {
        val currentSelectedTag = getLottieAnimationViewId(POSITION_OS)
        val newSelectedTag = getLottieAnimationViewId(POSITION_FEED)

        onView(withTagStringValue(currentSelectedTag)).perform(click())
        onView(withTagStringValue(newSelectedTag)).perform(click())

        Thread.sleep(3000)

        val lottieBottomNavbar: LottieBottomNavbar =
                activityRule.activity.findViewById(R.id.bottom_navbar)

        val linearLayout = lottieBottomNavbar.getChildAt(0)

        //assert both currentSelectedItem and nextSelectedItem is animating
        val lottieAnimation3 = linearLayout.findViewWithTag<LottieAnimationView>(currentSelectedTag)
        assertThat(lottieAnimation3.speed, Matchers.greaterThan(0f))

        val lottieAnimation4 = linearLayout.findViewWithTag<LottieAnimationView>(newSelectedTag)
        assertThat(lottieAnimation4.speed, Matchers.greaterThan(0f))
    }

    @Test
    fun testWhenBadgeUpdatedWith0_bottomNavItem_shouldShowBadgeTextViewWithoutNumber() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val lottieBottomNavbar: LottieBottomNavbar =
                    activityRule.activity.findViewById(R.id.bottom_navbar)
            lottieBottomNavbar.setBadge(0, POSITION_HOME)
        }
        onView(withTagStringValue(getLottieAnimationViewId(POSITION_HOME))).check(matches(isDisplayed()))
        onView(withTagStringValue(getBadgeTextViewId(POSITION_HOME))).check(matches(isDisplayed()))
        onView(withTagStringValue(getBadgeTextViewId(POSITION_HOME))).check(matches(withText("")))
    }

    @Test
    fun testWhenBadgeUpdatedWithAbove0_bottomNavItem_shouldShowBadgeTextViewWithNumberInPosition() {
        val badgeTestValue = 88
        val selectedPosition = POSITION_HOME
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val lottieBottomNavbar: LottieBottomNavbar =
                    activityRule.activity.findViewById(R.id.bottom_navbar)
            lottieBottomNavbar.setBadge(badgeTestValue, selectedPosition)
        }
        onView(withTagStringValue(getLottieAnimationViewId(selectedPosition))).check(matches(isDisplayed()))
        onView(withTagStringValue(getBadgeTextViewId(selectedPosition))).check(matches(isDisplayed()))
        onView(withTagStringValue(getBadgeTextViewId(selectedPosition))).check(matches(withText(badgeTestValue.toString())))
    }

    @Test
    fun testWhenBadgeRemoved_bottomNavItem_shouldNotShowAnyBadge() {
        val badgeTestValue = 88
        val selectedPosition = POSITION_HOME

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val lottieBottomNavbar: LottieBottomNavbar =
                    activityRule.activity.findViewById(R.id.bottom_navbar)
            lottieBottomNavbar.setBadge(badgeTestValue, selectedPosition)

            //remove
            lottieBottomNavbar.setBadge(visibility = View.INVISIBLE, iconPosition = selectedPosition)
        }
        onView(withTagStringValue(getLottieAnimationViewId(selectedPosition))).check(matches(isDisplayed()))
        onView(withTagStringValue(getBadgeTextViewId(selectedPosition))).check(matches(not(isDisplayed())))
    }

    private fun getLottieAnimationViewId(id: Int) = context.getString(R.string.tag_lottie_animation_view)+id
    private fun getBadgeTextViewId(id: Int) = context.getString(R.string.tag_badge_textview)+id
    private fun getTitleTextViewId(id: Int) = context.getString(R.string.tag_title_textview)+id

    private fun waitForData() {
        Thread.sleep(1000000)
    }
}
