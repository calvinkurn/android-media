package com.tokopedia.hotel.hoteldetail.presentation.activity

import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.hotel.hoteldetail.presentation.activity.mock.HotelDetailMockResponseConfig
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import kotlinx.android.synthetic.main.fragment_hotel_detail.*
import org.junit.*

/**
 * @author: astidhiyaa on 16/09/21.
 */
class HotelDetailNonNearbyActivityTest: BaseHotelPDPTest() {

    @get:Rule
    var activityRule: IntentsTestRule<HotelDetailActivity> = object : IntentsTestRule<HotelDetailActivity>(HotelDetailActivity::class.java) {

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            HotelDetailMockResponseConfig.isPDPNearby = false
            setupGraphqlMockResponse(HotelDetailMockResponseConfig())
        }

        override fun getActivityIntent(): Intent {
            return HotelDetailActivity.getCallingIntent(context, "2023-10-10",
                "2023-10-11", 11, 1, 1, "region",
                "Malang", true, "HOMEPAGE")
        }
    }

    override fun getTrackerFile(): String = "tracker/travel/hotel/hotel_pdp_non_nearby.json"

    override fun scrollView() {
        activityRule.activity.hotelDetailNestedScrollView.scrollTo(0,100)
    }
}