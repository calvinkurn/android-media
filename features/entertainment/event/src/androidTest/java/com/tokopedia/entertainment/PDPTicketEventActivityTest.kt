package com.tokopedia.entertainment

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.entertainment.pdp.activity.EventPDPTicketActivity
import com.tokopedia.entertainment.pdp.adapter.viewholder.PackageParentViewHolder
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.entertainment.mock.PDPTicketEventMockResponse
import com.tokopedia.entertainment.pdp.activity.EventPDPActivity
import com.tokopedia.graphql.GraphqlCacheManager
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PDPTicketEventActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)
    private lateinit var localCacheHandler: LocalCacheHandler
    private val graphqlCacheManager = GraphqlCacheManager()

    @get:Rule
    var activityRule =  ActivityTestRule(EventPDPTicketActivity::class.java, false, false)

    @Before
    fun setup(){
        graphqlCacheManager.deleteAll()
        gtmLogDBSource.deleteAll().subscribe()
        localCacheHandler = LocalCacheHandler(context, PREFERENCES_NAME)
        localCacheHandler.apply {
            putBoolean(SHOW_COACH_MARK_KEY, false)
            applyEditor()
        }
        setupGraphqlMockResponse(PDPTicketEventMockResponse())

        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(targetContext, EventPDPTicketActivity::class.java).apply {
            putExtra(EventPDPTicketActivity.EXTRA_URL_PDP, "doolive-ala-carte-booking-36116")
            putExtra(EventPDPTicketActivity.SELECTED_DATE, "1900534740")
            putExtra(EventPDPTicketActivity.START_DATE, "")
            putExtra(EventPDPTicketActivity.END_DATE, "")

        }
        activityRule.launchActivity(intent)
    }


    @Test
    fun validatePDPTicketEvent() {
        Thread.sleep(5000)
        click_package()
        click_quantity()
        click_beli()

        ViewMatchers.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ENTERTAINMENT_EVENT_PDP_TICKET_VALIDATOR_QUERY), hasAllSuccess())
    }

    fun click_package() {
        val rvInteraction = onView(withId(R.id.recycler_viewParent)).check(matches(isDisplayed()))
        rvInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<PackageParentViewHolder>(0, click()))
        Thread.sleep(3000)
    }

    fun click_quantity() {
        val pilihInteraction = onView(withId(R.id.txtPilih_ticket))
        pilihInteraction.perform(click())

        val addQuantiryinteraction = onView(withId(R.id.quantity_editor_add))
        addQuantiryinteraction.perform(click())
        addQuantiryinteraction.perform(click())
        Thread.sleep(3000)

    }

    fun click_beli() {
        onView(withId(R.id.pilihTicketBtn)).perform(click())
        Thread.sleep(3000)
    }

    companion object {
        private const val ENTERTAINMENT_EVENT_PDP_TICKET_VALIDATOR_QUERY = "tracker/event/pdpticketeventcheck.json"
        private const val PREFERENCES_NAME = "event_ticket_preferences"
        private const val SHOW_COACH_MARK_KEY = "show_coach_mark_key_event_ticket"
    }

}