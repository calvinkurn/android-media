package com.tokopedia.flight.booking.data.mapper

import com.tokopedia.flight.R
import com.tokopedia.flight.common.constant.FlightErrorConstant

/**
 * @author by jessica on 2019-11-08
 */

class FlightBookingErrorCodeMapper {
    companion object {
        fun mapToFlightErrorCode(errorCode: Int): String {
            return when (errorCode) {
                56, 73, 83, 731, 732, 733, 734, 735 -> FlightErrorConstant.FLIGHT_SOLD_OUT
                1, 2, 3, 4, 8, 9, 13, 14, 15,
                16, 31, 36, 37, 42, 53, 78, 79,
                80, 84, 90, 92, 96, 97, 99, 226 -> FlightErrorConstant.INVALID_JSON
                108, 225 -> FlightErrorConstant.FAILED_ADD_FACILITY
                28 -> FlightErrorConstant.ERROR_PROMO_CODE
                86, 106, 107, 224, 227 -> FlightErrorConstant.FLIGHT_DUPLICATE_BOOKING
                44 -> FlightErrorConstant.FLIGHT_STILL_IN_PROCESS
                62 -> FlightErrorConstant.FLIGHT_DUPLICATE_USER_NAME
                22, 23, 98 -> FlightErrorConstant.FLIGHT_INVALID_USER
                1335 -> FlightErrorConstant.FLIGHT_ERROR_GET_CART_EXCEED_MAX_RETRY
                1337 -> FlightErrorConstant.FLIGHT_ERROR_VERIFY_EXCEED_MAX_RETRY
                1339 -> FlightErrorConstant.FLIGHT_ERROR_ON_CHECKOUT_GENERAL
                else ->FlightErrorConstant.INVALID_JSON
            }
        }

        fun getErrorIcon(errorCode: String): Int {
            return when (errorCode) {
                FlightErrorConstant.FLIGHT_SOLD_OUT -> R.drawable.ic_travel_no_ticket
                FlightErrorConstant.INVALID_JSON -> R.drawable.ic_flight_booking_error_refresh
                FlightErrorConstant.FAILED_ADD_FACILITY -> R.drawable.ic_flight_booking_error_add_luggage
                FlightErrorConstant.ERROR_PROMO_CODE -> R.drawable.ic_flight_booking_error_promo_code
                FlightErrorConstant.FLIGHT_DUPLICATE_BOOKING -> R.drawable.ic_flight_booking_error_wait
                FlightErrorConstant.FLIGHT_STILL_IN_PROCESS -> R.drawable.ic_flight_booking_error_wait
                FlightErrorConstant.FLIGHT_INVALID_USER -> R.drawable.ic_flight_booking_error_invalid_passenger
                else -> R.drawable.ic_flight_booking_error_refresh
            }
        }
    }
}