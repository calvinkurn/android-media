package com.tokopedia.vouchercreation.mock

import android.content.Context
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.vouchercreation.test.R

class MerchantActiveVoucherListMockModelConfig : MockModelConfig() {
    companion object {
        private const val GET_ACTIVE_VOUCHER_LIST = "getAllMerchantPromotion"
        private const val GET_SHOP_BASIC_DATA = "ShopBasicData"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                GET_SHOP_BASIC_DATA,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_get_shop_basic_data),
                FIND_BY_CONTAINS
        )
        addMockResponse(
                GET_ACTIVE_VOUCHER_LIST,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_get_active_voucher_list),
                FIND_BY_CONTAINS
        )
        return this
    }
}