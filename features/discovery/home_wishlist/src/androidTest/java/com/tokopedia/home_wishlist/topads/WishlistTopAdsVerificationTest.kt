package com.tokopedia.home_wishlist.topads

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.test.espresso.Espresso
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.database.STATUS_MATCH
import com.tokopedia.analyticsdebugger.database.TopAdsLogDB
import com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst
import com.tokopedia.analyticsdebugger.debugger.data.repository.TopAdsLogLocalRepository
import com.tokopedia.analyticsdebugger.debugger.data.source.TopAdsLogDBSource
import com.tokopedia.analyticsdebugger.debugger.data.source.TopAdsVerificationNetworkSource
import com.tokopedia.analyticsdebugger.debugger.domain.GetTopAdsLogDataUseCase
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.activity.InstrumentationWishlistTestActivity
import com.tokopedia.home_wishlist.topads.TopAdsVerificationTestReportUtil.deleteTopAdsVerificatorReportData
import com.tokopedia.home_wishlist.topads.TopAdsVerificationTestReportUtil.writeTopAdsVerificatorLog
import com.tokopedia.home_wishlist.topads.TopAdsVerificationTestReportUtil.writeTopAdsVerificatorReportData
import com.tokopedia.home_wishlist.view.viewholder.DynamicCarouselRecommendationViewHolder
import com.tokopedia.home_wishlist.view.viewholder.RecommendationItemViewHolder
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.usecase.RequestParams
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.junit.*

class WishlistTopAdsVerificationTest {
    private var requestParams: RequestParams = RequestParams.create()

    @get:Rule
    var activityRule: ActivityTestRule<InstrumentationWishlistTestActivity> = ActivityTestRule(InstrumentationWishlistTestActivity::class.java)

    @Before
    fun deleteReportData() {
        deleteTopAdsVerificatorReportData(activityRule.activity)
    }

    @After
    fun deleteDatabase() {
        activityRule.activity.deleteDatabase("tkpd_gtm_log_analytics")
    }

    @Test
    fun testTopAds() {
        login()
        waitForData()

        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val itemCount = recyclerView.adapter?.itemCount?:0

        for (i in 0 until itemCount) {
            scrollHomeRecyclerViewToPosition(recyclerView, i)
            checkProductOnDynamicChannel(recyclerView, i)
        }

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
        logTestMessage("Topads product recorded on database : $allCount")
        logTestMessage("Impressed count : $impressedCount")
        logTestMessage("Click count : $clickCount")
        Assert.assertTrue(impressedCount >= clickCount)
        logTestMessage("Impressed count more than click! -> PASSED")
    }

    private fun verifyImpressionMoreThanResponse(impressedCount: Int) {
        logTestMessage("Check if topads impression product in database reach at least minimum from response...")
        val topAdsVerificatorInterface = activityRule.activity.application as TopAdsVerificatorInterface
        val minimumTopAdsProductFromResponse = topAdsVerificatorInterface.minimumTopAdsProductFromResponse
        logTestMessage("Topads from response (minimum) : $minimumTopAdsProductFromResponse")
        logTestMessage("Topads impression product recorded on database : $impressedCount")
        Assert.assertTrue(minimumTopAdsProductFromResponse <= impressedCount)
        logTestMessage("Topads impression product recorded on database is more than minimum! -> PASSED")
    }

    private fun logTestMessage(message: String) {
        writeTopAdsVerificatorLog(activityRule.activity, message)
        Log.d("TopAdsVerificatorLog", message)
    }

    private fun clickOnEachItemRecyclerView(view: View, recyclerViewId: Int) {
        val childRecyclerView = view.findViewById<RecyclerView>(recyclerViewId)
        val childItemCount = childRecyclerView.adapter?.itemCount ?: 0
        logTestMessage("ChildCount Here: $childItemCount item")

        for (j in 1 until childItemCount) {
            try {
                Espresso.onView(firstView(withId(recyclerViewId)))
                        .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(j, click()))
            } catch (e: PerformException) {
                e.printStackTrace()
                logTestMessage("Click failed: $j")
            }
        }
    }

    private fun <T> firstView(matcher: org.hamcrest.Matcher<T>): org.hamcrest.Matcher<T>? {
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
        Thread.sleep(10000)
    }

    private fun scrollHomeRecyclerViewToPosition(homeRecyclerView: RecyclerView, position: Int) {
        val layoutManager = homeRecyclerView.layoutManager as StaggeredGridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }

    private fun checkProductOnDynamicChannel(homeRecyclerView: RecyclerView, i: Int) {
        when (val viewHolder = homeRecyclerView.findViewHolderForAdapterPosition(i)) {
            is RecommendationItemViewHolder -> {
                clickOnEachItemInRecommendationProduct(2)
            }
            is DynamicCarouselRecommendationViewHolder -> {
                clickOnEachItemRecyclerView(viewHolder.itemView, R.id.carouselProductCardRecyclerView)
            }
        }
    }

    private fun clickOnEachItemInRecommendationProduct(numberOfTestProduct: Int) {
        for (j in 1 until numberOfTestProduct) {
            try {
                Espresso.onView(firstView(withId(R.id.recycler_view)))
                        .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(j, click()))
            } catch (e: PerformException) {
                e.printStackTrace()
                logTestMessage("Click failed: $j")
            }
        }
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