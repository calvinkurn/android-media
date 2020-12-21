package com.tokopedia.product.detail.analytics

import android.content.Context
import com.tokopedia.instrumentation.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper

class ProductDetailMockResponse: MockModelConfig() {

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(KEY_CONTAINS_PDP_GET_LAYOUT, InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_pdp_get_layout), FIND_BY_CONTAINS)
        addMockResponse(KEY_CONTAINS_DISCUSSION_MOST_HELPFUL, InstrumentationMockHelper.getRawString(context, com.tokopedia.product.detail.test.R.raw.response_mock_discussion_most_helpful), FIND_BY_CONTAINS)
        return this
    }

    companion object {
        private const val KEY_CONTAINS_PDP_GET_LAYOUT = "pdpGetLayout"
        private const val KEY_CONTAINS_DISCUSSION_MOST_HELPFUL = "discussionMostHelpful"
    }

}