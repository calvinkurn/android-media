package com.tokopedia.sellerorder.list

import android.app.Application
import android.content.Context
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingPolicies
import androidx.test.espresso.IdlingRegistry
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.seller.active.common.plt.LoadTimeMonitoringListener
import com.tokopedia.analytics.performance.util.PerformanceDataFileUtils
import com.tokopedia.sellerorder.SomIdlingResource
import com.tokopedia.sellerorder.Utils
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.list.presentation.activities.SomListActivity
import com.tokopedia.test.application.TestRepeatRule
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.environment.interceptor.size.GqlNetworkAnalyzerInterceptor
import com.tokopedia.test.application.util.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit

class SomListPltPerformanceTest {
    private val TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE = "som_list_test_case_page_load_time"

    @get:Rule
    var activityRule: ActivityTestRule<SomListActivity> = object : ActivityTestRule<SomListActivity>(SomListActivity::class.java, false, false) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponseWithCheckAndTotalSizeInterceptor(
                    createMockModelConfig(),
                    listOf("GetOrderFilterSom", "GetOrderList", "GetOrderTickers", "GetTopAdsGetShopInfo", "GetWaitingPaymentCounter", "GetUserRole"))
        }

        override fun afterActivityLaunched() {
            super.afterActivityLaunched()
            loadTimeMonitoringListener.onStartPltMonitoring()
            activity.loadTimeMonitoringListener = loadTimeMonitoringListener
            markAsIdleIfPltIsSucceed()
        }
    }

    @get:Rule
    var testRepeatRule: TestRepeatRule = TestRepeatRule()

    val loadTimeMonitoringListener = object : LoadTimeMonitoringListener {
        override fun onStartPltMonitoring() {
            SomIdlingResource.increment()
        }

        override fun onStopPltMonitoring() {
            SomIdlingResource.decrement()
        }
    }

    @Before
    fun init() {
        setUpTimeoutPolicy()
        registerIdlingResources()
    }

    @After
    fun tearDown() {
        unregisterIdlingResources()
    }

    @Test
    fun testPageLoadTimePerformance() {
        Utils.login()
        Espresso.onIdle() // wait for login to complete
        startSomListActivity()
        Espresso.onIdle() // wait for som list order render process to complete
        savePLTPerformanceResultData(TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE)
        activityRule.activity.finishAndRemoveTask()
    }

    private fun startSomListActivity() {
        activityRule.launchActivity(SomListActivity.createIntent(InstrumentationRegistry.getInstrumentation().targetContext).apply {
            putExtra(SomConsts.TAB_ACTIVE, "done")
        })
    }

    private fun savePLTPerformanceResultData(tag: String) {
        val performanceData = activityRule.activity.getSomListLoadTimeMonitoring()?.getPltPerformanceMonitoring()
        performanceData?.let {
            val datasource: String = if (!performanceData.isSuccess) "failed" else "network"
            PerformanceDataFileUtils.writePLTPerformanceFile(
                    activityRule.activity,
                    tag,
                    it,
                    datasource,
                    GqlNetworkAnalyzerInterceptor.getNetworkData()
            )
        }
    }

    private fun setUpTimeoutPolicy() {
        IdlingPolicies.setMasterPolicyTimeout(5, TimeUnit.MINUTES)
        IdlingPolicies.setIdlingResourceTimeout(5, TimeUnit.MINUTES)
    }

    private fun registerIdlingResources() {
        IdlingRegistry.getInstance().register(SomIdlingResource.idlingResource)
    }

    private fun unregisterIdlingResources() {
        IdlingRegistry.getInstance().unregister(SomIdlingResource.idlingResource)
    }

    private fun createMockModelConfig(): MockModelConfig {
        return object : MockModelConfig() {
            override fun createMockModel(context: Context): MockModelConfig {
                addMockResponse("GetOrderFilterSom", InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_som_list_get_order_filter_som), FIND_BY_CONTAINS)
                addMockResponse("GetOrderList", InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_som_list_get_order_list), FIND_BY_CONTAINS)
                addMockResponse("GetOrderTickers", InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_som_list_get_order_tickers), FIND_BY_CONTAINS)
                addMockResponse("GetTopAdsGetShopInfo", InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_som_list_get_topads_shop_info), FIND_BY_CONTAINS)
                addMockResponse("GetWaitingPaymentCounter", InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_som_list_get_waiting_payment_counter), FIND_BY_CONTAINS)
                addMockResponse("GetUserRole", InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_som_list_get_user_roles), FIND_BY_CONTAINS)
                return this
            }
        }
    }

    private fun markAsIdleIfPltIsSucceed() {
        val performanceData = activityRule.activity.getSomListLoadTimeMonitoring()?.getPltPerformanceMonitoring()
        if (performanceData?.isSuccess == true) {
            loadTimeMonitoringListener.onStopPltMonitoring()
        }
    }
}