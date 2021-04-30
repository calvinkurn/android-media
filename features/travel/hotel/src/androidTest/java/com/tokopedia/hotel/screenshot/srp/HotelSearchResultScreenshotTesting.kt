package com.tokopedia.hotel.screenshot.srp

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.hotel.R
import com.tokopedia.hotel.search.data.model.HotelSearchModel
import com.tokopedia.hotel.search.presentation.activity.HotelSearchResultActivity
import com.tokopedia.hotel.search.presentation.activity.mock.HotelSearchMockResponseConfig
import com.tokopedia.hotel.search.presentation.fragment.HotelSearchResultFragment
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Rule
import org.junit.Test

/**
 * @author by astidhiyaa on 28/04/21
 */
class HotelSearchResultScreenshotTesting {
    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    var activityRule: IntentsTestRule<HotelSearchResultActivity> = object : IntentsTestRule<HotelSearchResultActivity>(HotelSearchResultActivity::class.java) {

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
        activityRule.activity.finishAndRemoveTask()
    }

    private fun filePrefix(): String = "hotel-old-srp"

    private fun getHotelResultCount(): Int {
        val recyclerView: RecyclerView = activityRule.activity.findViewById(R.id.recycler_view) as RecyclerView
        return recyclerView.adapter?.itemCount ?: 0
    }
}