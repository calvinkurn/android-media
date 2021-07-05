package com.tokopedia.vouchercreation.instrumenttest

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.TokopediaGraphqlInstrumentationTestHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.mock.MerchantVoucherListReviewMockModelConfig
import com.tokopedia.vouchercreation.voucherlist.view.activity.VoucherListActivity
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MerchantVoucherListReviewPageAnalyticTest {

    companion object {
        private const val VOUCHER_LIST_REVIEW_SHARE_BUTTON_CLICK = "tracker/merchant/voucher_creation/mvc_voucher_list_review_share_button_click.json"
        private const val VOUCHER_LIST_REVIEW_DOWNLOAD_BUTTON_CLICK = "tracker/merchant/voucher_creation/mvc_voucher_list_review_download_button_click.json"
        private const val IS_ACTIVE = "is_active"
        private const val SUCCESS_VOUCHER_ID_KEY = "success_voucher_id"
    }

    @get:Rule
    var activityRule: IntentsTestRule<VoucherListActivity> = IntentsTestRule(VoucherListActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun beforeTest() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        setupGraphqlMockResponse(MerchantVoucherListReviewMockModelConfig())
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        activityRule.launchActivity(Intent().putExtra(SUCCESS_VOUCHER_ID_KEY, 10).putExtra(IS_ACTIVE,true))
    }

    @After
    fun afterTest() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        TokopediaGraphqlInstrumentationTestHelper.deleteAllDataInDb()
    }

    @Test
    fun validateClickShareButtonTracker() {
        onView(withId(R.id.successShareButton)).perform(click())
        doAnalyticDebuggerTest(VOUCHER_LIST_REVIEW_SHARE_BUTTON_CLICK)
    }

    @Test
    fun validateClickDownloadButtonTracker() {
        onView(withId(R.id.successDownloadButton)).perform(click())
        doAnalyticDebuggerTest(VOUCHER_LIST_REVIEW_DOWNLOAD_BUTTON_CLICK)
    }

    private fun doAnalyticDebuggerTest(fileName: String) {
        MatcherAssert.assertThat(
                getAnalyticsWithQuery(gtmLogDBSource, context, fileName),
                hasAllSuccess()
        )
    }
}