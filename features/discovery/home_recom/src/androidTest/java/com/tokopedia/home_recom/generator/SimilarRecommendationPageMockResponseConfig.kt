package com.tokopedia.home_recom.generator

import android.content.Context
import com.tokopedia.home_recom.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

class SimilarRecommendationPageMockResponseConfig: MockModelConfig() {
    companion object {
        const val KEY_QUERY_SIMILAR_RECOMMENDATION = "productRecommendationWidgetSingle"
    }
    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY_SIMILAR_RECOMMENDATION,
                getRawString(context, R.raw.response_mock_data_recom_single),
                FIND_BY_CONTAINS)

        return this
    }
}
