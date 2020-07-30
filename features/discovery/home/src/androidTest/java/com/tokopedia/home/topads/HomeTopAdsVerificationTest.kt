package com.tokopedia.home.topads

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.viewpager.widget.ViewPager
import com.tokopedia.analyticsdebugger.database.STATUS_MATCH
import com.tokopedia.analyticsdebugger.database.TopAdsLogDB
import com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst
import com.tokopedia.analyticsdebugger.debugger.data.repository.TopAdsLogLocalRepository
import com.tokopedia.analyticsdebugger.debugger.data.source.TopAdsLogDBSource
import com.tokopedia.analyticsdebugger.debugger.data.source.TopAdsVerificationNetworkSource
import com.tokopedia.analyticsdebugger.debugger.domain.GetTopAdsLogDataUseCase
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.DynamicChannelSprintViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.MixLeftViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.mix_top.MixTopBannerViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.HomeRecommendationFeedViewHolder
import com.tokopedia.home.environment.InstrumentationHomeTestActivity
import com.tokopedia.home.topads.TopAdsVerificationTestReportUtil.deleteTopAdsVerificatorReportData
import com.tokopedia.home.topads.TopAdsVerificationTestReportUtil.writeTopAdsVerificatorLog
import com.tokopedia.home.topads.TopAdsVerificationTestReportUtil.writeTopAdsVerificatorReportData
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.usecase.RequestParams
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.*

/**
 * Created by DevAra
 * Instrumented test, which will execute on an Android device.
 * Verify Topads for HomePage
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class HomeTopAdsVerificationTest {
    private var requestParams: RequestParams = RequestParams.create()

    @get:Rule
    var activityRule: ActivityTestRule<InstrumentationHomeTestActivity> = ActivityTestRule(InstrumentationHomeTestActivity::class.java)

    @Before
    fun deleteReportData() {
        deleteTopAdsVerificatorReportData(activityRule.activity)
    }

    @After
    fun deleteDatabase() {
        activityRule.activity.deleteDatabase("tkpd_gtm_log_analytics")
    }

    @Test
    fun testTopAdsHome() {
        login()
        waitForData()

        val homeRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
        val itemCount = homeRecyclerView.adapter?.itemCount?:0

        for (i in 0 until itemCount) {
            scrollHomeRecyclerViewToPosition(homeRecyclerView, i)
            checkProductOnDynamicChannel(homeRecyclerView, i)
        }
        logTestMessage("Done UI Test")
        logTestMessage("Asserting data...")

        val listTopAdsDbFirst = readDataFromDatabase()
        val impressedCount = listTopAdsDbFirst.filter { it.eventType == "impression" }.size
        val clickCount = listTopAdsDbFirst.filter { it.eventType == "click" }.size
        val allCount = listTopAdsDbFirst.size

        verifyImpressionMoreThanClick(allCount, impressedCount, clickCount)
        verifyImpressionMoreThanResponse(impressedCount)

        logTestMessage("Waiting for topads backend verificator ready... (>5mins)")

        waitForVerificatorReady()

        val listTopAdsDb = readDataFromDatabase()

        listTopAdsDb.forEach {
            logTestMessage(it.sourceName+" - "+it.eventType+" - "+it.eventStatus)
            Assert.assertEquals(STATUS_MATCH, it.eventStatus)
            writeTopAdsVerificatorReportData(activityRule.activity, it)
        }

        logTestMessage("Verified from topads backend that all data is MATCH! -> PASSED")
        logTestMessage("Done: "+listTopAdsDb.size+" topads products checked")
    }

    private fun verifyImpressionMoreThanClick(allCount: Int, impressedCount: Int, clickCount: Int) {
        logTestMessage("Check if impression is more than click...")
        logTestMessage("Topads product recorded on database : " + allCount)
        logTestMessage("Impressed count : " + impressedCount)
        logTestMessage("Click count : " + clickCount)
        Assert.assertTrue(impressedCount >= clickCount)
        logTestMessage("Impressed count more than click! -> PASSED")
    }

    private fun verifyImpressionMoreThanResponse(impressedCount: Int) {
        logTestMessage("Check if topads impression product in database reach at least minimum from response...")
        val topAdsVerificatorInterface = activityRule.activity.application as TopAdsVerificatorInterface
        val minimumTopAdsProductFromResponse = topAdsVerificatorInterface.minimumTopAdsProductFromResponse
        logTestMessage("Topads from response (minimum) : " + minimumTopAdsProductFromResponse)
        logTestMessage("Topads impression product recorded on database : " + impressedCount)
        Assert.assertTrue(minimumTopAdsProductFromResponse <= impressedCount)
        logTestMessage("Topads impression product recorded on database is more than minimum! -> PASSED")
    }

    private fun logTestMessage(message: String) {
        writeTopAdsVerificatorLog(activityRule.activity, message)
        Log.d("TopAdsVerificatorLog", message)
    }

    private fun checkProductOnDynamicChannel(homeRecyclerView: RecyclerView, i: Int) {
        val viewholder = homeRecyclerView.findViewHolderForAdapterPosition(i)
        when (viewholder) {
            is MixTopBannerViewHolder -> {
                clickOnEachItemRecyclerView(viewholder.itemView, R.id.dc_banner_rv)
            }
            is MixLeftViewHolder -> {
                clickOnEachItemRecyclerView(viewholder.itemView, R.id.rv_product)
            }
            is DynamicChannelSprintViewHolder -> {
                clickOnEachItemRecyclerView(viewholder.itemView, R.id.recycleList)
            }
            is HomeRecommendationFeedViewHolder -> {
                waitForData()
                clickOnEachItemInRecommendationProduct(40)
            }
        }
    }

    private fun clickOnEachItemInRecommendationProduct(numberOfTestProduct: Int) {
        val childItemCount = numberOfTestProduct
        for (j in 1 until childItemCount) {
            try {
                Espresso.onView(firstView(withId(R.id.home_feed_fragment_recycler_view)))
                        .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(j, click()))
            } catch (e: PerformException) {
                e.printStackTrace()
                logTestMessage("Click failed: "+j)
            }
        }
    }

    private fun clickOnEachItemRecyclerView(view: View, recyclerViewId: Int) {
        val childView = view
        val childRecyclerView = childView.findViewById<RecyclerView>(recyclerViewId)
        val childItemCount = childRecyclerView.adapter?.itemCount ?: 0
        logTestMessage("ChildCount Here: "+childItemCount+" item")

        for (j in 1 until childItemCount) {
            try {
                Espresso.onView(firstView(withId(recyclerViewId)))
                        .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(j, click()))
            } catch (e: PerformException) {
                e.printStackTrace()
                logTestMessage("Click failed: "+j)
            }
        }
    }

    private fun <T> firstView(matcher: Matcher<T>): Matcher<T>? {
        return object : BaseMatcher<T>() {
            var isFirst = true
            override fun matches(item: Any?): Boolean {
                if (isFirst && matcher.matches(item)) {
                    isFirst = false
                    return true
                }
                return false
            }

            override fun describeTo(description: Description) {
                description.appendText("should return first matching item")
            }
        }
    }

    private fun waitForVerificatorReady() {
        //wait for 5 minutes and 30 seconds
        Thread.sleep(480000)
    }

    private fun waitForData() {
        Thread.sleep(5000)
    }

    private fun scrollHomeRecyclerViewToPosition(homeRecyclerView: RecyclerView, position: Int) {
        val layoutManager = homeRecyclerView.layoutManager as LinearLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }

    private fun readDataFromDatabase(): List<TopAdsLogDB> {
        val context = activityRule.activity.applicationContext
        val topAdsLogDBSource = TopAdsLogDBSource(context)
        val graphqlUseCase = GraphqlUseCase()
        val topAdsVerificationNetworkSource = TopAdsVerificationNetworkSource(context, graphqlUseCase)

        val topAdsLogLocalRepository = TopAdsLogLocalRepository(topAdsLogDBSource, topAdsVerificationNetworkSource)
        val getTopAdsLogDataUseCase = GetTopAdsLogDataUseCase(topAdsLogLocalRepository)

        setRequestParams(1, "")
        return getTopAdsLogDataUseCase.getData(requestParams)
    }

    private fun login() {
        InstrumentationAuthHelper.loginToAnUser(activityRule.activity.application)
    }

    private fun setRequestParams(page: Int, keyword: String) {
        requestParams.putString(AnalyticsDebuggerConst.KEYWORD, keyword)
        requestParams.putInt(AnalyticsDebuggerConst.PAGE, page)
        requestParams.putString(AnalyticsDebuggerConst.ENVIRONMENT, AnalyticsDebuggerConst.ENVIRONMENT_TEST)
    }
}
