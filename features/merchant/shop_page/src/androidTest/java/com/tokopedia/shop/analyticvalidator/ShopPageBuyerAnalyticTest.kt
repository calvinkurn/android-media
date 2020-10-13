package com.tokopedia.shop.analyticvalidator

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.ComponentNameMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.shop.R
import com.tokopedia.shop.mock.ShopPageWithHomeTabMockResponseConfig
import com.tokopedia.shop.pageheader.presentation.activity.ShopPageActivity
import com.tokopedia.shop.pageheader.presentation.activity.ShopPageActivity.Companion.SHOP_ID
import com.tokopedia.shop.sort.view.activity.ShopProductSortActivity
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.espresso_component.CommonMatcher.firstView
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.AllOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * @author by yoasfs on 07/07/20
 */
class ShopPageBuyerAnalyticTest {

    companion object {
        private const val SHOP_PAGE_JOURNEY_TRACKER_MATCHER_PATH = "tracker/shop/shop_page_journey_tracker.json"
        private const val SHOP_PAGE_CLICK_TABS_TRACKER_MATCHER_PATH = "tracker/shop/shop_page_click_tabs_tracker.json"
    }

    @get:Rule
    var activityRule: IntentsTestRule<ShopPageActivity> = IntentsTestRule(ShopPageActivity::class.java, false, false)


    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)
    private val SAMPLE_SHOP_ID = "3418893"

    @Before
    fun beforeTest() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        setupGraphqlMockResponse(ShopPageWithHomeTabMockResponseConfig())
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        val intent = Intent().apply {
            putExtra(SHOP_ID, SAMPLE_SHOP_ID)
        }
        activityRule.launchActivity(intent)
    }

    @Test
    fun testShopPageJourney() {
        waitForData(2000)
        testClickSearchBar()
        testClickTabs()
        testSelectSortOption()
        doAnalyticDebuggerTest(SHOP_PAGE_JOURNEY_TRACKER_MATCHER_PATH)
    }

    private fun testClickTabs() {
        Espresso.onView(firstView(withId(R.id.tabLayout)))
                .perform(CommonActions.selectTabLayoutPosition(0))
        Espresso.onView(firstView(withId(R.id.tabLayout)))
                .perform(CommonActions.selectTabLayoutPosition(1))
        Espresso.onView(firstView(withId(R.id.tabLayout)))
                .perform(CommonActions.selectTabLayoutPosition(2))
        Espresso.onView(firstView(withId(R.id.tabLayout)))
                .perform(CommonActions.selectTabLayoutPosition(3))
        doAnalyticDebuggerTest(SHOP_PAGE_CLICK_TABS_TRACKER_MATCHER_PATH)
    }

    private fun testSelectSortOption() {
        val mockIntentData = Intent().apply {
            putExtra(ShopProductSortActivity.SORT_ID, "1")
            putExtra(ShopProductSortActivity.SORT_NAME, "Terbaru")
        }
        Intents.intending(IntentMatchers.hasComponent(
                ComponentNameMatchers.hasClassName("com.tokopedia.shop.sort.view.activity.ShopProductSortActivity"))
        ).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, mockIntentData))
        Espresso.onView(firstView(withId(R.id.tabLayout)))
                .perform(CommonActions.selectTabLayoutPosition(1))
        waitForData(2000)
        Espresso.onView(AllOf.allOf(withText("Urutkan"), isDescendantOfA(withId(R.id.sort_filter_items_wrapper))))
                .check(matches(isDisplayed())).perform(click())
    }

    @After
    fun afterTest() {
        gtmLogDBSource.deleteAll().toBlocking().first()
    }

    private fun waitForData(ms: Long) {
        Thread.sleep(ms)
    }

    private fun testClickSearchBar() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(0, null));
        Espresso.onView(firstView(withId(R.id.searchBarText)))
                .perform(ViewActions.click())
    }

    private fun doAnalyticDebuggerTest(fileName: String) {
        waitForData(100)
        assertThat(
                getAnalyticsWithQuery(gtmLogDBSource, context, fileName),
                hasAllSuccess()
        )
    }

}