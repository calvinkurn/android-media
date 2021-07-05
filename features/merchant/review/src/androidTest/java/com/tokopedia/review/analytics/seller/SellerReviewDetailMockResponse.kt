package com.tokopedia.review.analytics.seller

import android.content.Context
import com.tokopedia.review.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

class SellerReviewDetailMockResponse: MockModelConfig() {

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(KEY_CONTAINS_FEEDBACK_DATA_PER_PRODUCT, getRawString(context, R.raw.review_detail_feedback_data_per_product), FIND_BY_CONTAINS)
        return this
    }

    companion object {
        private const val KEY_CONTAINS_FEEDBACK_DATA_PER_PRODUCT = "productrevFeedbackDataPerProduct"
    }

}