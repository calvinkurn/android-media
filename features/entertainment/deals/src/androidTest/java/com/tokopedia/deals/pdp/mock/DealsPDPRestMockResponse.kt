package com.tokopedia.deals.pdp.mock

import android.content.Context
import com.tokopedia.deals.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper

class DealsPDPRestMockResponse: MockModelConfig() {

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
            KEY_REST_RATING,
            InstrumentationMockHelper.getRawString(context, R.raw.mock_rest_deals_rating),
            FIND_BY_CONTAINS
        )
        return this
    }

    companion object {
        const val KEY_REST_RATING = "https://booking.tokopedia.com/v1/api/deal/rating/product/28603"
    }
}