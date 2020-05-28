package com.tokopedia.home.topads

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
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
import com.tokopedia.home.topads.TopAdsVerificationTestReportUtil.writeTopAdsVerificatorReportData
import com.tokopedia.productcard.ProductCardFlashSaleView
import com.tokopedia.productcard.ProductCardGridView
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
    private var numOfProduct = 0

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

        waitForVerificatorReady()

        val listTopAdsDb = readDataFromDatabase()
        logTestMessage("Product recorded on database : "+listTopAdsDb.size)

        listTopAdsDb.forEach {
            logTestMessage(it.sourceName+" - "+it.eventType+" - "+it.eventStatus)
            Assert.assertEquals(STATUS_MATCH, it.eventStatus)
            writeTopAdsVerificatorReportData(activityRule.activity, it)
        }

        logTestMessage("Done: "+numOfProduct+" products checked")
        logTestMessage("Done: "+listTopAdsDb.size+" topads products checked")
    }

    private fun logTestMessage(message: String) {
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
                clickOnEachItemInRecommendationProduct(viewholder, 20)
            }
        }
    }

    private fun clickOnEachItemInRecommendationProduct(viewholder: HomeRecommendationFeedViewHolder, numberOfTestProduct: Int) {
        val homeFeedsViewPager: ViewPager = viewholder.itemView.findViewById(R.id.view_pager_home_feeds)
        val childRecyclerView = homeFeedsViewPager.getChildAt(0) as RecyclerView
        val childItemCount = numberOfTestProduct
        for (j in 0 until childItemCount - 1) {
            try {
                Espresso.onView(firstView(withId(R.id.home_feed_fragment_recycler_view)))
                        .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(j, object: ViewAction{
                            override fun getDescription(): String {
                                return "Click on each item of recommendation feed"
                            }

                            override fun getConstraints(): Matcher<View>? {
                                return null
                            }

                            override fun perform(uiController: UiController?, view: View?) {
                                checkProductCardGridCounter(view)
                                view?.performClick()
                            }
                        }))
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

        for (j in 0 until childItemCount - 1) {
            try {
                Espresso.onView(firstView(withId(recyclerViewId)))
                        .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(j,
                        object: ViewAction{
                            override fun getDescription(): String {
                                return "Click on each item of dynamic channel"
                            }

                            override fun getConstraints(): Matcher<View>? {
                                return null
                            }

                            override fun perform(uiController: UiController?, view: View?) {
                                checkProductCardFlashsaleCounter(view)
                                view?.performClick()
                            }

                        }))
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

    private fun checkProductCardFlashsaleCounter(view: View?) {
        val productCardView: ProductCardFlashSaleView? = view?.findViewById(R.id.productCardView)
        productCardView?.let {
            numOfProduct++
        }
        logTestMessage("Product added, current: "+numOfProduct)
    }

    private fun checkProductCardGridCounter(view: View?) {
        val productCardView: ProductCardGridView? = view?.findViewById(R.id.productCardView)
        productCardView?.let {
            numOfProduct++
        }
        logTestMessage("Product added, current: "+numOfProduct)
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
