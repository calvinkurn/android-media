package com.tokopedia.flight.bookingV3.data.mapper

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
                80, 84, 92, 96, 97, 99-> FlightErrorConstant.INVALID_JSON
                108 -> FlightErrorConstant.FAILED_ADD_FACILITY
                28 -> FlightErrorConstant.ERROR_PROMO_CODE
                86, 106, 107 -> FlightErrorConstant.FLIGHT_DUPLICATE_BOOKING
                44 -> FlightErrorConstant.FLIGHT_STILL_IN_PROCESS
                62 -> FlightErrorConstant.FLIGHT_DUPLICATE_USER_NAME
                22, 23, 98 -> FlightErrorConstant.FLIGHT_INVALID_USER
                else -> FlightErrorConstant.INVALID_JSON
            }
        }
    }
}