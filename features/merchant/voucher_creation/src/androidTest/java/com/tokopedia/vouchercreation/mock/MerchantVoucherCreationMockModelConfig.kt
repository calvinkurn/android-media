package com.tokopedia.vouchercreation.mock

import android.content.Context
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.vouchercreation.test.R

class MerchantVoucherCreationMockModelConfig: MockModelConfig()  {
    companion object {
        private const val GET_INITIATE_VOUCHER = "InitiateVoucher"
        private const val GET_VOUCHER_RECOMMENDATION = "MerchantPromotionGetVoucherRecommendation"
        private const val CREATE_MERCHANT_VOUCHER = "CreateVoucher"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                GET_INITIATE_VOUCHER,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_get_initiate_voucher),
                FIND_BY_CONTAINS
        )
        addMockResponse(
                GET_VOUCHER_RECOMMENDATION,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_get_voucher_recommendation),
                FIND_BY_CONTAINS
        )
        addMockResponse(
                CREATE_MERCHANT_VOUCHER,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_create_merchant_voucher),
                FIND_BY_CONTAINS
        )
        return this
    }
}