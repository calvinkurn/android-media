package com.tokopedia.vouchercreation.mock

import android.content.Context
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.vouchercreation.test.R

class MerchantDuplicateVoucherReviewMockModelConfig : MockModelConfig() {
    companion object {
        private const val GET_SHOP_BASIC_DATA = "ShopBasicData"
        private const val GET_INITIATE_VOUCHER = "InitiateVoucher"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                GET_SHOP_BASIC_DATA,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_get_shop_basic_data),
                FIND_BY_CONTAINS
        )
        addMockResponse(
                GET_INITIATE_VOUCHER,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_get_initiate_voucher),
                FIND_BY_CONTAINS
        )
        return this
    }
}