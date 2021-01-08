package com.tokopedia.shop.open.testcase

import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.shop.open.presentation.view.activity.ShopOpenRevampActivity
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.TokopediaGraphqlInstrumentationTestHelper
import com.tokopedia.trackingoptimizer.constant.Constant
import com.tokopedia.shop.open.R
import com.tokopedia.shop.open.mock.ShopOpenMockResponseConfig
import com.tokopedia.test.application.espresso_component.CommonMatcher.firstView
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShopOpenAnalyticTest {

    companion object {
        private const val SHOP_OPEN_LANDING_MATCHER_PATH = "tracker/shop_open/shop_open_landing_tracker.json"
        private const val SHOP_OPEN_SHOP_REGISTRATION_MATCHER_PATH = "tracker/shop_open/shop_open_shop_registration_tracker.json"
    }

    @get:Rule
    var activityRule: IntentsTestRule<ShopOpenRevampActivity> = IntentsTestRule(ShopOpenRevampActivity::class.java, false, false)
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun beforeTest() {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        remoteConfig.setString(Constant.TRACKING_QUEUE_SEND_TRACK_NEW_REMOTECONFIGKEY, "true")
        gtmLogDBSource.deleteAll().toBlocking().first()
        setupGraphqlMockResponse(ShopOpenMockResponseConfig())
        InstrumentationAuthHelper.loginInstrumentationTestUser2()
        activityRule.launchActivity(Intent())
    }

    @After
    fun afterTest() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        TokopediaGraphqlInstrumentationTestHelper.deleteAllDataInDb()
    }

    private fun doAnalyticDebuggerTest(fileName: String) {
        assertThat(
                getAnalyticsWithQuery(gtmLogDBSource, context, fileName),
                hasAllSuccess()
        )
    }

    @Test
    fun testShopOpenResultJourney() {
        waitForData(5000)
        testInputShopNameAndDomain()
    }

    fun testInputShopNameAndDomain() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(0, null));
        Espresso.onView(firstView(withId(R.id.btn_back_input_shop)))
                .perform(click())

        onView(withText("Batal")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());



//        Espresso.onView(allOf(withId(R.id.text_field_input),
//                ViewMatchers.isDescendantOfA(withId(R.id.text_input_shop_open_revamp_shop_name))))
//                .perform(typeText("hello world"), closeSoftKeyboard())
//        waitForData(2000)
//
//        Espresso.onView(allOf(withId(R.id.text_field_input),
//                ViewMatchers.isDescendantOfA(withId(R.id.text_input_shop_open_revamp_domain_name))))
//                .perform(replaceText("helloworld"))
//        waitForData(2000)
//
//        Espresso.onView(firstView(withId(R.id.shop_registration_button)))
//                .perform(click())
    }

    private fun waitForData(ms: Long) {
        Thread.sleep(ms)
    }

    private fun validateTracker() {
        activityRule.activity.finish()
        waitForData(5000)
        doAnalyticDebuggerTest(SHOP_OPEN_LANDING_MATCHER_PATH)
        doAnalyticDebuggerTest(SHOP_OPEN_SHOP_REGISTRATION_MATCHER_PATH)
    }
}