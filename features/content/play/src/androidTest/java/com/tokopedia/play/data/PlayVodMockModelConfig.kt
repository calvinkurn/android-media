package com.tokopedia.play.data

import android.content.Context
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig


/**
 * Created by mzennis on 03/09/20.
 */
class PlayVodMockModelConfig : MockModelConfig() {

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY,
                RESPONSE_MOCK_CHANNEL_DETAIL_VOD,
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
                KEY_QUERY_PINNED_PRODUCT,
                RESPONSE_MOCK_PINNED_PRODUCT,
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_PRODUCT_VARIANT,
                RESPONSE_MOCK_PRODUCT_VARIANT,
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_MUTATION_ADD_TO_CART,
                RESPONSE_MOCK_ADD_TO_CART,
                FIND_BY_CONTAINS)
        return this
    }
}
