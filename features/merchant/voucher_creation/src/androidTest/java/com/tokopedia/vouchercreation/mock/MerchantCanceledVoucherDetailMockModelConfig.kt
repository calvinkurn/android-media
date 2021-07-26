package com.tokopedia.vouchercreation.mock

import android.content.Context
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.vouchercreation.test.R

class MerchantCanceledVoucherDetailMockModelConfig : MockModelConfig() {
    companion object {
        private const val GET_VOUCHER_BY_ID = "GetVoucherDataById"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                GET_VOUCHER_BY_ID,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_get_canceled_voucher_detail),
                FIND_BY_CONTAINS
        )
        return this
    }
}