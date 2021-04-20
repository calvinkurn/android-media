package com.tokopedia.play.data

import android.content.Context
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig


/**
 * Created by mzennis on 03/09/20.
 */
class PlayLiveMockModelConfig : MockModelConfig() {

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY,
                RESPONSE_MOCK_CHANNEL_DETAIL_LIVE,
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
                KEY_MUTATION_FOLLOW_SHOP,
                RESPONSE_MOCK_SHOP_FOLLOW,
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_MUTATION_LIKE_CHANNEL,
                RESPONSE_MOCK_LIKE_CHANNEL,
                FIND_BY_CONTAINS)
        return this
    }
}
