package com.tokopedia.home.component

import android.app.Activity
import android.app.Instrumentation
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecycleAdapter
import com.tokopedia.home.environment.InstrumentationHomeRevampTestActivity
import com.tokopedia.home.mock.HomeMockResponseConfig
import com.tokopedia.home.ui.HomeMockValueHelper
import com.tokopedia.home.util.HomeRecyclerViewIdlingResource
import com.tokopedia.home.util.ViewVisibilityIdlingResource
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.test.application.assertion.topads.TopAdsVerificationTestReportUtil
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.core.AllOf
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters
import kotlin.reflect.KClass

private const val TAG = "HomeNonLoggedInAnalyticsTest"

/**
 * Created by frenzelts on 12/7/23
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@CassavaTest
class HomeNonLoggedInAnalyticsTest {
    @get:Rule
    var activityRule = object : IntentsTestRule<InstrumentationHomeRevampTestActivity>(
        InstrumentationHomeRevampTestActivity::class.java
    ) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            disableCoachMark(context)
            HomeMockValueHelper.setupAbTestRemoteConfig(atf2Rollence = true)
            setupGraphqlMockResponse(HomeMockResponseConfig())
        }
    }

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private var visibilityIdlingResource: ViewVisibilityIdlingResource? = null
    private var homeRecyclerViewIdlingResource: HomeRecyclerViewIdlingResource? = null

    @Before
    fun resetAll() {
        InstrumentationAuthHelper.clearUserSession()
        disableCoachMark(context)
        Intents.intending(IntentMatchers.isInternal()).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                null
            )
        )
        val recyclerView: RecyclerView =
            activityRule.activity.findViewById(R.id.home_fragment_recycler_view)
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
    fun testLoginWidget() {
        HomeDCCassavaTest {
            initTest()
            Espresso.onView(
                AllOf.allOf(
                    ViewMatchers.withId(R.id.home_login_widget_button),
                    ViewMatchers.isDisplayed(),
                    ViewMatchers.withText("Masuk")
                )
            ).perform(ViewActions.click())
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_LOGIN_WIDGET)
        }
    }

    private fun initTest() {
        InstrumentationAuthHelper.clearUserSession()
        waitForData()
    }

    private fun <T : Any> doActivityTestByModelClass(
        delayBeforeRender: Long = 2000L,
        dataModelClass: KClass<T>,
        predicate: (T?) -> Boolean = { true },
        isTypeClass: (viewHolder: RecyclerView.ViewHolder, itemClickLimit: Int) -> Unit
    ) {
        val homeRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
        val homeRecycleAdapter = homeRecyclerView.adapter as? HomeRecycleAdapter

        val visitableList = homeRecycleAdapter?.currentList ?: listOf()
        val targetModel = visitableList.find { it.javaClass.simpleName == dataModelClass.simpleName && predicate.invoke(it as? T) }
        val targetModelIndex = visitableList.indexOf(targetModel)

        targetModelIndex.let { targetModelIndex ->
            scrollHomeRecyclerViewToPosition(homeRecyclerView, targetModelIndex)
            if (delayBeforeRender > 0) Thread.sleep(delayBeforeRender)
            val targetModelViewHolder = homeRecyclerView.findViewHolderForAdapterPosition(targetModelIndex)
            targetModelViewHolder?.let { targetModelViewHolder -> isTypeClass.invoke(targetModelViewHolder, targetModelIndex) }
        }
        endActivityTest()
    }

    private fun <T : Any> doActivityTest(viewClass: KClass<T>, isTypeClass: (viewHolder: RecyclerView.ViewHolder, itemPosition: Int) -> Unit) {
        val homeRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
        val itemCount = homeRecyclerView.adapter?.itemCount ?: 0
        countLoop@ for (i in 0 until itemCount) {
            scrollHomeRecyclerViewToPosition(homeRecyclerView, i)
            Thread.sleep(1000)
            val viewHolder = homeRecyclerView.findViewHolderForAdapterPosition(i)
            if (viewHolder != null && viewClass.simpleName == viewHolder.javaClass.simpleName) {
                isTypeClass.invoke(viewHolder, i)
            }
        }
        endActivityTest()
    }

    private fun <T : Any> doActivityTest(viewClass: KClass<T>, isTypeClass: (viewHolder: RecyclerView.ViewHolder, itemPosition: Int, recycleView: RecyclerView) -> Unit) {
        val homeRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
        val itemCount = homeRecyclerView.adapter?.itemCount ?: 0
        countLoop@ for (i in 0 until itemCount) {
            scrollHomeRecyclerViewToPosition(homeRecyclerView, i)
            val viewHolder = homeRecyclerView.findViewHolderForAdapterPosition(i)
            if (viewHolder != null && viewClass.simpleName == viewHolder.javaClass.simpleName) {
                isTypeClass.invoke(viewHolder, i, homeRecyclerView)
            }
        }
        endActivityTest()
    }

    private fun doActivityTest(viewClassName: List<String>, isTypeClass: (viewHolder: RecyclerView.ViewHolder, itemPosition: Int) -> Unit) {
        val homeRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
        val itemCount = homeRecyclerView.adapter?.itemCount ?: 0
        countLoop@ for (i in 0 until itemCount) {
            scrollHomeRecyclerViewToPosition(homeRecyclerView, i)
            val viewHolder = homeRecyclerView.findViewHolderForAdapterPosition(i)
            if (viewHolder != null && viewClassName.contains(viewHolder.javaClass.simpleName)) {
                isTypeClass.invoke(viewHolder, i)
            }
        }
        endActivityTest()
    }

    private fun endActivityTest() {
        activityRule.activity.moveTaskToBack(true)
        logTestMessage("Done UI Test")
        waitForLoadCassavaAssert()
    }

    private fun login() {
        InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser()
        InstrumentationAuthHelper.loginToAnUser(activityRule.activity.application)
    }

    private fun scrollHomeRecyclerViewToPosition(homeRecyclerView: RecyclerView, position: Int) {
        val layoutManager = homeRecyclerView.layoutManager as LinearLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 400) }
    }

    private fun logTestMessage(message: String) {
        TopAdsVerificationTestReportUtil.writeTopAdsVerificatorLog(activityRule.activity, message)
        Log.d(TAG, message)
    }
}
