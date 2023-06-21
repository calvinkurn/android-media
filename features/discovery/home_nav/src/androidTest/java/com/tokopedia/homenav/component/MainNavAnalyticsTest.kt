package com.tokopedia.homenav.component

import android.app.Activity
import android.app.Instrumentation
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.homenav.R
import com.tokopedia.homenav.base.datamodel.HomeNavMenuDataModel
import com.tokopedia.homenav.base.diffutil.holder.HomeNavMenuViewHolder
import com.tokopedia.homenav.mainnav.MainNavConst
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.MainNavListAdapter
import com.tokopedia.homenav.mainnav.view.datamodel.TransactionListItemDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.account.AccountHeaderDataModel
import com.tokopedia.homenav.mock.MainNavMockResponseConfig
import com.tokopedia.homenav.util.MainNavRecyclerViewIdlingResource
import com.tokopedia.homenav.view.activity.HomeNavActivity
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.test.application.assertion.topads.TopAdsVerificationTestReportUtil
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters
import kotlin.reflect.KClass

/**
 * Created by dhaba
 */
private const val TAG = "MainNavAnalyticsTest"

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@CassavaTest
class MainNavAnalyticsTest {
    @get:Rule
    var activityRule = object : IntentsTestRule<HomeNavActivity>(
        HomeNavActivity::class.java
    ) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(MainNavMockResponseConfig())
        }
    }

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    private var mainNavRecyclerViewIdlingResource: MainNavRecyclerViewIdlingResource? = null

    @Before
    fun resetAll() {
        login()
        Intents.intending(IntentMatchers.isInternal()).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                null
            )
        )
        val recyclerView: RecyclerView =
            activityRule.activity.findViewById(com.tokopedia.homenav.R.id.recycler_view)
        mainNavRecyclerViewIdlingResource = MainNavRecyclerViewIdlingResource(
            recyclerView = recyclerView
        )
        IdlingRegistry.getInstance().register(mainNavRecyclerViewIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(mainNavRecyclerViewIdlingResource)
    }

    @Test
    fun testClickAllMenu() {
        mainNavCassavaTest {
            login()
            val recyclerView =
                activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
            val itemCount = recyclerView.adapter?.itemCount ?: 0

            for (i in 0 until itemCount) {
                Espresso.onView(ViewMatchers.withId(R.id.recycler_view)).perform(
                    RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                        i
                    )
                )
                checkMenuViewHolderOnRecyclerView(recyclerView, i)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(
                cassavaTestRule,
                ANALYTIC_VALIDATOR_QUERY_FILE_NAME_USER_MENU
            )
        }
    }

    @Test
    fun testComponentOrderHistory() {
        mainNavCassavaTest {
            login()
            doActivityTestByModelClass(
                delayBeforeRender = 2000,
                dataModelClass = TransactionListItemDataModel::class
            ) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                clickOnOrderHistory(viewHolder)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(
                cassavaTestRule,
                ANALYTIC_VALIDATOR_QUERY_FILE_NAME_ORDER_TRANSACTION
            )
        }
    }

    @Test
    fun testComponentShopAndAffiliate() {
        mainNavCassavaTest {
            login()
            doActivityTestByModelClass(
                delayBeforeRender = 2000,
                dataModelClass = AccountHeaderDataModel::class
            ) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                clickOnShopAndAffiliate(viewHolder)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(
                cassavaTestRule,
                ANALYTIC_VALIDATOR_QUERY_FILE_NAME_SHOP_AFFILIATE
            )
        }
    }

    @Test
    fun testComponentTokopediaPlus() {
        mainNavCassavaTest {
            login()
            doActivityTestByModelClass(
                delayBeforeRender = 2000,
                dataModelClass = AccountHeaderDataModel::class
            ) { _: RecyclerView.ViewHolder, _: Int ->
                clickOnTokopediaPlus()
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(
                cassavaTestRule,
                ANALYTIC_VALIDATOR_QUERY_FILE_NAME_TOKOPEDIA_PLUS
            )
        }
    }

    private fun checkMenuViewHolderOnRecyclerView(recyclerView: RecyclerView, position: Int) {
        val list = (recyclerView.adapter as? MainNavListAdapter)?.currentList
        val dataModel = list?.get(position)
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
        if (viewHolder is HomeNavMenuViewHolder &&
            dataModel is HomeNavMenuDataModel &&
            (
                dataModel.sectionId == MainNavConst.Section.ORDER ||
                    dataModel.sectionId == MainNavConst.Section.USER_MENU
                )
        ) {
            clickUserMenu(recyclerView.id, position)
        }
    }

    private fun login() {
        InstrumentationAuthHelper.clearUserSession()
        InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser()
        InstrumentationAuthHelper.loginToAnUser(activityRule.activity.application)
    }

    private fun scrollMainNavRecyclerViewToPosition(homeRecyclerView: RecyclerView, position: Int) {
        val layoutManager = homeRecyclerView.layoutManager as LinearLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 400) }
    }

    private fun logTestMessage(message: String) {
        TopAdsVerificationTestReportUtil.writeTopAdsVerificatorLog(activityRule.activity, message)
        Log.d(TAG, message)
    }

    private fun endActivityTest() {
        activityRule.activity.moveTaskToBack(true)
        logTestMessage("Done UI Test")
        waitForLoadCassavaAssert()
    }

    private fun <T : Any> doActivityTestByModelClass(
        delayBeforeRender: Long = 2000L,
        dataModelClass: KClass<T>,
        predicate: (T?) -> Boolean = { true },
        isTypeClass: (viewHolder: RecyclerView.ViewHolder, itemClickLimit: Int) -> Unit
    ) {
        val mainNavRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val mainNavRecycleAdapter = mainNavRecyclerView.adapter as? MainNavListAdapter

        val visitableList = mainNavRecycleAdapter?.currentList ?: listOf()
        val targetModel = visitableList.find {
            it.javaClass.simpleName == dataModelClass.simpleName && predicate.invoke(it as? T)
        }
        val targetModelIndex = visitableList.indexOf(targetModel)

        targetModelIndex.let { targetModelIndex ->
            scrollMainNavRecyclerViewToPosition(mainNavRecyclerView, targetModelIndex)
            if (delayBeforeRender > 0) Thread.sleep(delayBeforeRender)
            val targetModelViewHolder =
                mainNavRecyclerView.findViewHolderForAdapterPosition(targetModelIndex)
            targetModelViewHolder?.let { targetModelViewHolder ->
                isTypeClass.invoke(
                    targetModelViewHolder,
                    targetModelIndex
                )
            }
        }
        endActivityTest()
    }
}
