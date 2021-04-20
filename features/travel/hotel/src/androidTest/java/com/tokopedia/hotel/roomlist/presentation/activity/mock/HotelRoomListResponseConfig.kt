package com.tokopedia.hotel.roomlist.presentation.activity.mock

import android.content.Context
import com.tokopedia.hotel.test.R
import com.tokopedia.hotel.hoteldetail.presentation.activity.mock.HotelDetailMockResponseConfig
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper

/**
 * @author by jessica on 17/09/20
 */

class HotelRoomListResponseConfig : MockModelConfig() {
    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                HotelDetailMockResponseConfig.KEY_QUERY_GET_HOTEL_ROOM,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_get_room_list),
                FIND_BY_CONTAINS
        )
        addMockResponse(
                KEY_QUERY_HOTEL_ATC,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_hotel_add_to_cart),
                FIND_BY_CONTAINS
        )
        return this
    }

    companion object {
        const val KEY_QUERY_HOTEL_ATC = "propertyAddToCart"
    }
}