package com.tokopedia.deals.brand.ui.activity.mock

import android.content.Context
import com.tokopedia.deals.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper

class DealsBrandsMockResponse : MockModelConfig() {

    override fun createMockModel(context: Context): MockModelConfig {

        addMockResponse(KEY_EVENT_BRAND_PAGE,
                InstrumentationMockHelper.getRawString(context, R.raw.mock_gql_deals_get_brandpage), FIND_BY_CONTAINS)
        return this
    }


    companion object {
        const val KEY_EVENT_BRAND_PAGE = "event_search"
    }
}