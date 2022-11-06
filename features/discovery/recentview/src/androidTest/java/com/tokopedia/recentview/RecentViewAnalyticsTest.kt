package com.tokopedia.recentview

import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.recentview.view.activity.RecentViewActivity
import com.tokopedia.test.application.assertion.topads.TopAdsVerificationTestReportUtil
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 1/11/21.
 */
class RecentViewAnalyticsTest {

    companion object{
        private const val TAG = "RecentViewAnalyticsTest"
        private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_RECENT_VIEW = "tracker/recentview/recent_view.json"
    }

    @get:Rule
    var activityRule = ActivityTestRule<RecentViewActivity>(RecentViewActivity::class.java, false, false)

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        setupGraphqlMockResponse(RecentViewMockResponseConfig())
        activityRule.launchActivity(Intent(InstrumentationRegistry.getInstrumentation().targetContext, RecentViewActivity::class.java))
    }

    @After
    fun dispose(){
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
        val homeRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.rv_recent_view_page)
            CommonActions.clickOnEachItemRecyclerView(
                    homeRecyclerView.rootView,
                    R.id.rv_recent_view_page,
                    0
            )
        activityRule.activity.finish()
        logTestMessage("Done UI Test")
    }

    private fun logTestMessage(message: String) {
        TopAdsVerificationTestReportUtil.writeTopAdsVerificatorLog(activityRule.activity, message)
        Log.d(TAG, message)
    }

    @Test
    fun testRecentViewCassava() {

        RecentViewCassavaTest {
            initTest()
            doActivityTest()
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_RECENT_VIEW)
        }
    }
}