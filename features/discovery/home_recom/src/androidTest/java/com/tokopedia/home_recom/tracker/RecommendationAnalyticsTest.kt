package com.tokopedia.home_recom.tracker

import android.net.Uri
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.home_recom.HomeRecommendationActivity
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.ui.HomeRecomMockValueHelper
import com.tokopedia.home_recom.view.viewholder.ProductInfoViewHolder
import com.tokopedia.home_recom.view.viewholder.RecommendationCarouselViewHolder
import com.tokopedia.home_recom.view.viewholder.RecommendationItemViewHolder
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.test.application.assertion.topads.TopAdsVerificationTestReportUtil
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 1/11/21.
 */
@CassavaTest
class RecommendationAnalyticsTest {

    companion object {
        private const val TAG = "RecommendationAnalyticsTest"
        private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME = "tracker/recom/recom.json"
    }

    @get:Rule
    var activityRule = IntentsTestRule<HomeRecommendationActivity>(
        HomeRecommendationActivity::class.java,
        false,
        false
    )

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        HomeRecomMockValueHelper.setupRemoteConfig()
        setupGraphqlMockResponse(RecommendationMockResponseConfig())
        activityRule.launchActivity(
            HomeRecommendationActivity.newInstance(
                InstrumentationRegistry.getInstrumentation().targetContext,
                "547113763"
            ).apply {
                data = Uri.parse("tokopedia://rekomendasi/547113763/?ref=googleshopping")
            }
        )
    }

    @After
    fun dispose() {
    }

    @Test
    fun testRecomCassava() {
        RecommendationCassavaTest {
            initTest()
            doActivityTest()
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME)
        }
    }

    private fun initTest() {
        InstrumentationAuthHelper.clearUserSession()
        waitForData()
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
    }

    private fun waitForData() {
        Thread.sleep(5000)
    }

    private fun addDebugEnd() {
        Thread.sleep(5000)
    }

    private fun doActivityTest() {
        val homeRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val itemCount = homeRecyclerView.adapter?.itemCount ?: 0
        for (i in 0 until itemCount) {
            scrollRecyclerViewToPosition(homeRecyclerView, i)
            checkProduct(homeRecyclerView, i)
        }
        activityRule.activity.finish()
        logTestMessage("Done UI Test")
    }

    private fun scrollRecyclerViewToPosition(homeRecyclerView: RecyclerView, position: Int) {
        val layoutManager = homeRecyclerView.layoutManager as StaggeredGridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 400) }
    }

    private fun logTestMessage(message: String) {
        TopAdsVerificationTestReportUtil.writeTopAdsVerificatorLog(activityRule.activity, message)
        Log.d(TAG, message)
    }

    private fun checkProduct(homeRecyclerView: RecyclerView, i: Int) {
        when (val viewHolder = homeRecyclerView.findViewHolderForAdapterPosition(i)) {
            is ProductInfoViewHolder -> {
                val holderName = "ProductInfoViewHolder"
                logTestMessage("VH $holderName")
                Espresso.onView(ViewMatchers.withId(R.id.recycler_view))
                    .perform(
                        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                            i,
                            ViewActions.click()
                        )
                    )
            }
            is RecommendationItemViewHolder -> {
                val holderName = "RecommendationItemViewHolder"
                logTestMessage("VH $holderName")
                Espresso.onView(ViewMatchers.withId(R.id.recycler_view))
                    .perform(
                        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                            i,
                            ViewActions.click()
                        )
                    )
            }
            is RecommendationCarouselViewHolder -> {
                val recyclerCarousel = viewHolder.view.findViewById<RecyclerView>(R.id.list)
                val itemCount = recyclerCarousel.adapter?.itemCount ?: 0
                for (child in 0 until itemCount) {
                    scrollRecyclerViewToPosition(homeRecyclerView, child)
                    Espresso.onView(ViewMatchers.withId(R.id.list))
                        .perform(
                            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                                child,
                                ViewActions.click()
                            )
                        )
                }
            }
        }
    }
}
