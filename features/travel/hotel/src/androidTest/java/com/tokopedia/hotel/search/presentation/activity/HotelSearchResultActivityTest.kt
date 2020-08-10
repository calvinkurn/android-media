package com.tokopedia.hotel.search.presentation.activity

import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.hotel.search.data.model.HotelSearchModel
import com.tokopedia.hotel.search.presentation.fragment.HotelSearchResultFragment
import kotlinx.coroutines.delay
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

/**
 * @author by jessica on 07/08/20
 */
class HotelSearchResultActivityTest {

    @get:Rule
    var activityRule: ActivityTestRule<HotelSearchResultActivity> = object : IntentsTestRule<HotelSearchResultActivity>(HotelSearchResultActivity::class.java) {
        override fun getActivityIntent(): Intent {
            val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
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
    fun testOpenHotelSearchResultActivity() {
        Thread.sleep(5000)
    }
}