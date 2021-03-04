package com.tokopedia.entertainment

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.entertainment.pdp.activity.EventPDPActivity
import com.tokopedia.test.application.espresso_component.CommonMatcher.getElementFromMatchAtPosition
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.ResourcePathUtil
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PDPEventActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)
    private val graphqlCacheManager = GraphqlCacheManager()

    @get:Rule
    var activityRule =  ActivityTestRule(EventPDPActivity::class.java, false, false)

    @Before
    fun setup() {
        Intents.init()
        graphqlCacheManager.deleteAll()
        gtmLogDBSource.deleteAll().subscribe()
        setupGraphqlMockResponse{
            addMockResponse(
                    KEY_QUERY_PDP_V3,
                    ResourcePathUtil.getJsonFromResource(PATH_RESPONSE_PDP),
                    MockModelConfig.FIND_BY_CONTAINS)

            addMockResponse(
                    KEY_QUERY_CONTENT,
                    ResourcePathUtil.getJsonFromResource(PATH_RESPONSE_PDP_CONTENT),
                    MockModelConfig.FIND_BY_CONTAINS)

            addMockResponse(
                    KEY_TRAVEL_HOLIDAY,
                    ResourcePathUtil.getJsonFromResource(PATH_RESPONSE_TRAVEL_HOLIDAY),
                    MockModelConfig.FIND_BY_CONTAINS)
        }

        val seo = "bali-zoo-30995"
        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(targetContext, EventPDPActivity::class.java).apply {
            putExtra(EventPDPActivity.EXTRA_URL_PDP, seo)
        }
        activityRule.launchActivity(intent)
    }

    @Test
    fun validatePDPEvent() {
        Thread.sleep(5000)
        click_lanjutkan_ticket()
        click_check_ticket()
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ENTERTAINMENT_EVENT_PDP_VALIDATOR_QUERY), hasAllSuccess())

    }

    fun click_lanjutkan_ticket() {
        val viewInteraction = onView(withId(R.id.btn_event_pdp_cek_tiket))
        viewInteraction.perform(click())
    }

    fun click_check_ticket() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        Thread.sleep(3000)
        onView(getElementFromMatchAtPosition(withText("12"), 1)).check(matches(isDisplayed()))
        Thread.sleep(3000)
        onView(getElementFromMatchAtPosition(withText("12"), 1)).perform(click())
    }

    @After
    fun cleanUp() {
        Intents.release()
    }

    companion object {
        private const val KEY_QUERY_PDP_V3 = "EventProductDetail"
        private const val KEY_TRAVEL_HOLIDAY = "TravelGetHoliday"
        private const val KEY_QUERY_CONTENT = "EventContentById"

        private const val ENTERTAINMENT_EVENT_PDP_VALIDATOR_QUERY = "tracker/event/pdpeventcheck.json"

        private const val PATH_RESPONSE_PDP = "event_pdp.json"
        private const val PATH_RESPONSE_PDP_CONTENT = "event_pdp_content.json"
        private const val PATH_RESPONSE_TRAVEL_HOLIDAY = "event_travel_holiday.json"
    }
}