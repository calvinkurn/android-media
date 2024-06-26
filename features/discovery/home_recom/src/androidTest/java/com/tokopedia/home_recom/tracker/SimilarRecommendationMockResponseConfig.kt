package com.tokopedia.home_recom.tracker

import android.content.Context
import com.tokopedia.instrumentation.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

class SimilarRecommendationMockResponseConfig : MockModelConfig() {
    companion object {
        const val KEY_QUERY_SIMILAR_RECOMMENDATION = "productRecommendationWidgetSingle("
        const val KEY_QUERY_SIMILAR_RECOMMENDATION_V2 = "productRecommendationWidgetSingleV2("
    }
    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
            KEY_QUERY_SIMILAR_RECOMMENDATION,
            getRawString(context, R.raw.response_mock_data_similar_recom),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            KEY_QUERY_SIMILAR_RECOMMENDATION_V2,
            getRawString(context, R.raw.response_mock_data_similar_recom_v2),
            FIND_BY_CONTAINS
        )

        return this
    }
}
