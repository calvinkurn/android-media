package com.tokopedia.hotel.hoteldetail.presentation.activity.mock

import android.content.Context
import com.tokopedia.hotel.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

/**
 * @author by jessica on 16/09/20
 */

class HotelDetailMockResponseConfig: MockModelConfig() {
    companion object {
        const val KEY_QUERY_GET_HOTEL_DETAIL = "propertyDetail"
        const val KEY_QUERY_GET_HOTEL_ROOM = "propertySearchRoom"
        const val KEY_QUERY_GET_PROPERTY_REVIEW = "propertyReview"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY_GET_HOTEL_DETAIL,
                getRawString(context, R.raw.response_mock_property_detail),
                FIND_BY_CONTAINS
        )

        addMockResponse(
                KEY_QUERY_GET_HOTEL_ROOM,
                getRawString(context, R.raw.response_mock_get_room_list),
                FIND_BY_CONTAINS
        )

        addMockResponse(
                KEY_QUERY_GET_PROPERTY_REVIEW,
                getRawString(context, R.raw.response_mock_property_review),
                FIND_BY_CONTAINS
        )
        return this
    }

}