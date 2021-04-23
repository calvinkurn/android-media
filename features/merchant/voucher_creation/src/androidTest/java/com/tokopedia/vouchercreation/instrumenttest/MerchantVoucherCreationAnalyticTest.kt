package com.tokopedia.vouchercreation.instrumenttest

import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.vouchercreation.create.view.activity.CreateMerchantVoucherStepsActivity
import org.hamcrest.MatcherAssert
import org.junit.Rule

class MerchantVoucherCreationAnalyticTest {

    companion object {
        private const val VOUCHER_TARGET_PAGE_OPEN = "tracker/merchant/voucher_creation/mvc_voucher_history_list_page_open.json"
        private const val VOUCHER_TYPE_AND_BUDGET_PAGE_OPEN = "tracker/merchant/voucher_creation/mvc_voucher_type_and_budget_page_open.json"
        private const val VOUCHER_PERIOD_PAGE_OPEN = "tracker/merchant/voucher_creation/mvc_voucher_period_page_open.json"
        private const val VOUCHER_REVIEW_PAGE_OPEN = "tracker/merchant/voucher_creation/mvc_voucher_review_page_open.json"
    }

    @get:Rule
    var activityRule: IntentsTestRule<CreateMerchantVoucherStepsActivity> = IntentsTestRule(CreateMerchantVoucherStepsActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    private fun doAnalyticDebuggerTest(fileName: String) {
        MatcherAssert.assertThat(
                getAnalyticsWithQuery(gtmLogDBSource, context, fileName),
                hasAllSuccess()
        )
    }
}