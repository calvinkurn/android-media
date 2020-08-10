package com.tokopedia.sellerhome.testcase

import android.app.Application
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingPolicies
import androidx.test.espresso.IdlingRegistry
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analytics.performance.util.PerformanceDataFileUtils
import com.tokopedia.sellerhome.common.SellerHomeIdlingResource
import com.tokopedia.sellerhome.view.activity.SellerHomeActivity
import com.tokopedia.sellerhome.view.activity.SellerHomeActivity.Companion.createIntent
import com.tokopedia.test.application.TestRepeatRule
import com.tokopedia.test.application.util.InstrumentationAuthHelper
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
    var activityRule: ActivityTestRule<SellerHomeActivity> = ActivityTestRule(SellerHomeActivity::class.java, false, false)

    @get:Rule
    var testRepeatRule: TestRepeatRule = TestRepeatRule()

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
                SellerHomeIdlingResource.idlingResource
        )
    }

    private fun savePLTPerformanceResultData(tag: String) {
        val performanceData = activityRule.activity.getPerformanceMonitoringSellerHomeLayoutPlt()?.getPltPerformanceMonitoring()
        performanceData?.let {
            PerformanceDataFileUtils.writePLTPerformanceFile(
                    activityRule.activity,
                    tag,
                    it
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
}