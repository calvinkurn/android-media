package com.tokopedia.home.mock

import android.content.Context
import com.tokopedia.home.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

internal open class HomeAtfPositionErrorResponseConfig : HomeMockResponseConfig() {
    override fun updateMock(context: Context) {
        /**
         * Error response for ATF position
         */

        addMockResponse(
            KEY_QUERY_DYNAMIC_POSITION,
            getRawString(context, R.raw.response_error_mock_data_dynamic_position),
            FIND_BY_CONTAINS
        )

        /**
         * End of error response for ATF position
         */
    }
}