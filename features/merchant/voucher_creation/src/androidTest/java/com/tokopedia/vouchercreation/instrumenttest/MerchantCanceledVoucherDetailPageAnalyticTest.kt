package com.tokopedia.vouchercreation.instrumenttest

import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.TokopediaGraphqlInstrumentationTestHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.vouchercreation.mock.MerchantCanceledVoucherDetailMockModelConfig
import com.tokopedia.vouchercreation.shop.detail.view.activity.VoucherDetailActivity
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MerchantCanceledVoucherDetailPageAnalyticTest {

    companion object {
        private const val CANCELED_VOUCHER_DETAIL_PAGE_OPEN = "tracker/merchant/voucher_creation/mvc_canceled_voucher_detail_page_open.json"
    }

    @get:Rule
    var activityRule: IntentsTestRule<VoucherDetailActivity> = IntentsTestRule(VoucherDetailActivity::class.java, false, false)

    @get:Rule
    var cassavaRule = CassavaTestRule()

    @Before
    fun beforeTest() {
        setupGraphqlMockResponse(MerchantCanceledVoucherDetailMockModelConfig())
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        activityRule.launchActivity(Intent())
    }

    @After
    fun afterTest() {
        TokopediaGraphqlInstrumentationTestHelper.deleteAllDataInDb()
    }

    @Test
    fun validateOpenScreenTracker() {
        Thread.sleep(1000)
        doAnalyticDebuggerTest(CANCELED_VOUCHER_DETAIL_PAGE_OPEN)
    }

    private fun doAnalyticDebuggerTest(fileName: String) {
        MatcherAssert.assertThat(
                cassavaRule.validate(fileName),
                hasAllSuccess()
        )
    }
}