package com.tokopedia.deals.search.ui.activity.mock

import android.content.Context
import com.tokopedia.deals.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper

class DealsSearchMockResponse : MockModelConfig() {

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(KEY_EVENT_CHILD_CATEGORY,
                InstrumentationMockHelper.getRawString(context, R.raw.mock_gql_deals_get_recent_search),
                FIND_BY_CONTAINS)

        addMockResponse(KEY_EVENT_LOCATION_SEARCH,
                InstrumentationMockHelper.getRawString(context, R.raw.mock_gql_deals_get_nearest_location),
                FIND_BY_CONTAINS)

        addMockResponse(KEY_EVENT_BRAND,
                InstrumentationMockHelper.getRawString(context, R.raw.mock_gql_deals_search_brands),
                FIND_BY_CONTAINS)

        return this
    }


    companion object {
        const val KEY_EVENT_LOCATION_SEARCH = "event_location_search"
        const val KEY_EVENT_CHILD_CATEGORY = "event_child_category"
        const val KEY_EVENT_BRAND = "event_search"
    }
}