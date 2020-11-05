package com.tokopedia.entertainment

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.entertainment.pdp.activity.EventCheckoutActivity
import com.tokopedia.entertainment.pdp.adapter.viewholder.EventPDPTextFieldViewHolder
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.entertainment.data.MockMetaData
import com.tokopedia.entertainment.mock.CheckoutEventMockResponse
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CheckoutEventActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)
    private val graphqlCacheManager = GraphqlCacheManager()


    @get:Rule
    var activityRule =
            ActivityTestRule(EventCheckoutActivity::class.java, false, false)

    @Before
    fun setup() {
        graphqlCacheManager.deleteAll()
        gtmLogDBSource.deleteAll().subscribe()
        setupGraphqlMockResponse {
            addMockResponse(
                    KEY_QUERY_PDP_V3,
                    InstrumentationMockHelper.getRawString(context, com.tokopedia.entertainment.test.R.raw.event_checkout),
                    MockModelConfig.FIND_BY_CONTAINS)

            addMockResponse(
                    KEY_CONTENT,
                    InstrumentationMockHelper.getRawString(context, com.tokopedia.entertainment.test.R.raw.event_checkout_content),
                    MockModelConfig.FIND_BY_CONTAINS)

        }

        InstrumentationAuthHelper.loginInstrumentationTestUser1()

        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(targetContext, EventCheckoutActivity::class.java).apply {
            putExtra(EventCheckoutActivity.EXTRA_URL_PDP, "7-day-jr-all-shikoku-pass-23233")
            putExtra(EventCheckoutActivity.EXTRA_META_DATA, MockMetaData.getMetaDataResponse())
            putExtra(EventCheckoutActivity.EXTRA_PACKAGE_ID, "2104")
        }

        activityRule.launchActivity(intent)
    }

    @Test
    fun validateCheckoutEvent() {
        Thread.sleep(5000)
        onView(withId(R.id.tg_event_checkout_widget_pessanger_name)).perform(click())
        Thread.sleep(3000)
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition<EventPDPTextFieldViewHolder>(0, typeText("Firmanda Mulyawan Nugroho")))
        Thread.sleep(3000)
        onView(withId(R.id.simpanBtn)).perform(click())
        Thread.sleep(3000)
        onView(withId(R.id.rv_event_checkout_additional)).perform(RecyclerViewActions.actionOnItemAtPosition<EventPDPTextFieldViewHolder>(0, click()))
        Thread.sleep(7000)
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition<EventPDPTextFieldViewHolder>(0, typeText("Firmanda Mulyawan Nugroho")))
        Thread.sleep(3000)
        onView(withId(R.id.simpanBtn)).perform(click())
        Thread.sleep(3000)
        onView(withId(R.id.item_checkout_event_data_tambahan_package)).perform(click())
        Thread.sleep(7000)
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition<EventPDPTextFieldViewHolder>(0, typeText("085327499272")))
        Thread.sleep(3000)
        onView(withId(R.id.simpanBtn)).perform(click())
        Thread.sleep(3000)
        onView(withId(android.R.id.content)).perform(ViewActions.swipeUp())
        Thread.sleep(3000)
        onView(withId(R.id.cb_event_checkout)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.btn_event_checkout)).perform(click())

        ViewMatchers.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ENTERTAINMENT_EVENT_CHECKOUT_VALIDATOR_QUERY), hasAllSuccess())
    }

    companion object {
        const val KEY_QUERY_PDP_V3 = "event_product_detail_v3"
        const val KEY_CONTENT = "event_content_by_id"
        private const val ENTERTAINMENT_EVENT_CHECKOUT_VALIDATOR_QUERY = "tracker/event/checkouteventcheck.json"
    }
}
