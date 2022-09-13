package com.tokopedia.deals.pdp.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.deals.pdp.mock.DealsPDPGQLMockResponse
import com.tokopedia.deals.pdp.mock.DealsPDPRestMockResponse
import com.tokopedia.deals.pdp.ui.activity.DealsPDPActivity
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.test.application.util.setupRestMockResponse
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UiTest
class DealsPDPActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val graphqlCacheManager = GraphqlCacheManager()

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    @get:Rule
    var activityRule =
        ActivityTestRule(DealsPDPActivity::class.java, false, false)

    @Before
    fun setup() {
        Intents.init()
        graphqlCacheManager.deleteAll()
        setupGraphqlMockResponse(DealsPDPGQLMockResponse())
        setupRestMockResponse(DealsPDPRestMockResponse())

        val intent = Intent(context, DealsPDPActivity::class.java).apply {
            putExtra(DealsPDPActivity.EXTRA_PRODUCT_ID, "lalalala")
        }

        activityRule.launchActivity(intent)
    }

    @Test
    fun testPDPDeals() {
        Thread.sleep(10000)
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK, null))

        Assert.assertThat(cassavaTestRule.validate(
            ANALYTIC_VALIDATOR_QUERY_DEALS_PRODUCT_DETAIL_PAGE), hasAllSuccess())
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_DEALS_PRODUCT_DETAIL_PAGE = "tracker/entertainment/deals/deals_pdp_tracking.json"
    }

}