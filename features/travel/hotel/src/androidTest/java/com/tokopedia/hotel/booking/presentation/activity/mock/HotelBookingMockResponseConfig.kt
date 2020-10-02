package com.tokopedia.hotel.booking.presentation.activity.mock

import android.content.Context
import com.tokopedia.hotel.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper

/**
 * @author by jessica on 22/09/20
 */

class HotelBookingMockResponseConfig: MockModelConfig() {
    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_PROPERTY_GET_CART,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_hotel_get_cart),
                FIND_BY_CONTAINS
        )
        addMockResponse(
                KEY_PROPERTY_CHECKOUT,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_hotel_checkout),
                FIND_BY_CONTAINS
        )
        return this
    }

    companion object {
        const val KEY_PROPERTY_CHECKOUT = "propertyCheckout"
        const val KEY_PROPERTY_GET_CART = "propertyGetCart"
    }
}