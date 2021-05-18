package com.tokopedia.shop.analyticvalidator.testcase

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.shop.R
import com.tokopedia.shop.mock.ShopPageSearchProductPageMockResponseConfig
import com.tokopedia.shop.search.view.activity.ShopSearchProductActivity
import com.tokopedia.shop.search.view.adapter.viewholder.ShopSearchProductFixResultViewHolder
import com.tokopedia.test.application.espresso_component.CommonMatcher
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.TokopediaGraphqlInstrumentationTestHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.trackingoptimizer.constant.Constant
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class ShopProductSearchPageAnalyticTest {

    companion object {
        private const val SHOP_PAGE_SEARCH_PRODUCT_CLICK_DI_TOKOPEDIA_TRACKER_MATCHER_PATH = "tracker/shop/shop_page_search_product_click_di_tokopedia_tracker.json"

        private const val KEY_SHOP_ID = "SHOP_ID"
        private const val SAMPLE_SHOP_ID = "3418893"
        private const val SAMPLE_KEYWORD = "sabun"
    }

    @get:Rule
    var activityRule: IntentsTestRule<ShopSearchProductActivity> = IntentsTestRule(ShopSearchProductActivity::class.java, false, false)
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun beforeTest() {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        remoteConfig.setString(Constant.TRACKING_QUEUE_SEND_TRACK_NEW_REMOTECONFIGKEY, "true")
        gtmLogDBSource.deleteAll().toBlocking().first()
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        setupGraphqlMockResponse(ShopPageSearchProductPageMockResponseConfig())
        activityRule.launchActivity(Intent().apply {
            putExtra(KEY_SHOP_ID, SAMPLE_SHOP_ID)
        })
    }

    @Test
    fun testClickDiTokopedia() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(0, null))
        Espresso.onView(CommonMatcher.firstView(withId(R.id.editTextSearchProduct))).perform(ViewActions.typeText(SAMPLE_KEYWORD))
        waitForData(5000)
        Espresso.onView(CommonMatcher.firstView(withId(R.id.recycler_view))).perform(
                RecyclerViewActions.actionOnItemAtPosition<ShopSearchProductFixResultViewHolder>(1, ViewActions.click())
        )
        doAnalyticDebuggerTest(SHOP_PAGE_SEARCH_PRODUCT_CLICK_DI_TOKOPEDIA_TRACKER_MATCHER_PATH)
    }

    @After
    fun afterTest() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        TokopediaGraphqlInstrumentationTestHelper.deleteAllDataInDb()
    }

    private fun waitForData(ms: Long = 2000) {
        Thread.sleep(ms)
    }

    private fun doAnalyticDebuggerTest(fileName: String) {
        waitForData(200)
        assertThat(getAnalyticsWithQuery(
                gtmLogDBSource,
                context,
                fileName
        ), hasAllSuccess())
    }

}