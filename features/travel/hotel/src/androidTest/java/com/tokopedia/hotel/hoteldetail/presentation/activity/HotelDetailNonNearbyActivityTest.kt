package com.tokopedia.hotel.hoteldetail.presentation.activity

import android.content.Intent
import androidx.core.widget.NestedScrollView
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.hotel.R
import com.tokopedia.hotel.hoteldetail.presentation.activity.mock.HotelDetailMockResponseConfig
import com.tokopedia.hotel.test.R as hoteltestR
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.*

/**
 * @author: astidhiyaa on 16/09/21.
 */
class HotelDetailNonNearbyActivityTest: BaseHotelPDPTest() {

    @get:Rule
    var activityRule: IntentsTestRule<HotelDetailActivity> = object : IntentsTestRule<HotelDetailActivity>(HotelDetailActivity::class.java) {

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponse{
                addMockResponse(
                    HotelDetailMockResponseConfig.KEY_QUERY_GET_HOTEL_DETAIL,
                    InstrumentationMockHelper.getRawString(
                        context,
                        hoteltestR.raw.response_mock_property_detail
                    ),
                    MockModelConfig.FIND_BY_CONTAINS
                )

                addMockResponse(
                    HotelDetailMockResponseConfig.KEY_QUERY_GET_HOTEL_ROOM,
                    InstrumentationMockHelper.getRawString(
                        context,
                        hoteltestR.raw.response_mock_get_room_list
                    ),
                    MockModelConfig.FIND_BY_CONTAINS
                )

                addMockResponse(
                    HotelDetailMockResponseConfig.KEY_QUERY_GET_PROPERTY_REVIEW,
                    InstrumentationMockHelper.getRawString(
                        context,
                        hoteltestR.raw.response_mock_property_review
                    ),
                    MockModelConfig.FIND_BY_CONTAINS
                )
            }
        }

        override fun getActivityIntent(): Intent {
            return HotelDetailActivity.getCallingIntent(context, "2023-10-10",
                "2023-10-11", 11, 1, 1, "region",
                "Malang", true, "HOMEPAGE")
        }
    }

    override fun getTrackerFile(): String = "tracker/travel/hotel/hotel_pdp_non_nearby.json"

    override fun scrollView() {
        activityRule.activity.findViewById<NestedScrollView>(R.id.hotelDetailNestedScrollView)
            .scrollTo(0, 100)
    }
}
