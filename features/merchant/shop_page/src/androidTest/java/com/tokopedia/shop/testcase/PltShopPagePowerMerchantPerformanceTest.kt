package com.tokopedia.shop.testcase

import android.content.Context
import android.content.Intent
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
import com.tokopedia.shop.mock.ShopPageWithoutHomeTabMockResponseConfig
import com.tokopedia.shop.mock.ShopPageWithoutHomeTabMockResponseConfig.Companion.KEY_QUERY_GET_SHOP_PRODUCT
import com.tokopedia.shop.mock.ShopPageWithoutHomeTabMockResponseConfig.Companion.KEY_QUERY_SHOP_INFO_CORE_AND_ASSETS
import com.tokopedia.shop.mock.ShopPageWithoutHomeTabMockResponseConfig.Companion.KEY_QUERY_SHOP_INFO_FAVORITE
import com.tokopedia.shop.mock.ShopPageWithoutHomeTabMockResponseConfig.Companion.KEY_QUERY_SHOP_INFO_GOLD
import com.tokopedia.shop.mock.ShopPageWithoutHomeTabMockResponseConfig.Companion.KEY_QUERY_SHOP_INFO_HOME_TYPE
import com.tokopedia.shop.mock.ShopPageWithoutHomeTabMockResponseConfig.Companion.KEY_QUERY_SHOP_INFO_OS
import com.tokopedia.shop.mock.ShopPageWithoutHomeTabMockResponseConfig.Companion.KEY_QUERY_SHOP_INFO_TOP_CONTENT
import com.tokopedia.shop.mock.ShopPageWithoutHomeTabMockResponseConfig.Companion.KEY_QUERY_WHITELIST
import com.tokopedia.shop.pageheader.presentation.activity.ShopPageActivity
import com.tokopedia.test.application.util.TokopediaGraphqlInstrumentationTestHelper
import com.tokopedia.test.application.environment.interceptor.size.GqlNetworkAnalyzerInterceptor
import com.tokopedia.test.application.util.setupGraphqlMockResponseWithCheck
import java.util.HashMap
import com.tokopedia.test.application.util.setupTotalSizeInterceptor

class PltShopPagePowerMerchantPerformanceTest {

    companion object {
        private const val SAMPLE_SHOP_ID = "1154916"
    }

    private val TEST_CASE_SHOP_PAGE_LOAD_TIME_PERFORMANCE = "shop_page_test_case_page_load_time"
    private val TEST_CASE_SHOP_PAGE_HEADER_LOAD_TIME_PERFORMANCE = "shop_page_header_test_case_page_load_time"
    private val TEST_CASE_SHOP_PAGE_HOME_TAB_LOAD_TIME_PERFORMANCE = "shop_page_home_tab_test_case_page_load_time"
    private val TEST_CASE_SHOP_PAGE_PRODUCT_TAB_LOAD_TIME_PERFORMANCE = "shop_page_product_tab_test_case_page_load_time"

    private var context: Context? = null

    @get:Rule
    var activityRule: ActivityTestRule<InstrumentationShopPageTestActivity> = ActivityTestRule(InstrumentationShopPageTestActivity::class.java, false, false)

    @get:Rule
    var testRepeatRule: TestRepeatRule = TestRepeatRule()

    @Before
    fun init() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        context?.let {
            setupGraphqlMockResponseWithCheck(ShopPageWithoutHomeTabMockResponseConfig())
            setupTotalSizeInterceptor(listOf(
                    KEY_QUERY_GET_SHOP_PRODUCT,
                    KEY_QUERY_SHOP_INFO_OS,
                    KEY_QUERY_SHOP_INFO_CORE_AND_ASSETS,
                    KEY_QUERY_SHOP_INFO_HOME_TYPE,
                    KEY_QUERY_SHOP_INFO_GOLD,
                    KEY_QUERY_SHOP_INFO_TOP_CONTENT,
                    KEY_QUERY_SHOP_INFO_FAVORITE,
                    KEY_QUERY_WHITELIST
            ))

            val intent = Intent()
            intent.putExtra(ShopPageActivity.SHOP_ID, SAMPLE_SHOP_ID)
            activityRule.launchActivity(intent)
        }
    }

    @Test
    fun testPageLoadTimePerformance() {
        waitForData()
        activityRule.activity.getShopPageHeaderLoadTimePerformanceCallback()?.let {
            savePLTPerformanceResultData(
                    it.getPltPerformanceData(),
                    TEST_CASE_SHOP_PAGE_HEADER_LOAD_TIME_PERFORMANCE
            )
        }
        activityRule.activity.getShopPageHomeTabLoadTimePerformanceCallback()?.let {
            savePLTPerformanceResultData(
                    it.getPltPerformanceData(),
                    TEST_CASE_SHOP_PAGE_HOME_TAB_LOAD_TIME_PERFORMANCE
            )
        }
        activityRule.activity.getShopPageProductTabLoadTimePerformanceCallback()?.let {
            savePLTPerformanceResultData(
                    it.getPltPerformanceData(),
                    TEST_CASE_SHOP_PAGE_PRODUCT_TAB_LOAD_TIME_PERFORMANCE
            )
        }
        activityRule.activity.getShopPageLoadTimePerformanceCallback()?.let {
            savePLTPerformanceResultData(
                    it.getPltPerformanceData(),
                    TEST_CASE_SHOP_PAGE_LOAD_TIME_PERFORMANCE,
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
    ) {
        PerformanceDataFileUtils.writePLTPerformanceFile(
                activityRule.activity,
                testCaseName,
                performanceData,
                networkData = networkData
        )
    }

}
