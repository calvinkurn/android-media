package com.tokopedia.home.mock

import android.content.Context
import com.tokopedia.home.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

internal open class HomeDynamicChannelErrorResponseConfig : HomeMockResponseConfig() {
    override fun updateMock(context: Context) {
        /**
         * Error response for dynamic channel
         */

        addMockResponse(
            KEY_QUERY_DYNAMIC_HOME_CHANNEL_ONLY,
            getRawString(context, R.raw.response_error_mock_data_dynamic_home_channel_screenshot),
            FIND_BY_CONTAINS
        )

        /**
         * End of error response for dynamic channel
         */
    }
}