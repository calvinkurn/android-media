package com.tokopedia.home.mock

import android.content.Context
import com.tokopedia.home.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

internal open class HomeDynamicChannelErrorResponseConfig : HomeMockResponseConfig() {
    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
            KEY_QUERY_DYNAMIC_HOME_CHANNEL_ATF_1,
            getRawString(context, R.raw.response_mock_data_dynamic_home_channel_atf_1),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_QUERY_DYNAMIC_HOME_CHANNEL_ATF_2,
            getRawString(context, R.raw.response_mock_data_dynamic_home_channel_atf_2),
            FIND_BY_CONTAINS
        )

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


        addMockResponse(
            KEY_QUERY_DYNAMIC_POSITION,
            getRawString(context, R.raw.response_mock_data_dynamic_position),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_QUERY_DYNAMIC_POSITION_ICON,
            getRawString(context, R.raw.response_mock_data_dynamic_position_icon),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_QUERY_DYNAMIC_POSITION_TICKER,
            getRawString(context, R.raw.response_mock_data_dynamic_position_ticker),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_DYNAMIC_HOME_RECOM_FEED,
            getRawString(context, R.raw.response_mock_data_dynamic_home_recom_feed),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_POPULAR_KEYWORDS,
            getRawString(context, R.raw.response_mock_data_dynamic_home_popular_keyword),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_QUERY_DYNAMIC_HOME_SUCCESS_OCC,
            getRawString(context, R.raw.response_mock_data_home_success_occ),
            FIND_BY_QUERY_NAME
        )

        addMockResponse(
            KEY_CONTAINS_WIDGET_TAB,
            getRawString(context, R.raw.response_mock_data_home_widget_tab),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_WIDGET_GRID,
            getRawString(context, R.raw.response_mock_data_home_widget_grid),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_SUGGESTED_REVIEW,
            getRawString(context, R.raw.response_mock_data_suggested_review),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_PLAY_DC,
            getRawString(context, R.raw.response_mock_data_play_widget),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_RECHARGE,
            getRawString(context, R.raw.response_mock_data_recharge_recommendation),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_SALAM,
            getRawString(context, R.raw.response_mock_data_salam_content),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_RECHARGE_BU_WIDGET,
            getRawString(context, R.raw.response_mock_data_recharge_bu_widget),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_RECOMMENDATION_TAB,
            getRawString(context, R.raw.response_mock_data_dynamic_home_recom_feed_tab),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_UNIVERSE_PLACEHOLDER,
            getRawString(context, R.raw.response_mock_data_universe_placeholder),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_QUERY_FLOATING_EGG,
            getRawString(context, R.raw.response_mock_data_home_egg),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_WALLET,
            getRawString(context, R.raw.response_mock_data_wallet),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_TOKOPOINTS_LIST,
            getRawString(context, R.raw.response_mock_data_tokopoints_list),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_FLAG,
            getRawString(context, R.raw.response_mock_data_home_flag),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_PRODUCT_REVIEW,
            getRawString(context, R.raw.response_mock_data_suggested_product_review),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_PRODUCT_RECOMMENDATION,
            getRawString(context, R.raw.response_mock_data_best_selling_widget),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_HEADLINE_ADS,
            getRawString(context, R.raw.response_mock_data_headline_ads),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_CHIPS,
            getRawString(context, R.raw.response_mock_data_best_seller_chips),
            FIND_BY_CONTAINS
        )

        return this
    }
}