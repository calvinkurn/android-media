package com.tokopedia.home.mock

import android.content.Context
import com.tokopedia.home.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

internal class HomeScreenshotMockResponseConfig: HomeMockResponseConfig() {
    companion object {
        const val KEY_QUERY_DYNAMIC_HOME_CHANNEL_ONLY = "dynamicHomeChannel"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        val mockModelConfig = super.createMockModel(context)
        mockModelConfig.addMockResponse(
                KEY_QUERY_DYNAMIC_HOME_CHANNEL_ONLY,
                getRawString(context, R.raw.response_mock_data_dynamic_home_channel_screenshot),
                FIND_BY_QUERY_NAME)
        return mockModelConfig
    }
}