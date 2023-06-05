package com.tokopedia.digital_product_detail.pulsa.utils

import android.content.Context
import com.tokopedia.digital_product_detail.utils.ResourceUtils
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig

class DigitalPDPPulsaIndosatOTPMockConfig : MockModelConfig() {

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
            KEY_QUERY_MENU_DETAIL,
            ResourceUtils.getJsonFromResource(PATH_RESPONSE_MENU_DETAIL),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            KEY_QUERY_OPERATOR_PREFIX,
            ResourceUtils.getJsonFromResource(PATH_RESPONSE_OPERATOR_PREFIX),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            KEY_QUERY_CATALOG_INPUT_MULTITAB,
            ResourceUtils.getJsonFromResource(PATH_RESPONSE_CATALOG_INPUT_MULTITAB),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            KEY_CHANNEL_INDOSAT_CHECK_BALANCE,
            ResourceUtils.getJsonFromResource(PATH_RESPONSE_INDOSAT_CHECK_BALANCE),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            KEY_QUERY_SAVE_USER_BALANCE_ACCESS_TOKEN,
            ResourceUtils.getJsonFromResource(PATH_RESPONSE_SAVE_INDOSAT_ACCESS_TOKEN),
            FIND_BY_CONTAINS
        )
        return this
    }

    companion object {
        private const val KEY_QUERY_MENU_DETAIL = "rechargeCatalogMenuDetail"
        private const val KEY_QUERY_OPERATOR_PREFIX = "rechargeCatalogPrefixSelect"
        private const val KEY_QUERY_CATALOG_INPUT_MULTITAB = "rechargeCatalogProductInputMultiTab"
        private const val KEY_QUERY_SAVE_USER_BALANCE_ACCESS_TOKEN = "rechargeSaveTelcoUserBalanceAccessToken"
        private const val KEY_CHANNEL_INDOSAT_CHECK_BALANCE = "indosat_check_balance"

        private const val PATH_RESPONSE_MENU_DETAIL = "pulsa/get_menu_detail_mock.json"
        private const val PATH_RESPONSE_OPERATOR_PREFIX = "pulsa/get_operator_prefix_mock.json"
        private const val PATH_RESPONSE_CATALOG_INPUT_MULTITAB = "pulsa/get_catalog_input_multitab_mock.json"
        private const val PATH_RESPONSE_INDOSAT_CHECK_BALANCE = "pulsa/get_indosat_check_balance_otp.json"
        private const val PATH_RESPONSE_SAVE_INDOSAT_ACCESS_TOKEN = "pulsa/save_indosat_access_token.json"
    }
}
