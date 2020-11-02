package com.tokopedia.hotel.cancellation.presentation.activity.mock

import android.content.Context
import com.tokopedia.hotel.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper

/**
 * @author by jessica on 22/09/20
 */

class HotelCancellationMockResponseConfig: MockModelConfig() {
    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_PROPERTY_GET_CANCELLATION,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_hotel_get_cancellation),
                FIND_BY_CONTAINS
        )
        addMockResponse(
                KEY_HOTEL_SUBMIT_CANCELLATION,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_hotel_submit_cancellation),
                FIND_BY_CONTAINS
        )
        return this
    }

    companion object {
        const val KEY_PROPERTY_GET_CANCELLATION = "propertyGetCancellation"
        const val KEY_HOTEL_SUBMIT_CANCELLATION = "hotelSubmitCancelRequest"
    }

}