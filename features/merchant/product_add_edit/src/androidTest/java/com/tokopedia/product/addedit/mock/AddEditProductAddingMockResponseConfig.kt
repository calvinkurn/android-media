package com.tokopedia.product.addedit.mock

import android.content.Context
import com.tokopedia.product.addedit.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper

class AddEditProductAddingMockResponseConfig: MockModelConfig() {

    companion object {
        private const val VALIDATE_SHOP_INFO = "shopInfoByID"
        private const val UPLOADPEDIA_POLICY = "uploadpedia_policy"
        private const val PRODUCT_ADD_RULE = "ProductAddRule"
        private const val PRODUCT_VALIDATE = "ProductValidateV3"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                VALIDATE_SHOP_INFO,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_validate_shop_info),
                FIND_BY_CONTAINS
        )
        addMockResponse(
                UPLOADPEDIA_POLICY,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_uploadpedia_policy),
                FIND_BY_CONTAINS
        )
        addMockResponse(
                PRODUCT_ADD_RULE,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_product_add_rule),
                FIND_BY_CONTAINS
        )
        addMockResponse(
                PRODUCT_VALIDATE,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_product_validate),
                FIND_BY_CONTAINS
        )

        return this
    }
}