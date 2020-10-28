package com.tokopedia.flight.bookingV3.presentation.activity

import android.content.Context
import com.tokopedia.flight.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

/**
 * @author by furqan on 18/09/2020
 */
class FlightBookingMockResponse : MockModelConfig() {

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_CONTAINS_PROFILE,
                getRawString(context, R.raw.response_mock_data_flight_profile),
                FIND_BY_CONTAINS
        )
        addMockResponse(
                KEY_CONTAINS_ADD_TO_CART,
                getRawString(context, R.raw.response_mock_data_flight_add_to_cart),
                FIND_BY_CONTAINS
        )
        addMockResponse(
                KEY_CONTAINS_GET_CART,
                getRawString(context, R.raw.response_mock_data_flight_get_cart),
                FIND_BY_CONTAINS
        )
        addMockResponse(
                KEY_CONTAINS_CHECKOUT,
                getRawString(context, R.raw.response_mock_data_flight_checkout),
                FIND_BY_CONTAINS
        )
        addMockResponse(
                KEY_CONTAINS_VERIFY,
                getRawString(context, R.raw.response_mock_data_flight_verify),
                FIND_BY_CONTAINS
        )
        return this
    }

    companion object {
        private const val KEY_CONTAINS_PROFILE = "profile"
        private const val KEY_CONTAINS_ADD_TO_CART = "flightAddToCart"
        private const val KEY_CONTAINS_GET_CART = "flightCart"
        private const val KEY_CONTAINS_CHECKOUT = "flightCheckout"
        private const val KEY_CONTAINS_VERIFY = "flightVerify"
    }
}