package com.tokopedia.promogamification.common.floating.view.fragment

import android.content.Intent
import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.promogamification.common.FloatingEggMockResponse
import com.tokopedia.promogamification.common.R
import com.tokopedia.promogamification.common.floating.view.activity.FloatingEggActivity
import com.tokopedia.promotion.common.idling.TkpdIdlingResource
import com.tokopedia.promotion.common.idling.TkpdIdlingResourceProvider
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FloatingEggButtonFragmentAnalyticsTest {

    @get:Rule
    val activityRule = ActivityTestRule(FloatingEggActivity::class.java, false, false)
    @get:Rule
    var cassavaRule = CassavaTestRule()
    var floatingIdlingResource: TkpdIdlingResource? = null
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        login()
        setupIdlingResource()
        setupGraphqlMockResponse(FloatingEggMockResponse())
        launchActivity()
    }

    @Test
    fun test_impression() {
        Espresso.onView(ViewMatchers.withId(R.id.vg_floating_egg)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.vg_floating_egg)).perform(ViewActions.click())

        val luckyEggClickQuery = "tracker/promo/promo_gamification_common/lucky_egg_trackers.json"
        assertThat(cassavaRule.validate(luckyEggClickQuery), hasAllSuccess())
    }

    private val idlingResource = object : IdlingResource {
        private var resourceCallback: IdlingResource.ResourceCallback? = null

        override fun getName() = "fragment idling resource"

        override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
            resourceCallback = callback
        }

        override fun isIdleNow(): Boolean {
            val floatingIcon = activityRule.activity.findViewById<View>(R.id.vg_floating_egg)
            val isIdle = floatingIcon != null && floatingIcon.visibility == View.VISIBLE
            if (isIdle) {
                resourceCallback?.onTransitionToIdle()
            }
            return isIdle
        }
    }

    private fun setupIdlingResource() {
        IdlingRegistry.getInstance().register(idlingResource)
        floatingIdlingResource = TkpdIdlingResourceProvider.provideIdlingResource("FloatingEgg")
        if (floatingIdlingResource != null) {
            IdlingRegistry.getInstance().register(floatingIdlingResource?.countingIdlingResource)
        }
        else {
            throw RuntimeException("No idling resource found")
        }
    }

    private fun launchActivity() {
        val intent = Intent(context, FloatingEggActivity::class.java)
        activityRule.launchActivity(intent)
    }

    @After
    fun unregisterIdlingResource() {
        floatingIdlingResource?.let {
            IdlingRegistry.getInstance().unregister(it.countingIdlingResource)
        }
        IdlingRegistry.getInstance().unregister(idlingResource)
    }

    private fun login() {
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
    }

}