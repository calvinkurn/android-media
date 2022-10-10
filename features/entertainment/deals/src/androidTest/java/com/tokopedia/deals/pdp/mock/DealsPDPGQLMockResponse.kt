package com.tokopedia.deals.pdp.mock

import android.content.Context
import com.tokopedia.deals.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper

class DealsPDPGQLMockResponse: MockModelConfig() {

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(KEY_DEALS_PDP_DETAIL_DATA,
            InstrumentationMockHelper.getRawString(context, R.raw.mock_gql_deals_pdp),
            FIND_BY_CONTAINS
        )
        addMockResponse(KEY_DEALS_PDP_RECOMMENDATION_DATA,
            InstrumentationMockHelper.getRawString(context, R.raw.mock_gql_deals_pdp_recommendation),
            FIND_BY_CONTAINS
        )
        return this
    }

    companion object {
        const val KEY_DEALS_PDP_DETAIL_DATA = "event_product_detail_v3"
        const val KEY_DEALS_PDP_RECOMMENDATION_DATA = "event_search"
    }
}