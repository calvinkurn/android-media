package com.tokopedia.vouchercreation.instrumenttest

import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.TokopediaGraphqlInstrumentationTestHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.vouchercreation.mock.MerchantVoucherHistoryListMockModelConfig
import com.tokopedia.vouchercreation.shop.voucherlist.view.activity.VoucherListActivity
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MerchantVoucherHistoryListPageAnalyticTest {

    companion object {
        private const val VOUCHER_HISTORY_LIST_PAGE_OPEN = "tracker/merchant/voucher_creation/mvc_voucher_history_list_page_open.json"
        private const val IS_ACTIVE = "is_active"
    }

    @get:Rule
    var activityRule: IntentsTestRule<VoucherListActivity> = IntentsTestRule(VoucherListActivity::class.java, false, false)

    @get:Rule
    var cassavaRule = CassavaTestRule()

    @Before
    fun beforeTest() {
        setupGraphqlMockResponse(MerchantVoucherHistoryListMockModelConfig())
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        activityRule.launchActivity(Intent().putExtra(IS_ACTIVE, false))
    }

    @After
    fun afterTest() {
        TokopediaGraphqlInstrumentationTestHelper.deleteAllDataInDb()
    }

    @Test
    fun validateOpenScreenTracker() {
        doAnalyticDebuggerTest(VOUCHER_HISTORY_LIST_PAGE_OPEN)
    }

    private fun doAnalyticDebuggerTest(fileName: String) {
        MatcherAssert.assertThat(
                cassavaRule.validate(fileName),
                hasAllSuccess()
        )
    }
}