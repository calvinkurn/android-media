package com.tokopedia.home_wishlist.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.test.espresso.Espresso
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.home_wishlist.component.HasComponent
import com.tokopedia.home_wishlist.di.DaggerWishlistComponent
import com.tokopedia.home_wishlist.di.WishlistComponent
import com.tokopedia.home_wishlist.mock.WishlistMockData
import com.tokopedia.home_wishlist.test.R
import com.tokopedia.home_wishlist.view.fragment.WishlistFragment
import com.tokopedia.home_wishlist.view.fragment.WishlistFragment.Companion.COACH_MARK_TAG
import com.tokopedia.home_wishlist.view.viewholder.WishlistItemViewHolder
import com.tokopedia.test.application.assertion.topads.TopAdsVerificationTestReportUtil
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.espresso_component.CommonMatcher.firstView
import com.tokopedia.test.application.util.InstrumentationAuthHelper.clearUserSession
import com.tokopedia.test.application.util.InstrumentationAuthHelper.loginInstrumentationTestUser1
import com.tokopedia.test.application.util.RestMockUtil
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.test.application.util.setupRestMockResponse
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
private const val TAG = "CassavaWishlistTest"


private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_WISHLIST_ITEM = "tracker/wishlist/wishlist_item.json"

class CassavaWishlistTest {

    private val SHOWCASE_PREFERENCES = "show_case_pref"

    @get:Rule
    var activityRule = object: ActivityTestRule<InstrumentationWishlistTestActivity>(InstrumentationWishlistTestActivity::class.java) {
        override fun beforeActivityLaunched() {
            gtmLogDBSource.deleteAll().subscribe()
            disableCoachMark()
            super.beforeActivityLaunched()
            loginInstrumentationTestUser1()
            setupGraphqlMockResponse(WishlistMockData())
            setupMockFromRestResponse()
        }
    }

    private fun setupMockFromRestResponse() {
        setupRestMockResponse {
            addMockResponse(WishlistMockData.KEY_CONTAINS_BANNER,
                    RestMockUtil.getJsonFromResource("response_mock_data_wishlist_banner.json"),
                    MockModelConfig.FIND_BY_CONTAINS)
        }
    }

    private fun disableCoachMark(){
        val sharedPrefs = context.getSharedPreferences(SHOWCASE_PREFERENCES, Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean(
                COACH_MARK_TAG, true).apply()
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun resetAll() {
        gtmLogDBSource.deleteAll().subscribe()
    }

    @Test
    fun testWishlistImpressionAndClickLogin() {
        initTestWithLogin()

        scrollToItemAndBanner()

        doCassavaCheck()

        onFinishTest()

        addDebugEnd()
    }

    private fun initTest() {
        clearUserSession()
        waitForData()
    }

    private fun scrollToItemAndBanner() {
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val itemCount = recyclerView.adapter?.itemCount ?: 0
        for (i in 0 until itemCount) {
            scrollHomeRecyclerViewToPosition(recyclerView, i)
            checkItemsAndBanner(recyclerView, i)
        }
        activityRule.activity.finish()
        logTestMessage("Done UI Test")
    }

    private fun scrollHomeRecyclerViewToPosition(recyclerView: RecyclerView, position: Int) {
        val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset   (position, 400) }
    }


    private fun doCassavaCheck() {
        waitForData()
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_WISHLIST_ITEM),
                hasAllSuccess())
    }

    private fun checkItemsAndBanner(homeRecyclerView: RecyclerView, i: Int) {
        val viewholder = homeRecyclerView.findViewHolderForAdapterPosition(i)
        when (viewholder) {
            is WishlistItemViewHolder -> {
                val holderName = "WishlistItemViewHolder"
                logTestMessage("VH $holderName")
                clickWishlistItem(viewholder.itemView, holderName, i)
            }
        }
    }

    private fun clickWishlistItem(view: View, viewComponent: String, itemPos: Int) {
        try {
            Espresso.onView(firstView(ViewMatchers.withId(R.id.wishlist_card)))
                    .perform(ViewActions.click())
            logTestMessage("Click SUCCESS wishlist item")
        } catch (e: PerformException) {
            e.printStackTrace()
            logTestMessage("Click FAILED wishlist item")
        }
    }

    private fun logTestMessage(message: String) {
        TopAdsVerificationTestReportUtil.writeTopAdsVerificatorLog(activityRule.activity, message)
        Log.d(TAG, message)
    }


    private fun initTestWithLogin() {
        initTest()
    }

    private fun waitForData() {
        Thread.sleep(5000)
    }

    private fun addDebugEnd() {
        Thread.sleep(5000)
    }

    private fun onFinishTest() {
        gtmLogDBSource.deleteAll().subscribe()
    }

}