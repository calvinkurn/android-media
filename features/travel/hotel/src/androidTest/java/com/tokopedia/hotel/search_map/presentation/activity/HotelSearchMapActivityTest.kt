package com.tokopedia.hotel.search_map.presentation.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.constraintlayout.widget.ConstraintLayout
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
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.google.android.material.bottomsheet.BottomSheetBehavior
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
        val bottomSheetLayout = activityRule.activity.findViewById<ConstraintLayout>(R.id.hotel_search_map_bottom_sheet)
        val bottomSheet =  BottomSheetBehavior.from<ConstraintLayout>(bottomSheetLayout)

        uiDevice.findObject(
                UiSelector().description("MAP READY")
        ).waitForExists(10000)

        clickQuickFilterChips()
        clickOnSortAndFilter()

        Thread.sleep(3000)

        //full map
        activityRule.runOnUiThread {
            bottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED)
        }
        getCurrentPosition()
        scrollToPropertyCard()
        clickHotelFromHorizontalItems()

        Thread.sleep(3000)

        //full srp list
        activityRule.runOnUiThread {
            bottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED)
        }
        validateHotelSearchPageTracking()
        clickOnChangeDestination()
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, targetContext, ANALYTIC_VALIDATOR_QUERY_HOTEL_DISCO), hasAllSuccess())
    }

    private fun validateHotelSearchPageTracking() {
        Thread.sleep(2000)
        assert(getHotelResultCount() > 1)

        Thread.sleep(2000)
        if (getHotelResultCount() > 0) {
            Espresso.onView(ViewMatchers.withId(R.id.rvVerticalPropertiesHotelSearchMap)).perform(RecyclerViewActions
                    .actionOnItemAtPosition<SearchPropertyViewHolder>(0, ViewActions.click()))
        }
        Espresso.onView(isRoot()).perform(ViewActions.pressBack())
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

    private fun clickHotelFromHorizontalItems() {
        Thread.sleep(2000)
        assert(getHotelResultCount() > 1)

        Thread.sleep(2000)
        if (getHotelResultCount() > 0) {
            Espresso.onView(ViewMatchers.withId(R.id.rvHorizontalPropertiesHotelSearchMap)).perform(RecyclerViewActions
                    .actionOnItemAtPosition<HotelSearchMapItemViewHolder>(0, ViewActions.click()))
        }
        Espresso.onView(isRoot()).perform(ViewActions.pressBack())
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
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.ivGetLocationHotelSearchMap)).perform(ViewActions.click())
    }

    /**Get user radius and screen mid point*/
    private fun getRadiusAndMidScreenPoint() {
        Thread.sleep(2000)
        //to make user interact with maps to make maps appear
        Espresso.onView(ViewMatchers.withId(R.id.mapHotelSearchMap)).perform(ViewActions.swipeUp())
        Thread.sleep(2000)
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