package com.tokopedia.vouchercreation.mock

import android.content.Context
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.vouchercreation.test.R

class MerchantVoucherHistoryListErrorMockModelConfig: MockModelConfig()  {
    companion object {
        private const val GET_VOUCHER_HISTORY_LIST = "getAllMerchantPromotion"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                GET_VOUCHER_HISTORY_LIST,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_get_error_voucher_history_list),
                FIND_BY_CONTAINS
        )
        return this
    }
}