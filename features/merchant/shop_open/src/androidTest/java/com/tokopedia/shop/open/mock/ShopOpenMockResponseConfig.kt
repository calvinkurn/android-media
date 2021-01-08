package com.tokopedia.shop.open.mock

import android.content.Context
import com.tokopedia.shop.open.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper

class ShopOpenMockResponseConfig: MockModelConfig() {
    companion object{
        private const val VALIDATE_SHOP_NAME_DOMAIN = "validateDomainShopName"
        private const val SHOP_DOMAIN_SUGGESTION = "shopDomainSuggestion"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                VALIDATE_SHOP_NAME_DOMAIN,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_validate_domain_shop_name),
                FIND_BY_CONTAINS
        )

        addMockResponse(
                SHOP_DOMAIN_SUGGESTION,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_shop_domain_suggestion),
                FIND_BY_CONTAINS
        )

        return this
    }
}