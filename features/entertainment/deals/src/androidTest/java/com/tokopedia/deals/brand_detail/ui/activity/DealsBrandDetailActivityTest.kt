package com.tokopedia.deals.brand_detail.ui.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.deals.test.R
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.*

class DealsBrandDetailActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val graphqlCacheManager = GraphqlCacheManager()

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    @get:Rule
    var activityRule =
            ActivityTestRule(DealsBrandDetailActivity::class.java, false, false)

    @Before
    fun setup() {
        Intents.init()
        graphqlCacheManager.deleteAll()
        setupGraphqlMockResponse{
            addMockResponse(KEY_EVENT_BRAND_DETAIL,
                    InstrumentationMockHelper.getRawString(context, R.raw.mock_gql_deals_brand_detail),
                    MockModelConfig.FIND_BY_CONTAINS)
        }

        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(targetContext, DealsBrandDetailActivity::class.java).apply {
            putExtra(DealsBrandDetailActivity.EXTRA_SEO_URL, "klik-dokter-6519")
        }

        activityRule.launchActivity(intent)
    }

    @Test
    fun testBrandDetailFlow() {
        Thread.sleep(3000)
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        Espresso.onView(ViewMatchers.withText("KlikDokter ECG")).perform(ViewActions.click())

        Assert.assertThat(cassavaTestRule.validate(ANALYTIC_VALIDATOR_QUERY_DEALS_BRAND_DETAIL_PAGE), hasAllSuccess())
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    companion object {
        private const val KEY_EVENT_BRAND_DETAIL = "event_brand_detail_v2"
        private const val ANALYTIC_VALIDATOR_QUERY_DEALS_BRAND_DETAIL_PAGE = "tracker/entertainment/deals/deals_brand_detail_tracking.json"
    }

}