package com.tokopedia.hotel.search_map.presentation.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.hotel.R
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity
import com.tokopedia.hotel.search.data.model.HotelSearchModel
import com.tokopedia.hotel.search.presentation.activity.mock.HotelSearchMockResponseConfig
import com.tokopedia.hotel.search.presentation.adapter.viewholder.SearchPropertyViewHolder
import com.tokopedia.hotel.search.presentation.fragment.HotelSearchResultFragment
import com.tokopedia.hotel.search_map.presentation.fragment.HotelSearchMapFragment
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.core.AllOf
import org.junit.After
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class HotelSearchMapActivityTest {

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(targetContext)

    @get:Rule
    var activityRule: IntentsTestRule<HotelSearchMapActivity> = object : IntentsTestRule<HotelSearchMapActivity>(HotelSearchMapActivity::class.java) {

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(HotelSearchMockResponseConfig())
            val localCacheHandler = LocalCacheHandler(targetContext, HotelSearchResultFragment.PREFERENCES_NAME)
            localCacheHandler.apply {
                putBoolean(HotelSearchResultFragment.SHOW_COACH_MARK_KEY, false)
                applyEditor()
            }
        }

        override fun getActivityIntent(): Intent {
            return Intent(targetContext, HotelSearchMapActivity::class.java).apply {
                putExtra(HotelSearchMapFragment.ARG_HOTEL_SEARCH_MODEL, getHotelSearchModel())
            }
        }
    }

    private fun getHotelSearchModel(): HotelSearchModel {
        return HotelSearchModel(
                searchId = "835",
                searchType = "regionOrigin",
                name = "Bali"
        )
    }

    @Test
    fun validateSearchResultPageTracking() {
        clickQuickFilterChips()
        clickOnSortAndFilter()
        clickOnChangeDestination()

        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        validateHotelSearchPageTracking()

        assertThat(getAnalyticsWithQuery(gtmLogDBSource, targetContext, ANALYTIC_VALIDATOR_QUERY_HOTEL_DISCO), hasAllSuccess())
    }

    private fun validateHotelSearchPageTracking() {
        Thread.sleep(2000)
        assert(getHotelResultCount() > 1)

        Thread.sleep(3000)
        if (getHotelResultCount() > 0) {
            Espresso.onView(ViewMatchers.withId(R.id.recycler_view)).perform(RecyclerViewActions
                    .actionOnItemAtPosition<SearchPropertyViewHolder>(0, ViewActions.click()))
        }

        Thread.sleep(3000)
    }

    private fun clickQuickFilterChips() {
        Thread.sleep(4000)
        Espresso.onView(AllOf.allOf(ViewMatchers.withText("Hygiene Verified"))).perform(ViewActions.click())
    }

    private fun clickOnSortAndFilter() {
        Thread.sleep(3000)

        Espresso.onView(AllOf.allOf(ViewMatchers.withText("Filters"))).perform(ViewActions.click())
        Espresso.onView(AllOf.allOf(ViewMatchers.withId(R.id.hotel_selection_chip_title), ViewMatchers.withText("3"))).perform(ViewActions.click())
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.hotel_filter_submit_button)).perform(ViewActions.click())

        Thread.sleep(3000)
    }

    private fun clickOnChangeDestination() {
        Thread.sleep(3000)
        Espresso.onView(ViewMatchers.withId(R.id.rightContentID)).perform(ViewActions.click())

        Thread.sleep(4000)
        Intents.intending(IntentMatchers.hasComponent(HotelDestinationActivity::class.java.name)).respondWith(createDummyDestination())
        Espresso.onView(ViewMatchers.withId(R.id.tv_hotel_homepage_destination)).perform(ViewActions.click())
        Intents.intended(AllOf.allOf(IntentMatchers.hasComponent(HotelDestinationActivity::class.java.name)))

        Espresso.onView(ViewMatchers.withId(R.id.tv_hotel_homepage_destination)).check(ViewAssertions.matches(ViewMatchers.withText("Jakarta")))

        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.tv_hotel_homepage_destination)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_hotel_homepage_search)).perform(ViewActions.click())
    }

    private fun createDummyDestination(): Instrumentation.ActivityResult {
        val resultData = Intent()
        resultData.putExtra(HotelDestinationActivity.HOTEL_DESTINATION_NAME, "Jakarta")
        resultData.putExtra(HotelDestinationActivity.HOTEL_DESTINATION_SEARCH_TYPE, "regionOrigin")
        resultData.putExtra(HotelDestinationActivity.HOTEL_DESTINATION_SEARCH_ID, "835")
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

    private fun getHotelResultCount(): Int {
        val recyclerView: RecyclerView = activityRule.activity.findViewById(R.id.recycler_view) as RecyclerView
        return recyclerView.adapter?.itemCount ?: 0
    }

    @After
    fun tearDown() {
        gtmLogDBSource.deleteAll().subscribe()
    }

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_HOTEL_DISCO = "tracker/travel/hotel/hotel_search_result_page.json"
    }
}