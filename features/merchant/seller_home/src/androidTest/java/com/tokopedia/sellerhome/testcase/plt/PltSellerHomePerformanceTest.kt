package com.tokopedia.sellerhome.testcase.plt

import android.app.Application
import android.content.Context
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingPolicies
import androidx.test.espresso.IdlingRegistry
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.seller.active.common.plt.LoadTimeMonitoringListener
import com.tokopedia.analytics.performance.util.PerformanceDataFileUtils
import com.tokopedia.sellerhome.SellerHomeIdlingResource
import com.tokopedia.sellerhome.view.activity.SellerHomeActivity
import com.tokopedia.sellerhome.view.activity.SellerHomeActivity.Companion.createIntent
import com.tokopedia.test.application.TestRepeatRule
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.environment.interceptor.size.GqlNetworkAnalyzerInterceptor
import com.tokopedia.test.application.util.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit


/**
 * Created by Yusuf on 22/04/20
 */

class PltSellerHomePerformanceTest {
    private val TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE = "seller_home_test_case_page_load_time"

    @get:Rule
    var activityRule: ActivityTestRule<SellerHomeActivity> = object : ActivityTestRule<SellerHomeActivity>(SellerHomeActivity::class.java, false, false) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponseWithCheckAndTotalSizeInterceptor(
                    createMockModelConfig(),
                    listOf("getNotifications", "getShopInfoMoengage", "shopInfoByID",
                            "goldGetPMOSStatus", "GetSellerDashboardLayout", "getCardWidgetData",
                            "getLineGraphData", "getCarouselWidgetData", "getPostWidgetData",
                            "getProgressData", "getBarChartData", "getPieChartData", "getTableData",
                            "getTicker"
                    )
            )

        }

        override fun afterActivityLaunched() {
            super.afterActivityLaunched()
            sellerHomeLoadTimeMonitoringListener.onStartPltMonitoring()
            activity.loadTimeMonitoringListener = sellerHomeLoadTimeMonitoringListener
            markAsIdleIfPltIsSucceed()
        }
    }

    @get:Rule
    var testRepeatRule: TestRepeatRule = TestRepeatRule()

    val sellerHomeLoadTimeMonitoringListener = object : LoadTimeMonitoringListener {
        override fun onStartPltMonitoring() {
            SellerHomeIdlingResource.increment()
        }

        override fun onStopPltMonitoring() {
            SellerHomeIdlingResource.decrement()
        }
    }

    @Before
    fun init() {
        setUpTimeoutPolicy()
        this.registerIdlingResources()
    }

    @After
    fun tearDown() {
        this.unregisterIdlingResources()
    }

    @Test
    fun testPageLoadTimePerformance() {
        login()
        Espresso.onIdle() // wait for login to complete
        activityRule.launchActivity(createIntent(InstrumentationRegistry.getInstrumentation().targetContext))
        Espresso.onIdle() // wait for seller home render process to complete
        savePLTPerformanceResultData(TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE)
        activityRule.activity.finishAndRemoveTask()
    }

    private fun login() {
        InstrumentationAuthHelper.loginToAnUser(
                InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as Application,
                SellerHomeIdlingResource.idlingResource,
                "try.sugiharto+02@tokopedia.com",
                "tokopedia789"
        )
    }

    private fun savePLTPerformanceResultData(tag: String) {
        val performanceData = activityRule.activity.performanceMonitoringSellerHomeLayoutPlt?.getPltPerformanceMonitoring()
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
        IdlingRegistry.getInstance().register(SellerHomeIdlingResource.idlingResource)
    }

    private fun unregisterIdlingResources() {
        IdlingRegistry.getInstance().unregister(SellerHomeIdlingResource.idlingResource)
    }

    private fun createMockModelConfig(): MockModelConfig {
        return object : MockModelConfig() {
            override fun createMockModel(context: Context): MockModelConfig {
                addMockResponse("GoldGetUserShopInfo", InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_get_user_role), FIND_BY_CONTAINS)
                addMockResponse("shopInfoMoengage", InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_shop_info_moengage), FIND_BY_CONTAINS)
                addMockResponse("notifications", InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_notification), FIND_BY_CONTAINS)
                addMockResponse("GetSellerDashboardPageLayout", InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_layout), FIND_BY_CONTAINS)
                addMockResponse("getTicker", InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_get_ticker), FIND_BY_CONTAINS)
                addMockResponse("updateShopActive", InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_update_shop_active), FIND_BY_CONTAINS)
                addMockResponse("shopInfoByID", InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_shop_info_location), FIND_BY_CONTAINS)
                addMockResponse("fetchCardWidgetData", InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_card_widgets), FIND_BY_CONTAINS)
                addMockResponse("fetchLineGraphWidgetData", InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_line_graph_widgets), FIND_BY_CONTAINS)
                addMockResponse("fetchProgressBarWidgetData", InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_progressbar_widgets), FIND_BY_CONTAINS)
                addMockResponse("fetchPostWidgetData", InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_post_widgets), FIND_BY_CONTAINS)
                addMockResponse("fetchCarouselWidgetData", InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_carousel_widgets), FIND_BY_CONTAINS)
                return this
            }
        }
    }

    private fun markAsIdleIfPltIsSucceed() {
        val performanceData = activityRule.activity.performanceMonitoringSellerHomeLayoutPlt?.getPltPerformanceMonitoring()
        if (performanceData?.isSuccess == true) {
            sellerHomeLoadTimeMonitoringListener.onStopPltMonitoring()
        }
    }
}