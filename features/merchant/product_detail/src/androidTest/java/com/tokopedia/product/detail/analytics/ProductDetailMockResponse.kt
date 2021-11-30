package com.tokopedia.product.detail.analytics

import android.content.Context
import com.tokopedia.product.detail.util.ResponseUtil.getJsonFromResource
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig

class ProductDetailMockResponse: MockModelConfig() {

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(KEY_CONTAINS_PDP_GET_LAYOUT, getJsonFromResource(PATH_P1_CASSAVA), FIND_BY_CONTAINS)
        addMockResponse(KEY_CONTAINS_PDP_GET_DATA, getJsonFromResource(PATH_P2_CASSAVA), FIND_BY_CONTAINS)
        addMockResponse(KEY_CONTAINS_DISCUSSION_MOST_HELPFUL, getJsonFromResource(DISCUSSION_MOST_HELPFUL_PATH), FIND_BY_CONTAINS)
        return this
    }

    companion object {
        private const val KEY_CONTAINS_PDP_GET_LAYOUT = "pdpGetLayout"
        private const val KEY_CONTAINS_PDP_GET_DATA = "GetPdpGetData"
        private const val KEY_CONTAINS_DISCUSSION_MOST_HELPFUL = "discussionMostHelpful"
        const val DISCUSSION_MOST_HELPFUL_PATH = "raw/response_mock_discussion_most_helpful.json"
        const val PATH_P1_CASSAVA = "raw/response_pdp_p1_cassava.json"
        const val PATH_P2_CASSAVA = "raw/response_pdp_p2_cassava.json"
    }

}