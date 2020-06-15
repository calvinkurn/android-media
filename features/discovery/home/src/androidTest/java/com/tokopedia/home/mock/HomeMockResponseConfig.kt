package com.tokopedia.home.mock

import android.content.Context
import com.tokopedia.instrumentation.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

class HomeMockResponseConfig: MockModelConfig() {
    companion object {
        const val KEY_QUERY_DYNAMIC_HOME_CHANNEL = "homeData"
    }
    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY_DYNAMIC_HOME_CHANNEL,
                getRawString(context, R.raw.response_mock_data_dynamic_home_channel),
                FIND_BY_QUERY_NAME)

        addMockResponse(
                "widget_tab",
                getRawString(context, R.raw.response_mock_data_home_widget_tab),
                FIND_BY_CONTAINS)

        addMockResponse(
                "widget_grid",
                getRawString(context, R.raw.response_mock_data_home_widget_grid),
                FIND_BY_CONTAINS)

        addMockResponse(
                "suggestedProductReview",
                getRawString(context, R.raw.response_mock_data_suggested_review),
                FIND_BY_CONTAINS)

        addMockResponse(
                "playGetLiveDynamicChannels",
                getRawString(context, R.raw.response_mock_data_play_widget),
                FIND_BY_CONTAINS)

        addMockResponse(
                "rechargeRecommendation",
                getRawString(context, R.raw.response_mock_data_recharge_recommendation),
                FIND_BY_CONTAINS)

        return this
    }
}