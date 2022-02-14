package com.tokopedia.review.analytics.seller.mockresponse

import android.content.Context
import com.tokopedia.review.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper

class SellerReviewListMockResponse : MockModelConfig() {

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
            KEY_CONTAINS_PRODUCT_RATING_OVERALL,
            InstrumentationMockHelper.getRawString(
                context,
                R.raw.review_list_product_review_aggregate
            ),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            KEY_CONTAINS_PRODUCT_RATING_LIST,
            InstrumentationMockHelper.getRawString(context, R.raw.review_list_product_review_list),
            FIND_BY_CONTAINS
        )
        return this
    }

    companion object {
        private const val KEY_CONTAINS_PRODUCT_RATING_OVERALL =
            "productrevGetProductRatingOverallByShop"
        private const val KEY_CONTAINS_PRODUCT_RATING_LIST = "productrevShopRatingAggregate"
    }
}