package com.tokopedia.discovery2.config

import android.content.Context
import com.tokopedia.discovery2.data.RESPONSE_MOCK_CAROUSEL_DATA
import com.tokopedia.discovery2.data.RESPONSE_MOCK_DISCOVERY_PAGE_DATA
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig


class DiscoveryTopadsMockModelConfig : MockModelConfig() {

    companion object {
        private const val KEY_QUERY_DISCOVERY_PAGE = "discoveryPageInfo"
        private const val KEY_QUERY_COMPONENT_DATA = "componentInfo"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY_DISCOVERY_PAGE,
                RESPONSE_MOCK_DISCOVERY_PAGE_DATA,
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_COMPONENT_DATA,
                RESPONSE_MOCK_CAROUSEL_DATA,
                FIND_BY_CONTAINS)
        return this
    }


}
