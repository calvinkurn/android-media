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
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.hotel.R
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity
import com.tokopedia.hotel.search.data.model.HotelSearchModel
import com.tokopedia.hotel.search.presentation.activity.mock.HotelSearchMockResponseConfig
import com.tokopedia.hotel.search.presentation.adapter.viewholder.SearchPropertyViewHolder
import com.tokopedia.hotel.search.presentation.fragment.HotelSearchResultFragment
import com.tokopedia.hotel.search_map.presentation.adapter.viewholder.HotelSearchMapItemViewHolder
import com.tokopedia.hotel.search_map.presentation.fragment.HotelSearchMapFragment
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.core.AllOf
import org.junit.After
import org.junit.Rule
import org.junit.Test

class HotelSearchMapActivityTest {

    private val targetContext = getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(targetContext)
    private val uiDevice = UiDevice.getInstance(getInstrumentation())

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
    fun validateSearchMapPageTracking() {
        clickCoachMark()
        clickQuickFilterChips()
        clickOnSortAndFilter()
        clickOnChangeDestination()
        actionViewFullMap()
        actionCloseMap()

        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        clickHotelFromHorizontalItems()
        validateHotelSearchPageTracking()

        scrollToPropertyCard()
        onMarkerClick()

//        assertThat(getAnalyticsWithQuery(gtmLogDBSource, targetContext, ANALYTIC_VALIDATOR_QUERY_HOTEL_DISCO), hasAllSuccess())
    }

    private fun clickCoachMark() {
        Thread.sleep(2000)

        try {
            // if coachmark show, it will have 3 items
            Espresso.onView(ViewMatchers.withText("Lanjut"))
                    .inRoot(RootMatchers.isPlatformPopup())
                    .perform(ViewActions.click())
            Espresso.onView(ViewMatchers.withText("Lanjut"))
                    .inRoot(RootMatchers.isPlatformPopup())
                    .perform(ViewActions.click())
            Espresso.onView(ViewMatchers.withText("Mengerti"))
                    .inRoot(RootMatchers.isPlatformPopup())
                    .perform(ViewActions.click())
        } catch (t: Throwable) {
            // do nothing because no more coachmark shown
        }
    }

    private fun validateHotelSearchPageTracking() {
        Thread.sleep(2000)
        assert(getHotelResultCount() > 1)
        actionCloseMap()

        Thread.sleep(2000)
        if (getHotelResultCount() > 0) {
            Espresso.onView(ViewMatchers.withId(R.id.rvVerticalPropertiesHotelSearchMap)).perform(RecyclerViewActions
                    .actionOnItemAtPosition<SearchPropertyViewHolder>(0, ViewActions.click()))
        }

        Thread.sleep(2000)
    }

    private fun clickQuickFilterChips() {
        Thread.sleep(2000)
        Espresso.onView(AllOf.allOf(ViewMatchers.withText("Hygiene Verified"))).perform(ViewActions.click())
    }

    private fun clickOnSortAndFilter() {
        Thread.sleep(2000)

        Espresso.onView(AllOf.allOf(ViewMatchers.withText("Filter"))).perform(ViewActions.click())
        Espresso.onView(AllOf.allOf(ViewMatchers.withId(R.id.hotel_selection_chip_title), ViewMatchers.withText("3"))).perform(ViewActions.click())
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.hotel_filter_submit_button)).perform(ViewActions.click())

        Thread.sleep(2000)
    }

    private fun clickOnChangeDestination() {
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.rightContentID)).perform(ViewActions.click())

        Thread.sleep(2000)
        Intents.intending(IntentMatchers.hasComponent(HotelDestinationActivity::class.java.name)).respondWith(createDummyDestination())
        Espresso.onView(ViewMatchers.withId(R.id.tv_hotel_homepage_destination)).perform(ViewActions.click())
        Intents.intended(AllOf.allOf(IntentMatchers.hasComponent(HotelDestinationActivity::class.java.name)))

        Espresso.onView(ViewMatchers.withId(R.id.tv_hotel_homepage_destination)).check(ViewAssertions.matches(ViewMatchers.withText("Jakarta")))

        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.tv_hotel_homepage_destination)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_hotel_homepage_search)).perform(ViewActions.click())
    }

    private fun actionViewFullMap() {
        Thread.sleep(2000)

        Espresso.onView(ViewMatchers.withId(R.id.topHotelSearchMapListKnob))
                .perform(ViewActions.swipeDown())
        Espresso.onView(ViewMatchers.withId(R.id.topHotelSearchMapListKnob))
                .perform(ViewActions.swipeDown())

        Thread.sleep(2000)
    }

    private fun actionCloseMap() {
        Thread.sleep(2000)

        try {
            Espresso.onView(ViewMatchers.withId(R.id.topHotelSearchMapListKnob))
                    .perform(ViewActions.swipeUp())
            Espresso.onView(ViewMatchers.withId(R.id.topHotelSearchMapListKnob))
                    .perform(ViewActions.swipeUp())
        } catch (t: Throwable) {
            // do nothing, the knob is not visible anymore
        }

        Thread.sleep(2000)
    }

    private fun clickHotelFromHorizontalItems() {
        Thread.sleep(2000)
        assert(getHotelResultCount() > 1)
        actionViewFullMap()

        Thread.sleep(2000)
        if (getHotelResultCount() > 0) {
            Espresso.onView(ViewMatchers.withId(R.id.rvHorizontalPropertiesHotelSearchMap)).perform(RecyclerViewActions
                    .actionOnItemAtPosition<HotelSearchMapItemViewHolder>(0, ViewActions.click()))
        }

        Thread.sleep(2000)
    }

    private fun createDummyDestination(): Instrumentation.ActivityResult {
        val resultData = Intent()
        resultData.putExtra(HotelDestinationActivity.HOTEL_DESTINATION_NAME, "Jakarta")
        resultData.putExtra(HotelDestinationActivity.HOTEL_DESTINATION_SEARCH_TYPE, "regionOrigin")
        resultData.putExtra(HotelDestinationActivity.HOTEL_DESTINATION_SEARCH_ID, "835")
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

    private fun getHotelResultCount(): Int {
        val recyclerView: RecyclerView = activityRule.activity.findViewById(R.id.rvVerticalPropertiesHotelSearchMap) as RecyclerView
        return recyclerView.adapter?.itemCount ?: 0
    }

    private fun getPropertyResultCount(): Int {
        val recyclerView: RecyclerView = activityRule.activity.findViewById(R.id.rvHorizontalPropertiesHotelSearchMap) as RecyclerView
        return recyclerView.adapter?.itemCount ?: 0
    }

    private fun scrollToPropertyCard(){
        Thread.sleep(2000)
        assert(getPropertyResultCount() > 1)
        actionCloseMap()

        Thread.sleep(2000)
        if (getPropertyResultCount() > 0) {
            Espresso.onView(ViewMatchers.withId(R.id.rvHorizontalPropertiesHotelSearchMap)).perform(RecyclerViewActions
                    .actionOnItemAtPosition<SearchPropertyViewHolder>(3, ViewActions.scrollTo()))
        }

        Thread.sleep(2000)
    }

    /**When marker clicked, scrollTo hotel position*/
    private fun onMarkerClick() {
        val mMarker = uiDevice.findObject(UiSelector().descriptionContains("Rp 4.172.597"))
        mMarker.click()
        Espresso.onView(ViewMatchers.withId(R.id.rvHorizontalPropertiesHotelSearchMap)).perform(RecyclerViewActions.scrollToPosition<SearchPropertyViewHolder>(0))
    }

    /**Get user current position*/
    private fun getCurrentPosition() {
        Espresso.onView(ViewMatchers.withId(R.id.ivGetLocationHotelSearchMap)).perform(ViewActions.click())
    }

    /**Get user radius and screen mid point*/
    private fun getRadiusAndMidScreenPoint() {
        Espresso.onView(ViewMatchers.withId(R.id.btnGetRadiusHotelSearchMap)).perform(ViewActions.click())
    }

    @After
    fun tearDown() {
        gtmLogDBSource.deleteAll().subscribe()
    }

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_HOTEL_DISCO = "tracker/travel/hotel/hotel_search_map_page.json"
    }
}