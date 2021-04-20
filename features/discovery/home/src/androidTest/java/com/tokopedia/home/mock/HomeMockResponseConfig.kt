package com.tokopedia.home.mock

import android.content.Context
import com.tokopedia.home.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

class HomeMockResponseConfig: MockModelConfig() {
    companion object {
        const val KEY_QUERY_DYNAMIC_HOME_CHANNEL = "homeData"
        const val KEY_QUERY_DYNAMIC_HOME_CHANNEL_ATF_1 = "channel_ids=65312"
        const val KEY_QUERY_DYNAMIC_HOME_CHANNEL_ATF_2 = "channel_ids=45397"

        const val KEY_QUERY_DYNAMIC_HOME_CHANNEL_ONLY = "dynamicHomeChannel"
        const val KEY_QUERY_DYNAMIC_POSITION = "dynamicPosition"
        const val KEY_QUERY_DYNAMIC_POSITION_ICON = "homeIcon"
        const val KEY_QUERY_DYNAMIC_POSITION_TICKER = "homeTicker"
        const val KEY_QUERY_DYNAMIC_HOME_POPULAR_KEYWORD = "PopularKeywords"
        const val KEY_QUERY_DYNAMIC_HOME_SUCCESS_OCC = "add_to_cart_occ"
        const val KEY_CONTAINS_WIDGET_TAB = "widget_tab"
        const val KEY_CONTAINS_WIDGET_GRID = "widget_grid"
        const val KEY_CONTAINS_DYNAMIC_HOME_RECOM_FEED = "recommendation_product"
        const val KEY_CONTAINS_SUGGESTED_REVIEW = "suggestedProductReview"
        const val KEY_CONTAINS_PLAY_DC = "playGetLiveDynamicChannels"
        const val KEY_CONTAINS_RECHARGE = "rechargeRecommendation"
        const val KEY_CONTAINS_RECHARGE_BU_WIDGET = "getBUWidget"
        const val KEY_CONTAINS_SALAM = "salamWidget"
    }
    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY_DYNAMIC_HOME_CHANNEL,
                getRawString(context, R.raw.response_mock_data_dynamic_home_channel),
                FIND_BY_QUERY_NAME)

        addMockResponse(
                KEY_QUERY_DYNAMIC_HOME_CHANNEL_ATF_1,
                getRawString(context, R.raw.response_mock_data_dynamic_home_channel_atf_1),
                FIND_BY_CONTAINS)

        addMockResponse(
                KEY_QUERY_DYNAMIC_HOME_CHANNEL_ATF_2,
                getRawString(context, R.raw.response_mock_data_dynamic_home_channel_atf_2),
                FIND_BY_CONTAINS)

        addMockResponse(
                KEY_QUERY_DYNAMIC_POSITION,
                getRawString(context, R.raw.response_mock_data_dynamic_position),
                FIND_BY_CONTAINS)

        addMockResponse(
                KEY_QUERY_DYNAMIC_POSITION_ICON,
                getRawString(context, R.raw.response_mock_data_dynamic_position_icon),
                FIND_BY_CONTAINS)

        addMockResponse(
                KEY_QUERY_DYNAMIC_POSITION_TICKER,
                getRawString(context, R.raw.response_mock_data_dynamic_position_ticker),
                FIND_BY_CONTAINS)

        addMockResponse(
                KEY_QUERY_DYNAMIC_HOME_CHANNEL_ONLY,
                getRawString(context, R.raw.response_mock_data_dynamic_home_channel_only),
                FIND_BY_CONTAINS)

        addMockResponse(
                KEY_CONTAINS_DYNAMIC_HOME_RECOM_FEED,
                getRawString(context, R.raw.response_mock_data_dynamic_home_recom_feed),
                FIND_BY_CONTAINS)

        addMockResponse(
                KEY_QUERY_DYNAMIC_HOME_POPULAR_KEYWORD,
                getRawString(context, R.raw.response_mock_data_dynamic_home_popular_keyword),
                FIND_BY_QUERY_NAME)

        addMockResponse(
                KEY_QUERY_DYNAMIC_HOME_SUCCESS_OCC,
                getRawString(context, R.raw.response_mock_data_home_success_occ),
                FIND_BY_QUERY_NAME)

        addMockResponse(
                KEY_CONTAINS_WIDGET_TAB,
                getRawString(context, R.raw.response_mock_data_home_widget_tab),
                FIND_BY_CONTAINS)

        addMockResponse(
                KEY_CONTAINS_WIDGET_GRID,
                getRawString(context, R.raw.response_mock_data_home_widget_grid),
                FIND_BY_CONTAINS)

        addMockResponse(
                KEY_CONTAINS_SUGGESTED_REVIEW,
                getRawString(context, R.raw.response_mock_data_suggested_review),
                FIND_BY_CONTAINS)

        addMockResponse(
                KEY_CONTAINS_PLAY_DC,
                getRawString(context, R.raw.response_mock_data_play_widget),
                FIND_BY_CONTAINS)

        addMockResponse(
                KEY_CONTAINS_RECHARGE,
                getRawString(context, R.raw.response_mock_data_recharge_recommendation),
                FIND_BY_CONTAINS)

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

        return this
    }
}