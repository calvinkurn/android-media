package com.tokopedia.pms.analytics

import android.content.Context
import com.tokopedia.pms.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper

class PmsDetailMockResponse: MockModelConfig() {
    override fun createMockModel(context: Context): MockModelConfig {
        //addMockResponse(GQL_PAYMENT_LIST, InstrumentationMockHelper.getRawString(context,  R.raw.response_deferred_payments), FIND_BY_CONTAINS)
        //addMockResponse(GQL_CANCEL_DETAIL, InstrumentationMockHelper.getRawString(context, R.raw.response_cancel_detail), FIND_BY_CONTAINS)
        return this
    }

    companion object {
        const val GQL_PAYMENT_LIST = "paymentList"
        const val GQL_NOTIFICATION_COUNT = "notification_status"
        const val GQL_CANCEL_DETAIL = "cancelDetail"
        const val GQL_KLIC_BCA_EDIT = "editKlikbca"
        const val GQL_BANK_DETAIL_EDIT = "editTransfer"
    }
}