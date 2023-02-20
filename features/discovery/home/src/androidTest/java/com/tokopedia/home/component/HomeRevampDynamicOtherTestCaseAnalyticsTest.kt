package com.tokopedia.home.component

import android.app.Activity
import android.app.Instrumentation
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.isInternal
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecycleAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderDataModel
import com.tokopedia.home.environment.InstrumentationHomeRevampTestActivity
import com.tokopedia.home.mock.HomeMockResponseConfig
import com.tokopedia.home.util.HomeRecyclerViewIdlingResource
import com.tokopedia.home.util.ViewVisibilityIdlingResource
import com.tokopedia.home_component.visitable.MissionWidgetListDataModel
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.test.application.assertion.topads.TopAdsVerificationTestReportUtil
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.reflect.KClass


private const val TAG = "HomeRevampDynamicChannelComponentOtherTestCaseAnalyticsTest"
@CassavaTest
class HomeRevampDynamicChannelComponentOtherTestCaseAnalyticsTest {
    @get:Rule
    var activityRuleOtherTestCase = object: IntentsTestRule<InstrumentationHomeRevampTestActivity>(InstrumentationHomeRevampTestActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(HomeMockResponseConfig(isLinkedBalanceWidget = false))
        }
    }

    @get:Rule
    var cassavaTestRuleOtherTestCase = CassavaTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private var homeRecyclerViewIdlingResource: HomeRecyclerViewIdlingResource? = null
    private var visibilityIdlingResource: ViewVisibilityIdlingResource? = null

    @Before
    fun resetAll() {
        disableCoachMark(context)
        intending(isInternal()).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                null
            )
        )
        val recyclerView: RecyclerView =
                activityRuleOtherTestCase.activity.findViewById(R.id.home_fragment_recycler_view)
        homeRecyclerViewIdlingResource = HomeRecyclerViewIdlingResource(
                recyclerView = recyclerView
        )
        IdlingRegistry.getInstance().register(homeRecyclerViewIdlingResource)
    }

    @After
    fun tearDown() {
        visibilityIdlingResource?.let {
            IdlingRegistry.getInstance().unregister(visibilityIdlingResource)
        }
        IdlingRegistry.getInstance().unregister(homeRecyclerViewIdlingResource)
    }

    @Test
    fun testMissionWidget() {
        HomeDCCassavaTest {
            initTest()
            login()
            doActivityTestByModelClass(dataModelClass = MissionWidgetListDataModel::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                waitForData()
                actionOnMissionWidget(viewHolder)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRuleOtherTestCase, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_MISSION_WIDGET)
        }
    }

    @Test
    fun testBalanceWidgetGopayNotLinked() {
        login()
        waitForData()
        onView(withId(R.id.home_fragment_recycler_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        HomeDCCassavaTest {
            doActivityTestByModelClass(dataModelClass = HomeHeaderDataModel::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                actionOnBalanceWidget(viewHolder)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRuleOtherTestCase, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_BALANCE_WIDGET_GOPAY_NOT_LINKED)
        }
    }

    private fun initTest() {
        InstrumentationAuthHelper.clearUserSession()
        waitForData()
        hideStickyLogin()
    }

    private fun hideStickyLogin() {
        activityRuleOtherTestCase.runOnUiThread {
            val layout = activityRuleOtherTestCase.activity.findViewById<ConstraintLayout>(R.id.layout_sticky_container)
            if (layout.visibility == View.VISIBLE) {
                layout.visibility = View.GONE
            }
        }
    }

    private fun <T: Any> doActivityTestByModelClass(
        delayBeforeRender: Long = 2000L,
        dataModelClass : KClass<T>,
        predicate: (T?) -> Boolean = {true},
        isTypeClass: (viewHolder: RecyclerView.ViewHolder, itemClickLimit: Int)-> Unit) {
        val homeRecyclerView = activityRuleOtherTestCase.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
        val homeRecycleAdapter = homeRecyclerView.adapter as? HomeRecycleAdapter

        val visitableList = homeRecycleAdapter?.currentList?: listOf()
        val targetModel = visitableList.find { it.javaClass.simpleName == dataModelClass.simpleName && predicate.invoke(it as? T) }
        val targetModelIndex = visitableList.indexOf(targetModel)

        targetModelIndex.let { targetModelIndex->
            scrollHomeRecyclerViewToPosition(homeRecyclerView, targetModelIndex)
            if (delayBeforeRender > 0) Thread.sleep(delayBeforeRender)
            val targetModelViewHolder = homeRecyclerView.findViewHolderForAdapterPosition(targetModelIndex)
            targetModelViewHolder?.let { targetModelViewHolder-> isTypeClass.invoke(targetModelViewHolder, targetModelIndex) }
        }
        endActivityTest()
    }

    private fun endActivityTest() {
        activityRuleOtherTestCase.activity.moveTaskToBack(true)
        logTestMessage("Done UI Test")
        waitForLoadCassavaAssert()
    }

    private fun login() {
        InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser()
        InstrumentationAuthHelper.loginToAnUser(activityRuleOtherTestCase.activity.application)
    }

    private fun scrollHomeRecyclerViewToPosition(homeRecyclerView: RecyclerView, position: Int) {
        val layoutManager = homeRecyclerView.layoutManager as LinearLayoutManager
        activityRuleOtherTestCase.runOnUiThread { layoutManager.scrollToPositionWithOffset   (position, 400) }
    }

    private fun logTestMessage(message: String) {
        TopAdsVerificationTestReportUtil.writeTopAdsVerificatorLog(activityRuleOtherTestCase.activity, message)
        Log.d(TAG, message)
    }
}