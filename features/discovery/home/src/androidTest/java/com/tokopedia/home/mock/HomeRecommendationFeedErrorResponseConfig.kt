package com.tokopedia.home.mock

import android.content.Context
import com.tokopedia.home.test.R
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

internal open class HomeRecommendationFeedErrorResponseConfig : HomeMockResponseConfig() {
    override fun updateMock(context: Context) {
        /**
         * Error response for recommendation tab
         */

        addMockResponse(
            KEY_CONTAINS_RECOMMENDATION_TAB,
            getRawString(context, R.raw.response_error_mock_data_dynamic_home_recom_feed_tab),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_RECOMMENDATION_TAB_V2,
            getRawString(context, R.raw.response_error_mock_data_dynamic_home_recom_feed_tab),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_DYNAMIC_HOME_RECOM_FEED,
            getRawString(context, R.raw.response_error_mock_data_dynamic_home_recom_feed_tab),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_DYNAMIC_HOME_RECOM_FEED_V2,
            getRawString(context, R.raw.response_error_mock_data_dynamic_home_recom_feed_tab),
            FIND_BY_CONTAINS
        )

        /**
         * End of error response for recommendation tab
         */
    }
}
