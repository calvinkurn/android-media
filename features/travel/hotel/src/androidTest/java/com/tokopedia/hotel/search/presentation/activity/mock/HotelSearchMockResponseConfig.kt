package com.tokopedia.hotel.search.presentation.activity.mock

import android.content.Context
import com.tokopedia.hotel.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

/**
 * @author by jessica on 14/09/20
 */

class HotelSearchMockResponseConfig: MockModelConfig() {
    companion object {
        const val KEY_QUERY_HOTEL_SEARCHES = "propertySearch"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY_HOTEL_SEARCHES,
                getRawString(context, R.raw.response_mock_hotel_search_result),
                FIND_BY_CONTAINS
        )

        return this
    }

}