package com.tokopedia.activation

import android.content.Context
import com.tokopedia.pdpsimulation.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper

internal open class ActivationCheckoutMockResponseConfig : MockModelConfig() {

    companion object {
        const val OPTIMIZED_CHECKOUT_KEY = "paylater_getOptimizedCheckout"
        const val PRODUCT_V3_KEY = "GetProductV3"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
            PRODUCT_V3_KEY,
            InstrumentationMockHelper.getRawString(
                context,
                R.raw.product_detail_response_lessamount
            ),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            OPTIMIZED_CHECKOUT_KEY,
            InstrumentationMockHelper.getRawString(context, R.raw.optimized_checkout_response),
            FIND_BY_CONTAINS
        )
        return this
    }

}