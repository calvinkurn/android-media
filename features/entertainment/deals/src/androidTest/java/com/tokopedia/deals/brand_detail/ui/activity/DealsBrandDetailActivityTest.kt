package com.tokopedia.deals.brand_detail.ui.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.deals.test.R
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class DealsBrandDetailActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)
    private val graphqlCacheManager = GraphqlCacheManager()

    @get:Rule
    var activityRule: IntentsTestRule<DealsBrandDetailActivity> = object : IntentsTestRule<DealsBrandDetailActivity>(DealsBrandDetailActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            graphqlCacheManager.deleteAll()
            gtmLogDBSource.deleteAll().subscribe()
            setupGraphqlMockResponse{
                addMockResponse(KEY_EVENT_BRAND_DETAIL,
                    InstrumentationMockHelper.getRawString(context, R.raw.mock_gql_deals_brand_detail),
                    MockModelConfig.FIND_BY_CONTAINS)
            }
        }

        override fun getActivityIntent(): Intent {
            return DealsBrandDetailActivity.getCallingIntent(context, "klik-dokter-6519")
        }
    }


    @Test
    fun testBrandDetailFlow() {
        Thread.sleep(3000)
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        Espresso.onView(ViewMatchers.withText("KlikDokter ECG")).perform(ViewActions.click())

        Assert.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_DEALS_BRAND_DETAIL_PAGE), hasAllSuccess())
    }

    @After
    fun tearDown() {
        gtmLogDBSource.deleteAll().subscribe()
    }

    companion object {
        private const val KEY_EVENT_BRAND_DETAIL = "event_brand_detail_v2"
        private const val ANALYTIC_VALIDATOR_QUERY_DEALS_BRAND_DETAIL_PAGE = "tracker/entertainment/deals/deals_brand_detail_tracking.json"
    }

}