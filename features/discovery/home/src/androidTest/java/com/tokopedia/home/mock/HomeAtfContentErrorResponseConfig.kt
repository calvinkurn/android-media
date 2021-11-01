package com.tokopedia.home.mock

import android.content.Context
import com.tokopedia.home.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

internal open class HomeAtfContentErrorResponseConfig : HomeMockResponseConfig() {
    override fun updateMock(context: Context) {
        /**
         * Error response for ATF content
         */

        addMockResponse(
            KEY_QUERY_DYNAMIC_HOME_CHANNEL_ATF_1,
            getRawString(context, R.raw.response_error_mock_data_dynamic_home_channel_atf_1),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_QUERY_DYNAMIC_HOME_CHANNEL_ATF_2,
            getRawString(context, R.raw.response_error_mock_data_dynamic_home_channel_atf_2),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_QUERY_DYNAMIC_POSITION,
            getRawString(context, R.raw.response_mock_data_dynamic_position),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_QUERY_DYNAMIC_POSITION_ICON,
            getRawString(context, R.raw.response_error_mock_data_dynamic_position_icon),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_QUERY_DYNAMIC_POSITION_TICKER,
            getRawString(context, R.raw.response_error_mock_data_dynamic_position_ticker),
            FIND_BY_CONTAINS
        )

        /**
         * End of error response for ATF content
         */
    }
}