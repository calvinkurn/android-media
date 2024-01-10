package com.tokopedia.deals.ui.brand_detail

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.deals.di.DealsComponentFactory
import com.tokopedia.deals.di.DealsComponentFactoryStub
import com.tokopedia.test.application.environment.ActivityScenarioTestRule
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DealsBrandDetailActivityTest {

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    @get:Rule
    var activityRule = ActivityScenarioTestRule<DealsBrandDetailActivity>()

    @Before
    fun setup() {
        Intents.init()
        DealsComponentFactory.instance = DealsComponentFactoryStub()

        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(targetContext, DealsBrandDetailActivity::class.java).apply {
            putExtra(DealsBrandDetailActivity.EXTRA_SEO_URL, "klik-dokter-6519")
        }

        activityRule.launchActivity(intent)
    }

    @Test
    fun testBrandDetailFlow() {
        Thread.sleep(1000)
        Intents.intending(anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        onView(withText("KlikDokter ECG")).perform(click())

        Assert.assertThat(
            cassavaTestRule.validate(ANALYTIC_VALIDATOR_QUERY_DEALS_BRAND_DETAIL_PAGE),
            hasAllSuccess()
        )
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    companion object {
        private const val KEY_EVENT_BRAND_DETAIL = "event_brand_detail_v2"
        private const val ANALYTIC_VALIDATOR_QUERY_DEALS_BRAND_DETAIL_PAGE =
            "tracker/entertainment/deals/deals_brand_detail_tracking.json"
    }
}
