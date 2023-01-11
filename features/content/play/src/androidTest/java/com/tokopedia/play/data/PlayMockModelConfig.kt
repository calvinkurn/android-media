package com.tokopedia.play.data

import android.content.Context
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig


/**
 * Created by mzennis on 24/08/20.
 *
 * mock response for calculating Page Load Time
 */
class PlayMockModelConfig : MockModelConfig() {

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY_CHANNEL_DETAILS,
                RESPONSE_MOCK_CHANNEL_DETAILS,
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_SHOP_INFO,
                RESPONSE_MOCK_SHOP_INFO,
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_TOTAL_LIKE,
                RESPONSE_MOCK_TOTAL_LIKE,
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_IS_LIKED,
                RESPONSE_MOCK_IS_LIKED,
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_TOTAL_CART,
                RESPONSE_MOCK_TOTAL_CART,
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_TOTAL_CART,
                RESPONSE_MOCK_TOTAL_CART,
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_PINNED_PRODUCT,
                RESPONSE_MOCK_PINNED_PRODUCT,
                FIND_BY_CONTAINS)
        return this
    }
}
