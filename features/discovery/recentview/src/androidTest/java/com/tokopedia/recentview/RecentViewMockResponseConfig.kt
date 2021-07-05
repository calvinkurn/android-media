package com.tokopedia.recentview

import android.content.Context
import com.tokopedia.instrumentation.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

class RecentViewMockResponseConfig: MockModelConfig() {
    companion object {
        const val KEY_QUERY_RECENT_VIEW = "get_recent_view"
    }
    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY_RECENT_VIEW,
                getRawString(context, R.raw.response_mock_data_recent_view),
                FIND_BY_CONTAINS)

        return this
    }
}