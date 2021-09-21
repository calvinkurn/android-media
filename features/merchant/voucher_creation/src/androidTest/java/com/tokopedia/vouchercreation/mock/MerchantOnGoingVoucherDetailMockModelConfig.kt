package com.tokopedia.vouchercreation.mock

import android.content.Context
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.vouchercreation.test.R

class MerchantOnGoingVoucherDetailMockModelConfig : MockModelConfig() {
    companion object {
        private const val GET_VOUCHER_BY_ID = "GetVoucherDataById"
        private const val GET_SHOP_BASIC_DATA = "ShopBasicData"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                GET_SHOP_BASIC_DATA,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_get_shop_basic_data),
                FIND_BY_CONTAINS
        )
        addMockResponse(
                GET_VOUCHER_BY_ID,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_get_ongoing_voucher_detail),
                FIND_BY_CONTAINS
        )
        return this
    }
}