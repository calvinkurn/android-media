package com.tokopedia.shop.testcase

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.test.application.TestRepeatRule
import com.tokopedia.shop.environment.InstrumentationShopPageTestActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analytics.performance.util.NetworkData
import com.tokopedia.analytics.performance.util.PerformanceDataFileUtils
import com.tokopedia.analytics.performance.util.PltPerformanceData
import com.tokopedia.shop.mock.ShopPageWithHomeTabMockResponseConfig
import com.tokopedia.shop.mock.ShopPageWithHomeTabMockResponseConfig.Companion.KEY_QUERY_GET_IS_SHOP_OFFICIAL
import com.tokopedia.shop.pageheader.presentation.activity.ShopPageActivity.Companion.SHOP_ID
import com.tokopedia.test.application.environment.interceptor.size.GqlNetworkAnalyzerInterceptor
import com.tokopedia.test.application.util.TokopediaGraphqlInstrumentationTestHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponseWithCheckAndTotalSizeInterceptor
class PltShopPageOfficialStorePerformanceTest {

    companion object {
        private const val SAMPLE_SHOP_ID = "3418893"
    }

    private val TEST_CASE_SHOP_PAGE_OFFICIAL_STORE_HOME_TAB_LOAD_TIME_PERFORMANCE = "shop_page_test_case_page_load_time"
    private var context: Context? = null

    @get:Rule
    var activityRule: ActivityTestRule<InstrumentationShopPageTestActivity> = ActivityTestRule(InstrumentationShopPageTestActivity::class.java, false, false)

    @get:Rule
    var testRepeatRule: TestRepeatRule = TestRepeatRule()

    @Before
    fun init() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        context?.let {
            setupGraphqlMockResponseWithCheckAndTotalSizeInterceptor(
                    ShopPageWithHomeTabMockResponseConfig(),
                    listOf(KEY_QUERY_GET_IS_SHOP_OFFICIAL)
            )
            val intent = Intent()
            intent.putExtra(SHOP_ID, SAMPLE_SHOP_ID)
            activityRule.launchActivity(intent)
        }
    }

    @Test
    fun testPageLoadTimePerformance() {
        waitForData()
        activityRule.activity.getShopPageLoadTimePerformanceCallback()?.let {
            savePLTPerformanceResultData(
                    it.getPltPerformanceData(),
                    TEST_CASE_SHOP_PAGE_OFFICIAL_STORE_HOME_TAB_LOAD_TIME_PERFORMANCE,
                    GqlNetworkAnalyzerInterceptor.getNetworkData()
            )
        }
        TokopediaGraphqlInstrumentationTestHelper.deleteAllDataInDb()
        activityRule.activity.finishAndRemoveTask()
    }

    private fun waitForData() {
        Thread.sleep(10000)
    }

    private fun savePLTPerformanceResultData(
            performanceData: PltPerformanceData,
            testCaseName: String,
            networkData: NetworkData? = null
    ){
        PerformanceDataFileUtils.writePLTPerformanceFile(
                activityRule.activity,
                testCaseName,
                performanceData,
                networkData = networkData
        )
    }

}
