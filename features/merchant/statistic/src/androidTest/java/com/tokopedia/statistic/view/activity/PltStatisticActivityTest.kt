package com.tokopedia.statistic.view.activity

import android.app.Application
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingPolicies
import androidx.test.espresso.IdlingRegistry
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analytics.performance.util.PerformanceDataFileUtils
import com.tokopedia.statistic.analytics.performance.StatisticIdlingResourceListener
import com.tokopedia.statistic.common.StatisticIdlingResource
import com.tokopedia.statistic.mock.StatisticMockResponseConfig
import com.tokopedia.test.application.TestRepeatRule
import com.tokopedia.test.application.environment.interceptor.size.GqlNetworkAnalyzerInterceptor
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponseWithCheck
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit

/**
 * Created By @ilhamsuaib on 07/01/21
 */

class PltStatisticActivityTest {

    companion object {
        private const val STATISTIC_TEST_CASE_PLT_NAME = "statistic_test_case_plt"
    }

    @get:Rule
    var activityRule = object : ActivityTestRule<StatisticActivity>(StatisticActivity::class.java, false, false) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponseWithCheck(StatisticMockResponseConfig())
        }

        override fun afterActivityLaunched() {
            super.afterActivityLaunched()
            pltMonitoringListener.startMonitoring()
            activity.pltListener = pltMonitoringListener
            markAsIdleIfPltIsSucceed()
        }
    }

    @get:Rule
    var testRepeatRule: TestRepeatRule = TestRepeatRule()

    private val pltMonitoringListener: StatisticIdlingResourceListener = object : StatisticIdlingResourceListener {
        override fun startMonitoring() {
            StatisticIdlingResource.increment()
        }

        override fun stopMonitoring() {
            StatisticIdlingResource.decrement()
        }
    }

    @Before
    fun setup() {
        setupIdlingTimeOutPolicy()
        registerIdlingResource()
    }

    @After
    fun tearDown() {
        unregisterIdlingResource()
    }

    @Test
    fun testPltPerformance() {
        login()
        Espresso.onIdle() // wait for login to complete
        val statisticIntent = Intent(InstrumentationRegistry.getInstrumentation().targetContext, StatisticActivity::class.java)
        activityRule.launchActivity(statisticIntent)
        Espresso.onIdle() // wait for seller home render process to complete
        savePLTPerformanceResultData()
        activityRule.activity.finishAndRemoveTask()
    }

    private fun savePLTPerformanceResultData() {
        val performanceData = activityRule.activity.performanceMonitoring.getPltResult()
        performanceData?.let {
            val datasource: String = if (!performanceData.isSuccess) "failed" else "network"
            PerformanceDataFileUtils.writePLTPerformanceFile(
                    activityRule.activity,
                    STATISTIC_TEST_CASE_PLT_NAME,
                    it,
                    datasource,
                    GqlNetworkAnalyzerInterceptor.getNetworkData()
            )
        }
    }

    private fun setupIdlingTimeOutPolicy() {
        IdlingPolicies.setMasterPolicyTimeout(3, TimeUnit.MINUTES)
        IdlingPolicies.setIdlingResourceTimeout(3, TimeUnit.MINUTES)
    }

    private fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(StatisticIdlingResource.idlingResource)
    }

    private fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(StatisticIdlingResource.idlingResource)
    }

    private fun login() {
        InstrumentationAuthHelper.loginToAnUser(
                InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as Application,
                StatisticIdlingResource.idlingResource
        )
    }

    private fun markAsIdleIfPltIsSucceed() {
        val performanceData = activityRule.activity.performanceMonitoring.getPltResult()
        if (performanceData?.isSuccess == true) {
            pltMonitoringListener.stopMonitoring()
        }
    }
}