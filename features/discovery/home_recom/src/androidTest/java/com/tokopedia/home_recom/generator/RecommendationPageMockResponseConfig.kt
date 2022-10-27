package com.tokopedia.home_recom.generator

import android.content.Context
import com.tokopedia.instrumentation.test.R
import com.tokopedia.home_recom.R as homeRecomR
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

class RecommendationPageMockResponseConfig: MockModelConfig() {
    companion object {
        const val KEY_QUERY_PRODUCT_DETAIL_RECOM = "productRecommendationProductDetail"
        const val KEY_QUERY_WIDGET_RECOM = "productRecommendationWidget"
    }
    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY_PRODUCT_DETAIL_RECOM,
                getRawString(context, R.raw.response_mock_data_recom_product_detail),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_WIDGET_RECOM,
                getRawString(context, homeRecomR.raw.response_mock_data_recom_page),
                FIND_BY_CONTAINS)

        return this
    }
}
