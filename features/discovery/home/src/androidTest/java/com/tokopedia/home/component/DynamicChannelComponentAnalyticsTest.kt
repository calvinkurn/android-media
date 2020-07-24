package com.tokopedia.home.component

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.getAnalyticsWithQuery
import com.tokopedia.analyticsdebugger.validator.core.hasAllSuccess
import com.tokopedia.home.R
import com.tokopedia.home.environment.InstrumentationHomeTestActivity
import com.tokopedia.home.mock.HomeMockResponseConfig
import com.tokopedia.home_component.viewholders.MixLeftComponentViewHolder
import com.tokopedia.home_component.viewholders.MixTopComponentViewHolder
import com.tokopedia.test.application.assertion.topads.TopAdsVerificationTestReportUtil
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_MIX_LEFT = "tracker/home/mix_left.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_MIX_TOP = "tracker/home/mix_top.json"
private const val TAG = "DynamicChannelComponentAnalyticsTest"

/**
 * @author by yoasfs on 07/07/20
 */
class DynamicChannelComponentAnalyticsTest {

    @get:Rule
    var activityRule = object: ActivityTestRule<InstrumentationHomeTestActivity>(InstrumentationHomeTestActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(HomeMockResponseConfig())
        }
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun resetAll() {
        gtmLogDBSource.deleteAll().subscribe()
    }

    @Test
    fun testMixLeftHome() {
        initTest()

        doActivityTest()

        doAnalyticDebuggerTest()

        onFinishTest()

        addDebugEnd()
    }

    private fun initTest() {
        waitForData()
    }

    private fun waitForData() {
        Thread.sleep(5000)
    }

    private fun addDebugEnd() {
        Thread.sleep(5000)
    }

    private fun doActivityTest() {
        val homeRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
        val itemCount = homeRecyclerView.adapter?.itemCount ?: 0
        for (i in 0 until itemCount) {
            scrollHomeRecyclerViewToPosition(homeRecyclerView, i)
            checkProductOnDynamicChannel(homeRecyclerView, i)
        }
        activityRule.activity.finish()
        logTestMessage("Done UI Test")
    }

    private fun doAnalyticDebuggerTest() {
        waitForData()
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_MIX_LEFT),
                hasAllSuccess())
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_MIX_TOP),
                hasAllSuccess())
    }

    private fun onFinishTest() {
        gtmLogDBSource.deleteAll().subscribe()
    }

    private fun scrollHomeRecyclerViewToPosition(homeRecyclerView: RecyclerView, position: Int) {
        val layoutManager = homeRecyclerView.layoutManager as LinearLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }

    private fun checkProductOnDynamicChannel(homeRecyclerView: RecyclerView, i: Int) {
        val viewholder = homeRecyclerView.findViewHolderForAdapterPosition(i)
        when (viewholder) {
            is MixLeftComponentViewHolder -> {
                logTestMessage("VH MixLeftComponentViewHolder")
                clickOnEachItemRecyclerView(viewholder.itemView, R.id.rv_product)
            }
            is MixTopComponentViewHolder -> {
                logTestMessage("VH MixTopComponentViewHolder")
                clickOnEachItemRecyclerView(viewholder.itemView, R.id.dc_banner_rv)
            }
        }
    }

    private fun clickOnEachItemRecyclerView(view: View, recyclerViewId: Int) {
        val childView = view
        val childRecyclerView = childView.findViewById<RecyclerView>(recyclerViewId)
        val childItemCount = childRecyclerView.adapter?.itemCount ?: 0
        logTestMessage("ChildCount Here: " + childItemCount + " item")

        for (j in 1 until childItemCount) {
            try {
                Espresso.onView(firstView(ViewMatchers.withId(recyclerViewId)))
                        .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(j, ViewActions.click()))
                logTestMessage("Click SUCCESS child pos: " + j)
            } catch (e: PerformException) {
                e.printStackTrace()
                logTestMessage("Click FAILED child pos: " + j)
            }
        }
    }

    private fun logTestMessage(message: String) {
        TopAdsVerificationTestReportUtil.writeTopAdsVerificatorLog(activityRule.activity, message)
        Log.d(TAG, message)
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

}