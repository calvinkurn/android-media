package com.tokopedia.sellerorder.detail

import android.app.Application
import android.content.Context
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingPolicies
import androidx.test.espresso.IdlingRegistry
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analytics.performance.util.PerformanceDataFileUtils
import com.tokopedia.seller.active.common.plt.LoadTimeMonitoringListener
import com.tokopedia.sellerorder.SomIdlingResource
import com.tokopedia.sellerorder.Utils
import com.tokopedia.sellerorder.detail.presentation.activity.SomDetailActivity
import com.tokopedia.test.application.TestRepeatRule
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.environment.interceptor.size.GqlNetworkAnalyzerInterceptor
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponseWithCheckAndTotalSizeInterceptor
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit

class SomDetailPltPerformanceTest {
    private val TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE = "som_detail_test_case_page_load_time"

    @get:Rule
    var activityRule: ActivityTestRule<SomDetailActivity> = object : ActivityTestRule<SomDetailActivity>(SomDetailActivity::class.java, false, false) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponseWithCheckAndTotalSizeInterceptor(
                    createMockModelConfig(),
                    listOf("GetSOMDetail", "GetSOMDynamicPrice")
            )
        }

        override fun afterActivityLaunched() {
            super.afterActivityLaunched()
            loadTimeMonitoringListener.onStartPltMonitoring()
            activity.somLoadTimeMonitoringListener = loadTimeMonitoringListener
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
        startSomDetailActivity()
        Espresso.onIdle() // wait for som order detail render process to complete
        savePLTPerformanceResultData(TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE)
        activityRule.activity.finishAndRemoveTask()
    }

    private fun startSomDetailActivity() {
        activityRule.launchActivity(
                SomDetailActivity.createIntent(
                        InstrumentationRegistry.getInstrumentation().targetContext,
                        "738147040"
                )
        )
    }

    private fun savePLTPerformanceResultData(tag: String) {
        val performanceData = activityRule.activity.somDetailLoadTimeMonitoring?.getPltPerformanceMonitoring()
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
                addMockResponse("GetSOMDetail", InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_som_detail_get_order_detail), FIND_BY_CONTAINS)
                addMockResponse("GetSOMDynamicPrice", InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_som_detail_get_order_detail_dynamic_button), FIND_BY_CONTAINS)
                return this
            }
        }
    }

    private fun markAsIdleIfPltIsSucceed() {
        val performanceData = activityRule.activity.somDetailLoadTimeMonitoring?.getPltPerformanceMonitoring()
        if (performanceData?.isSuccess == true) {
            loadTimeMonitoringListener.onStopPltMonitoring()
        }
    }
}