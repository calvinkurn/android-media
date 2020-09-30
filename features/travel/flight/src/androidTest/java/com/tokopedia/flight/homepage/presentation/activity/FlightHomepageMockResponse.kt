package com.tokopedia.flight.homepage.presentation.activity

import android.content.Context
import com.tokopedia.flight.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

/**
 * @author by furqan on 02/09/2020
 */
class FlightHomepageMockResponse : MockModelConfig() {

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_CONTAINS_HOMEPAGE_BANNER,
                getRawString(context, R.raw.response_mock_data_flight_homepage_banner),
                FIND_BY_CONTAINS
        )
        addMockResponse(
                KEY_CONTAINS_FLIGHT_POPULAR_CITY,
                getRawString(context, R.raw.response_mock_data_flight_popular_city),
                FIND_BY_CONTAINS
        )
        addMockResponse(
                KEY_CONTAINS_FLIGHT_FARE,
                getRawString(context, R.raw.response_mock_data_flight_calendar_fare),
                FIND_BY_CONTAINS
        )
        addMockResponse(
                KEY_CONTAINS_CALENDAR_HOLIDAY,
                getRawString(context, R.raw.response_mock_data_flight_calendar_holiday),
                FIND_BY_CONTAINS
        )

        return this
    }

    companion object {
        private const val KEY_CONTAINS_HOMEPAGE_BANNER = "travelCollectiveBanner"
        private const val KEY_CONTAINS_FLIGHT_POPULAR_CITY = "flightPopularCity"
        private const val KEY_CONTAINS_FLIGHT_FARE = "flightFare"
        private const val KEY_CONTAINS_CALENDAR_HOLIDAY = "TravelGetHoliday"
    }
}