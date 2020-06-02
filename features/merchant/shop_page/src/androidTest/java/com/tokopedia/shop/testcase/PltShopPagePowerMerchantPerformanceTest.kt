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
import com.tokopedia.shop.test.R
import com.tokopedia.shop.util.Util
import com.tokopedia.test.application.environment.MockResponseInterface
import java.util.HashMap

class PltShopPagePowerMerchantPerformanceTest {
    private val TEST_CASE_SHOP_PAGE_HEADER_LOAD_TIME_PERFORMANCE = "shop_page_header_test_case_page_load_time"
    private val TEST_CASE_SHOP_PAGE_HOME_TAB_LOAD_TIME_PERFORMANCE = "shop_page_home_tab_test_case_page_load_time"
    private val TEST_CASE_SHOP_PAGE_PRODUCT_TAB_LOAD_TIME_PERFORMANCE = "shop_page_product_tab_test_case_page_load_time"

    private var context: Context? = null

    @get:Rule
    var activityRule: ActivityTestRule<InstrumentationShopPageTestActivity> = ActivityTestRule(InstrumentationShopPageTestActivity::class.java,false, false)

    @get:Rule
    var testRepeatRule: TestRepeatRule = TestRepeatRule()

    @Before
    fun init() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        context?.let{
            (it.applicationContext as MockResponseInterface).reInitMockResponse(createShopPagePowerMerchantMockResponse(it))
            activityRule.launchActivity(Intent())
            activityRule.activity.deleteDatabase("tokopedia_graphql.db")
        }
    }

    private fun createShopPagePowerMerchantMockResponse(context: Context): HashMap<String, String> {
        val responseList = HashMap<String, String>()
        responseList["shopInfoByID"] = Util.getRawString(context, R.raw.response_mock_data_shop_info_none_home_type)
        responseList["getShopOperationalHourStatus"] = Util.getRawString(context, R.raw.response_mock_data_shop_operational_hour)
        responseList["shopShowcasesByShopID"] = Util.getRawString(context, R.raw.response_mock_data_shop_showcase_by_shop_id)
        responseList["GetShopProduct"] = Util.getRawString(context, R.raw.response_mock_data_get_shop_product)
        responseList["shopPageGetLayout"] = Util.getRawString(context, R.raw.response_mock_data_shop_page_get_layout)
        return responseList
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
        activityRule.activity.deleteDatabase("tokopedia_graphql.db")
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
