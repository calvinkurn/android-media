package com.tokopedia.sellerhome.testcase.cassava

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.centralizedpromo.view.activity.CentralizedPromoActivity
import com.tokopedia.test.application.espresso_component.CommonMatcher
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.TokopediaGraphqlInstrumentationTestHelper
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CentralizedPromoAnalyticTest {

    companion object {
        private const val CENTRALIZED_PROMO_PAGE_OPEN = "tracker/merchant/seller_home/centralized_promo_page_open.json"
        private const val CENTRALIZED_PROMO_CREATE_BUTTON_CLICK = "tracker/merchant/seller_home/centralized_promo_bottom_sheet_create_button_click.json"
    }

    @get:Rule
    var activityRule: IntentsTestRule<CentralizedPromoActivity> = IntentsTestRule(CentralizedPromoActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun beforeTest() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        activityRule.launchActivity(Intent())
    }

    @After
    fun afterTest() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        TokopediaGraphqlInstrumentationTestHelper.deleteAllDataInDb()
    }

    @Test
    fun validateOpenCentralizedPromoPageTracker() {
        doAnalyticDebuggerTest(CENTRALIZED_PROMO_PAGE_OPEN)
        activityRule.activity.finish()
    }

    @Test
    fun validateClickCreateNewVoucherButtonTracker() {
        // click the voucher cash back menu
        Espresso.onView(CommonMatcher
                .firstView(ViewMatchers.withText("Voucher Cashback")))
                .perform(ViewActions.click())
        // click the "buat voucher" button on the bottomn sheet
        Espresso.onView(CommonMatcher
                .firstView(ViewMatchers.withText("Buat Voucher")))
                .perform(ViewActions.click())
        doAnalyticDebuggerTest(CENTRALIZED_PROMO_CREATE_BUTTON_CLICK)
    }

    private fun doAnalyticDebuggerTest(fileName: String) {
        MatcherAssert.assertThat(
                getAnalyticsWithQuery(gtmLogDBSource, context, fileName),
                hasAllSuccess()
        )
    }
}