package com.tokopedia.entertainment

import android.content.Intent
import android.view.View
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.accordion.AccordionItemUnify
import com.tokopedia.accordion.AccordionUnify
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.entertainment.pdp.activity.EventPDPTicketActivity
import com.tokopedia.entertainment.pdp.adapter.viewholder.PackageParentViewHolder
import com.tokopedia.entertainment.pdp.fragment.EventPDPTicketFragment
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.ResourcePathUtil
import org.hamcrest.CoreMatchers.anything
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.not
import org.hamcrest.core.AllOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PDPTicketEventActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)
    private val graphqlCacheManager = GraphqlCacheManager()

    @get:Rule
    var activityRule = ActivityTestRule(EventPDPTicketActivity::class.java, false, false)

    @Before
    fun setup() {
        Intents.init()
        graphqlCacheManager.deleteAll()
        gtmLogDBSource.deleteAll().subscribe()
        setupGraphqlMockResponse {
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


        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(targetContext, EventPDPTicketActivity::class.java).apply {
            putExtra(EventPDPTicketActivity.EXTRA_URL_PDP, "doolive-ala-carte-booking-36116")
            putExtra(EventPDPTicketActivity.SELECTED_DATE, "1900534740")
            putExtra(EventPDPTicketActivity.START_DATE, "")
            putExtra(EventPDPTicketActivity.END_DATE, "")

        }

        LocalCacheHandler(context, EventPDPTicketFragment.PREFERENCES_NAME).also {
            it.putBoolean(EventPDPTicketFragment.SHOW_COACH_MARK_KEY, true)
            it.applyEditor()
        }

        activityRule.launchActivity(intent)
    }


    @Test
    fun validatePDPTicketEvent() {
        Thread.sleep(5000)

        click_package()
        check_ticketState_default()
        click_quantity()
        check_ticketState_chosen()
        edit_quantity()
        click_beli()

        ViewMatchers.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ENTERTAINMENT_EVENT_PDP_TICKET_VALIDATOR_QUERY), hasAllSuccess())
    }

    fun click_package() {
        val accordion = onView(withId(R.id.accordion_header))
        accordion
                .check(matches(isDisplayed()))
                .perform(click())
        Thread.sleep(3000)

        // check if package is collapse then expand (re-expand), all item state are reset
        onView(AllOf.allOf(withId(R.id.txtPilih_ticket), isDisplayed())).perform(click())
        Thread.sleep(1000)
        onView(AllOf.allOf(withId(R.id.txtPilih_ticket))).check(matches(not(isDisplayed())))

        accordion.perform(click())
        Thread.sleep(1000)
        accordion.perform(click())

        Thread.sleep(1000)

        onView(AllOf.allOf(withId(R.id.txtPilih_ticket), isDisplayed()))
                .check(matches(isDisplayed()))
    }

    fun click_quantity() {
        val pilihInteraction = onView(AllOf.allOf(withId(R.id.txtPilih_ticket), isDisplayed()))
        pilihInteraction.perform(click())

        val addQuantiryinteraction = onView(withId(R.id.quantity_editor_add))
        addQuantiryinteraction.perform(click())
        addQuantiryinteraction.perform(click())
        Thread.sleep(3000)

        onView(withId(R.id.quantity_editor_qty)).check(matches(withText("3")))
        onView(withId(R.id.txtTotalHarga)).check(matches(withText("Rp150.000")))

        val subtractQuantityinteraction = onView(withId(R.id.quantity_editor_substract))
        subtractQuantityinteraction.perform(click())

        onView(withId(R.id.quantity_editor_qty)).check(matches(withText("2")))
        onView(withId(R.id.txtTotalHarga)).check(matches(withText("Rp100.000")))
    }

    fun edit_quantity() {
        onView(withId(R.id.quantity_editor_qty)).perform(replaceText("5"))
        onView(withId(R.id.txtTotalHarga)).check(matches(withText("Rp250.000")))
    }

    fun check_ticketState_default() {
        // state: quantity == 0
        onView(withId(R.id.txtPilih_ticket)).check(matches(isDisplayed()))
        onView(withId(R.id.greenDivider)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        onView(withId(R.id.quantityEditor)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }

    fun check_ticketState_chosen() {
        // state: quantity > 0
        onView(withId(R.id.txtPilih_ticket)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        onView(withId(R.id.greenDivider)).check(matches(isDisplayed()))
        onView(withId(R.id.quantityEditor)).check(matches(isDisplayed()))
    }

    fun click_beli() {
        onView(withId(R.id.pilihTicketBtn)).perform(click())
        Thread.sleep(3000)
    }

    @After
    fun cleanUp() {
        Intents.release()
    }

    companion object {
        private const val ENTERTAINMENT_EVENT_PDP_TICKET_VALIDATOR_QUERY = "tracker/event/pdpticketeventcheck.json"

        private const val KEY_QUERY_PDP_V3 = "EventProductDetail"
        private const val KEY_TRAVEL_HOLIDAY = "TravelGetHoliday"
        private const val KEY_QUERY_CONTENT = "EventContentById"

        private const val PATH_RESPONSE_PDP = "event_pdp_ticket.json"
        private const val PATH_RESPONSE_PDP_CONTENT = "event_pdp_content.json"
        private const val PATH_RESPONSE_TRAVEL_HOLIDAY = "event_travel_holiday.json"
    }

}