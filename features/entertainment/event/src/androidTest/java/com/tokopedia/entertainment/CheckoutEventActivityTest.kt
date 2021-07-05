package com.tokopedia.entertainment

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
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
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.ResourcePathUtil
import org.hamcrest.core.AllOf
import org.junit.After
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
        Intents.init()
        graphqlCacheManager.deleteAll()
        gtmLogDBSource.deleteAll().subscribe()
        setupGraphqlMockResponse {
            addMockResponse(
                    KEY_QUERY_PDP_V3,
                    ResourcePathUtil.getJsonFromResource(PATH_RESPONSE_CHECKOUT),
                    MockModelConfig.FIND_BY_CONTAINS)

            addMockResponse(
                    KEY_CONTENT,
                    ResourcePathUtil.getJsonFromResource(PATH_RESPONSE_CHECKOUT_CONTENT),
                    MockModelConfig.FIND_BY_CONTAINS)

        }

        InstrumentationAuthHelper.loginInstrumentationTestUser1()

        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(targetContext, EventCheckoutActivity::class.java).apply {
            putExtra(EventCheckoutActivity.EXTRA_URL_PDP, "7-day-jr-all-shikoku-pass-23233")
            putExtra(EventCheckoutActivity.EXTRA_META_DATA, MockMetaData.getMetaDataResponse())
            putExtra(EventCheckoutActivity.EXTRA_PACKAGE_ID, "2104")
            putExtra(EventCheckoutActivity.EXTRA_GATEWAY_CODE, "ZERO")
        }

        activityRule.launchActivity(intent)
    }

    @Test
    fun validateCheckoutEvent() {
        Thread.sleep(5000)
        onView(AllOf.allOf(withId(R.id.tg_event_checkout_widget_pessanger_name), isDisplayed())).perform(click())
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
        onView(withId(android.R.id.content)).perform(ViewActions.swipeUp())
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

    @After
    fun cleanUp() {
        Intents.release()
    }


    companion object {
        private const val KEY_QUERY_PDP_V3 = "EventProductDetail"
        private const val KEY_CONTENT = "EventContentById"

        private const val PATH_RESPONSE_CHECKOUT = "event_checkout.json"
        private const val PATH_RESPONSE_CHECKOUT_CONTENT = "event_checkout_content.json"


        private const val ENTERTAINMENT_EVENT_CHECKOUT_VALIDATOR_QUERY = "tracker/event/checkouteventcheck.json"
    }
}
