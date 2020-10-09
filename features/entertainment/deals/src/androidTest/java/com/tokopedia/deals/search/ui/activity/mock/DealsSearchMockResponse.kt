package com.tokopedia.deals.search.ui.activity.mock

import android.content.Context
import com.tokopedia.deals.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper

class DealsSearchMockResponse : MockModelConfig() {

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(KEY_EVENT_SEARCH_INITIAL,
                InstrumentationMockHelper.getRawString(context, R.raw.mock_gql_deals_get_search_initial), FIND_BY_CONTAINS)

        addMockResponse(KEY_EVENT_LOCATION_SEARCH,
                InstrumentationMockHelper.getRawString(context, R.raw.mock_gql_deals_get_nearest_location),
                FIND_BY_CONTAINS)

        addMockResponse(KEY_EVENT_LOAD_MORE,
                InstrumentationMockHelper.getRawString(context, R.raw.mock_gql_deals_get_search_load_more), FIND_BY_CONTAINS)

        addMockResponse(KEY_EVENT_RESULT_BRAND,
                InstrumentationMockHelper.getRawString(context, R.raw.mock_gql_deals_search_brands), FIND_BY_CONTAINS)

        addMockResponse(KEY_EVENT_RESULT_BRAND,
                InstrumentationMockHelper.getRawString(context, R.raw.mock_gql_deals_get_search_load_more), FIND_BY_CONTAINS)

        addMockResponse(KEY_EVENT_RESULT_PRODUCT,
                InstrumentationMockHelper.getRawString(context, R.raw.mock_gql_deals_search_brands_product), FIND_BY_CONTAINS)
        return this
    }


    companion object {
        const val KEY_EVENT_SEARCH_INITIAL = "event_seacrh"
        const val KEY_EVENT_LOAD_MORE = "event_search"
        const val KEY_EVENT_RESULT_PRODUCT = "event_search"
        const val KEY_EVENT_RESULT_BRAND = ""
        const val KEY_EVENT_LOCATION_SEARCH = "event_location_search"
    }
}