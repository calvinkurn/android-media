package com.tokopedia.navigation.com.tokopedia.navigation.mock

import android.content.Context
import com.tokopedia.navigation.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

class MainHomeMockResponseConfig: MockModelConfig() {
    companion object {
        const val KEY_QUERY_DYNAMIC_HOME_CHANNEL_ATF_1 = "channel_ids=65312"
        const val KEY_QUERY_DYNAMIC_HOME_CHANNEL_ATF_2 = "channel_ids=45397"
        const val KEY_QUERY_DYNAMIC_HOME_CHANNEL_ONLY = "dynamicHomeChannel"
        const val KEY_QUERY_DYNAMIC_POSITION = "dynamicPosition"
        const val KEY_QUERY_DYNAMIC_POSITION_ICON = "homeIcon"
    }
    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY_DYNAMIC_HOME_CHANNEL_ONLY,
                getRawString(context, R.raw.response_mock_data_dynamic_home_channel_only),
                FIND_BY_CONTAINS)

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

        return this
    }
}