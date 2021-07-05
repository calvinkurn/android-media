package com.tokopedia.shop.mock

import android.content.Context
import com.tokopedia.shop.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

class ShopPageSearchProductPageMockResponseConfig : MockModelConfig() {
    companion object {
        const val KEY_QUERY_GET_SEARCH_UNIVERSE = "universe_search"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY_GET_SEARCH_UNIVERSE,
                getRawString(context, R.raw.response_mock_data_universe_search),
                FIND_BY_CONTAINS
        )
        return this
    }
}