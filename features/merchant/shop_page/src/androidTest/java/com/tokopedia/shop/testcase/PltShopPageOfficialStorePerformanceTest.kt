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
import com.tokopedia.analytics.performance.util.PerformanceDataFileUtils
import com.tokopedia.analytics.performance.util.PltPerformanceData
import com.tokopedia.shop.mock.ShopPageWithHomeTabMockResponseConfig
import com.tokopedia.shop.mock.ShopPageWithHomeTabMockResponseConfig.Companion.KEY_QUERY_GET_IS_SHOP_OFFICIAL
import com.tokopedia.shop.mock.ShopPageWithHomeTabMockResponseConfig.Companion.KEY_QUERY_GET_SHOP_PAGE_HOME_TYPE
import com.tokopedia.shop.mock.ShopPageWithHomeTabMockResponseConfig.Companion.KEY_QUERY_GET_SHOP_PRODUCT
import com.tokopedia.shop.mock.ShopPageWithHomeTabMockResponseConfig.Companion.KEY_QUERY_IS_SHOP_POWER_MERCHANT
import com.tokopedia.shop.mock.ShopPageWithHomeTabMockResponseConfig.Companion.KEY_QUERY_SHOP_INFO_CORE_AND_ASSETS
import com.tokopedia.shop.mock.ShopPageWithHomeTabMockResponseConfig.Companion.KEY_QUERY_SHOP_INFO_TOP_CONTENT
import com.tokopedia.shop.mock.ShopPageWithHomeTabMockResponseConfig.Companion.KEY_QUERY_WHITELIST
import com.tokopedia.shop.pageheader.presentation.activity.ShopPageActivity.Companion.SHOP_ID
import com.tokopedia.test.application.util.setupGraphqlMockResponseWithCheck
import com.tokopedia.test.application.util.TokopediaGraphqlInstrumentationTestHelper
import com.tokopedia.test.application.util.setupTotalSizeInterceptor

class PltShopPageOfficialStorePerformanceTest {

    companion object {
        private const val SAMPLE_SHOP_ID = "3418893"
    }

    private val TEST_CASE_SHOP_PAGE_OFFICIAL_STORE_HEADER_LOAD_TIME_PERFORMANCE = "shop_page_official_store_header_test_case_page_load_time"
    private val TEST_CASE_SHOP_PAGE_OFFICIAL_STORE_HOME_TAB_LOAD_TIME_PERFORMANCE = "shop_page_official_store_home_tab_test_case_page_load_time"
    private val TEST_CASE_SHOP_PAGE_OFFICIAL_STORE_PRODUCT_TAB_LOAD_TIME_PERFORMANCE = "shop_page_official_store_product_tab_test_case_page_load_time"

    private var context: Context? = null

    @get:Rule
    var activityRule: ActivityTestRule<InstrumentationShopPageTestActivity> = ActivityTestRule(InstrumentationShopPageTestActivity::class.java, false, false)

    @get:Rule
    var testRepeatRule: TestRepeatRule = TestRepeatRule()

    @Before
    fun init() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        context?.let {
            setupGraphqlMockResponseWithCheck(ShopPageWithHomeTabMockResponseConfig())
            setupTotalSizeInterceptor(listOf(
                    KEY_QUERY_GET_SHOP_PRODUCT,
                    KEY_QUERY_GET_IS_SHOP_OFFICIAL,
                    KEY_QUERY_SHOP_INFO_CORE_AND_ASSETS,
                    KEY_QUERY_GET_SHOP_PAGE_HOME_TYPE,
                    KEY_QUERY_IS_SHOP_POWER_MERCHANT,
                    KEY_QUERY_SHOP_INFO_TOP_CONTENT,
                    KEY_QUERY_WHITELIST
            ))
            val intent = Intent()
            intent.putExtra(SHOP_ID, SAMPLE_SHOP_ID)
            activityRule.launchActivity(intent)
        }
    }

    @Test
    fun testPageLoadTimePerformance() {
        waitForData()
        activityRule.activity.getShopPageHeaderLoadTimePerformanceCallback()?.let {
            savePLTPerformanceResultData(
                    it.getPltPerformanceData(),
                    TEST_CASE_SHOP_PAGE_OFFICIAL_STORE_HEADER_LOAD_TIME_PERFORMANCE
            )
        }
        activityRule.activity.getShopPageHomeTabLoadTimePerformanceCallback()?.let {
            savePLTPerformanceResultData(
                    it.getPltPerformanceData(),
                    TEST_CASE_SHOP_PAGE_OFFICIAL_STORE_HOME_TAB_LOAD_TIME_PERFORMANCE
            )
        }
        activityRule.activity.getShopPageProductTabLoadTimePerformanceCallback()?.let {
            savePLTPerformanceResultData(
                    it.getPltPerformanceData(),
                    TEST_CASE_SHOP_PAGE_OFFICIAL_STORE_PRODUCT_TAB_LOAD_TIME_PERFORMANCE
            )
        }
        TokopediaGraphqlInstrumentationTestHelper.deleteAllDataInDb()
        activityRule.activity.finishAndRemoveTask()
    }

    private fun waitForData() {
        Thread.sleep(10000)
    }

    private fun savePLTPerformanceResultData(performanceData: PltPerformanceData, testCaseName: String) {
        PerformanceDataFileUtils.writePLTPerformanceFile(
                activityRule.activity,
                testCaseName,
                performanceData
        )
    }

}
