package com.tokopedia.hotel.homepage.presentation.activity.mock

import android.content.Context
import com.tokopedia.hotel.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

/**
 * @author by jessica on 12/08/20
 */

class HotelMockResponseConfig: MockModelConfig() {
    companion object {
        const val KEY_QUERY_HOTEL_DEFAULT_HOMEPAGE = "propertyDefaultHome"
        const val KEY_QUERY_HOTEL_GET_BANNER = "getBanner"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY_HOTEL_DEFAULT_HOMEPAGE,
                getRawString(context, R.raw.response_mock_property_default_home),
                FIND_BY_QUERY_NAME
        )

        addMockResponse(
                KEY_QUERY_HOTEL_GET_BANNER,
                getRawString(context, R.raw.response_mock_travel_banner),
                FIND_BY_QUERY_NAME
        )

        return this
    }

}