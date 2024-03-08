package com.tokopedia.hotel.hoteldetail.presentation.activity

import android.content.Intent
import androidx.core.widget.NestedScrollView
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.hotel.hoteldetail.presentation.activity.mock.HotelDetailMockResponseConfig
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Rule
import com.tokopedia.hotel.R as hotelR
import com.tokopedia.hotel.test.R as hoteltestR

/**
 * @author by jessica on 16/09/20
 */
class HotelDetailActivityTest : BaseHotelPDPTest() {

    @get:Rule
    var activityRule: IntentsTestRule<HotelDetailActivity> = object : IntentsTestRule<HotelDetailActivity>(HotelDetailActivity::class.java) {

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponse {
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
                addMockResponse(
                    HotelDetailMockResponseConfig.KEY_QUERY_GET_NEARBY_LANDMARK,
                    InstrumentationMockHelper.getRawString(
                        context,
                        hoteltestR.raw.response_mock_nearby_landmark
                    ),
                    MockModelConfig.FIND_BY_CONTAINS
                )
            }
        }

        override fun getActivityIntent(): Intent {
            return HotelDetailActivity.getCallingIntent(
                context, "2023-10-10",
                "2023-10-11", 11, 1, 1, "region",
                "Jakarta", true, "HOMEPAGE"
            )
        }
    }

    override fun getTrackerFile(): String = "tracker/travel/hotel/hotel_pdp.json"

    override fun scrollView() {
        activityRule.activity.findViewById<NestedScrollView>(hotelR.id.hotelDetailNestedScrollView)
            .scrollTo(0, 100)
    }
}
