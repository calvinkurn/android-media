package com.tokopedia.product.detail.analytics

import android.content.Context
import com.tokopedia.product.detail.util.ResponseUtil.getJsonFromResource
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig

class ProductDetailMockResponse: MockModelConfig() {

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(KEY_CONTAINS_PDP_GET_LAYOUT, getJsonFromResource(PATH_P1_CASSAVA), FIND_BY_CONTAINS)
        addMockResponse(KEY_CONTAINS_PDP_GET_DATA, getJsonFromResource(PATH_P2_CASSAVA), FIND_BY_CONTAINS)
        addMockResponse(KEY_ATC_CASSAVA, getJsonFromResource(PATH_SUCCESS_ATC_CASSAVA), FIND_BY_CONTAINS)
        addMockResponse(KEY_P2_LOGIN, getJsonFromResource(PATH_P2_LOGIN), FIND_BY_CONTAINS)
        addMockResponse(KEY_CONTAINS_DISCUSSION_MOST_HELPFUL, getJsonFromResource(DISCUSSION_MOST_HELPFUL_PATH), FIND_BY_CONTAINS)
        return this
    }

    companion object {
        private const val KEY_CONTAINS_PDP_GET_LAYOUT = "pdpGetLayout"
        private const val KEY_CONTAINS_PDP_GET_DATA = "GetPdpGetData"
        private const val KEY_ATC_CASSAVA = "add_to_cart_v2"
        private const val KEY_P2_LOGIN = "getProductIsWishlisted"
        private const val KEY_CONTAINS_DISCUSSION_MOST_HELPFUL = "discussionMostHelpful"
        const val DISCUSSION_MOST_HELPFUL_PATH = "raw/response_mock_discussion_most_helpful.json"
        const val PATH_P1_CASSAVA = "raw/response_pdp_p1_cassava.json"
        const val PATH_P2_CASSAVA = "raw/response_pdp_p2_cassava.json"
        const val PATH_SUCCESS_ATC_CASSAVA = "raw/response_success_atc_cassava.json"
        const val PATH_P2_LOGIN = "raw/response_success_p2_login.json"
    }

}