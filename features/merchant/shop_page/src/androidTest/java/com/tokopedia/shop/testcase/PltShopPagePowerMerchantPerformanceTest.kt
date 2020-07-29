package com.tokopedia.shop.testcase

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.test.application.TestRepeatRule
import com.tokopedia.shop.environment.InstrumentationShopPageTestActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analytics.performance.util.PerformanceDataFileUtils
import com.tokopedia.analytics.performance.util.PltPerformanceData
import com.tokopedia.graphql.data.db.DbMetadata
import com.tokopedia.shop.pageheader.presentation.activity.ShopPageActivity
import com.tokopedia.shop.test.R
import com.tokopedia.shop.util.Util
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.setupGraphqlMockResponseWithCheck
import java.util.HashMap

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
            setupGraphqlMockResponseWithCheck(createShopPagePowerMerchantMockResponse(it))

            val intent = Intent()
            intent.putExtra(ShopPageActivity.SHOP_ID, SAMPLE_SHOP_ID)
            activityRule.launchActivity(intent)
        }
    }

    private fun createShopPagePowerMerchantMockResponse(context: Context): MockModelConfig {
        val responseList = HashMap<String, String>()
        responseList["shopInfoByID"] = Util.getRawString(context, R.raw.response_mock_data_shop_pm_info_none_home_type)
        responseList["getShopOperationalHourStatus"] = Util.getRawString(context, R.raw.response_mock_data_shop_operational_hour)
        responseList["shopShowcasesByShopID"] = Util.getRawString(context, R.raw.response_mock_data_shop_showcase_by_shop_id)
        responseList["membershipStampProgress"] = Util.getRawString(context, R.raw.response_mock_data_shop_membership_stamp)
        responseList["GetShopProduct"] = Util.getRawString(context, R.raw.response_mock_data_get_shop_product)
        responseList["getPublicMerchantVoucherList"] = Util.getRawString(context, R.raw.response_mock_data_shop_public_merchant_voucher_list)
        responseList["shop_featured_product"] = Util.getRawString(context, R.raw.response_mock_data_shop_featured_product)

        return object: MockModelConfig() {
            override fun createMockModel(context: Context): MockModelConfig {
                responseList.forEach {
                    addMockResponse(it.key, it.value, FIND_BY_CONTAINS)
                }

                return this
            }
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
                    TEST_CASE_SHOP_PAGE_LOAD_TIME_PERFORMANCE
            )
        }


        val db: SQLiteOpenHelper = object : SQLiteOpenHelper(InstrumentationRegistry.getInstrumentation().context, DbMetadata.NAME, null, DbMetadata.VERSION) {
            override fun onCreate(p0: SQLiteDatabase?) {
                // noop
            }

            override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
                //noop
            }
        }

        //delete all data in all tables in gql database
        try {
            val dbWrite1 = db.writableDatabase
            dbWrite1.execSQL("DELETE FROM tokopedia_graphql")
            dbWrite1.close()
        } catch (e: Exception) { }
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
