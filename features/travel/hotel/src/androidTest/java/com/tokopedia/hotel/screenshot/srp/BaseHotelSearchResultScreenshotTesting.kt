package com.tokopedia.hotel.screenshot.srp

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.hotel.R
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity
import com.tokopedia.hotel.search.data.model.HotelSearchModel
import com.tokopedia.hotel.search.presentation.activity.HotelSearchResultActivity
import com.tokopedia.hotel.search.presentation.activity.mock.HotelSearchMockResponseConfig
import com.tokopedia.hotel.search.presentation.fragment.HotelSearchResultFragment
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.setupDarkModeTest
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.core.AllOf
import org.junit.Rule
import org.junit.Test

/**
 * @author by astidhiyaa on 28/04/21
 */
abstract class BaseHotelSearchResultScreenshotTesting {
    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    var activityRule: IntentsTestRule<HotelSearchResultActivity> = object : IntentsTestRule<HotelSearchResultActivity>(HotelSearchResultActivity::class.java) {

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupDarkModeTest(forceDarkMode())
            setupGraphqlMockResponse(HotelSearchMockResponseConfig())
            val localCacheHandler = LocalCacheHandler(targetContext, HotelSearchResultFragment.PREFERENCES_NAME)
            localCacheHandler.apply {
                putBoolean(HotelSearchResultFragment.SHOW_COACH_MARK_KEY, false)
                applyEditor()
            }
        }

        override fun getActivityIntent(): Intent {
            return Intent(targetContext, HotelSearchResultActivity::class.java).apply {
                putExtra(HotelSearchResultFragment.ARG_HOTEL_SEARCH_MODEL, getHotelSearchModel())
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
    fun screenShotOldSrp(){
        CommonActions.findViewAndScreenShot(R.id.hotel_search_header, filePrefix(), "header")
        CommonActions.findViewAndScreenShot(R.id.quick_filter_sort_filter, filePrefix(), "quick-filter")
        CommonActions.screenShotFullRecyclerView(R.id.recycler_view,
                1,
                getHotelResultCount()- 1,
                "${filePrefix()}-full")
        CommonActions.findViewHolderAndScreenshot(R.id.recycler_view, 0, filePrefix(), "item-hotel")

        //SS testing for filter
        Espresso.onView(AllOf.allOf(ViewMatchers.withText("Filter"))).perform(ViewActions.click())
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            CommonActions.takeScreenShotVisibleViewInScreen(activityRule.activity.window.decorView, filePrefix(), "filter-bs-page")
        }
        Espresso.onView(ViewMatchers.withId(R.id.bottom_sheet_close)).perform(ViewActions.click())

        //SS testing for "Ubah"
        Espresso.onView(ViewMatchers.withId(R.id.rightContentID)).perform(ViewActions.click())
        Intents.intending(IntentMatchers.hasComponent(HotelDestinationActivity::class.java.name)).respondWith(createDummyDestination())
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            CommonActions.takeScreenShotVisibleViewInScreen(activityRule.activity.window.decorView, filePrefix(), "ubah-page")
        }

        activityRule.activity.finishAndRemoveTask()
    }

    abstract fun filePrefix(): String

    abstract fun forceDarkMode(): Boolean

    private fun getHotelResultCount(): Int {
        val recyclerView: RecyclerView = activityRule.activity.findViewById(R.id.recycler_view) as RecyclerView
        return recyclerView.adapter?.itemCount ?: 0
    }

    private fun createDummyDestination(): Instrumentation.ActivityResult {
        val resultData = Intent()
        resultData.putExtra(HotelDestinationActivity.HOTEL_DESTINATION_NAME, "Jakarta")
        resultData.putExtra(HotelDestinationActivity.HOTEL_DESTINATION_SEARCH_TYPE, "regionOrigin")
        resultData.putExtra(HotelDestinationActivity.HOTEL_DESTINATION_SEARCH_ID, "835")
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }
}