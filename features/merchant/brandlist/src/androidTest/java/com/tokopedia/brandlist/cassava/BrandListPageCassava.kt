package com.tokopedia.brandlist.cassava

import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_category.presentation.activity.BrandlistActivity
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder.*
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.officialstore.extension.selectTabAtPosition
import com.tokopedia.test.application.assertion.topads.TopAdsVerificationTestReportUtil
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.espresso_component.CommonMatcher
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 1/28/21.
 */
class BrandListPageCassava {

    companion object{
        private const val TAG = "BrandListPageAnalyticsTest"
        private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME = "tracker/official_store/brandlist_page.json"
    }

    @get:Rule
    var activityRule = ActivityTestRule(BrandlistActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun setup() {
        gtmLogDBSource.deleteAll().subscribe()
        setupGraphqlMockResponse(BrandListPageMockResponse())
        activityRule.launchActivity(Intent(InstrumentationRegistry.getInstrumentation().targetContext, BrandlistActivity::class.java))
    }

    @After
    fun dispose(){
        gtmLogDBSource.deleteAll().subscribe()
    }

    @Test
    fun testBrandList() {
        initTest()
        doActivityTest()
        doBrandlistCassavaTest()
        addDebugEnd()
    }

    private fun initTest() {
        InstrumentationAuthHelper.clearUserSession()
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        waitForData()
    }

    private fun waitForData() {
        Thread.sleep(5000)
    }

    private fun addDebugEnd() {
        Thread.sleep(5000)
    }

    private fun doActivityTest() {
        // 2. scroll and click item at OS
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val itemCount = recyclerView.adapter?.itemCount ?: 0

        waitForData()

        // 1. click category OS
        Espresso.onView(withId(R.id.tablayout)).perform(selectTabAtPosition(0))

        Espresso.onView(CommonMatcher.firstView(withId(R.id.recycler_view))).perform(ViewActions.swipeDown())

        for (i in 0 until itemCount) {
            scrollRecyclerViewToPosition(recyclerView, i)
            checkProductOnDynamicChannel(recyclerView, i)
        }

        // 3. Click Searchbar
        Espresso.onView(withId(R.id.layout_search)).perform(click())

        activityRule.activity.finish()
        logTestMessage("Done UI Test")
    }

    private fun doBrandlistCassavaTest() {
        waitForData()
        //worked
        MatcherAssert.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME),
                hasAllSuccess())
    }

    private fun scrollRecyclerViewToPosition(homeRecyclerView: RecyclerView, position: Int) {
        val layoutManager = homeRecyclerView.layoutManager as GridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset   (position, 400) }
    }

    private fun logTestMessage(message: String) {
        TopAdsVerificationTestReportUtil.writeTopAdsVerificatorLog(activityRule.activity, message)
        Log.d(TAG, message)
    }

    private fun checkProductOnDynamicChannel(officialStoreRecyclerView: RecyclerView, i: Int) {
        when (val viewHolder = officialStoreRecyclerView.findViewHolderForAdapterPosition(i)) {
            is FeaturedBrandViewHolder -> {
                Espresso.onView(withId(R.id.tv_expand_button)).perform(click())
                Thread.sleep(2000)
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.rv_featured_brand, 0)
            }
            is PopularBrandViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.rv_popular_brand, 0)
            }
            is NewBrandViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.rv_new_brand, 0)
            }
            is AllBrandGroupHeaderViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.rv_groups_chip, 0)
            }
            is AllBrandViewHolder -> {
                Espresso.onView(CoreMatchers.allOf(ViewMatchers.isDisplayed(), withId(R.id.recycler_view)))
                        .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(i, click()))
            }
        }
    }
}