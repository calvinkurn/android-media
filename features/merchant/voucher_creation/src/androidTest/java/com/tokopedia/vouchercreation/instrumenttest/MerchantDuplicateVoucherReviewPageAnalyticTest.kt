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
import com.tokopedia.vouchercreation.create.view.activity.CreateMerchantVoucherStepsActivity
import com.tokopedia.vouchercreation.mock.MerchantDuplicateVoucherReviewMockModelConfig
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MerchantDuplicateVoucherReviewPageAnalyticTest {

    companion object {
        private const val DUPLICATE_VOUCHER_REVIEW_PAGE_OPEN = "tracker/merchant/voucher_creation/mvc_duplicate_voucher_page_open.json"
        private const val IS_DUPLICATE = "is_duplicate"
        private const val DUPLICATE_VOUCHER = "duplicate_voucher"
    }

    @get:Rule
    var activityRule: IntentsTestRule<CreateMerchantVoucherStepsActivity> = IntentsTestRule(CreateMerchantVoucherStepsActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun beforeTest() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        setupGraphqlMockResponse(MerchantDuplicateVoucherReviewMockModelConfig())
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        val voucherUiModel = VoucherUiModel(
                id = 3543171,
                name = "nama voucher",
                type = 3,
                typeFormatted = "Cashback",
                image = "https://ecs7.tokopedia.net/img/attachment/2021/5/25/128068031/128068031_62782f5b-87cb-4880-a82d-6e642f9d6978.jpg",
                imageSquare = "https://ecs7.tokopedia.net/img/attachment/2021/5/25/128068031/128068031_99b2d51e-7632-489e-a84d-7c2a8a38e14b.jpg",
                status = 3, discountTypeFormatted = "idr",
                discountAmt = 20000,
                discountAmtFormatted = "20rb",
                discountAmtMax = 20000,
                minimumAmt = 287500,
                quota = 15,
                confirmedQuota = 0,
                bookedQuota = 0,
                startTime = "2021-05-25T19:00:00Z",
                finishTime = "2021-06-01T16:00:00Z",
                code = "YDPSQI7SB2",
                createdTime = "2021-05-25T15:57:29Z",
                updatedTime = "2021-06-01T16:00:05.002229Z",
                isPublic = true)
        activityRule.launchActivity(Intent().putExtra(IS_DUPLICATE, true).putExtra(DUPLICATE_VOUCHER, voucherUiModel))
    }

    @After
    fun afterTest() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        TokopediaGraphqlInstrumentationTestHelper.deleteAllDataInDb()
    }

    @Test
    fun validateOpenScreenTracker() {
        Thread.sleep(1000)
        doAnalyticDebuggerTest(DUPLICATE_VOUCHER_REVIEW_PAGE_OPEN)
    }

    private fun doAnalyticDebuggerTest(fileName: String) {
        MatcherAssert.assertThat(
                getAnalyticsWithQuery(gtmLogDBSource, context, fileName),
                hasAllSuccess()
        )
    }
}