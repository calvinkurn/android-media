package com.tokopedia.vouchercreation.instrumenttest

import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.TokopediaGraphqlInstrumentationTestHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.vouchercreation.detail.view.activity.VoucherDetailActivity
import com.tokopedia.vouchercreation.mock.MerchantActiveVoucherListMockModelConfig
import com.tokopedia.vouchercreation.mock.MerchantCanceledVoucherDetailMockModelConfig
import com.tokopedia.vouchercreation.mock.MerchantExpiredVoucherDetailMockModelConfig
import com.tokopedia.vouchercreation.voucherlist.view.activity.VoucherListActivity
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MerchantExpiredVoucherDetailPageAnalyticTest {

    companion object {
        private const val EXPIRED_VOUCHER_DETAIL_PAGE_OPEN = "tracker/merchant/voucher_creation/mvc_expired_voucher_detail_page_open.json"
    }

    @get:Rule
    var activityRule: IntentsTestRule<VoucherDetailActivity> = IntentsTestRule(VoucherDetailActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun beforeTest() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        setupGraphqlMockResponse(MerchantExpiredVoucherDetailMockModelConfig())
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        activityRule.launchActivity(Intent())
    }

    @After
    fun afterTest() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        TokopediaGraphqlInstrumentationTestHelper.deleteAllDataInDb()
    }

    @Test
    fun validateOpenScreenTracker() {
        Thread.sleep(1000)
        doAnalyticDebuggerTest(EXPIRED_VOUCHER_DETAIL_PAGE_OPEN)
    }

    private fun doAnalyticDebuggerTest(fileName: String) {
        MatcherAssert.assertThat(
                getAnalyticsWithQuery(gtmLogDBSource, context, fileName),
                hasAllSuccess()
        )
    }
}