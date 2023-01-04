package com.tokopedia.home_recom.tracker

import android.content.Context
import com.tokopedia.instrumentation.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

class RecommendationMockResponseConfig : MockModelConfig() {
    companion object {
        const val KEY_QUERY_PRODUCT_DETAIL_RECOM = "productRecommendationProductDetail("
        const val KEY_QUERY_WIDGET_RECOM = "productRecommendationWidget("
        const val KEY_QUERY_PRODUCT_DETAIL_RECOM_V2 = "productRecommendationProductDetailV2("
        const val KEY_QUERY_WIDGET_RECOM_V2 = "productRecommendationWidgetV2("
    }
    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
            KEY_QUERY_PRODUCT_DETAIL_RECOM,
            getRawString(context, R.raw.response_mock_data_recom_product_detail),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            KEY_QUERY_WIDGET_RECOM,
            getRawString(context, R.raw.response_mock_data_recom_widget),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            KEY_QUERY_PRODUCT_DETAIL_RECOM_V2,
            getRawString(context, R.raw.response_mock_data_recom_product_detail_v2),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            KEY_QUERY_WIDGET_RECOM_V2,
            getRawString(context, R.raw.response_mock_data_recom_widget),
            FIND_BY_CONTAINS
        )

        return this
    }
}
