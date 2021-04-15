package com.tokopedia.flight.searchV4.presentation.activity

import android.content.Context
import com.tokopedia.flight.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

/**
 * @author by furqan on 25/08/2020
 */
class FlightSearchMockResponse : MockModelConfig() {

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_CONTAINS_SEARCH_LIST,
                getRawString(context, R.raw.response_mock_data_flight_search_list),
                FIND_BY_CONTAINS
        )
        addMockResponse(
                KEY_CONTAINS_PROMO_CHIPS,
                getRawString(context, R.raw.response_mock_data_flight_promo_chips),
                FIND_BY_QUERY_NAME
        )

        return this
    }

    companion object {
        private const val KEY_CONTAINS_SEARCH_LIST = "flightSearch"
        private const val KEY_CONTAINS_PROMO_CHIPS = "flightLowestPrice"
    }
}