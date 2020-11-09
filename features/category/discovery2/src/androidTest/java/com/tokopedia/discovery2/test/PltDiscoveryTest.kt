package com.tokopedia.discovery2.test
import android.content.Intent
import android.net.Uri
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analytics.performance.PerformanceAnalyticsUtil
import com.tokopedia.analytics.performance.util.PerformanceDataFileUtils
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery.INTERNAL_DISCOVERY
import com.tokopedia.discovery2.config.DiscoveryMockModelConfig
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity
import com.tokopedia.test.application.TestRepeatRule
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class PltDiscoveryTest {
    @get:Rule
    var intentsTestRule = object: IntentsTestRule<DiscoveryActivity>(DiscoveryActivity::class.java,false,false) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
        }
    }


    @get:Rule
    var testRepeatRule: TestRepeatRule = TestRepeatRule()

    private var pltIdlingResource: IdlingResource? = PerformanceAnalyticsUtil.performanceIdlingResource


    private fun setupIdlingResource() {
        IdlingRegistry.getInstance().register(pltIdlingResource)
    }
    @Before
    fun setup() {
        setupIdlingResource()
        setupGraphqlMockResponse(DiscoveryMockModelConfig())
        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        intentsTestRule.launchActivity(Intent(targetContext, DiscoveryActivity::class.java).apply {
            data = Uri.parse("${INTERNAL_DISCOVERY}/waktu-indonesia-belanja-29-septtt")
        })

    }

    @Test
    fun testPageLoadTimePerformance() {
        Espresso.onIdle()
        intentsTestRule.activity.getPltPerformanceResultData()?.let { data->
            PerformanceDataFileUtils.writePLTPerformanceFile(
                    intentsTestRule.activity,
                    TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE,
                    data)
        }
    }

    @After
    fun tearDown() {
        intentsTestRule.activity.finishAndRemoveTask()
        IdlingRegistry.getInstance().unregister(pltIdlingResource)
    }

    companion object {
        const val TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE = "discovery_test_case_page_load_time"
    }
}
