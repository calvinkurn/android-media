package com.tokopedia.pms.analytics

import android.content.Context
import com.tokopedia.pms.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper

class PmsDetailMockResponse: MockModelConfig() {
    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(GQL_PAYMENT_LIST, InstrumentationMockHelper.getRawString(context,  R.raw.response_deferred_payments), FIND_BY_CONTAINS)
        //addMockResponse(GQL_NOTIFICATION_COUNT, InstrumentationMockHelper.getRawString(context, com.tokopedia.pms.test.R.raw.), FIND_BY_CONTAINS)
        return this
    }

    companion object {
        const val GQL_PAYMENT_LIST = "paymentList"
        const val GQL_NOTIFICATION_COUNT = "notification_status"
    }
}