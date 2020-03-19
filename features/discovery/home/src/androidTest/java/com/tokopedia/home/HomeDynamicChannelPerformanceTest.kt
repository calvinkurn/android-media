package com.tokopedia.home

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class HomeDynamicChannelPerformanceTest {

    @get:Rule
    var activityRule: ActivityTestRule<InstrumentationHomeTestActivity> = ActivityTestRule(InstrumentationHomeTestActivity::class.java)

    @Test
    @Throws(Exception::class)
    fun testScrollHome() {
        Thread.sleep(4000)
        onView(withId(R.id.home_fragment_recycler_view))
                .perform(ViewActions.swipeUp())
        onView(withId(R.id.home_fragment_recycler_view))
                .perform(ViewActions.swipeUp())
        onView(withId(R.id.home_fragment_recycler_view))
                .perform(ViewActions.swipeUp())
        onView(withId(R.id.home_fragment_recycler_view))
                .perform(ViewActions.swipeUp())
        Thread.sleep(10000)
    }
}
