package com.tokopedia.hotel.hoteldetail.presentation.activity

import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.hotel.hoteldetail.presentation.activity.mock.HotelDetailMockResponseConfig
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import kotlinx.android.synthetic.main.fragment_hotel_detail.*
import org.junit.*

/**
 * @author by jessica on 16/09/20
 */
class HotelDetailActivityTest: BaseHotelPDPTest() {

    @get:Rule
    var activityRule: IntentsTestRule<HotelDetailActivity> = object : IntentsTestRule<HotelDetailActivity>(HotelDetailActivity::class.java) {

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            HotelDetailMockResponseConfig.isPDPNearby = true
            setupGraphqlMockResponse(HotelDetailMockResponseConfig())
        }

        override fun getActivityIntent(): Intent {
            return HotelDetailActivity.getCallingIntent(context, "2023-10-10",
                    "2023-10-11", 11, 1, 1, "region",
                    "Jakarta", true, "HOMEPAGE")
        }
    }

    override fun getTrackerFile(): String = "tracker/travel/hotel/hotel_pdp.json"

    override fun scrollView() {
        activityRule.activity.hotelDetailNestedScrollView.scrollTo(0,100)
    }
}